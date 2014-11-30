package com.bb.dd.fragment;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bb.dd.BikeApplication;
import com.bb.dd.R;
import com.bb.dd.dao.DaoFactory;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.service.BikeSiteInforService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.BeanUtils;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.PreferencesUtil;
import com.j256.ormlite.dao.Dao;
import com.topdt.application.entity.BikeSitesView;
import com.topdt.coal.entity.BikeSite;

public class UpdateDataFragment extends AbstractFragment implements OnClickListener{
	private static final String TAG = UpdateDataFragment.class.getName();
	private TextView last_update_time, update_count, nativ_count;
	private ProgressDialog progress;
	private Button btn_update;
	private TelephonyManager telephonemanage ;
	private String updateTime;
	BikeSitesView bikeSitesView;
	private RelativeLayout synchroCountLayout;
	public UpdateDataFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		telephonemanage =(TelephonyManager) _context.getSystemService(_context.TELEPHONY_SERVICE);
		rootView = inflat(R.layout.update_data);
		initView();
		return rootView;
	}

	private void initView() {
		synchroCountLayout = (RelativeLayout) ((Activity) _context).findViewById(R.id.rl_synchron_count);
		last_update_time = (TextView) rootView.findViewById(R.id.last_update_time);
		btn_update = (Button) rootView.findViewById(R.id.start_synchro);
		update_count = (TextView) rootView.findViewById(R.id.update_count);
		btn_update.setOnClickListener(this);
		updateTime=PreferencesUtil.getStr(PreferencesUtil.LAST_UPDATE_TIME);
		setLastUpdateTime(updateTime);
		int count=BikeApplication.getInstance().getSynchroCount();
		if (count!=0) {
			btn_update.setBackgroundResource(R.drawable.bt_submit_bg);
			btn_update.setTextColor(Color.WHITE);
			btn_update.setText("同步");
			setUpdateCount(count);
		} else {
			setUpdateCount(0);
			btn_update.setTextColor(Color.WHITE);
			btn_update.setText("暂无数据更新");
			btn_update.setClickable(false);
		}
	}
	private void setUpdateCount(Integer uCount) {
		update_count.setText("需要更新的数据：有"+uCount+"条");
	}

	private void setLastUpdateTime(String time) {
		last_update_time.setText("最后更新时间："+time);
	}


	@Override
	public void closed() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_synchro:
			UpdateTask task = new UpdateTask();
			task.execute(CommonVariable.REQUEST_update_BIKE_SITES);
			break;
		}
	}
	// 创建提示框
		public ProgressDialog createProgress(Context context, String tip) {
			ProgressDialog progress = new ProgressDialog(context);
			progress.setMessage(tip);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return progress;
		}
		public class UpdateTask extends AsyncTask<String, Void, Void> {


			protected void onPreExecute() {
				progress = createProgress(_context, "同步中...");
				progress.show();
			};


			@Override
			protected Void doInBackground(String... params) {
				String url = params[0];
				Dao<BikeSite_Lite, Integer> bikeDao = DaoFactory.instant()
						.getBikeSiteDao();
				try {
					bikeSitesView = new BikeSiteInforService().loadBikeSitesUpdate(updateTime);
					List<BikeSite> sites = bikeSitesView.getBikeSites();
					int sizelong = sites.size();
					for (int i = 0, size = sizelong; i < size; i++) {
						BikeSite bikeSite = sites.get(i);
						BikeSite_Lite site = new BikeSite_Lite();
						BeanUtils.copyProperties(site, bikeSite);
						if (site.getState().intValue()==0) {
							bikeDao.delete(site);
						} else {
							bikeDao.createOrUpdate(site);
						}
					}
					
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				progress.dismiss();
				String now=bikeSitesView.getUpdateDate();
				setLastUpdateTime(now);
				PreferencesUtil.setStr(PreferencesUtil.LAST_UPDATE_TIME, now);
				//nativ_count.setVisibility(View.GONE);
				BikeApplication.getInstance().setSynchroCount(0);
				setUpdateCount(0);
				btn_update.setBackgroundResource(R.drawable.gengxin);
				synchroCountLayout.setVisibility(View.GONE);
			}

		}

}

package com.bb.dd.fragment;



import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.bb.dd.IndexMainActivity;
import com.bb.dd.LoginActivity;
import com.bb.dd.R;
import com.bb.dd.adapter.UserAttentionBikesiteAdapter;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.service.BikeSiteInforService;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;
import com.topdt.application.entity.BikeSiteRealView;
import com.topdt.coal.entity.User;
import com.topdt.coal.entity.UserAttentionBikesite;

public class FocusFragment extends AbstractFragment {
	private static final String TAG = FocusFragment.class.getName();

	private ListView listView;
	private List<BikeSiteRealView> bikeSites = new ArrayList<BikeSiteRealView>();
	private UserAttentionBikesiteAdapter userAttentionBikesiteAdapter;
	private BikeSiteUserService bikeSiteUserService = new BikeSiteUserService();
	private BikeSite_Lite bikeSite_Lite;
	public FocusFragment(Context context) {
		super(context);
	}
	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.focus);
		initView();
		return rootView;
	}
	
	@Override
	public void closed() {
		
		
	}
	

	private void initView() {
		showProgressDialog();
		listView = (ListView) rootView.findViewById(R.id.focus_listview);
		listView.setVisibility(View.GONE);
		loadLocalFocus();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Intent intent_point = new Intent();
//				intent_point.setClass(_context, MapPointActivity.class);
//				intent_point.putExtra("bikesiteId", bikeSites.get(arg2).getId());
//				_context.startActivity(intent_point);
				BikeSiteRealView bikeSiteRealView=bikeSites.get(arg2);
				if(null!=bikeSiteRealView){
					String id=bikeSiteRealView.getId();
					searchBikeSite(id);
				}else{
					Util.t("该站点已暂时关闭或移除,请您查看其它的站点");
				}
			}
		});
	}
	
	public void showMessage(String msg){
		rootView = inflat(R.layout.message);
		TextView textView = (TextView) rootView.findViewById(R.id.message_name);
		textView.setText(msg);
		
		if(IndexMainActivity.container!= null){
			IndexMainActivity.container.removeAllViews();
			IndexMainActivity.container.addView(rootView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		}
	}
	
	private void loadLocalFocus(){
		int login_flag = Cons.LOGIN_FLAG;
		User user = Util.getLoginUser();
		if(login_flag==0 || user==null){
			//Util.t("该功能登录用户方可使用，请先登录。");
			Message message = new Message();
			message.what = 001;
			message.obj = "该功能登录用户方可使用，请先登录";
			handler.sendMessage(message);
			return;
		}else{
			bikeSiteUserService.loadUserFocus(user.getApnUserId().toString(),new AsyncOperator() {
				@Override
				public void onSuccess(Object obj) {
					StringBuffer bikesiteIdBuffer = new StringBuffer();
					String bikesiteIds = null;
					List<UserAttentionBikesite> list = (List<UserAttentionBikesite>)obj;
					for (UserAttentionBikesite focus : list) {
						bikesiteIdBuffer.append(focus.getBikesiteId()+"#");
					}
					if (bikesiteIdBuffer.length()>0) {
						bikesiteIds = bikesiteIdBuffer.substring(0, bikesiteIdBuffer.length()-1);
						loadBikeSitesReal(bikesiteIds);
					}else{
						Message message = new Message();
						message.what = 002;
						message.obj = "您还没有关注的站点信息。";
						handler.sendMessage(message);
					}
					super.onSuccess(obj);
				}

				@Override
				public void onFail(String message) {
					closedProgressDialog();
					super.onFail(message);
				}
				
			});
		}
	}
	
	private void loadBikeSitesReal(String bikesiteIds){
		try {
			bikeSites = new BikeSiteInforService().loadBikeSitesReal(bikesiteIds);
			Message message = new Message();
			message.what = 000;
			handler.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			Util.t("服务异常");
		} finally {
			closedProgressDialog();
		}
	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 000:
					userAttentionBikesiteAdapter = new UserAttentionBikesiteAdapter(_context, bikeSites);
					listView.setAdapter(userAttentionBikesiteAdapter);
					listView.setVisibility(View.VISIBLE);
					closedProgressDialog();
					break;
				case 001:
					String msgstr = (String) msg.obj;
					showMessage(msgstr);
					closedProgressDialog();
					Intent intent=new Intent(_context, LoginActivity.class);
					intent.putExtra("focus", "focus");
					_context.startActivity(intent);
					break;	
				case 002:
					String msgstr1 = (String) msg.obj;
					showMessage(msgstr1);
					closedProgressDialog();
					break;	
				case 003:
					Intent intent_point = new Intent();
					intent_point.setClass(_context, IndexMainActivity.class);
					intent_point.putExtra("longitude", bikeSite_Lite.getSign4()
							.trim());
					intent_point.putExtra("latitude", bikeSite_Lite.getSign3()
							.trim());
					intent_point.putExtra("bikesiteName", bikeSite_Lite.getBikesiteName()
							.trim());
					intent_point.putExtra("bikesiteId", bikeSite_Lite.getBikesiteId()
							.trim());
					_context.startActivity(intent_point);
					break;
			}
		}
	};
	/**
	 * 查询站点
	 * 
	 */
	private void searchBikeSite(final String value) {
		new Thread() {
			private BikeOverlayDao dao = new BikeOverlayDao();
			String[] colums = { "bikesite_id" };

			public void run() {
				if (null != value) {
					if (!("".equals(value.trim()))) {
						String likeValue = "%" + value + "%";
						List<BikeSite_Lite> sites = dao.query4Like(colums,
								likeValue);
						if (null != sites && 0 != sites.size()) {
							bikeSite_Lite = sites.get(0);
							Message message = new Message();
							message.what = 003;
							handler.sendMessage(message);
						}

					}
				}
			}

		}.start();

	}

}

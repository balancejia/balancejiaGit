package com.bb.dd.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bb.dd.IndexMainActivity;
import com.bb.dd.R;
import com.bb.dd.fragment.AbstractFragment;
import com.bb.dd.fragment.FocusFragment;
import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.Cons;
import com.bb.dd.util.ProgressDialogUtil;
import com.bb.dd.util.Util;
import com.topdt.application.entity.BikeSiteRealView;
import com.topdt.coal.entity.User;

public class UserAttentionBikesiteAdapter extends BaseAdapter {

	private Context _context;
	private List<BikeSiteRealView> _sites;
	private BikeSiteRealView bikeSiteRealView;
	public UserAttentionBikesiteAdapter(Context context,List<BikeSiteRealView> sites){
		_context=context;
		_sites=sites;
	}
	@Override
	public int getCount() {
		if(_sites==null){
			return 0;
		}
		return _sites.size();
	}

	@Override
	public Object getItem(int arg0) {
		return _sites.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BikeSiteRealView site=(BikeSiteRealView)getItem(position);
		Holder holder;
		if(null==convertView){
			convertView=View.inflate(_context, R.layout.focus_listview_item, null);
			holder=new Holder();
			holder.textView1=(TextView)convertView.findViewById(R.id.bike_site_name);
			holder.textView2=(TextView)convertView.findViewById(R.id.bike_site_can_borrow_num);
			holder.textView3=(TextView)convertView.findViewById(R.id.bike_site_can_send_num);
			holder.button= (LinearLayout)convertView.findViewById(R.id.del_focus_button);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_sitedetail);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		if(site!=null){
			holder.textView1.setText(site.getName());
			holder.textView2.setText(String.valueOf(site.getCanusebikecount()));
			holder.textView3.setText(String.valueOf(site.getCanuseposcount()));
			holder.button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					ProgressDialogUtil.showProgressDialog(_context, "正在取消关注...");
					bikeSiteRealView = site;
					if (null!=bikeSiteRealView) {
						String id=bikeSiteRealView.getId();
						if(null!=id){
							delUserFocus();
						}
					}
					
				}
			});
			Double useBikecount = Double.valueOf(site.getCanusebikecount());
			Double usePosCount =  Double.valueOf(site.getCanuseposcount());
			Double totalCount=useBikecount+usePosCount;
			if(totalCount!=0){
				Double count = (useBikecount / totalCount) * 100;
				DecimalFormat df = new DecimalFormat("0");
				int useBikePro = Integer.parseInt(df.format(count));
				holder.progressBar.setProgress(useBikePro);
			}
		}
		return convertView;
	}
	
	private void delUserFocus(){
		int login_flag = Cons.LOGIN_FLAG;
		User user = Util.getLoginUser();
		new BikeSiteUserService().delUserFocus(user.getApnUserId().toString(), bikeSiteRealView.getId(), new AsyncOperator() {
			@Override
			public void onSuccess(Object obj) {
				Boolean flag = (Boolean)obj;
				if (flag) {
					_sites.remove(bikeSiteRealView);
					Message message = new Message();
					message.what = 001;
					handler.sendMessage(message);
					Util.t("成功取消关注");
				}else{
					Util.t("取消关注失败");
				}
				ProgressDialogUtil.closedProgressDialog();
				super.onSuccess(obj);
			}
			
			@Override
			public void onFail(String message) {
				ProgressDialogUtil.closedProgressDialog();
				super.onFail(message);
			}
		});
	}
	
	private static class Holder{
		public TextView textView1,textView2,textView3;
		public LinearLayout button;
		public ProgressBar progressBar;
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg)
		{
			switch (msg.what){
			case 001:
				AbstractFragment currentFragment = new FocusFragment(_context);
				IndexMainActivity.container.removeAllViews();
				IndexMainActivity.container.addView(currentFragment.getView(), LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				break;
			}
		}
	};
}

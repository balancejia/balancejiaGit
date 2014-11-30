package com.bb.dd.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bb.dd.R;
import com.bb.dd.util.BadHandler;

public class AboutUsFragment extends AbstractFragment{
	private static final String TAG = AboutUsFragment.class.getName();
	public AboutUsFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.aboutus);
		initView();
		return rootView;
	}

	@Override
	public void closed() {
		
	}
	private void initView() {
		TextView zixingche_web=(TextView)rootView.findViewById(R.id.zixingche_web);
		TextView wuxian_web=(TextView)rootView.findViewById(R.id.wuxian_web);
		zixingche_web.setOnClickListener(new bikeWebText());
		wuxian_web.setOnClickListener(new wifiWebText());
	}
	/*
	 * 启动手机浏览器
	 */
	private class bikeWebText implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent= new Intent();        
		    intent.setAction("android.intent.action.VIEW");    
		    Uri content_url = Uri.parse("http://www.ty7772345.com");   
		    intent.setData(content_url);  
		    _context.startActivity(intent);
		}
	}
	private class wifiWebText implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			Intent intent= new Intent();        
			intent.setAction("android.intent.action.VIEW");    
			Uri content_url = Uri.parse("http://wap.uwxsx.com");   
			intent.setData(content_url);  
			_context.startActivity(intent);
		}
		
	}
}

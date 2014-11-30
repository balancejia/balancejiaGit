package com.bb.dd.fragment;

import java.io.IOException;

import mixedserver.client.tools.GlobalTools;
import mixedserver.protocol.jsonrpc.client.Session;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bb.dd.R;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.Util;

public class LostGongGaoFragment extends AbstractFragment {
	private WebView webview; 
	private String url;
	private LinearLayout container;
	private TextView title;
	private LinearLayout top_ll_right;
	private ImageView topRight;
	private FragmentView currentFragment;
	private static final String TAG = LostGongGaoFragment.class.getName();

	public LostGongGaoFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.main_announcement);
		initView();
		return rootView;
	}

	@Override
	public void closed() {

	}
	private void initView() {
		
		container = (LinearLayout) ((Activity) _context).findViewById(R.id.container);
		title = (TextView) ((Activity) _context)
				.findViewById(R.id.top_tv_center);
		top_ll_right = (LinearLayout) ((Activity) _context)
				.findViewById(R.id.top_ll_right);
		topRight = (ImageView) ((Activity) _context)
				.findViewById(R.id.top_iv_right);
		
		
		
		
		
		
		//变化url
		url = CommonVariable.AD_LOST_INFO;
		Session session = GlobalTools.getClient().getSession();

//		if (session.getSessionId() != null) {
//			CookieManager cookieManager = CookieManager.getInstance();
//			cookieManager.setCookie(url, session.getSessionKey() + "="
//					+ session.getSessionId() + "; domain="
//					+ CommonVariable.REQUEST_RPCPATH);
//			CookieSyncManager.getInstance().sync();
//		}
		
        //实例化WebView对象 
        webview = (WebView) rootView.findViewById(R.id.main_announcement_webView); 
        //设置WebView属性，能够执行Javascript脚本 
        webview.requestFocus();
        webview.getSettings().setJavaScriptEnabled(true); 
        //加载需要显示的网页 
      //  webview.loadUrl(url); 
        new Thread(new Runnable() {   
            @Override   
            public void run() {   
                    Message msg = new Message();  
                    Util.l("url---------");
                    //此处判断主页是否存在，因为主页是通过loadUrl加载的，   
                    //此时不会执行shouldOverrideUrlLoading进行页面是否存在的判断   
                    //进入主页后，点主页里面的链接，链接到其他页面就一定会执行shouldOverrideUrlLoading方法了   
                    if(getRespStatus(url)!=200) { 
                    	 Util.l("url---------"+404);
                            msg.what = 404;   
                    }   
                    handler.sendMessage(msg);   
            }   
    }).start(); 
        webview.setWebViewClient(new MyWebViewClient());
       
	}
	private Handler handler = new Handler() {   
        @Override   
        public void handleMessage(Message msg) {   
                if(msg.what==404) {//主页不存在   
                        //载入本地assets文件夹下面的错误提示页面404.html   
                	webview.loadUrl("file:///android_asset/404HTML/404.html");     
                	webview.getSettings().setUseWideViewPort(true); 
                	webview.getSettings().setLoadWithOverviewMode(true);
                	 Util.l("url---------"+404);
                } else {   
                	 Util.l("url------success---");
                	webview.loadUrl(url);   
                }   
        }   
};   
	
		// 设置回退
		// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//		@Override
//		public boolean onKeyDown(int keyCode, KeyEvent event) {
//			if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
//				webview.goBack(); // goBack()表示返回WebView的上一页面
//				return true;
//			}
//			return super.onKeyDown(keyCode, event);  
//		}
		
    //Web视图 
    private class MyWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	topRight.setVisibility(View.VISIBLE);
			Drawable drawable = _context.getResources().getDrawable(
					R.drawable.top_back);
			topRight.setBackgroundDrawable(drawable);
			top_ll_right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					topRight.setVisibility(View.GONE);
					webview.goBack();
					/*currentFragment = new AnnouncementFragment(_context);
					infaltView();*/
				}
			});
        	if(url.startsWith("http://") && getRespStatus(url)!=200) {  
        		 Util.l("url---------"+404);
                 view.stopLoading();   
                 //载入本地assets文件夹下面的错误提示页面404.html   
                 view.loadUrl("file:///android_asset/404HTML/404.html");   
                 view.getSettings().setUseWideViewPort(true); 
                 view.getSettings().setLoadWithOverviewMode(true);
        	} else {   
        	     Util.l("url----success--");
        	     if(url.indexOf("tel:")<0){
        	    	 view.loadUrl(url);
        	    	 }
                //view.loadUrl(url);   
        	}   
         return true;   
        }

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			closedProgressDialog();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			showProgressDialog();
			super.onPageStarted(view, url, favicon);
		} 
		
    } 
    
    private int getRespStatus(String url) {   
        int status = -1;   
        try {   
                HttpHead head = new HttpHead(url);   
                HttpClient client = new DefaultHttpClient();   
                HttpResponse resp = client.execute(head);   
                status = resp.getStatusLine().getStatusCode();   
        } catch (IOException e) {}   
        Util.l("url---------"+status);
        return status;   
    }  
}

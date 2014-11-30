package com.bb.dd.fragment;

import java.io.IOException;

import mixedserver.client.tools.GlobalTools;
import mixedserver.protocol.jsonrpc.client.Session;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bb.dd.R;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.Util;

public class HelpbFragment extends AbstractFragment {
	private WebView webview; 
	private String url;
	private static final String TAG = HelpbFragment.class.getName();
	public HelpbFragment(Context context) {
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
		url = CommonVariable.AD_HELPb_INFO;
		Session session = GlobalTools.getClient().getSession();
        //实例化WebView对象 
        webview = (WebView) rootView.findViewById(R.id.main_announcement_webView); 
        //设置WebView属性，能够执行Javascript脚本 
        webview.requestFocus();
        webview.getSettings().setJavaScriptEnabled(true); 
        //加载需要显示的网页 
        //webview.loadUrl(url); 
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
	
		
    //Web视图 
    private class MyWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
//            view.loadUrl(url); 
//            return true; 
        	if(url.startsWith("http://") && getRespStatus(url)!=200) {  
       		 Util.l("url---------"+404);
                view.stopLoading();   
                //载入本地assets文件夹下面的错误提示页面404.html   
                view.loadUrl("file:///android_asset/404HTML/404.html"); 
                view.getSettings().setUseWideViewPort(true); 
            	view.getSettings().setLoadWithOverviewMode(true);
        	} else {   
        		Util.l("url----success--");
                view.loadUrl(url);   
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

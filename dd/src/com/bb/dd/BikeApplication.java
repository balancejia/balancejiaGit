package com.bb.dd;

import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;

public class BikeApplication extends Application {

	private static BikeApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;
	private static Application application = null;

//	public static final String strKey = "A22DCA85C92586D8FEEB93316E56CD8E40CA4996";
	public static Context APP_CONTEXT;
	private HashMap<String, Activity> tempContexts;
	private TelephonyManager telephonemanage;
	private int synchroCount;

	@Override
	public void onCreate() {
		super.onCreate();
		APP_CONTEXT = getApplicationContext();
		mInstance = this;
		application = this;
		tempContexts = new HashMap<String, Activity>(2);
		initEngineManager(this);
		
		// 获得手机管理者 ，用来获取手机串号.Ps:需要在配置文件中加入相应的权限
		telephonemanage = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		//telephonemanage.getSubscriberId();
		Cons.userInfor.setMobileType(android.os.Build.MODEL);	   //手机型号
		Cons.userInfor.setDeviceId(telephonemanage.getDeviceId()); //获取智能设备唯一编号
		Util.l("app------启动");
	}

	public static Context getApplication() {
		return application;
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(new MyGeneralListener())) {
			Toast.makeText(
					BikeApplication.getInstance().getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	public static BikeApplication getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(BikeApplication.getInstance().getApplicationContext(),"您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				//Toast.makeText(BikeApplication.getInstance().getApplicationContext(),"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			/*if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				//Toast.makeText(BikeApplication.getInstance().getApplicationContext(),"请在 DemoApplication.java文件输入正确的授权Key！",Toast.LENGTH_LONG).show();
				BikeApplication.getInstance().m_bKeyRight = false;
			}*/
			//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                Toast.makeText(BikeApplication.getInstance().getApplicationContext(), 
                        "请在 BikeApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
                BikeApplication.getInstance().m_bKeyRight = false;
            }
            else{
            	BikeApplication.getInstance().m_bKeyRight = true;
            	Toast.makeText(BikeApplication.getInstance().getApplicationContext(), 
                        "key认证成功", Toast.LENGTH_LONG).show();
            }
		}
	}
	
	public HashMap<String, Activity> getTempContext() {
		return tempContexts;
	}
	
	public int getSynchroCount() {
		return synchroCount;
	}

	public void setSynchroCount(int synchroCount) {
		this.synchroCount = synchroCount;
	}
}
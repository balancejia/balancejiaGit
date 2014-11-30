package com.bb.dd.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bb.dd.BikeApplication;
import com.bb.dd.R;
import com.bb.dd.UpdateVersionActivity;
import com.bb.dd.modle.UploadApk;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.Util;
import com.bb.dd.util.WebHelper;

public class UpdateVersionFragment extends AbstractFragment implements OnClickListener{
	private static final String TAG = UpdateVersionFragment.class.getName();
	private TextView nowVersion, newVersion, versionContent;
	private Button btn_update;
	private String nowVersionName,newVersionName,verContent;
	protected static final int HAVE_NEW_VERSION = 1;
	protected static final int NO_HAVE_VERSION = 2;
	protected static final int ERROR4UPDATE = 3;
	protected static final int LOAD_NAVITE_VERSION = 4;
	protected static final int START_UPDATE_VERSION = 10;
	public static final int MANAGER = 10;
	private UploadApk newSuj;
	private UploadApk locSuj;
	private MyHandler mhd = new MyHandler();
	private TelephonyManager telephonemanage ;
	 AlertDialog builder;
	 TextView do_cancle;

	public UpdateVersionFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		showProgressDialog();
		BadHandler.getInstance().init(_context);
		telephonemanage =(TelephonyManager) _context.getSystemService(_context.TELEPHONY_SERVICE);
		rootView = inflat(R.layout.update_version);
		getLoadEntity();
		initView();
		return rootView;
	}
	private void getLoadEntity() {
		locSuj = new UploadApk();
		locSuj.verCode = getVerCode();
		mhd.sendEmptyMessage(LOAD_NAVITE_VERSION);
	}

	private void initView() {
		nowVersion = (TextView) rootView.findViewById(R.id.now_version);
		newVersion = (TextView) rootView.findViewById(R.id.new_version);
		versionContent = (TextView) rootView.findViewById(R.id.version_content);
		btn_update = (Button) rootView.findViewById(R.id.start_update_version);
		btn_update.setOnClickListener(this);
	}
	private void setNowVersion(String s) {
		nowVersion.setText("当前版本："+s);
	}
	private void setNewVersion(String s) {
		newVersion.setText("最新版本："+s);
	}
	private void setVersionContent(String s) {
		versionContent.setText(s);
	}

	@Override
	public void closed() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_update_version:
			Intent intent=new Intent(_context,UpdateVersionActivity.class);
			intent.putExtra("newSuj", newSuj);
			_context.startActivity(intent);
			break;
		}
	}
	public int getVerCode() {
		int verCode = -1;
		try {
			verCode = BikeApplication.APP_CONTEXT.getPackageManager().getPackageInfo(
					CommonVariable.APP_NAME, 0).versionCode;
			nowVersionName= BikeApplication.APP_CONTEXT.getPackageManager().getPackageInfo(
					CommonVariable.APP_NAME, 0).versionName;
		} catch (NameNotFoundException e) {
				e.getMessage();
		}
		return verCode;
	}
	private class ToUpdate extends Thread {
		@Override
		public void run() {
			WebHelper helper = new WebHelper();
			String verjson = helper.getResult(CommonVariable.UPDATE_JSON, null);
			Util.l("--版本更新--"+verjson);
			newSuj = new UploadApk();
			try {
				if (null != verjson) {
					JSONArray array = new JSONArray(verjson);
					JSONObject obj = array.getJSONObject(0);
					newSuj.verCode = Integer.parseInt(obj.getString("verCode"));
					newSuj.verName = obj.getString("verName");
					newSuj.apkUrl = obj.getString("apkUrl");
					newSuj.apkname = obj.getString("apkname");
					newSuj.verContent = obj.getString("verContent");
					newVersionName=newSuj.verName;
					verContent=newSuj.verContent;
					doCompare();
				} else {
					mhd.sendEmptyMessage(ERROR4UPDATE);
				}
			} catch (JSONException e) {
				mhd.sendEmptyMessage(ERROR4UPDATE);
			}
		}
	}
	private void doCompare() {
		if (locSuj.verCode < newSuj.verCode) {
			mhd.sendEmptyMessage(HAVE_NEW_VERSION);
		} else {
			mhd.sendEmptyMessage(NO_HAVE_VERSION);
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HAVE_NEW_VERSION: {
				closedProgressDialog();
				setNowVersion(nowVersionName);
				setNewVersion(newVersionName);
				setVersionContent(verContent);
				btn_update.setBackgroundResource(R.drawable.bt_submit_bg);
				btn_update.setTextColor(Color.WHITE);
				btn_update.setText("更新");
			}
				break;
			case ERROR4UPDATE: {
				closedProgressDialog();
					Util.t("获取最新版本失败,请检查网络连接。");
			}
				break;
			case NO_HAVE_VERSION: {
				closedProgressDialog();
				setNowVersion(nowVersionName);
				setNewVersion(newVersionName);
				verContent="当前为最新版本";
				setVersionContent(verContent);
				btn_update.setTextColor(Color.WHITE);
				btn_update.setText("暂无最新版本");
				btn_update.setClickable(false);
			}
				break;
			case LOAD_NAVITE_VERSION:
				new ToUpdate().start();
			}
		}
	}
}

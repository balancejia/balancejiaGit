package com.bb.dd;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bb.dd.util.BadHandler;
/**
 * 退出程序
 * @author Administrator
 *
 */
public class ShowDialogActivity extends Activity {
	private static final String TAG = ShowDialogActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.quit_dialog);
	}
	public void doExit(View v){
		BikeApplication app=(BikeApplication)getApplication();
		HashMap<String,Activity> temps = app.getTempContext();
		Set<Entry<String, Activity>> sets=temps.entrySet();
		for(Entry<String,Activity> entry:sets){
			entry.getValue().finish();
		}
		temps.clear();
		finish();
		System.exit(-1);
	}
	public void doCancle(View v){
		finish();
	}
}

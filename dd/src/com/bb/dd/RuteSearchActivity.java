package com.bb.dd;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.bb.dd.modle.RuteView;
import com.bb.dd.util.BadHandler;



public class RuteSearchActivity extends ActivityGroup {
	private static final String TAG = RuteSearchActivity.class.getName();
	Intent intent = null;
	private int index_tab = 0;
	private TabWidget tabWidget;
	TextView title_name = null;
	public static int WIDTH=0;
	public static int HEIGHT = 0;
	private RuteView ruteView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.rute_search);
		initView();
		tabShow();
	}
	private void initView() {
		Intent intent=getIntent();
		ruteView=(RuteView)intent.getSerializableExtra("ruteView");
		title_name = (TextView) findViewById(R.id.top_tv_center);
		title_name.setText("线路指引");
		ruteView.setFlag(3);

	}
	private void tabShow() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WIDTH = dm.widthPixels;
		HEIGHT = dm.heightPixels;
		TabHost t = (TabHost) findViewById(R.id.tabhost_rute);
		t.setup(this.getLocalActivityManager());
		t.setPadding(0, 0, 0, 0);
		tabWidget = t.getTabWidget();
		LayoutInflater fi = LayoutInflater.from(RuteSearchActivity.this);
		View view = fi.inflate(R.layout.rute_search_tab, null);
		LinearLayout ll = (LinearLayout) view
				.findViewById(R.id.tablayout_rute);
		View tab_map_rute = view.findViewById(R.id.tab_map_rute);
		View tab_list_rute = view.findViewById(R.id.tab_list_rute);

		ll.removeAllViews();
		TabHost.TabSpec t_map_rute = t.newTabSpec("1");
		t_map_rute.setIndicator(tab_map_rute);
		intent = new Intent();
		intent.putExtra("ruteView", ruteView);
		intent.setClass(RuteSearchActivity.this, RuteMapActivity.class);
		t_map_rute.setContent(intent);
		t.addTab(t_map_rute);
		TabHost.TabSpec t_list_rute = t.newTabSpec("2");
		t_list_rute.setIndicator(tab_list_rute);
		intent = new Intent();
		intent.putExtra("ruteView", ruteView);
		intent.setClass(RuteSearchActivity.this, RuteListActivity.class);
		t_list_rute.setContent(intent);
		t.addTab(t_list_rute);
		
		tabWidget.setBackgroundColor(getResources().getColor(R.color.gray));
		tabWidget.setBaselineAligned(true);
		tab_map_rute.setBackgroundDrawable(getResources().getDrawable(R.drawable.rute_search_tab_bg));
		for (int i = 0; i < tabWidget.getChildCount(); i++) {

			tabWidget.getChildAt(i).getLayoutParams().width = WIDTH / 2;
			tabWidget.getChildAt(i).getLayoutParams().height = 70;
		}

		t.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				tabChanged(tabId);
			}
		});

	}
	public void tabChanged(String tabId) {
		if (index_tab != (Integer.valueOf(tabId) - 1)) {
			tabWidget.getChildAt(Integer.valueOf(tabId) - 1)
					.setBackgroundDrawable(getResources().getDrawable(R.drawable.rute_search_tab_bg));
			tabWidget.getChildAt(index_tab).setBackgroundDrawable(null);
			index_tab = Integer.valueOf(tabId) - 1;

		}
	}


	public void doBack(View v) {
		Intent intent=new Intent();
		intent.setClass(RuteSearchActivity.this, IndexMainActivity.class);
		intent.putExtra("ruteView", ruteView);
		startActivity(intent);
		finish();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                  && event.getRepeatCount() == 0) {
        	Intent intent=new Intent();
			intent.setClass(RuteSearchActivity.this, IndexMainActivity.class);
			intent.putExtra("ruteView", ruteView);
			startActivity(intent);
    		finish();
              return true;
          }
          return super.onKeyDown(keyCode, event);
      }
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
}

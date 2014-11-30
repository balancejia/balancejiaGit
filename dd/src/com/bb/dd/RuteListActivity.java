package com.bb.dd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bb.dd.adapter.RuteListAdapter;
import com.bb.dd.modle.RuteView;
import com.bb.dd.userdefined.MyListView;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
public class RuteListActivity extends Activity {
	private static final String TAG = RuteListActivity.class.getName();
	private RuteView ruteView;
	private MyListView ruteListView;
	private List<String> ruteList;
	private Context context;
	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		context=RuteListActivity.this;
		setContentView(R.layout.rute_list);
		initView();
	}


	private void initView() {
		Intent intent=getIntent();
		ruteView=(RuteView)intent.getSerializableExtra("ruteView");
		TextView startName=(TextView)findViewById(R.id.rute_list_startname);
		TextView endName=(TextView)findViewById(R.id.rute_list_endname);
		startName.setText(ruteView.getStartAddress());
		endName.setText(ruteView.getEndAddress());
		ruteListView=(MyListView)findViewById(R.id.rute_list_lv);
		ruteList=Cons.ruteList;
		RuteListAdapter adapter=new RuteListAdapter(context, ruteList);
		ruteListView.setAdapter(adapter);
		setListViewHeightBasedOnChildren(ruteListView);
	}
	/***
	 * 动态设置listview的高度
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// params.height += 5;// if without this statement,the listview will be
		// a
		// little short
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
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


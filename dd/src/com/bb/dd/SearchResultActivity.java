package com.bb.dd;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bb.dd.adapter.SearchBikesiteAdapter;
import com.bb.dd.adapter.SearchBikesiteLandmarkAdapter;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.service.BikeSiteInforService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Util;
import com.topdt.application.entity.BikeSiteLandmarkView;

public class SearchResultActivity extends Activity {
	private static final String TAG = SearchResultActivity.class.getName();
	private ProgressDialog progressDialog;
	private ListView searchListView;
	private SearchBikesiteAdapter searchBikesiteAdapter;
	private SearchBikesiteLandmarkAdapter searchBikesiteLandmarkAdapter;
	private List<BikeSite_Lite> searchSites = new ArrayList<BikeSite_Lite>();
	private List<BikeSiteLandmarkView> bikeSiteLandmarkViews = new ArrayList<BikeSiteLandmarkView>();
	private Context context;
	String searchString;
	String isLandmark;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		context = SearchResultActivity.this;
		setContentView(R.layout.search_result);
		Intent intent = getIntent();
		searchString = intent.getStringExtra("parameter");
		isLandmark = intent.getStringExtra("byLandMark");
		initView();
	}

	private void initView() {
		progressDialog = ProgressDialog.show(context, "正在加载",
				"请稍候...", true);
		progressDialog.closeOptionsMenu();
		TextView textView = (TextView) findViewById(R.id.top_tv_center);
		textView.setText("搜索结果");
		searchListView = (ListView) findViewById(R.id.main_search_listView);
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent_point = new Intent();
				intent_point.setClass(context, IndexMainActivity.class);
				if(isLandmark.equals("false")){
					BikeSite_Lite bikeSite_Lite=searchSites.get(arg2);
					intent_point.putExtra("longitude", bikeSite_Lite.getSign4().trim());
					intent_point.putExtra("latitude", bikeSite_Lite.getSign3().trim());
				}else{
					BikeSiteLandmarkView bikeSiteLandmarkView=bikeSiteLandmarkViews.get(arg2);
					intent_point.putExtra("longitude", bikeSiteLandmarkView.getSign4().trim());
					intent_point.putExtra("latitude", bikeSiteLandmarkView.getSign3().trim());
				}
				startActivity(intent_point);
				finish();
			}
		});
		SearchThread searchThread = new SearchThread();
		new Thread(searchThread).start();
	}

	private class SearchThread implements Runnable {
		private BikeOverlayDao dao;
		String[] colums = { "bikesite_id", "bikesite_name" };

		private SearchThread() {
			dao = new BikeOverlayDao();
		}

		@Override
		public void run() {
			Looper.prepare(); // 非主线程中new
								// Handler()需要加Looper.prepare();和Looper.loop();
			
			try {
				Message message = new Message();
				if (isLandmark.equals("true")) {
					bikeSiteLandmarkViews = new BikeSiteInforService()
							.loadBikeSitesSearch(searchString);
					if(bikeSiteLandmarkViews.size()>0){
						message.what = 11;
						handler.sendMessage(message);
					}else{
						message.what = 12;
						handler.sendMessage(message);
					}
					
				} else if (isLandmark.equals("false")) {
					String likeValue = "%" + searchString + "%";
					searchSites = dao.query4Like(colums, likeValue, likeValue);
					if(searchSites.size()>0){
						message.what = 00;
						handler.sendMessage(message);
					}else{
						message.what = 12;
						handler.sendMessage(message);
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				Util.t("搜索异常");
			} finally {
				progressDialog.dismiss();
			}
			Looper.loop(); // 非主线程中new
							// Handler()需要加Looper.prepare();和Looper.loop();
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 00:
				searchBikesiteAdapter = new SearchBikesiteAdapter(context,
						searchSites);
				searchListView.setAdapter(searchBikesiteAdapter);
				//Util.t("搜索完毕");
				break;
			case 11:
				searchBikesiteLandmarkAdapter = new SearchBikesiteLandmarkAdapter(
						context, bikeSiteLandmarkViews);
				searchListView.setAdapter(searchBikesiteLandmarkAdapter);
				//Util.t("搜索完毕");
				break;
			case 12:
				Util.t("抱歉未找到结果");
				finish();
				break;
			default:
				break;
			}
		}
	};

	public void doBack(View v) {
		finish();
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

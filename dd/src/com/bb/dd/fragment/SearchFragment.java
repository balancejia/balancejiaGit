package com.bb.dd.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.bb.dd.IndexMainActivity;
import com.bb.dd.R;
import com.bb.dd.SearchResultActivity;
import com.bb.dd.adapter.SearchBikesiteAdapter;
import com.bb.dd.adapter.SearchHistoryAdapter;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.dao.DaoFactory;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.SearchHistory;
import com.bb.dd.userdefined.CustomEditText;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;
import com.j256.ormlite.dao.Dao;

public class SearchFragment extends AbstractFragment {
	private static final String TAG = SearchFragment.class.getName();
	private ListView searchListView;
	private ListView searchHistoryListView;
	private SearchHistoryAdapter searchHistoryAdapter;
	private CustomEditText search_value;
	private Button main_search_bt;
	private RadioGroup radioGroup;
	private String isLandmark = "false";
	private List<SearchHistory> searchhHistories;
	private Dao<SearchHistory, Integer> searchHistoryDaos = DaoFactory
			.instant().getSearchHistoryDao();
	private SearchHistory searchHistory;
	boolean isExit = false;
	private List<BikeSite_Lite> searchSites = new ArrayList<BikeSite_Lite>();
	private static final int SEARCH_ET_IS_NULL = 0;
	private static final int SEARCH_FINISH = 1;
	private static final int SEARCH_FINISH_ERROR = 2;
	private static final int SEARCH_FOR_NULL = 3;
	private static final int SEARCH_HISTORY_FINISH = 4;
	private SearchBikesiteAdapter searchBikesiteAdapter;

	public SearchFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.search);
		initView();
		return rootView;
	}

	@Override
	public void closed() {

	}

	private void initView() {
		searchHistory = new SearchHistory();
		searchListView = (ListView) rootView
				.findViewById(R.id.main_search_listView);
		searchHistoryListView = (ListView) rootView
				.findViewById(R.id.main_search_history_listView);
		search_value = (CustomEditText) rootView
				.findViewById(R.id.main_search_text);
		searchBikesiteAdapter = new SearchBikesiteAdapter(_context, searchSites);
		searchListView.setAdapter(searchBikesiteAdapter);
		search_value.addTextChangedListener(new TextWatch());
		searchHistoryListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						try {
							searchHistoryDaos.deleteById(searchhHistories.get(
									arg2).getSearchHistoryId());
							searchhHistories.remove(searchhHistories.get(arg2));
							searchHistoryAdapter = new SearchHistoryAdapter(
									_context, searchhHistories);
							searchHistoryListView
									.setAdapter(searchHistoryAdapter);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return false;
					}
				});

		searchHistoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				SearchHistory history = searchhHistories.get(arg2);
				search_value.setText(history.getSearchHistoryName());
			}

		});
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				Intent intent_point = new Intent();
				intent_point.setClass(_context, IndexMainActivity.class);
				BikeSite_Lite bikeSite_Lite = searchSites.get(arg2);
				intent_point.putExtra("longitude", bikeSite_Lite.getSign4()
						.trim());
				intent_point.putExtra("latitude", bikeSite_Lite.getSign3()
						.trim());
				intent_point.putExtra("bikesiteName", bikeSite_Lite.getBikesiteName()
						.trim());
				intent_point.putExtra("bikesiteId", bikeSite_Lite.getBikesiteId()
						.trim());
				_context.startActivity(intent_point);
			}

		});
		search_value.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchHistoryThread searchHistoryThread = new SearchHistoryThread();
				new Thread(searchHistoryThread).start();

			}
		});
		main_search_bt = (Button) rootView.findViewById(R.id.main_search_bt);
		main_search_bt.setVisibility(View.GONE);
		main_search_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String searchValue = getEditValue(search_value);
				if ("".equalsIgnoreCase(searchValue)) {
					handler.sendEmptyMessage(SEARCH_ET_IS_NULL);
				} else {
					insertSearchHistory();
					Intent intent_search_result = new Intent(_context,
							SearchResultActivity.class);
					intent_search_result.putExtra("parameter", search_value
							.getText().toString().trim());
					intent_search_result.putExtra("byLandMark", isLandmark);
					_context.startActivity(intent_search_result);

				}

			}

		});
		radioGroup = (RadioGroup) rootView
				.findViewById(R.id.main_search_radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				RadioButton radioButton = (RadioButton) rootView
						.findViewById(radioButtonId);
				// 根据ID获取RadioButton的实例
				if (radioButton.getText().equals("附近标志物")) {
					isLandmark = "true";
					main_search_bt.setVisibility(View.VISIBLE);
					searchListView.setVisibility(View.GONE);
					searchHistoryListView.setVisibility(View.VISIBLE);
				} else {
					isLandmark = "false";
					main_search_bt.setVisibility(View.GONE);
					searchListView.setVisibility(View.VISIBLE);
					searchHistoryListView.setVisibility(View.GONE);

				}
			}
		});

	}

	private void insertSearchHistory() {
		int i = Cons.SEARCH_HISTORY;
		searchHistory.setSearchHistoryName(search_value.getText().toString());
		String updateTime = Util.getNow();
		searchHistory.setUpdateTime(updateTime);
		searchHistory.setSearchHistoryId(i);
		try {
			searchHistoryDaos.createOrUpdate(searchHistory);
			i++;
			Cons.SEARCH_HISTORY = i;
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private class TextWatch implements TextWatcher {

		public TextWatch() {
			super();
		}

		@Override
		public void afterTextChanged(Editable s) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			final String value = getEditValue(search_value);
			final BikeOverlayDao dao = new BikeOverlayDao();
			final String[] colums = { "bikesite_id", "bikesite_name" };

			new Thread() {
				@SuppressWarnings("unused")
				public void run() {

					if (null != value) {
						if ("".equals(value.trim())) {
							searchSites.clear();
							handler.sendEmptyMessage(SEARCH_FOR_NULL);
						} else {
							searchSites.clear();
							String likeValue = "%" + value + "%";
							List<BikeSite_Lite> sites = dao.query4Like(colums,
									likeValue, likeValue);
							if (null != sites && 0 != sites.size()) {
								searchSites.addAll(sites);
								sites.clear();
								sites = null;
								handler.sendEmptyMessage(SEARCH_FINISH);
							} else {
								handler.sendEmptyMessage(SEARCH_FINISH_ERROR);
							}

						}

					}
				};
			}.start();
		}

	}

	public String getEditValue(EditText ed) {
		return ed.getText().toString().trim();
	}

	private class SearchHistoryThread implements Runnable {
		private BikeOverlayDao dao;

		private SearchHistoryThread() {
			dao = new BikeOverlayDao();
		}

		@Override
		public void run() {
			Looper.prepare(); // 非主线程中new
								// Handler()需要加Looper.prepare();和Looper.loop();

			try {
				searchhHistories = dao.querySearchHistory();
				if (searchhHistories.size() > 0) {
					handler.sendEmptyMessage(SEARCH_HISTORY_FINISH);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Util.t("搜索异常");
			} finally {
				closedProgressDialog();
			}
			Looper.loop(); // 非主线程中new
							// Handler()需要加Looper.prepare();和Looper.loop();
		}

	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SEARCH_ET_IS_NULL:
				Util.t("请输入搜索关键字");
				searchSites.clear();
				searchBikesiteAdapter.setSites(searchSites);
				searchBikesiteAdapter.notifyDataSetChanged();
				break;
			case SEARCH_FINISH:
				searchBikesiteAdapter.setSites(searchSites);
				searchBikesiteAdapter.notifyDataSetChanged();
				break;
			case SEARCH_FINISH_ERROR:
				searchBikesiteAdapter.setSites(searchSites);
				searchBikesiteAdapter.notifyDataSetChanged();
				break;
			case SEARCH_FOR_NULL:
				Util.t("请输入搜索关键字");
				searchSites.clear();
				searchBikesiteAdapter.setSites(searchSites);
				searchBikesiteAdapter.notifyDataSetChanged();
				break;
			case SEARCH_HISTORY_FINISH:
				searchHistoryAdapter = new SearchHistoryAdapter(_context,
						searchhHistories);
				searchHistoryListView.setAdapter(searchHistoryAdapter);
				break;
			}
		}
	};
}

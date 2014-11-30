package com.bb.dd.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.IndexMainActivity;
import com.bb.dd.R;
import com.bb.dd.adapter.BikeSiteListAdapter;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.BikeSite_LiteView;
import com.bb.dd.service.BikeSiteInforService;
import com.bb.dd.util.BadHandler;
import com.topdt.application.entity.BikeSiteRealView;

public class BikeSiteListFragment extends AbstractFragment {
	private static final String TAG = BikeSiteListFragment.class.getName();
	private List<BikeSite_Lite> sites;
	public Map<String, BikeSiteRealView> areaBikeSiteRealView = new HashMap<String, BikeSiteRealView>();
	private ArrayList<BikeSite_LiteView> areaSmallSites = new ArrayList<BikeSite_LiteView>();
	private ArrayList<BikeSite_LiteView> areaMiddleSites = new ArrayList<BikeSite_LiteView>();
	private ArrayList<BikeSite_LiteView> areaBigSites = new ArrayList<BikeSite_LiteView>();
	private ArrayList<BikeSite_LiteView> areaOtherSites = new ArrayList<BikeSite_LiteView>();
	private List<String> groupList;
	private ArrayList<ArrayList<BikeSite_LiteView>> childList;
	private BikeSiteInforService bikeSiteInforService = new BikeSiteInforService();
	private static final int GET_SUCCESS = 0;
	private BikeSiteListAdapter adapter;
	private ExpandableListView listView;
	private TextView title;

	public BikeSiteListFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.bikesitelist);
		initView();
		return rootView;
	}

	@Override
	public void closed() {

	}

	private void initView() {
		title = (TextView) ((Activity) _context)
				.findViewById(R.id.top_tv_center);
		title.setText(R.string.top_tv_main);
		
		sites = IndexMainActivity.bikeSites;
		showProgressDialog();
		listView = (ExpandableListView) rootView
				.findViewById(R.id.bikesite_listview);
		listView.setGroupIndicator(null);
		
		adapter=new BikeSiteListAdapter(_context, groupList,
				childList);
		listView.setAdapter(adapter);
		
		initBikeSiteRealView();
		
		
		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				BikeSite_LiteView bikeSite_LiteView=(BikeSite_LiteView)childList.get(groupPosition).get(childPosition);
//				BikeSiteRealView bsv=bikeSite_LiteView.getBikeSiteRealView();
//				Intent intent_list = new Intent();
//				intent_list.setClass(_context,
//						BikeSiteDetialActivity.class);
//				intent_list.putExtra("bsv", bsv);
//				_context.startActivity(intent_list);
				Intent intent_point = new Intent();
				intent_point.setClass(_context, IndexMainActivity.class);
				intent_point.putExtra("longitude", bikeSite_LiteView.getBikeSite_Lite().getSign4()
						.trim());
				intent_point.putExtra("latitude", bikeSite_LiteView.getBikeSite_Lite().getSign3()
						.trim());
				intent_point.putExtra("bikesiteName", bikeSite_LiteView.getBikeSite_Lite().getBikesiteName()
						.trim());
				intent_point.putExtra("bikesiteId", bikeSite_LiteView.getBikeSite_Lite().getBikesiteId()
						.trim());
				_context.startActivity(intent_point);
				return true;
			}
		});
	}

	/**
	 * 初始化站点实时车辆信息
	 */
	private void initBikeSiteRealView() {
		new Thread(){

			@Override
			public void run() {
				String id = "";
				int size = sites.size();
				for (int i = 0; i < size; i++) {
					BikeSite_Lite lite = sites.get(i);
					if (i == size - 1) {
						id += lite.getBikesiteId();
					} else {
						id += lite.getBikesiteId() + "#";
					}
				}
				List<BikeSiteRealView> bikeSiteRealView = bikeSiteInforService
						.loadBikeSitesReal(id);
				if (bikeSiteRealView != null) {
					for (BikeSiteRealView obj : bikeSiteRealView) {
						if(obj!=null){
							areaBikeSiteRealView.put(obj.getId(), obj);
						}
					}
				}
				handler.sendEmptyMessage(GET_SUCCESS);
				
			}
			
			
			
		}.start();
	}
	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_SUCCESS:
				initListDate();
				break;
			}
		}
	};
	/**
	 * 初始化父/子菜单的list
	 */
	private void initListDate() {
		groupList= new ArrayList<String>();
		childList= new ArrayList<ArrayList<BikeSite_LiteView>>();
		GeoPoint myPosition = IndexMainActivity.curCenterPoint;
		if (sites.size() > 0) {
			for (int i = 0; i < sites.size(); i++) {
				BikeSite_Lite bikeSite_Lite = sites.get(i);
				Double latitude = Double.parseDouble(bikeSite_Lite.getSign4()
						.trim()) * 1E6;
				Double longitude = Double.parseDouble(bikeSite_Lite.getSign3()
						.trim()) * 1E6;
				GeoPoint sitePoint = new GeoPoint(longitude.intValue(),
						latitude.intValue());
				double realDistance = DistanceUtil.getDistance(myPosition,
						sitePoint)/1000;
				DecimalFormat df = new DecimalFormat("0.00");
				double distance = Double.valueOf(df.format(realDistance));
				BikeSite_LiteView bikeSite_LiteView = new BikeSite_LiteView();
				bikeSite_LiteView.setBikeSite_Lite(bikeSite_Lite);
				BikeSiteRealView bikeSiteRealView = areaBikeSiteRealView
						.get(bikeSite_Lite.getBikesiteId());
				if (bikeSiteRealView!=null) {
					bikeSite_LiteView.setBikeSiteRealView(bikeSiteRealView);
				}else{
					bikeSiteRealView=new BikeSiteRealView();
					bikeSiteRealView.setId(bikeSite_Lite.getBikesiteId());
					bikeSiteRealView.setName(bikeSite_Lite.getBikesiteName());
					bikeSite_LiteView.setBikeSiteRealView(bikeSiteRealView);
				}
				
				bikeSite_LiteView.setDistance(distance);
				if (distance <= 0.5) {
					areaSmallSites.add(bikeSite_LiteView);
				}
				if (distance > 0.5 && distance <=1) {
					areaMiddleSites.add(bikeSite_LiteView);
				}
				if (distance > 1 && distance <= 1.5) {
					areaBigSites.add(bikeSite_LiteView);
				}
				if (distance>1.5 && distance<=3) {
					areaOtherSites.add(bikeSite_LiteView);
				}
			}
			if (areaSmallSites.size() > 0) {
				sortList(areaSmallSites);
				groupList.add("距离:0-500米");
				childList.add(areaSmallSites);
			}
			if (areaMiddleSites.size() > 0) {
				sortList(areaMiddleSites);
				groupList.add("距离:500-1000米");
				childList.add(areaMiddleSites);

			}
			if (areaBigSites.size() > 0) {
				sortList(areaBigSites);
				groupList.add("距离:1000-1500米");
				childList.add(areaBigSites);
			}
			if (areaOtherSites.size() > 0) {
				sortList(areaOtherSites);
				groupList.add("距离:1500米以上");
				childList.add(areaOtherSites);

			}

		}
		adapter.setmGroupList(groupList);
		adapter.setmChildList(childList);
		adapter.notifyDataSetChanged();
		
		for (int i = 0; i < groupList.size(); i++) {
			listView.expandGroup(i);
		}
		
		closedProgressDialog();
	}
	public void sortList(List<BikeSite_LiteView> list){
		Collections.sort(list,new Comparator<BikeSite_LiteView>(){

			@Override
			public int compare(BikeSite_LiteView lhs,
					BikeSite_LiteView rhs) {
				Integer one=Integer.parseInt(new java.text.DecimalFormat("0").format(lhs.getDistance()*1000));
				Integer two=Integer.parseInt(new java.text.DecimalFormat("0").format(rhs.getDistance()*1000));
				return one.compareTo(two);
			} 
        }); 
	}
}

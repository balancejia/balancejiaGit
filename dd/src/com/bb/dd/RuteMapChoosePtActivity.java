package com.bb.dd;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.MapOverLay.BikeOverLayItem;
import com.bb.dd.MapOverLay.MyMapView;
import com.bb.dd.MapOverLay.MyOverlayItem;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.RuteView;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Bounds;
import com.bb.dd.util.BoundsUtil;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;
import com.topdt.application.entity.BikeSiteStatusView;

public class RuteMapChoosePtActivity extends Activity {
	private static final String TAG = RuteMapChoosePtActivity.class.getName();
	public final static int RETURN_CODE = 2;
	private MyMapView mMapView = null;
	private MapController mMapController = null;
	MKMapViewListener mMapListener = null;
	public static MKSearch mkSerach;
	public static View mi;
	public LocationClient mLocClient;
	public int locationResult;
	private Context context;
	private MKOfflineMap mOffline = null; // 离线地图

	private TextView topTitle;
	private RuteView ruteView = new RuteView();
	private String Flag;
	private static final int BIKE_REMAIN_ZEARO = 0;
	private static final int BIKE_REMAIN_NORMAL = 2;
	private static final int BIKE_REMAIN_HIGH = 3;
	private static final int MAP_BIKESITE_SEARCH = 8;
	public static GeoPoint curCenterPoint; // 当前地图中心点位置
	private Drawable _low, _normal, _high;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.rute_map_choose_point);

		context = RuteMapChoosePtActivity.this;
		Intent intent = getIntent();
		if (intent != null) {
			Flag = intent.getStringExtra("flag");
		}
		initView();
		initMapView();
		locClient(); // 定位设置
	}

	private void initMapView() {
		mMapView = (MyMapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		mMapController.enableClick(true);
		/**
		 * 将地图移动至指定点
		 * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index
		 * .html查询地理坐标 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
		 */

		GeoPoint point = new GeoPoint((int) (37.851969 * 1E6),
				(int) (112.56205 * 1E6));
		mMapController.setCenter(point);
		mMapController.setZoom(15);
		mkSerach = new MKSearch();
		mkSerach.init(BikeApplication.getInstance().mBMapManager,
				new mySearchListener());
		mOffline = new MKOfflineMap();
		mOffline.init(mMapController, new MyOfflineMapListener());
		mOffline.scan();
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				Util.l("地图移动完成，获取中心点位置中......");
				/**
				 * 在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
				 */
				int MyIconw = mi.getLeft() + mi.getWidth() / 2;
				int MyIconh = mi.getTop() + mi.getHeight() / 2;

				// 计算中心点坐标
				// GeoPoint point;
				GeoPoint curCenterPoint = mMapView.getProjection().fromPixels(
						MyIconw, MyIconh);
				if (curCenterPoint != null) {
					mkSerach.reverseGeocode(curCenterPoint);
					mMapView.refresh();

					Message msg = new Message();
					msg.what = MAP_BIKESITE_SEARCH;
					msg.obj = curCenterPoint;
					handler.sendMessage(msg);
				}
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过：
				 * mMapController.enableClick(true); 时，此回调才能被触发
				 * 
				 */
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * 地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}

			@Override
			public void onMapLoadFinish() {
				// TODO Auto-generated method stub
				
			}
		};
		mMapView.regMapViewListener(BikeApplication.getInstance().mBMapManager,
				mMapListener);
	}

	private void initView() {
		_low = getResources().getDrawable(R.drawable._low2);
		_normal = getResources().getDrawable(R.drawable._normal2);
		_high = getResources().getDrawable(R.drawable._high2);
		topTitle = (TextView) findViewById(R.id.top_tv_center);
		topTitle.setText("选取地图上的点");
		TextView imageView = (TextView) findViewById(R.id.top_iv_right);
		imageView.setOnClickListener(new mySearchListener());
		mi = (RelativeLayout) findViewById(R.id.map_my_icon_layout); // map_my_icon
	}

	/**
	 * 返回
	 */
	public void doBack(View v) {
		finish();
	}

	class MyOfflineMapListener implements MKOfflineMapListener {
		@Override
		public void onGetOfflineMapState(int type, int state) {
			switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				mOffline.getUpdateInfo(state);
			}
				break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				Log.d("OfflineDemo",
						String.format("add offlinemap num:%d", state));
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				Log.d("OfflineDemo", String.format("new offlinemap ver"));
				break;
			}
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if (mLocClient != null) {
			mLocClient.stop();
		}
//		mMapView.destroy();
		super.onDestroy();
	}

	/**
	 * 搜索监听
	 * 
	 * @author Administrator
	 * 
	 */
	class mySearchListener implements OnClickListener, MKSearchListener {
		@Override
		public void onClick(View v) {
			// int MyIconw = BikeSiteIcon.w + 15;
			// int MyIconh = BikeSiteIcon.h - 25;
			int MyIconw = mi.getLeft() + mi.getWidth() / 2 + 2;
			int MyIconh = mi.getTop() + mi.getHeight() / 2 - 2;

			// 计算中心点坐标
			GeoPoint curCenterPoint = mMapView.getProjection().fromPixels(
					MyIconw, MyIconh);
			/*
			 * Intent intent=new Intent(); intent.putExtra("longitude",
			 * String.valueOf(curCenterPoint.getLongitudeE6()/1e6));
			 * intent.putExtra("latitude",
			 * String.valueOf(curCenterPoint.getLatitudeE6()/1e6));
			 * setResult(RETURN_CODE,intent);
			 */
			mkSerach.reverseGeocode(curCenterPoint);
			if (Flag.equals("start")) {
				ruteView.setStartlatitude(Double.valueOf(curCenterPoint
						.getLatitudeE6()));
				ruteView.setStartlongitude(Double.valueOf(curCenterPoint
						.getLongitudeE6()));
				ruteView.setFlag(1);
			} else {
				ruteView.setEndlatitude(Double.valueOf(curCenterPoint
						.getLatitudeE6()));
				ruteView.setEndlongitude(Double.valueOf(curCenterPoint
						.getLongitudeE6()));
				ruteView.setFlag(2);
			}
			int flag=ruteView.getFlag();
			boolean intentFlag=false;
			if(flag==1){
				if(ruteView.getStartAddress()!=null){
					intentFlag=true;
				}else{
					intentFlag=false;
				}
			}else{
				if(ruteView.getEndAddress()!=null){
					intentFlag=true;
				}else{
					intentFlag=false;
				}
			}
			if(intentFlag){
				Intent intent = new Intent();
				intent.setClass(context, IndexMainActivity.class);
				intent.putExtra("ruteView", ruteView);
				startActivity(intent);
			}else{
				Util.t("正在加载当前位置信息，请稍后再试");
			}
			

			finish();
		}

		@Override
		public void onGetAddrResult(MKAddrInfo info, int arg1) {
			Util.t(info.strAddr);
			if (Flag.equals("start")) {
				ruteView.setFlag(1);
				ruteView.setStartAddress(info.strAddr);
			} else {
				ruteView.setFlag(2);
				ruteView.setEndAddress(info.strAddr);
			}

		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}
	}

	private void locClient() {
		// 定位
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(new MyLocationListener());
		// 参数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(0);
		option.disableCache(true); // 禁止启用缓存定位
		// 设置定位
		LocationManager locationManager = (LocationManager) this
				.getSystemService(this.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
			option.setPriority(option.GpsFirst);
		} else {
			option.setPriority(option.NetWorkFirst);
		}
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	class MyLocationListener implements BDLocationListener {
		LocationData locData = new LocationData();

		@Override
		public void onReceiveLocation(BDLocation location) {
			GeoPoint point2 = Cons.locPoint;
			mkSerach.reverseGeocode(point2);
			mMapController.animateTo(point2);
			if (Flag.equals("start")) {
				ruteView.setFlag(1);
				ruteView.setStartlatitude(Double.valueOf(point2.getLatitudeE6()));
				ruteView.setStartlongitude(Double.valueOf(point2
						.getLongitudeE6()));
			} else {
				ruteView.setFlag(2);
				ruteView.setEndlatitude(Double.valueOf(point2.getLatitudeE6()));
				ruteView.setEndlongitude(Double.valueOf(point2.getLongitudeE6()));
			}
			if (point2 != null) {
				mkSerach.reverseGeocode(point2);
				mMapView.refresh();

				Message msg = new Message();
				msg.what = MAP_BIKESITE_SEARCH;
				msg.obj = point2;
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {

		}
	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MAP_BIKESITE_SEARCH:
				GeoPoint point = (GeoPoint) msg.obj;
				mapBikeSiteSearch(point);
				break;
			}
		}
	};

	// 根据地图位置进行搜索
	private void mapBikeSiteSearch(GeoPoint point) {
		loadBikeSite(point); // 根据当前位置进行搜索
		mMapController.setCenter(point);
	}

	/**
	 * 取得中心点 并搜索站点
	 */
	public synchronized void loadBikeSite(GeoPoint pt) {
		if (IndexMainActivity.bikeOverLayItem != null
				|| IndexMainActivity.areaGraphicsOverlay != null) {
			if (IndexMainActivity.bikeOverLayItem != null)
				mMapView.getOverlays()
						.remove(IndexMainActivity.bikeOverLayItem);
			if (IndexMainActivity.areaGraphicsOverlay != null)
				mMapView.getOverlays().remove(
						IndexMainActivity.areaGraphicsOverlay);
			mMapView.refresh();
		}
		IndexMainActivity.bikeOverLayItem = new BikeOverLayItem(null, mMapView,
				null, false);
		mMapView.getOverlays().add(IndexMainActivity.bikeOverLayItem);

		// int MyIconw = MyIcon.w + 19;
		// int MyIconh = MyIcon.h + 60;
		int MyIconw = mi.getLeft() + mi.getWidth() / 2;
		int MyIconh = mi.getTop() + mi.getHeight() / 2;

		// 计算中心点坐标
		// GeoPoint point;
		if (pt != null) {
			curCenterPoint = pt;
		} else {
			curCenterPoint = mMapView.getProjection().fromPixels(MyIconw,
					MyIconh);
		}

		// 绘制范围
		int area = 1500;
		IndexMainActivity.areaGraphicsOverlay.removeAll();
		Graphic graphicArea = IndexMainActivity
				.drawCircle(curCenterPoint, area);// 添加圆
		Graphic graphicLine = IndexMainActivity.drawCircleLine(curCenterPoint,
				area);// 添加圆边
		if (graphicArea != null) {
			// areaGraphicsOverlay.setData(drawCircleCenter(curCenterPoint,
			// 20)); // 添加中心参照物
			IndexMainActivity.areaGraphicsOverlay.setData(graphicArea);
			IndexMainActivity.areaGraphicsOverlay.setData(graphicLine);
			mMapView.getOverlays().add(IndexMainActivity.areaGraphicsOverlay);
		}
		// 加载站点覆盖物
		queryBikeSiteByPoint(curCenterPoint, area);

		if (IndexMainActivity.bikeSites != null) {
			addOverlay(); // 描点
		} else {
			mMapView.refresh();
		}
	}

	/**
	 * 根据中心点位置和范围得到站点数据
	 * 
	 * @param point
	 * @param area
	 */
	private void queryBikeSiteByPoint(GeoPoint point, int area) {
		int num = -300; // 纠偏参数
		if (IndexMainActivity.bikeSites != null)
			IndexMainActivity.bikeSites.clear();
		BikeOverlayDao bikeOverlayDao = new BikeOverlayDao();
		if (area > 0) {
			Bounds bounds = BoundsUtil.conversion(
					Double.valueOf(point.getLatitudeE6() / 1E6),
					Double.valueOf(point.getLongitudeE6() / 1E6), area);
			List<BikeSite_Lite> BikeSites = bikeOverlayDao
					.queryBikeSitesByBounds(bounds);
			for (BikeSite_Lite bikeSite_Lite : BikeSites) {
				Double longitude = Double.parseDouble(bikeSite_Lite.getSign4()
						.trim()) * 1E6;
				Double latitude = Double.parseDouble(bikeSite_Lite.getSign3()
						.trim()) * 1E6;
				GeoPoint sitePoint = new GeoPoint(latitude.intValue(),
						longitude.intValue());
				double length_num = DistanceUtil.getDistance(point, sitePoint);
				if (length_num <= area + num) {
					IndexMainActivity.bikeSites.add(bikeSite_Lite);
				}
			}
			// bikeSites = bikeOverlayDao.queryBikeSitesByBounds(bounds);
		} else {
			IndexMainActivity.bikeSites = bikeOverlayDao.queryBikeSites();
		}

		String str = "1500米";

		if (!str.equals(""))
			str = "搜索“" + str + "”范围，";
		Util.t(str + "找到站点："
				+ String.valueOf(IndexMainActivity.bikeSites.size()) + "个");
	}

	/**
	 * 在地图上添加不同图标绘制的覆盖物
	 */
	private void addOverlay() {
		if (IndexMainActivity.bikeOverLayItem != null) {
			mMapView.getOverlays().remove(IndexMainActivity.bikeOverLayItem);
			mMapView.refresh();
		}
		IndexMainActivity.bikeOverLayItem = new BikeOverLayItem(null, mMapView,
				null, false);
		mMapView.getOverlays().add(IndexMainActivity.bikeOverLayItem);
		int size = IndexMainActivity.bikeSites.size();
		for (int i = 0; i < size; i++) {
			BikeSite_Lite bike = IndexMainActivity.bikeSites.get(i);
			Double longitude = Double.parseDouble(bike.getSign4().trim()) * 1E6;
			Double latitude = Double.parseDouble(bike.getSign3().trim()) * 1E6;
			GeoPoint point = new GeoPoint(latitude.intValue(),
					longitude.intValue());

			MyOverlayItem myOverlayItem = new MyOverlayItem(point,
					bike.getBikesiteName(), bike.getBikesiteId());
			String id = bike.getBikesiteId();
			int stat = getSiteStat(id); // 某一站点车辆最新状态

			// 获得状态进行判断
			switch (stat) {
			// 0为蓝色
			case BIKE_REMAIN_ZEARO:
				myOverlayItem.setMarker(_low);
				break;
			// 正常为绿色
			case BIKE_REMAIN_NORMAL:
				myOverlayItem.setMarker(_normal);
				break;
			// 满为红色
			case BIKE_REMAIN_HIGH:
				myOverlayItem.setMarker(_high);
				break;
			default:
				myOverlayItem.setMarker(_low);
				break;
			}
			IndexMainActivity.bikeOverLayItem.addItem(myOverlayItem);
		}
		mMapView.refresh();
	}

	/**
	 * 某一站点车辆最新状态
	 * 
	 * @param id
	 *            站点ID
	 * @return
	 */
	private int getSiteStat(String id) {
		BikeSiteStatusView obj = (BikeSiteStatusView) Cons.remainBikeSitesStatus
				.get(id);
		if (obj != null) {
			int status = obj.getStatus();
			return status;
		}
		return 0;
	}

}

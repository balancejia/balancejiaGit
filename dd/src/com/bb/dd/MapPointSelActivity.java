package com.bb.dd;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.MapOverLay.MapPointOverLayItem;
import com.bb.dd.MapOverLay.MyMapView;
import com.bb.dd.MapOverLay.MyOverlayItem;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.util.BadHandler;

public class MapPointSelActivity extends Activity {
	private static final String TAG = MapPointSelActivity.class.getName();
	public final static int RETURN_CODE=2;
	private static final int MAP_LOAD_DATA_FINISH = 1;
	
	private MyMapView mMapView = null;
	private MapController mMapController = null;
	MKMapViewListener mMapListener = null;
	public MapPointOverLayItem bikeOverLayItem; // 站点覆盖物
	public AlterView pop_alterView;
	public View itemPopView = null; // 站点提示
	public RelativeLayout pop_stationClose;
	
//	public static BikeSiteIcon mi;
//	public static View popview;
	public static View mi,popview;
	public LocationClient mLocClient;
	public int locationResult;
	
	private Context context;
	private String longitude,latitude ;	  //请求定位点
	private String longitude_old,latitude_old ;	  //请求定位点
	private String bikesiteId; //请求定位点
	private BikeSite_Lite bikeSite_Lite; 
	private MKOfflineMap mOffline = null; // 离线地图
	
	
	private TextView topTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.map_point);

		context = MapPointSelActivity.this;
		Intent intent = getIntent();
		bikesiteId = intent.getStringExtra("bikesiteId").trim();
		longitude = intent.getStringExtra("longitude").trim();
		latitude = intent.getStringExtra("latitude").trim();
		longitude_old = intent.getStringExtra("longitude_old").trim();
		latitude_old = intent.getStringExtra("latitude_old").trim();
		searchBikeSite();
		initView();
		initListener();
		initMapView();
		locClient(); // 定位设置
		locate(); // 定位请求
	}
	
	/**
	 * 查询站点
	 *
	 */
	private void searchBikeSite() {
		new Thread() {
			private BikeOverlayDao dao=new BikeOverlayDao();
			public void run() {
				List<BikeSite_Lite> sites = dao.queryBikeSitesById(bikesiteId);
				if (null != sites && 0 != sites.size()) {
					bikeSite_Lite = sites.get(0);
				}
				
				handler.sendEmptyMessage(MAP_LOAD_DATA_FINISH);
			}
		}.start();

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

		GeoPoint point = new GeoPoint((int) (37.851969 * 1E6),(int) (112.56205 * 1E6));
		mMapController.setCenter(point);
		mMapController.setZoom(16);
		mMapView.regMapViewListener(BikeApplication.getInstance().mBMapManager,mMapListener);
		
		GeoPoint point2 = new GeoPoint((int) (37.851969 * 1E6),(int) (112.56205 * 1E6));
		mMapController.animateTo(point2);
		
		mOffline = new MKOfflineMap();
		mOffline.init(mMapController, new MyOfflineMapListener());
		int num = mOffline.scan();
	}

	private void initView() {
		topTitle = (TextView) findViewById(R.id.top_tv_center);
		topTitle.setText("站点定位");
		TextView imageView = (TextView) findViewById(R.id.top_iv_right);
		imageView.setOnClickListener(new mySearchListener());
	}

	/**
	 * 返回
	 */
	public void doBack(View v) {
		finish();
	}

	
	//描点
	private void addOverlay() {
		if (bikeSite_Lite==null) return;
		
		if (bikeOverLayItem != null) {
			mMapView.getOverlays().remove(bikeOverLayItem);
			mMapView.refresh();
		}
		bikeOverLayItem = new MapPointOverLayItem(null, mMapView);
		mMapView.getOverlays().add(bikeOverLayItem);
		
		Double dlongitude = Double.parseDouble(longitude_old) * 1E6;
		Double dlatitude = Double.parseDouble(latitude_old) * 1E6;
		GeoPoint point = new GeoPoint(dlatitude.intValue(),dlongitude.intValue());
		
		MyOverlayItem myOverlayItem = new MyOverlayItem(point,bikeSite_Lite.getBikesiteName(), bikeSite_Lite.getBikesiteId());
		myOverlayItem.setMarker(getResources().getDrawable(R.drawable._low));
		bikeOverLayItem.addItem(myOverlayItem);
		mMapView.refresh();
		
		mi = (RelativeLayout) findViewById(R.id.map_my_icon_layout); //map_my_icon
//		ImageView map_my_icon = (ImageView) findViewById(R.id.map_myicon);
//		try {
//			Drawable drawable = getResources().getDrawable(R.drawable._low);
//			map_my_icon.setImageDrawable(drawable);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		popview = (RelativeLayout) findViewById(R.id.map_myalert_icon_layout);
		ImageView imageButton = (ImageView)findViewById(R.id.map_bubbleSearch);
		imageButton.setOnClickListener(new mySearchListener());
//		mi = new BikeSiteIcon(this);
//		this.addContentView(mi, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
//		
//		popview = LayoutInflater.from(this).inflate(R.layout.pop_view, null);
//		TextView TestText = (TextView) popview.findViewById(R.id.map_bubbleTitle);
//		TestText.setText(bikeSite_Lite.getBikesiteName());
//		ImageView imageButton = (ImageView) popview.findViewById(R.id.map_bubbleSearch);
//		imageButton.setOnClickListener(new mySearchListener());
//		
//		RelativeLayout relativeLayout = new RelativeLayout(context); // 就是当前的页面的布局
//		relativeLayout.addView(popview); // 加入新的view
//		relativeLayout.setPadding(180, 330, 0, 0); // 设置位置
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		relativeLayout.setLayoutParams(layoutParams); // 新的view的参数
//		this.addContentView(relativeLayout, layoutParams); // 加入新的view
	}
	


	
	public void initListener() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
					//mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
			}

			@Override
			public void onMapAnimationFinish() {
			}

			@Override
			public void onMapLoadFinish() {
				// TODO Auto-generated method stub
				
			}
		};
	}
	

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MAP_LOAD_DATA_FINISH:
				addOverlay(); // 描点
				break;
			}
		}
	};
	
	class MyOfflineMapListener implements MKOfflineMapListener {
		@Override
		public void onGetOfflineMapState(int type, int state) {
			switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				MKOLUpdateElement update = mOffline.getUpdateInfo(state);
				// mText.setText(String.format("%s : %d%%",
				// update.cityName,update.ratio));
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
		mMapView.destroy();
		// 退出时销毁定位
		if (mLocClient != null) {
			mLocClient.stop();
		}
		super.onDestroy();
	}
	
	private static class AlterView {
		public TextView map_pop_stationId; // 站点ID
		public TextView map_pop_stationName; // 站点名称
		public ProgressBar firstBar; //
		public ProgressBar secondBar; //
		public TextView map_pop_stationReal_bikepos; // 总量车辆
		public TextView map_pop_stationReal_usebike; // 可用车辆
		public TextView map_pop_stationReal_usepos; // 可还车辆
		public ImageView map_pop_station_line; 
		public ImageView map_pop_station_search; 
		public ImageView map_pop_station_focus; 
	}
	
	/**
	 * 搜索监听
	 * 
	 * @author Administrator
	 * 
	 */
	class mySearchListener implements OnClickListener {
		@Override
		public void onClick(View v) {
//			int MyIconw = BikeSiteIcon.w + 15;
//			int MyIconh = BikeSiteIcon.h - 25;
			int MyIconw = mi.getLeft() + mi.getWidth()/2 + 2;
			int MyIconh = mi.getTop() + mi.getHeight()/2 - 2;

			// 计算中心点坐标
			GeoPoint curCenterPoint = mMapView.getProjection().fromPixels(MyIconw, MyIconh);
			Intent intent=new Intent();
			intent.putExtra("longitude", String.valueOf(curCenterPoint.getLongitudeE6()/1e6));
			intent.putExtra("latitude", String.valueOf(curCenterPoint.getLatitudeE6()/1e6));
			setResult(RETURN_CODE,intent);
			finish();
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
	
	/**
	 * 我的位置
	 */
	private void locate() {
		if (mLocClient != null && mLocClient.isStarted()) {
			locationResult = mLocClient.requestLocation();
		} else {
			Log.d("LocSDK3:", "mLocClient is null or not started");
		}
	}
	
	class MyLocationListener implements BDLocationListener {
		LocationData locData = new LocationData();

		@Override
		public void onReceiveLocation(BDLocation location) {
			mMapView.refresh();
			int lon = (int) ((Double.parseDouble(longitude)) * 1E6);//112.56205
		    int lat = (int) ((Double.parseDouble(latitude)) * 1E6);//37.851969
			GeoPoint point2 = new GeoPoint(lat,lon);
			mMapController.animateTo(point2);
		}

		@Override
		public void onReceivePoi(BDLocation location) {

		}
	}
}

package com.bb.dd;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.MapOverLay.MapPointOverLayItem;
import com.bb.dd.MapOverLay.MyMapView;
import com.bb.dd.MapOverLay.MyOverlayItem;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.service.BikeSiteInforService;
import com.bb.dd.util.BadHandler;
import com.topdt.application.entity.BikeSiteRealView;
import com.topdt.application.entity.BikeSiteStatusView;

public class MapPointActivity extends Activity {
	private static final String TAG = MapPointActivity.class.getName();
	private static final int BIKE_REMAIN_ZEARO = 0;
	private static final int BIKE_REMAIN_NORMAL = 2;
	private static final int BIKE_REMAIN_HIGH = 3;
	private static final int MAP_LOA_DDATA_FINISH = 1;
	
	private MyMapView mMapView = null;
	private MapController mMapController = null;
	MKMapViewListener mMapListener = null;
	public MapPointOverLayItem bikeOverLayItem; // 站点覆盖物
	public View itemPopView = null; // 站点提示
	public AlterView pop_alterView;
	public RelativeLayout pop_stationClose;
	
	public static View mi,popview;
	private Context context;
	public String bikesiteId ;	  //请求定位点ID
	private BikeSite_Lite bikeSite_Lite; 
	private BikeSiteRealView bikeSiteRealView;
	private BikeSiteStatusView bikeSiteStatusView;
	private MKOfflineMap mOffline = null; // 离线地图
	
	private TextView topTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.map_point);
		context = MapPointActivity.this;
		Intent intent = getIntent();
		bikesiteId = intent.getStringExtra("bikesiteId");
		searchBikeSite();
		initView();
		initListener();
		initMapView();
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
		
		mOffline = new MKOfflineMap();
		mOffline.init(mMapController, new MyOfflineMapListener());
		int num = mOffline.scan();
		
		mi = (RelativeLayout) findViewById(R.id.map_my_icon_layout); //map_my_icon
		popview = (RelativeLayout) findViewById(R.id.map_myalert_icon_layout);
		mi.setVisibility(View.GONE);
		popview.setVisibility(View.GONE);
	}

	private void initView() {
		topTitle = (TextView) findViewById(R.id.top_tv_center);
		topTitle.setText("站点详情");
		View imageView = (View) findViewById(R.id.top_iv_right);
		imageView.setVisibility(View.GONE);
	}

	/**
	 * 返回
	 */
	public void doBack(View v) {
		finish();
	}

	/**
	 * 查询站点
	 *
	 */
	private void searchBikeSite() {
		new Thread() {
			private BikeOverlayDao dao=new BikeOverlayDao();
			private BikeSiteInforService bikeSiteInforService = new BikeSiteInforService();
			public void run() {
				List<BikeSite_Lite> sites = dao.queryBikeSitesById(bikesiteId);
				if (null != sites && 0 != sites.size()) {
					bikeSite_Lite = sites.get(0);
				}
				
				List<BikeSiteRealView> bikeSiteReals = bikeSiteInforService.loadBikeSitesReal(bikesiteId);
				if (bikeSiteReals != null) {
					bikeSiteRealView = bikeSiteReals.get(0);
				}
				
				List<BikeSiteStatusView> bikeSiteStatus = bikeSiteInforService.loadBikeSitesAnalysis(bikesiteId);
				if (bikeSiteStatus != null) {
					bikeSiteStatusView = bikeSiteStatus.get(0);
				}
				handler.sendEmptyMessage(MAP_LOA_DDATA_FINISH);
			}
		}.start();

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

		Double longitude = Double.parseDouble(bikeSite_Lite.getSign4().trim()) * 1E6;
		Double latitude = Double.parseDouble(bikeSite_Lite.getSign3().trim()) * 1E6;
		GeoPoint point = new GeoPoint(latitude.intValue(),longitude.intValue());
		
		MyOverlayItem myOverlayItem = new MyOverlayItem(point,bikeSite_Lite.getBikesiteName(), bikeSite_Lite.getBikesiteId());
		int stat = bikeSiteStatusView.getStatus(); // 某一站点车辆最新状态
		// 获得状态进行判断
		switch (stat) {
		// 0为蓝色
		case BIKE_REMAIN_ZEARO:
			myOverlayItem.setMarker(getResources().getDrawable(R.drawable._low2));
			break;
		// 正常为绿色
		case BIKE_REMAIN_NORMAL:
			myOverlayItem.setMarker(getResources().getDrawable(R.drawable._normal2));
			break;
		// 满为红色
		case BIKE_REMAIN_HIGH:
			myOverlayItem.setMarker(getResources().getDrawable(R.drawable._high2));
			break;
		default:
			myOverlayItem.setMarker(getResources().getDrawable(R.drawable._normal2));
			break;
		}
		
		onPrepareDialog(point);
		bikeOverLayItem.addItem(myOverlayItem);
		mMapView.refresh();
		mMapController.animateTo(point);
	}
	
	/**
	 * 站点提示信息
	 */
	public void onPrepareDialog(GeoPoint point) {
		pop_alterView = new AlterView();
		itemPopView = LayoutInflater.from(this).inflate(R.layout.pop_item_view,null);
		pop_alterView.map_pop_stationName = (TextView) itemPopView.findViewById(R.id.map_pop_stationName);
		pop_alterView.map_pop_stationId = (TextView) itemPopView.findViewById(R.id.map_pop_stationId_Name);
		pop_alterView.map_pop_stationName.setText(bikeSiteRealView.getName());
		setTextBikeSiteId(bikesiteId);
		pop_stationClose = (RelativeLayout) itemPopView.findViewById(R.id.map_pop_stationClose);
		pop_stationClose.setVisibility(View.GONE);

		pop_alterView.firstBar = (ProgressBar) itemPopView.findViewById(R.id.map_pop_stationReal_usebike_progress);
		pop_alterView.secondBar = (ProgressBar) itemPopView.findViewById(R.id.map_pop_stationReal_usepos_progress);
		pop_alterView.firstBar.setVisibility(View.GONE);
		pop_alterView.secondBar.setVisibility(View.GONE);
		
		pop_alterView.map_pop_stationReal_usebike = (TextView) itemPopView.findViewById(R.id.map_pop_stationReal_usebike);
		pop_alterView.map_pop_stationReal_usepos = (TextView) itemPopView.findViewById(R.id.map_pop_stationReal_usepos);
		pop_alterView.map_pop_stationReal_usebike.setText(String.valueOf(bikeSiteRealView.getCanusebikecount()));
		pop_alterView.map_pop_stationReal_usepos.setText(String.valueOf(bikeSiteRealView.getCanuseposcount()));
		ImageView focus= (ImageView) itemPopView.findViewById(R.id.map_pop_station_focus);
		ImageView line= (ImageView) itemPopView.findViewById(R.id.map_pop_station_line);
		ImageView search= (ImageView) itemPopView.findViewById(R.id.map_pop_station_search);
		ImageView error= (ImageView) itemPopView.findViewById(R.id.map_pop_station_error);
		focus.setVisibility(View.GONE);
		line.setVisibility(View.GONE);
		search.setVisibility(View.GONE);
		error.setVisibility(View.GONE);

		// 创建布局参数
		MapView.LayoutParams layoutParam = new MapView.LayoutParams(
		// 控件宽,继承自ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// 控件高,继承自ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// 使控件固定在某个地理位置
				point, 0, -55,
				// 控件对齐方式
				MapView.LayoutParams.BOTTOM_CENTER);
		// 添加View到MapView中
		mMapView.addView(itemPopView, layoutParam);
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
					mMapController.animateTo(mapPoiInfo.geoPt);
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
			case MAP_LOA_DDATA_FINISH:
				addOverlay(); // 描点
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
		super.onDestroy();
	}
	private void setTextBikeSiteId(String text) {
		String uTextBikeSiteId =context.getResources().getString(R.string.map_pop_stationId_Name);
		uTextBikeSiteId = String.format(uTextBikeSiteId, text);
		pop_alterView.map_pop_stationId.setText(uTextBikeSiteId);
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
}

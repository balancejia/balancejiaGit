package com.bb.dd;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.modle.RuteView;
import com.bb.dd.util.BMapUtil;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;

/**
 * 此用来展示如何进行步行路线搜索并在地图使用RouteOverlay、TransitOverlay绘制 同时展示如何进行节点浏览并弹出泡泡
 * 
 */
public class RuteMapActivity extends Activity {
	private static final String TAG = RuteMapActivity.class.getName();
	private RuteView ruteView;
	// 浏览路线节点相关
	Button mBtnPre = null;// 上一个节点
	Button mBtnNext = null;// 下一个节点
	int nodeIndex = -2;// 节点索引,供浏览节点时使用
	MKRoute route = null;// 保存驾车/步行路线数据的变量，供浏览节点时使用
	TransitOverlay transit = null;// 保存公交路线图层数据的变量，供浏览节点时使用
	int searchType = -1;// 记录搜索的类型，区分驾车/步行和公交
	private PopupOverlay pop = null;// 弹出泡泡图层，浏览节点时使用
	private TextView popupText = null;// 泡泡view
	private View viewCache = null;
	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MyRouteMapView mMapView = null; // 地图View
	private MapController mMapController = null;
	// 搜索相关
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private MKOfflineMap mOffline = null; // 离线地图
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		BikeApplication app = (BikeApplication) this.getApplication();
		setContentView(R.layout.rute_map);
		Intent intent=getIntent();
		ruteView=(RuteView)intent.getSerializableExtra("ruteView");
		Cons.ruteList=new ArrayList<String>();
		// 初始化地图
		mMapView = (MyRouteMapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		mMapView.setBuiltInZoomControls(false);
		GeoPoint point = new GeoPoint((int) (37.851969 * 1E6),
				(int) (112.56205 * 1E6));
		mMapView.getController().setCenter(point);
		mMapView.getController().setZoom(15);
		mMapView.getController().enableClick(true);
		mOffline = new MKOfflineMap();
		mOffline.init(mMapController, new MyOfflineMapListener());
		mOffline.scan();
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		OnClickListener nodeClickListener = new OnClickListener() {
			public void onClick(View v) {
				// 浏览路线节点
				nodeClick(v);
			}
		};
		mBtnPre.setOnClickListener(nodeClickListener);
		mBtnNext.setOnClickListener(nodeClickListener);
		// 创建 弹出泡泡图层
		createPaopao();
		// 初始化搜索模块，注册事件监听
		mSearch = new MKSearch();
		mSearch.init(app.mBMapManager, new MKSearchListener() {
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				// 起点或终点有歧义，需要选择具体的城市列表或地址列表
				if (error == MKEvent.ERROR_ROUTE_ADDR) {
					// 遍历所有地址
					// ArrayList<MKPoiInfo> stPois =
					// res.getAddrResult().mStartPoiList;
					// ArrayList<MKPoiInfo> enPois =
					// res.getAddrResult().mEndPoiList;
					// ArrayList<MKCityListInfo> stCities =
					// res.getAddrResult().mStartCityList;
					// ArrayList<MKCityListInfo> enCities =
					// res.getAddrResult().mEndCityList;
					return;
				}
				if (error != 0 || res == null) {
					Util.t("抱歉,未找到结果");
					finish();
					return;
				}

				searchType = 2;
				RouteOverlay routeOverlay = new RouteOverlay(
						RuteMapActivity.this, mMapView);
				// 此处仅展示一个方案作为示例
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				// 清除其他图层
				mMapView.getOverlays().clear();
				// 添加路线图层
				mMapView.getOverlays().add(routeOverlay);
				// 执行刷新使生效
				mMapView.refresh();
				// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
				// mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
				// routeOverlay.getLonSpanE6());
				// 移动地图到起点
				mMapView.getController().animateTo(res.getStart().pt);
				// 将路线数据保存给全局变量
				route = res.getPlan(0).getRoute(0);
				// 重置路线节点索引，节点浏览时使用
				nodeIndex = -1;
				mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
				// 获得详细信息
				ArrayList<ArrayList<GeoPoint>> point1 = res.getPlan(0)
						.getRoute(0).getArrayPoints();
				Cons.ruteList=new ArrayList<String>();
				for (int i = 0; i < point1.size(); i++) {
					StringBuffer sb = new StringBuffer();
					sb.append(i + 1 + ":")
							.append(res.getPlan(0).getRoute(0).getStep(i)
									.getContent()).append("\n");
					Cons.ruteList.add(sb.toString());
				}

			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetPoiDetailSearchResult(int type, int iError) {
			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {

			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0,
					int arg1) {

			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub
				
			}
		});
		searchWalking();
	}

	/**
	 * 发起路线规划搜索
	 * 
	 * @param v
	 */
	public void searchWalking() {
		if (pop != null) {
			pop.hidePop();
		}
		// 重置浏览节点的路线数据
		route = null;
		transit = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		if(ruteView.getEndlatitude()==null||ruteView.getEndlongitude()==null||ruteView.getStartlatitude()==null||ruteView.getStartlongitude()==null){
			Util.t("抱歉,未找到结果");
			finish();
		}else{
			MKPlanNode stNode = new MKPlanNode();
			stNode.pt = new GeoPoint(ruteView.getStartlatitude().intValue(),
					ruteView.getStartlongitude().intValue());
			MKPlanNode enNode = new MKPlanNode();
			enNode.pt = new GeoPoint(ruteView.getEndlatitude().intValue(),
					ruteView.getEndlongitude().intValue());
			mSearch.walkingSearch(null, stNode, null, enNode);
			
		}
	
	}

	/**
	 * 节点浏览
	 * 
	 * @param v
	 */
	public void nodeClick(View v) {
		viewCache = getLayoutInflater()
				.inflate(R.layout.rute_custom_text_view, null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		if (searchType == 2) {
			// 步行，节点浏览方法
			if (nodeIndex < -1 || route == null
					|| nodeIndex >= route.getNumSteps())
				return;

			// 上一个节点
			if (mBtnPre.equals(v) && nodeIndex > 0) {
				// 索引减
				nodeIndex--;
				// 移动到指定索引的坐标
				mMapView.getController().animateTo(
						route.getStep(nodeIndex).getPoint());
				// 弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText), route
						.getStep(nodeIndex).getPoint(), 5);
			}
			// 下一个节点
			if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps() - 1)) {
				// 索引加
				nodeIndex++;
				// 移动到指定索引的坐标
				mMapView.getController().animateTo(
						route.getStep(nodeIndex).getPoint());
				// 弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText), route
						.getStep(nodeIndex).getPoint(), 5);
			}
		}
	}
	/**
	 * 创建弹出泡泡图层
	 */
	public void createPaopao() {

		// 泡泡点击响应回调
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
		};
		pop = new PopupOverlay(mMapView, popListener);
		MyRouteMapView.pop = pop;
	}
	// 点击地图放大
		public void doMapZoomUp(View v) {
			mMapController.zoomIn();
		}

		// 点击地图缩小
		public void doMapZoomDown(View v) {
			mMapController.zoomOut();
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
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

}

/**
 * 继承MapView重写onTouchEvent实现泡泡处理操作
 * 
 */
class MyRouteMapView extends MapView {
	static PopupOverlay pop = null;// 弹出泡泡图层，浏览节点时使用

	public MyRouteMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyRouteMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyRouteMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// 消隐泡泡
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}
	
}

package com.bb.dd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

//import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.Symbol;
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
import com.bb.dd.adapter.LeftMainAdapter;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.fragment.AboutUsFragment;
import com.bb.dd.fragment.BikeSiteListFragment;
import com.bb.dd.fragment.FocusFragment;
import com.bb.dd.fragment.FragmentView;
import com.bb.dd.fragment.LostAndFoundFragment;
import com.bb.dd.fragment.MoreFragment;
import com.bb.dd.fragment.OpinionFragment;
import com.bb.dd.fragment.RuteFragment;
import com.bb.dd.fragment.SearchFragment;
import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.modle.BikeSite;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.LeftMainView;
import com.bb.dd.modle.RuteView;
import com.bb.dd.service.BikeSiteInforService;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.service.WeatherTodayService;
import com.bb.dd.userdefined.CenterRadioButton;
import com.bb.dd.util.Bounds;
import com.bb.dd.util.BoundsUtil;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.Cons;
import com.bb.dd.util.PreferencesUtil;
import com.bb.dd.util.ProgressDialogUtil;
import com.bb.dd.util.Util;
import com.bb.dd.util.WebHelper;
import com.topdt.application.entity.BikeSiteRealView;
import com.topdt.application.entity.BikeSiteStatusView;
import com.topdt.coal.entity.User;
import com.topdt.coal.entity.UserAttentionBikesite;
import com.topdt.coal.entity.Weather;

public class IndexMainActivity extends ActivityGroup implements
		SensorEventListener, OnTouchListener, GestureDetector.OnGestureListener {

	private LinearLayout ll_right;
	private LinearLayout ll_left;
	private GestureDetector mGestureDetector; // 手势检测器

	private int window_width; // 屏幕的宽度
	private static float SNAP_VELOCITY = 400; // x方向上滑动的距离
	private int SPEED = 50; // 滑动的速度
	private final static int sleep_time = 5;
	private int MAX_WIDTH = 0; // 滑动的最大距离
	private int mScrollX;
	private boolean isScrolling = false;
	private boolean isFinish = true;
	private boolean isMenuOpen = false;
	private boolean hasMeasured = false; // 是否Measured.
	private LinearLayout ll_top_left;
	public static LinearLayout container;
	private RelativeLayout rl_top;
	// 以上是滑动界面相关定义变量
	private static final String TAG = IndexMainActivity.class.getName();
	private final static int REQUEST_BIKESITEDETIAL_CODE = 1;
	private CenterRadioButton radioFirstPage, radioSecondPage, radioThirdPage,
			radioFourPage;
	private TextView synchroCount;
	private RelativeLayout synchroCountLayout;
	private int exitFlag = 1;
	private boolean leftFlag = false;
	/**
	 * MapView 是地图主控件
	 */
	private MyMapView mMapView = null;
	/**
	 * 用MapController完成地图控制
	 */
	private MapController mMapController = null;
	/**
	 * MKMapViewListener 用于处理地图事件回调
	 */
	MKMapViewListener mMapListener = null;
	private boolean mRegisteredSensor;
	private SensorManager mSensorManager;
	private static final int BIKE_REMAIN_ZEARO = 0;
	private static final int BIKE_REMAIN_NORMAL = 2;
	private static final int BIKE_REMAIN_HIGH = 3;
	private static final int LOAD_BIKE_REMAIN_FINISH = 1; // 站点实时单点
	private static final int HIT_POPULER = 7;
	private static final int MAP_BIKESITE_SEARCH = 8;
	private static final int MAIN_LOAD_FINISH = 4;
	private static final int LOAD_ONE_FINISH = 6;
	private static final int GET_WEATHER_SUCCESS = 9;

	public static List<BikeSite_Lite> bikeSites = new ArrayList<BikeSite_Lite>();
	public Map<String, BikeSiteStatusView> remainBikeSitesStatus = new HashMap<String, BikeSiteStatusView>();
	private List<BikeSiteStatusView> remainBikeSites = new ArrayList<BikeSiteStatusView>();

	public PopupOverlay popupOverlay;
	public LocationClient mLocClient;
	public int locationResult;

	private TelephonyManager telephonemanage;
	private String imsi = "";
	public static MyOverlayItem itemClicked = null; // 所选站点
	public View itemPopView = null; // 站点提示
	public AlterView pop_alterView;
	public RelativeLayout pop_stationClose;
	private LinearLayout pop_stationRealMsg, pop_stationReal;
	private TextView pop_stationRealMsgStr;

	public MapView.LayoutParams layoutParam = null; // 站点提示样式
	public boolean isExit = false; // 站点查询是否退出
	public LoadBikeRemain loadBikeRemain;
	public static GeoPoint curCenterPoint; // 当前地图中心点位置
	public GeoPoint requestPoint = null; // 请求定位点位置

	public static BikeOverLayItem bikeOverLayItem; // 站点覆盖物
	public static MyLocationOverlay myLocationOverlay;// 定位覆盖物
	public static GraphicsOverlay areaGraphicsOverlay;// 区域范围 覆盖物
	// public static MyIcon mi;
	// public static MyIconAlert popview;
	public static View mi, popview;
	private ImageView weatherImg;
	public static TextView position_titileName;
	public static MKSearch mkSerach;
	public RelativeLayout mapAreaLayout, mapAreaCurLayout;
	public ImageView mapAreaCur, mapArea500, mapArea1000, mapArea1500,
			mapAreaAll;
	public int mapAreaState = 3; // 0:全城 1:500米 2:1000米 3:1500米
	public MyOverlayItem centerOverlayItem; // 区域中心点参照物
	public ProgressDialog progressDialog; // 更新滚动
	public MKOfflineMap mOffline = null; // 离线地图
	private BikeSiteInforService bikeSiteInforService = new BikeSiteInforService();
	private BikeSiteUserService bikeSiteUserService = new BikeSiteUserService();
	private Context iContext;
	private RadioGroup radioGroup;
	private FragmentView currentFragment;
	private LinearLayout layout_above_map;
	BikeSiteRealView bsv = null;// 弹出层站点信息
	private int topflag = 1;
	private String bikesiteId;
	private StringBuffer bikesiteIdBuffer = new StringBuffer();
	/**
	 * 用户中心相关
	 */
	private ImageView userPhoto;
	private RelativeLayout userNoLogin, userMess;
	private TextView userName, userPhone;
	private TextView title;
	private ImageView topRight;
	private LinearLayout ll_zhuxiao;

	private Animation areHideAnimationa;
	private Animation areShowAnimationa;
	private Drawable _low, _normal, _high;
	private BikeSite_Lite bikeSite_Lite;
	private GeoPoint point;
	private String value;
	private LinearLayout top_ll_right;
	private boolean leftMenuFlag = false;
	private RelativeLayout rlOpenBar;
	private TextView topBarTv;
	private Weather weather;
	private LinearLayout left_one;
	private ImageView left_one_img;
	private TextView left_one_tv;
	private LinearLayout left_two;
	private ImageView left_two_img;
	private TextView left_two_tv;
	private LinearLayout left_three;
	private ImageView left_three_img;
	private TextView left_three_tv;
	private LinearLayout left_four;
	private ImageView left_four_img;
	private TextView left_four_tv;
	private LinearLayout left_five;
	private ImageView left_five_img;
	private TextView left_five_tv;
	private Context context;
	Intent intent;
	String bikesiteName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=IndexMainActivity.this;
		//BadHandler.getInstance().init(getApplicationContext());
		ProgressDialogUtil.progressDialog=null;
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */
		BikeApplication app = (BikeApplication) this.getApplication();
		if (null != app.getTempContext().get("IndexMainActivity")) {
			app.getTempContext().get("IndexMainActivity").finish();
		}
		app.getTempContext().put("IndexMainActivity", IndexMainActivity.this);
		// 获得手机管理者 ，用来获取手机串号.Ps:需要在配置文件中加入相应的权限
		telephonemanage = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		imsi = telephonemanage.getSubscriberId();
		/**
		 * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
		 */
		setContentView(R.layout.index_main);
		getView();
		setRightLayout();
		iContext = this;
		intent = getIntent();
		RuteView ruteView = (RuteView) intent.getSerializableExtra("ruteView");
		if (ruteView != null) {
			initView();
			// closeProgressDialog();
			initLeft();
			initRight();
			initMapView();
			topRight.setVisibility(View.GONE);
			radioThirdPage.setChecked(true);
			currentFragment = new RuteFragment(iContext, ruteView);
			layout_above_map.setVisibility(View.GONE);
			mMapView.setVisibility(View.GONE);
			infaltView();
		} else {
			initView();
			initOtherView();
			initLeft();
			initRight();
			initListener();
			initMapView();
			getTodayWeather();
			if (PreferencesUtil.getValue(PreferencesUtil.FIRST_INDEX_MAIN) == 0) {
				Util.l("读取站点实时数据 批量...");
				loadRemainData(); // 读取站点实时数据 批量
			} else {
				remainBikeSites = Cons.remainBikeSites;
				remainBikeSitesStatus = Cons.remainBikeSitesStatus;
				Util.l("定位设置...");
				locClient(); // 定位设置
				locate();// 定位请求
			}

			loadLocalUserFocus(); // 加载用户关注数据
			PreferencesUtil.setValue(PreferencesUtil.FIRST_INDEX_MAIN, 1);
			String longitude = intent.getStringExtra("longitude");
			String latitude = intent.getStringExtra("latitude");
			bikesiteName = intent.getStringExtra("bikesiteName");
			bikesiteId= intent.getStringExtra("bikesiteId");
			if (longitude != null && latitude != null &&bikesiteName!=null&&bikesiteId!=null&& !longitude.equals("")
					&& !latitude.equals("")&&!bikesiteName.equals("")&&!bikesiteId.equals("")) {
				loadBikeRemain=new LoadBikeRemain();
				Double dlongitude = Double.parseDouble(longitude) * 1E6;
				Double dlatitude = Double.parseDouble(latitude) * 1E6;
				requestPoint = new GeoPoint(dlatitude.intValue(),
						dlongitude.intValue());
			}			/**
			 * 添加照片跳转回来
			 */
			/*
			 * String tagPhoto = intent.getStringExtra("tag"); if (tagPhoto !=
			 * null && tagPhoto != "") { Util.l("-----------addphoto---------");
			 * new AsynMove().execute(SPEED); }
			 */
		}
	}

	private void getTodayWeather() {
		new WeatherTodayService().getWeather(new AsyncOperator() {

			@Override
			public void onSuccess(Object obj) {
				weather = (Weather) obj;
				handler.sendEmptyMessage(GET_WEATHER_SUCCESS);
				super.onSuccess(obj);
			}

			@Override
			public void onFail(String message) {
				super.onFail(message);
			}

		});

	}

	void getView() {// 得到控件的对象实例
		ll_right = (LinearLayout) findViewById(R.id.layout_right);
		ll_left = (LinearLayout) findViewById(R.id.layout_left);
		ll_top_left = (LinearLayout) findViewById(R.id.top_iv_left);
		rl_top = (RelativeLayout) findViewById(R.id.rl_top);
		// left_listView = (ListView) findViewById(R.id.left_listview);
	}

	void setRightLayout() {// 设置右边布局的滑动
		ll_top_left.setOnTouchListener(this);
		rl_top.setOnTouchListener(this);
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setIsLongpressEnabled(false);// 禁用长按监听
		getMaxX();

	}

	void getMaxX() {// 得到滑动的最大宽度,即此layout的宽度

		ViewTreeObserver viewTreeObserver = ll_right.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth(); // 屏幕宽度
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
							.getLayoutParams(); // layout参数
					layoutParams.width = window_width;
					ll_right.setLayoutParams(layoutParams);
					hasMeasured = true;
					MAX_WIDTH = ll_left.getWidth();// 左边layout宽度
				}
				return true;
			}
		});
	}

	@Override
	public boolean onDown(MotionEvent e) {
		mScrollX = 0;
		isScrolling = false;
		return true;// 将之改为true，不然事件不会向下传递.
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		int currentX = (int) e2.getX();
		int lastX = (int) e1.getX();

		if (isMenuOpen) {

			if (!isScrolling && currentX - lastX >= 0) {

				return false;
			}
		} else {

			if (!isScrolling && currentX - lastX <= 0) {

				return false;

			}
		}

		boolean suduEnough = false;

		if (velocityX > IndexMainActivity.SNAP_VELOCITY
				|| velocityX < -IndexMainActivity.SNAP_VELOCITY) {

			suduEnough = true;

		} else {

			suduEnough = false;

		}

		doCloseScroll(suduEnough);

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (isFinish)
			doScrolling(distanceX);
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (isFinish) {

			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
					.getLayoutParams();
			// 左移动
			if (layoutParams.leftMargin >= MAX_WIDTH) {
				new AsynMove().execute(-SPEED);
			} else {
				// 右移动
				new AsynMove().execute(SPEED);
			}
		}
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 松开的时候要判断，如果不到半屏幕位子则缩回去，
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
					.getLayoutParams();
			// 缩回去
			if (layoutParams.leftMargin > window_width / 2) {
				new AsynMove().execute(SPEED);
			} else {
				new AsynMove().execute(-SPEED);
			}
		}
		//TODO 关闭软键盘
		InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		if (getCurrentFocus()!=null) {
			im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		
		
		return mGestureDetector.onTouchEvent(event);
	}

	public void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX += distanceX;// distanceX:向左为正，右为负
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
				.getLayoutParams();

		layoutParams.leftMargin -= mScrollX;
		layoutParams.rightMargin += mScrollX;

		if (layoutParams.leftMargin <= 0) {
			isScrolling = false;// 拖过头了不需要再执行AsynMove了
			layoutParams.leftMargin = 0;
			layoutParams.rightMargin = 0;

		} else if (layoutParams.leftMargin >= MAX_WIDTH) {
			// 拖过头了不需要再执行AsynMove了
			isScrolling = false;
			layoutParams.leftMargin = MAX_WIDTH;
		}
		ll_right.setLayoutParams(layoutParams);
		ll_left.invalidate();
	}

	public void doCloseScroll(boolean suduEnough) {
		if (isFinish) {

			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
					.getLayoutParams();

			int tempSpeed = SPEED;

			if (isMenuOpen) {
				tempSpeed = -tempSpeed;
			}

			if (suduEnough
					|| (!isMenuOpen && (layoutParams.leftMargin > window_width / 2))
					|| (isMenuOpen && (layoutParams.leftMargin < window_width / 2))) {

				new AsynMove().execute(tempSpeed);

			} else {

				new AsynMove().execute(-tempSpeed);

			}

		}
	}

	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			isFinish = false;
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			isFinish = true;
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
					.getLayoutParams();
			if (layoutParams.leftMargin >= MAX_WIDTH) {
				isMenuOpen = true;
			} else {
				isMenuOpen = false;
			}
			super.onPostExecute(result);
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_right
					.getLayoutParams();
			// 右移动
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], MAX_WIDTH);
				layoutParams.rightMargin = Math.max(layoutParams.rightMargin
						- values[0], -MAX_WIDTH);
				/*
				 * left_listView .setOnItemClickListener(new
				 * LeftOnItemClickListern());
				 */
				ll_left.setOnTouchListener((OnTouchListener) context);
				left_one.setOnClickListener(new LeftonClickListern());
				left_two.setOnClickListener(new LeftonClickListern());
				left_three.setOnClickListener(new LeftonClickListern());
				left_four.setOnClickListener(new LeftonClickListern());
				left_five.setOnClickListener(new LeftonClickListern());

				if (Cons.LOGIN_FLAG == 1) {
					ll_zhuxiao.setVisibility(View.VISIBLE);
					userNoLogin.setVisibility(View.GONE);
					userMess.setVisibility(View.VISIBLE);
					if (Cons.drawablePhoto != null) {
						userPhoto.setBackgroundDrawable(Cons.drawablePhoto);
					} else {
						String photoPath = Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/_TAIYUAN_PBIKE/userPhoto/"
								+ PreferencesUtil
										.getStr(PreferencesUtil.USER_PHOTO);
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						Bitmap bm = BitmapFactory
								.decodeFile(photoPath, options);
						userPhoto.setImageBitmap(bm);
					}
					userName.setText(PreferencesUtil
							.getStr(PreferencesUtil.USER_NAME));
					String userPhoneNum = PreferencesUtil.getStr(
							PreferencesUtil.USER_PHONE).substring(0, 7)
							+ "xxxx";
					userPhone.setText(userPhoneNum);
				}
				leftMenuFlag = true;
			} else {
				ll_left.setOnTouchListener(null);
				// left_listView.setOnItemClickListener(null);
				left_one.setOnClickListener(null);
				left_two.setOnClickListener(null);
				left_three.setOnClickListener(null);
				left_four.setOnClickListener(null);
				left_five.setOnClickListener(null);

				// 左移动
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], 0);
				leftMenuFlag = false;
			}
			ll_right.setLayoutParams(layoutParams);
			ll_left.invalidate();

		}
	}

	/**
	 * 加载用户关注数据
	 */
	private void loadLocalUserFocus() {
		int login_flag = Cons.LOGIN_FLAG;
		User user = Util.getLoginUser();
		if (login_flag == 0 || user == null) {
			return;
		} else {
			bikeSiteUserService.loadUserFocus(user.getApnUserId().toString(),
					new AsyncOperator() {
						public void onSuccess(Object obj) {
							List<UserAttentionBikesite> list = (List<UserAttentionBikesite>) obj;
							for (UserAttentionBikesite focus : list) {
								bikesiteIdBuffer.append(focus.getBikesiteId()
										+ "#");
							}
							super.onSuccess(obj);
						}

						public void onFail(String obj) {
							super.onSuccess(obj);
						}
					});
		}
	}

	private void initView() {
		View v = findViewById(R.id.map_position_layout);//找到你要设透明背景的layout 的id 
		v.getBackground().setAlpha(200);//0~255透明度值 ，0为完全透明，255为不透明
		/**
		 * 首次进入首页
		 */
		if (PreferencesUtil.getValue(PreferencesUtil.FIRST_INDEX_MAIN) == 0) {
			showProgressDialog();
		}
		container = (LinearLayout) findViewById(R.id.container);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupTab);
		radioFirstPage = (CenterRadioButton) findViewById(R.id.radioStartPage);
		radioSecondPage = (CenterRadioButton) findViewById(R.id.radioSecondPage);
		radioThirdPage = (CenterRadioButton) findViewById(R.id.radioThirdPage);
		radioFourPage = (CenterRadioButton) findViewById(R.id.radioFourthPage);
		synchroCount = (TextView) findViewById(R.id.synchron_count);
		synchroCountLayout = (RelativeLayout) findViewById(R.id.rl_synchron_count);
		top_ll_right = (LinearLayout) findViewById(R.id.top_ll_right);
		topRight = (ImageView) findViewById(R.id.top_iv_right);
		userPhoto = (ImageView) findViewById(R.id.user_mess_photo);
		userNoLogin = (RelativeLayout) findViewById(R.id.user_no_login);
		userMess = (RelativeLayout) findViewById(R.id.user_mess);
		userName = (TextView) findViewById(R.id.user_mess_name);
		userPhone = (TextView) findViewById(R.id.user_mess_phone);
		// 注销账户
		ll_zhuxiao = (LinearLayout) findViewById(R.id.ll_zhuxiao);

		title = (TextView) findViewById(R.id.top_tv_center);
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setIsLongpressEnabled(false);

		if (BikeApplication.getInstance().getSynchroCount() > 0) {
			synchroCountLayout.setVisibility(View.VISIBLE);
			synchroCount.setText(BikeApplication.getInstance()
					.getSynchroCount() + "");
		}else{
			synchroCountLayout.setVisibility(View.GONE);
		}

	}

	private void initRight() {
		radioGroup
				.setOnCheckedChangeListener(new RadioOnCheckedChangeListener());
	}

	private void initLeft() {
		/*
		 * 左侧相关listView
		 */
		initLeftListView();

	}

	private class RadioOnCheckedChangeListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// if (currentFragment != null && leftFlag == false) {
			// closedFragment();
			// }
			switch (checkedId) {

			case R.id.radioStartPage: {
				radioFirstPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_liu));
				radioSecondPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioThirdPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioFourPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));

				Intent intent_main = new Intent(IndexMainActivity.this,
						IndexMainActivity.class);
				startActivity(intent_main);
				finish();
			}
				break;
			case R.id.radioSecondPage: {
				if (leftFlag == false) {
					radioFirstPage.setTextColor(getResources().getColor(
							R.color.bottom_text_bg_hui));
					radioSecondPage.setTextColor(getResources().getColor(
							R.color.bottom_text_bg_liu));
					radioThirdPage.setTextColor(getResources().getColor(
							R.color.bottom_text_bg_hui));
					radioFourPage.setTextColor(getResources().getColor(
							R.color.bottom_text_bg_hui));

					title.setText("搜索");
					topRight.setVisibility(View.GONE);
					currentFragment = new SearchFragment(iContext);
					//TODO 修改
//					currentFragment = new SearchFragment(BikeApplication.getApplication());
					infaltView();
				}

			}
				break;
			case R.id.radioThirdPage: {
				radioFirstPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioSecondPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioThirdPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_liu));
				radioFourPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));

				title.setText("导航");
				topRight.setVisibility(View.GONE);
				currentFragment = new RuteFragment(iContext);
				infaltView();
			}
				break;
			case R.id.radioFourthPage: {
				radioFirstPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioSecondPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioThirdPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioFourPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_liu));

				title.setText("更多");
				topRight.setVisibility(View.GONE);
				currentFragment = new MoreFragment(iContext);
				infaltView();
			}
				break;
			}
			if (leftFlag == false) {
				//layout_above_map.setVisibility(View.GONE);
				//mMapView.setVisibility(View.GONE);
			}
		}
	}

	private class LeftonClickListern implements OnClickListener {

//		@SuppressLint("ResourceAsColor")
		@Override
		public void onClick(View v) {
			leftFlag = true;
			radioSecondPage.setChecked(true);
			radioGroup.clearCheck();
			radioFirstPage.setTextColor(getResources().getColor(
					R.color.bottom_text_bg_hui));
			radioSecondPage.setTextColor(getResources().getColor(
					R.color.bottom_text_bg_hui));
			radioThirdPage.setTextColor(getResources().getColor(
					R.color.bottom_text_bg_hui));
			radioFourPage.setTextColor(getResources().getColor(
					R.color.bottom_text_bg_hui));
			/*
			 * if (null != currentFragment) { closedFragment(); }
			 */
			switch (v.getId()) {
			
			case R.id.left_one: {
				left_one.setBackgroundResource(R.drawable.left_bg);
				left_one_tv.setTextColor(ColorStateList
						.valueOf(R.color.left_textcolor));
				left_one_img.setImageResource(R.drawable.left_my_focus_up);

				// radioFourthPage.setChecked(true);
				title.setText("我的关注");
				topRight.setVisibility(View.GONE);
				currentFragment = new FocusFragment(iContext);
			}
				break;
			case R.id.left_two: {
				// title.setText("公告");
				// currentFragment = new AnnouncementFragment(iContext);
				// topRight.setVisibility(View.VISIBLE);
				// Drawable drawable = getResources().getDrawable(
				// R.drawable.top_back);
				// topRight.setBackgroundDrawable(drawable);
				// top_ll_right.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// currentFragment = new AnnouncementFragment(iContext);
				// infaltView();
				// }
				// });

				left_two.setBackgroundResource(R.drawable.left_bg);
				left_two_tv.setTextColor(ColorStateList
						.valueOf(R.color.left_textcolor));
				left_two_img.setImageResource(R.drawable.left_feedback_up);

				title.setText("意见反馈");
				topRight.setVisibility(View.VISIBLE);
				currentFragment = new OpinionFragment(iContext);
				Drawable drawable = getResources().getDrawable(
						R.drawable.top_go_index);
				topRight.setBackgroundDrawable(drawable);
				top_ll_right.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent_main = new Intent(IndexMainActivity.this,
								IndexMainActivity.class);
						startActivity(intent_main);
						finish();
					}
				});

			}
				break;
			case R.id.left_three: {
				left_three.setBackgroundResource(R.drawable.left_bg);
				left_three_tv.setTextColor(ColorStateList
						.valueOf(R.color.left_textcolor));
				left_three_img.setImageResource(R.drawable.left_about_us_up);

				title.setText("关于我们");
				topRight.setVisibility(View.GONE);
				currentFragment = new AboutUsFragment(iContext);
			}
				break;
			case R.id.left_four: {
				left_four.setBackgroundResource(R.drawable.left_bg);
				left_four_tv.setTextColor(ColorStateList
						.valueOf(R.color.left_textcolor));
				left_four_img.setImageResource(R.drawable.left_swzl_up);

				title.setText("失物招领");
				topRight.setVisibility(View.GONE);
				currentFragment = new LostAndFoundFragment(iContext);
			}
				break;
			case R.id.left_five: {
				left_five.setBackgroundResource(R.drawable.left_bg);
				left_five_tv.setTextColor(ColorStateList
						.valueOf(R.color.left_textcolor));
				left_five_img.setImageResource(R.drawable.left_exit_up);

				exitFlag = 0;
				/*
				 * title.setText("关于我们"); currentFragment = new
				 * AboutUsFragment(iContext);
				 */
				Intent intent = new Intent(IndexMainActivity.this,
						ShowDialogActivity.class);
				startActivity(intent);
			}
				break;
			// case 5: {
			// title.setText("帮助");
			// topRight.setVisibility(View.GONE);
			// currentFragment = new HelpFragment(iContext);
			// }
			// break;
			// case 6: {
			// title.setText("数据更新");
			// topRight.setVisibility(View.GONE);
			// currentFragment = new UpdateDataFragment(iContext);
			// }
			// break;
			//
			// case 7: {
			// title.setText("版本更新");
			// topRight.setVisibility(View.GONE);
			// currentFragment = new UpdateVersionFragment(iContext);
			// }
			// break;
			// case 8: {
			//
			// }
			// break;
			default:
				break;
			}
			if (exitFlag == 1) {
				infaltView();
				//mMapView.setVisibility(View.GONE);
				new AsynMove().execute(-SPEED);
			}
			exitFlag = 1;
			leftFlag = false;
		}

	}

	private void infaltView() {
		layout_above_map.setVisibility(View.GONE);
		container.removeAllViews();
		container.addView(currentFragment.getView(), LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
	}

	private void closedFragment() {
		currentFragment.closed();
		container.removeAllViews();
		currentFragment = null;
	}

	/**
	 * 登录
	 */
	public void doLogin(View v) {
		Intent intent_login = new Intent(IndexMainActivity.this,
				LoginActivity.class);
		startActivity(intent_login);
	}

	/**
	 * 注销
	 */
	public void doExit(View v) {
		new AlertDialog.Builder(iContext)
				.setTitle("注销")
				// 设置标题
				.setMessage("你确定要注销当前用户吗？")
				// 设置提示消息
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 设置确定的按键
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Util.t("注销成功");
								Cons.LOGIN_FLAG = 0;
								userNoLogin.setVisibility(View.VISIBLE);
								userMess.setVisibility(View.GONE);
								ll_zhuxiao.setVisibility(View.GONE);
								Util.resetUserData();
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {// 设置取消按键
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).setCancelable(false)// 设置按返回键是否响应返回，这是是不响应
				.show();// 显示

	}

	/**
	 * 添加头像
	 */
	public void doAddPhoto(View v) {
		Intent intent_addphoto = new Intent(IndexMainActivity.this,
				AddPhotoActivity.class);
		startActivity(intent_addphoto);

	}

	/**
	 * 编辑用户信息
	 */
	public void doEdit(View v) {
		Intent intent_edituser = new Intent(IndexMainActivity.this,
				EditUserMsgActivity.class);
		startActivity(intent_edituser);
		//TODO 左侧框恢复至原位
		// 左侧框缩回去
		new AsynMove().execute(-100);

	}

	/**
	 * 左侧相关listView布局加载
	 */
	protected void initLeftListView() {
		List<LeftMainView> leftMainViews = new ArrayList<LeftMainView>();
		// String[] descriptionLeft = new String[] { "我的关注", "公告", "意见反馈",
		// "关于我们",
		// "客户端分享", "帮助", "数据更新", "版本更新", "退出" };
		String[] descriptionLeft = new String[] { "我的关注","失物招领","意见反馈", "关于我们",
				 "退出" };
		int[] imageLeft = new int[] { R.drawable.left_my_focus_up,R.drawable.left_swzl_up,
				R.drawable.left_feedback_up, R.drawable.left_about_us_up,
				 R.drawable.left_exit_up };
		// int[] imageLeft = new int[] { R.drawable.left_my_focus_up,
		// R.drawable.left_notice_up, R.drawable.left_feedback_up,
		// R.drawable.left_about_us_up, R.drawable.left_share_up,
		// R.drawable.left_help_up, R.drawable.left_data_update_up,
		// R.drawable.left_version_update_up, R.drawable.left_exit_up };
		for (int i = 0; i < descriptionLeft.length; i++) {
			LeftMainView leftMainView = new LeftMainView();
			leftMainView.setDrawable(imageLeft[i]);
			leftMainView.setTextView(descriptionLeft[i]);
			leftMainViews.add(leftMainView);
		}
		LeftMainAdapter leftMainAdapter = new LeftMainAdapter(
				getApplicationContext(), leftMainViews);
		// left_listView.setAdapter(leftMainAdapter);

		// 获取五个列表项
		left_one = (LinearLayout) findViewById(R.id.left_one);
		left_one_img = (ImageView) findViewById(R.id.left_one_img);
		left_one_tv = (TextView) findViewById(R.id.left_one_tv);
		left_one.setOnClickListener(new LeftonClickListern());

		left_one.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isMenuOpen) {
						left_one.setBackgroundResource(R.color.left_xuanzhong);
						left_one_tv.setTextColor(android.graphics.Color.WHITE);
						left_one_img
								.setImageResource(R.drawable.left_my_focus_down);
					}
					break;
				case MotionEvent.ACTION_UP:
					left_one.setBackgroundResource(R.drawable.left_bg);
					left_one_tv.setTextColor(ColorStateList
							.valueOf(R.color.left_textcolor));
					left_one_img.setImageResource(R.drawable.left_my_focus_up);

					break;

				default:
					break;
				}
				

				return false;
			}
		});

		left_two = (LinearLayout) findViewById(R.id.left_two);
		left_two_img = (ImageView) findViewById(R.id.left_two_img);
		left_two_tv = (TextView) findViewById(R.id.left_two_tv);
		left_two.setOnClickListener(new LeftonClickListern());
		left_two.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isMenuOpen) {
						left_two.setBackgroundResource(R.color.left_xuanzhong);
						left_two_tv.setTextColor(android.graphics.Color.WHITE);
						left_two_img
								.setImageResource(R.drawable.left_feedback_down);
					}
					break;
				case MotionEvent.ACTION_UP:
					left_two.setBackgroundResource(R.drawable.left_bg);
					left_two_tv.setTextColor(ColorStateList
							.valueOf(R.color.left_textcolor));
					left_two_img.setImageResource(R.drawable.left_feedback_up);

					break;

				default:
					break;
				}
				
				
				return false;
			}
		});

		left_three = (LinearLayout) findViewById(R.id.left_three);
		left_three_img = (ImageView) findViewById(R.id.left_three_img);
		left_three_tv = (TextView) findViewById(R.id.left_three_tv);
		left_three.setOnClickListener(new LeftonClickListern());
		left_three.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isMenuOpen) {
						left_three.setBackgroundResource(R.color.left_xuanzhong);
						left_three_tv.setTextColor(android.graphics.Color.WHITE);
						left_three_img
								.setImageResource(R.drawable.left_about_us_down);
					}
					break;
				case MotionEvent.ACTION_UP:
					left_three.setBackgroundResource(R.drawable.left_bg);
					left_three_tv.setTextColor(ColorStateList
							.valueOf(R.color.left_textcolor));
					left_three_img.setImageResource(R.drawable.left_about_us_up);
					break;

				default:
					break;
				}
				
				return false;
			}
		});

		left_four = (LinearLayout) findViewById(R.id.left_four);
		left_four_img = (ImageView) findViewById(R.id.left_four_img);
		left_four_tv = (TextView) findViewById(R.id.left_four_tv);
		left_four.setOnClickListener(new LeftonClickListern());
		left_four.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isMenuOpen) {
						left_four.setBackgroundResource(R.color.left_xuanzhong);
						left_four_tv.setTextColor(android.graphics.Color.WHITE);
						left_four_img.setImageResource(R.drawable.left_swzl_down);
					}
					
					break;
				case MotionEvent.ACTION_UP:
					left_four.setBackgroundResource(R.drawable.left_bg);
					left_four_tv.setTextColor(ColorStateList
							.valueOf(R.color.left_textcolor));
					left_four_img.setImageResource(R.drawable.left_swzl_up);
					break;

				default:
					break;
				}
				return false;
			}
		});

		left_five = (LinearLayout) findViewById(R.id.left_five);
		left_five_img = (ImageView) findViewById(R.id.left_five_img);
		left_five_tv = (TextView) findViewById(R.id.left_five_tv);
		left_five.setOnClickListener(new LeftonClickListern());
		left_five.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isMenuOpen) {
						left_five.setBackgroundResource(R.color.left_xuanzhong);
						left_five_tv.setTextColor(android.graphics.Color.WHITE);
						left_five_img.setImageResource(R.drawable.left_exit_down);
					}
					
					break;
				case MotionEvent.ACTION_UP:
					left_five.setBackgroundResource(R.drawable.left_bg);
					left_five_tv.setTextColor(ColorStateList
							.valueOf(R.color.left_textcolor));
					left_five_img.setImageResource(R.drawable.left_exit_up);
					break;

				default:
					break;
				}
				
				return false;
			}
		});
	}

	private void initMapView() {
		layout_above_map = (LinearLayout) findViewById(R.id.main_above_map);
		mMapView = (MyMapView) findViewById(R.id.bmapView);
		/**
		 * 获取地图控制器
		 */
		mMapController = mMapView.getController();
		/**
		 * 设置地图是否响应点击事件 .
		 */
		mMapController.enableClick(true);
		/**
		 * 设置地图缩放级别
		 */
		mMapController.setZoom(16);

		/**
		 * 将地图移动至指定点
		 * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index
		 * .html查询地理坐标 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
		 */
		GeoPoint point = new GeoPoint((int) (37.851969 * 1E6),
				(int) (112.56205 * 1E6));
		mMapController.setCenter(point);
		mMapController.setCompassMargin(35, 300);

		mMapView.regMapViewListener(BikeApplication.getInstance().mBMapManager,
				mMapListener);

		myLocationOverlay = new MyLocationOverlay(mMapView);
		// myLocationOverlay.enableMyLocation(); // 注册GPS位置更新的事件,让地图能实时显示当前位置
		myLocationOverlay.enableCompass(); // 开启磁场感应传感器
		mMapView.getOverlays().add(myLocationOverlay);

		mRegisteredSensor = false;
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// 取得SensorManager实例

		areaGraphicsOverlay = new GraphicsOverlay(mMapView);

		mi = (RelativeLayout) findViewById(R.id.map_my_icon_layout); // map_my_icon
		popview = (RelativeLayout) findViewById(R.id.map_myalert_icon_layout);
		popview.setOnClickListener(new AreaSearchListener());

		mkSerach = new MKSearch();
		mkSerach.init(BikeApplication.getInstance().mBMapManager,
				new MySearchListener());

		mOffline = new MKOfflineMap();
		mOffline.init(mMapController, new MyOfflineMapListener());
		int num = mOffline.scan();
		Log.i(TAG, String.format("已安装%d个离线包", num));
		// loadBaiDuOffline();
	}

	private void initOtherView() {
		_low = getResources().getDrawable(R.drawable._low2);
		_normal = getResources().getDrawable(R.drawable._normal2);
		_high = getResources().getDrawable(R.drawable._high2);
		weatherImg = (ImageView) findViewById(R.id.weather_img);
		position_titileName = (TextView) findViewById(R.id.map_position_titileName);
		mapAreaCur = (ImageView) findViewById(R.id.map_area_cur);
		mapArea500 = (ImageView) findViewById(R.id.map_area_500);
		mapArea1000 = (ImageView) findViewById(R.id.map_area_1000);
		mapArea1500 = (ImageView) findViewById(R.id.map_area_1500);
		mapAreaAll = (ImageView) findViewById(R.id.map_area_all);
		mapAreaCurLayout = (RelativeLayout) findViewById(R.id.map_area_cur_layout);
		mapAreaLayout = (RelativeLayout) findViewById(R.id.map_area_layout);
		rlOpenBar = (RelativeLayout) findViewById(R.id.rl_open_bar);
		topBarTv = (TextView) findViewById(R.id.bar_limit_tv);
		RelativeLayout my_location = (RelativeLayout) findViewById(R.id.my_location);
		my_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mMapController.setOverlooking(0);
				mMapController.setRotation(0);
				locate();
			}
		});
		mapAreaLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapAreaLayout.setVisibility(View.GONE);
				rlOpenBar.setVisibility(View.VISIBLE);

			}
		});
		rlOpenBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapArea500.setImageResource(R.drawable.map_area_500_up);
				mapArea1000.setImageResource(R.drawable.map_area_1000_up);
				mapArea1500.setImageResource(R.drawable.map_area_1500_up);
				mapAreaAll.setImageResource(R.drawable.map_area_all_up);
				switch (mapAreaState) {
				case 3:
					mapArea1500.setImageResource(R.drawable.map_area_1500_down);
					break;
				case 2:
					mapArea1000.setImageResource(R.drawable.map_area_1000_down);
					break;
				case 1:
					mapArea500.setImageResource(R.drawable.map_area_500_down);
					break;
				case 0:
					mapAreaAll.setImageResource(R.drawable.map_area_all_down);
					break;
				}
				mapAreaLayout.setVisibility(View.VISIBLE);
				mapAreaLayout.startAnimation(areShowAnimationa);
				mapAreaCurLayout.setVisibility(View.GONE);
				rlOpenBar.setVisibility(View.GONE);
			}
		});
		RelativeLayout handUpdate = (RelativeLayout) findViewById(R.id.hand_update);
		handUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestPoint=null;
				mMapController.setOverlooking(0);
				mMapController.setRotation(0);
				showProgressDialog();
				new Thread() {
					public void run() {
						try {
							getRemainBikeSite(); // 加载实时数据

							// loadBikeSite(curCenterPoint);
							// mMapController.setCenter(curCenterPoint);
							// closeProgressDialog();

							// 根据当前位置进行搜索
							Message msg = new Message();
							msg.what = MAP_BIKESITE_SEARCH;
							msg.obj = curCenterPoint;
							handler.sendMessage(msg);
						} catch (Exception e) {
						}
					}
				}.start();

			}
		});

		// 地图放大
		ImageView mapZooUp = (ImageView) findViewById(R.id.map_zoom_up);
		mapZooUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doMapZoomUp(v);
			}
		});
		// 地图缩小
		ImageView mapZooDown = (ImageView) findViewById(R.id.map_zoom_down);
		mapZooDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doMapZoomDown(v);
			}
		});

		areHideAnimationa = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f);
		areHideAnimationa.setDuration(500);

		areShowAnimationa = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		areShowAnimationa.setDuration(500);

		mapAreaCurLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapArea500.setImageResource(R.drawable.map_area_500_up);
				mapArea1000.setImageResource(R.drawable.map_area_1000_up);
				mapArea1500.setImageResource(R.drawable.map_area_1500_up);
				mapAreaAll.setImageResource(R.drawable.map_area_all_up);
				switch (mapAreaState) {
				case 3:
					mapArea1500.setImageResource(R.drawable.map_area_1500_down);
					break;
				case 2:
					mapArea1000.setImageResource(R.drawable.map_area_1000_down);
					break;
				case 1:
					mapArea500.setImageResource(R.drawable.map_area_500_down);
					break;
				case 0:
					mapAreaAll.setImageResource(R.drawable.map_area_all_down);
					break;
				}
				mapAreaLayout.setVisibility(View.VISIBLE);
				mapAreaLayout.startAnimation(areShowAnimationa);
				mapAreaCurLayout.setVisibility(View.GONE);
			}
		});

		RelativeLayout mapAreaBack = (RelativeLayout) findViewById(R.id.map_area_back_layout);
		mapAreaBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapAreaLayout.startAnimation(areHideAnimationa);
				mapAreaLayout.setVisibility(View.GONE);
				mapAreaCurLayout.setVisibility(View.VISIBLE);
			}
		});

		ImageView mapArea1500 = (ImageView) findViewById(R.id.map_area_1500);
		mapArea1500.setOnClickListener(new MyAreaListener());
		ImageView mapArea1000 = (ImageView) findViewById(R.id.map_area_1000);
		mapArea1000.setOnClickListener(new MyAreaListener());
		ImageView mapArea500 = (ImageView) findViewById(R.id.map_area_500);
		mapArea500.setOnClickListener(new MyAreaListener());
		ImageView mapAreaAll = (ImageView) findViewById(R.id.map_area_all);
		mapAreaAll.setOnClickListener(new MyAreaListener());
	}

	@Override
	protected void onPause() {
		if (mRegisteredSensor) {
			// 如果调用了registerListener
			// 这里我们需要unregisterListener来卸载/取消注册
			mSensorManager.unregisterListener(this);
			mRegisteredSensor = false;
		}
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		isExit = true;// 退出循环
		loadBikeRemain.execute(null);
		loadBikeRemain = null;
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		/**
		 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		 */
		isExit = false;// 退出循环
		loadBikeRemain = new LoadBikeRemain();
		mMapView.onResume();
		super.onResume();

		// 接受SensorManager的一个列表(Listener)
		// 这里我们指定类型为TYPE_ORIENTATION(方向感应器)
		List<Sensor> sensors = mSensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);

		if (sensors.size() > 0) {
			Sensor sensor = sensors.get(0);
			// 注册SensorManager
			// this->接收sensor的实例
			// 接收传感器类型的列表
			// 接受的频率
			mRegisteredSensor = mSensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_UI);
		}
	}

	@Override
	protected void onDestroy() {
		if (mRegisteredSensor) {
			// 如果调用了registerListener
			// 这里我们需要unregisterListener来卸载/取消注册
			mSensorManager.unregisterListener(this);
			mRegisteredSensor = false;
		}
		/**
		 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		 */
		isExit = true;// 退出循环
//		mMapView.destroy();
		// 退出时销毁定位
		if (mLocClient != null) {
			mLocClient.stop();
		}
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

	/**
	 * 绘制圆，该圆随地图状态变化
	 * 
	 * @return 圆对象
	 */
	public static Graphic drawCircle(GeoPoint g, int area) {
		if (area == 0)
			return null; // 全城不画范围
		int lat = g.getLatitudeE6();
		int lon = g.getLongitudeE6();
		GeoPoint pt1 = new GeoPoint(lat, lon);

		// 构建圆
		Geometry circleGeometry = new Geometry();

		// 设置圆中心点坐标和半径
		circleGeometry.setCircle(pt1, area);
		// 设置样式
		Symbol circleSymbol = new Symbol();
		Symbol.Color circleFillColor = circleSymbol.new Color();
		circleFillColor.red = 207;
		circleFillColor.green = 227;
		circleFillColor.blue = 245;
		circleFillColor.alpha = 100;
		circleSymbol.setSurface(circleFillColor, 1, 1);

		// 生成Graphic对象
		Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);
		return circleGraphic;
	}

	/**
	 * 绘制圆，该圆随地图状态变化
	 * 
	 * @return 圆对象
	 */
	public static Graphic drawCircleCenter(GeoPoint g, int area) {
		if (area == 0)
			return null; // 全城不画范围
		int lat = g.getLatitudeE6();
		int lon = g.getLongitudeE6();
		GeoPoint pt1 = new GeoPoint(lat, lon);

		// 构建圆
		Geometry circleGeometry = new Geometry();

		// 设置圆中心点坐标和半径
		circleGeometry.setCircle(pt1, area);
		// 设置样式
		Symbol circleSymbol = new Symbol();
		Symbol.Color circleFillColor = circleSymbol.new Color();
		circleFillColor.red = 107;
		circleFillColor.green = 127;
		circleFillColor.blue = 255;
		circleFillColor.alpha = 100;
		circleSymbol.setSurface(circleFillColor, 1, 1);

		// 生成Graphic对象
		Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);
		return circleGraphic;
	}

	/**
	 * 绘制圆，该圆随地图状态变化
	 * 
	 * @return 圆对象
	 */
	public static Graphic drawCircleLine(GeoPoint g, int area) {
		if (area == 0)
			return null; // 全城不画范围
		int lat = g.getLatitudeE6();
		int lon = g.getLongitudeE6();
		GeoPoint pt1 = new GeoPoint(lat, lon);

		// 构建圆
		Geometry circleGeometry = new Geometry();

		// 设置圆中心点坐标和半径
		circleGeometry.setCircle(pt1, area);
		// 设置样式
		Symbol circleSymbol = new Symbol();
		Symbol.Color circleLineColor = circleSymbol.new Color();
		circleLineColor.red = 103;
		circleLineColor.green = 159;
		circleLineColor.blue = 248;
		circleLineColor.alpha = 150;
		circleSymbol.setLineSymbol(circleLineColor, 2);

		// 生成Graphic对象
		Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);
		return circleGraphic;
	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_WEATHER_SUCCESS:
				if (weather != null) {
					if(weather.getWeatherTrendStart()!=null){
						String img = weather.getWeatherTrendStart();
						int dot = img.lastIndexOf('.');
						String imgName = "";
						if ((dot > -1) && (dot < (img.length()))) {
							imgName = "w_" + img.substring(0, dot);
						}
						Class drawable = R.drawable.class;
						Field field = null;
						try {
							field = drawable.getField(imgName);
							int r_id = field.getInt(field.getName());
							weatherImg.setBackgroundResource(r_id);
						} catch (Exception e) {
							Drawable drawables = getResources().getDrawable(
									R.drawable.w_0);
							topRight.setBackgroundDrawable(drawables);

						}
					}
					String weatherState="";
					if(weather.getAirState()!=null){
						weatherState+=weather.getAirState() + " ";
					}
					if(weather.getAirTemperature()!=null){
						weatherState+=weather.getAirTemperature()+" ";
					}
					if(weather.getTodayWeatherState()!=null){
						weatherState+=weather.getTodayWeatherState();
					}
					position_titileName.setText(weatherState+"");
				}else{
					position_titileName.setText("获取实时天气情况系统维护中！给您带来的不便敬请谅解......");
				}
				break;

			case MAIN_LOAD_FINISH: // 加载站点完成
				locClient(); // 定位设置
				locate();
				// closeProgressDialog();
				break;
			case LOAD_BIKE_REMAIN_FINISH:
				BikeSiteRealView site = (BikeSiteRealView) msg.obj;
				loadBikeRemain_Finish(site);
				break;
			case HIT_POPULER:
				if (popupOverlay != null)
					popupOverlay.hidePop();
				break;
			case MAP_BIKESITE_SEARCH:
				GeoPoint point = (GeoPoint) msg.obj;
				mapBikeSiteSearch(point);
				break;
			case LOAD_ONE_FINISH:
				radioThirdPage.setChecked(true);
				GeoPoint pointOne = (GeoPoint) msg.obj;
				RuteView ruteView = new RuteView();
				ruteView.setStartAddress(bikeSite_Lite.getBikesiteName());
				ruteView.setStartlatitude(Double.valueOf(pointOne
						.getLatitudeE6()));
				ruteView.setStartlongitude(Double.valueOf(pointOne
						.getLongitudeE6()));
				ruteView.setFlag(1);
				currentFragment = new RuteFragment(iContext, ruteView);
				layout_above_map.setVisibility(View.GONE);
				mMapView.setVisibility(View.GONE);
				infaltView();
				break;
			}
		}
	};

	/**
	 * 更新站点实时数据页面显示
	 * 
	 * @param site
	 */
	private void loadBikeRemain_Finish(BikeSiteRealView site) {
		if (null != site) {
			int useBikecount = site.getCanusebikecount();
			int usePosCount = site.getCanuseposcount();
			if (useBikecount == 0 && usePosCount == 0) {
				pop_stationRealMsg.setVisibility(View.VISIBLE);
				pop_stationReal.setVisibility(View.GONE);
				pop_stationRealMsgStr.setText(R.string.no_data);
				// pop_alterView.map_pop_stationReal_usebike.setText(getResources().getString(R.string.no_data));
				// pop_alterView.map_pop_stationReal_usepos.setText(getResources().getString(R.string.no_data));
			} else {
				pop_alterView.map_pop_stationReal_usebike.setText(String
						.valueOf(site.getCanusebikecount()));
				pop_alterView.map_pop_stationReal_usepos.setText(String
						.valueOf(site.getCanuseposcount()));
			}
			if (bikesiteIdBuffer.indexOf(site.getId()) >= 0) {
				pop_alterView.map_pop_station_focus
						.setImageResource(R.drawable.map_pop_station_focus_down);
				pop_alterView.map_pop_station_focus.setClickable(false);
				pop_alterView.map_pop_station_focus.setFocusable(false);
				pop_alterView.map_pop_station_focus.setEnabled(false);
			}
		} else {
			// pop_alterView.map_pop_stationReal_usebike.setText(getResources().getString(R.string.no_net));
			// pop_alterView.map_pop_stationReal_usepos.setText(getResources().getString(R.string.no_net));
			pop_stationRealMsgStr.setText(R.string.no_net);
			pop_stationRealMsg.setVisibility(View.VISIBLE);
			pop_stationReal.setVisibility(View.GONE);
		}
		pop_alterView.firstBar.setVisibility(View.GONE);
		pop_alterView.secondBar.setVisibility(View.GONE);
		pop_alterView.map_pop_stationReal_usebike.setVisibility(View.VISIBLE);
		pop_alterView.map_pop_stationReal_usepos.setVisibility(View.VISIBLE);
	}

	// 根据地图位置进行搜索
	private void mapBikeSiteSearch(GeoPoint point) {
		loadBikeSite(point); // 根据当前位置进行搜索
		mMapController.setCenter(point);
		// mMapController.animateTo(point);
		closeProgressDialog();
	}

	/**
	 * 在地图上添加不同图标绘制的覆盖物
	 */
	private void addOverlay() {
		if (bikeOverLayItem != null) {
			mMapView.getOverlays().remove(bikeOverLayItem);
			mMapView.refresh();
		}
		bikeOverLayItem = new BikeOverLayItem(null, mMapView, this, true);
		mMapView.getOverlays().add(bikeOverLayItem);
		int size = bikeSites.size();
		for (int i = 0; i < size; i++) {
			BikeSite_Lite bike = bikeSites.get(i);
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
			bikeOverLayItem.addItem(myOverlayItem);
		}
		mMapView.refresh();
	}

	/**
	 * 提示信息
	 */
	public void showProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("请稍候");
		progressDialog.setMessage("获取最新车位信息中......");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置风格为圆形进度条
		progressDialog.setIndeterminate(false); // 设置进度条是否为不明确
		progressDialog.setCanceledOnTouchOutside(false); // 禁止触屏
		progressDialog.setCancelable(false); // 禁止返回键
		progressDialog.show();
	}

	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * 提示信息
	 */
	public void showProgressDialog1() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("请稍候");
		progressDialog.setMessage("添加关注中......");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置风格为圆形进度条
		progressDialog.setIndeterminate(false); // 设置进度条是否为不明确
		progressDialog.setCanceledOnTouchOutside(false); // 禁止触屏
		progressDialog.setCancelable(false); // 禁止返回键
		progressDialog.show();
	}

	/**
	 * 某一站点车辆最新状态
	 * 
	 * @param id
	 *            站点ID
	 * @return
	 */
	private int getSiteStat(String id) {
		BikeSiteStatusView obj = (BikeSiteStatusView) remainBikeSitesStatus
				.get(id);
		if (obj != null) {
			int status = obj.getStatus();
			return status;
		}
		return 0;
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

	// 读取站点实时数据 批量
	private void loadRemainData() {
		new Thread() {
			public void run() {
				try {
					getRemainBikeSite();
					handler.sendEmptyMessage(MAIN_LOAD_FINISH);
				} catch (Exception e) {
				}
			}
		}.start();
	}

	/**
	 * 获取高低储量状态
	 */
	protected List<BikeSiteStatusView> getRemainBikeSite() {
		remainBikeSites = bikeSiteInforService.loadBikeSitesAnalysis("all");
		Cons.remainBikeSites = remainBikeSites;
		for (BikeSiteStatusView obj : remainBikeSites) {
			remainBikeSitesStatus.put(obj.getBikeSiteId(), obj);
		}
		Cons.remainBikeSitesStatus = remainBikeSitesStatus;
		if (remainBikeSites != null && remainBikeSites.size() > 0) {
			return remainBikeSites;
		}

		return null;
	}

	// 读取站点实时数据 单点
	private class LoadBikeRemain extends Thread {
		private String _bikeId;
		private WebHelper<BikeSite> helper;
		private Object key;

		public LoadBikeRemain() {
			helper = new WebHelper<BikeSite>(BikeSite.class);
			key = new Object();
			start();
		}

		@Override
		public void run() {
			while (!isExit) {
				synchronized (key) {
					try {
						key.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (null != _bikeId) {
					delay(); // 非移动手机延迟5秒
					List<BikeSiteRealView> bikeSiteRealView = bikeSiteInforService
							.loadBikeSitesReal(_bikeId);
					if (bikeSiteRealView != null) {
						bsv = bikeSiteRealView.get(0);
					} else {
						bsv = null;
					}
					handler.obtainMessage(LOAD_BIKE_REMAIN_FINISH, bsv)
							.sendToTarget();
				}
			}
		}

		public void execute(String bikeId) {
			this._bikeId = bikeId;
			synchronized (key) {
				key.notifyAll();
			}
		}

		private void delay() {
			if (imsi != null) {
				if (imsi.startsWith("46001") || imsi.startsWith("46003")) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// 点击地图放大
	public void doMapZoomUp(View v) {
		mMapController.zoomIn();
	}

	// 点击地图缩小
	public void doMapZoomDown(View v) {
		mMapController.zoomOut();
	}

	/**
	 * 弹出可借可还车辆信息
	 * 
	 * @param item
	 */
	public void showDetail(MyOverlayItem item) {
		itemClicked = item;
		if (item != null) {
			setMyIconVisibility("all", View.GONE);
			onPrepareDialog(item.getPoint());
		}
	}

	/**
	 * 关闭可借可还车辆信息
	 */
	public void closeDetail() {
		if (itemClicked != null) {
			itemClicked = null;
			setMyIconVisibility("all", View.VISIBLE);
			mMapView.removeView(itemPopView);
		}
	}

	/**
	 * 站点提示信息
	 */
	public void onPrepareDialog(GeoPoint point) {
		pop_alterView = new AlterView();
		itemPopView = LayoutInflater.from(this).inflate(R.layout.pop_item_view,
				null);
		pop_alterView.map_pop_stationName = (TextView) itemPopView
				.findViewById(R.id.map_pop_stationName);
		pop_alterView.map_pop_stationName.setText(itemClicked.getTitle());

		String stationId = getResources().getString(
				R.string.map_pop_stationId_Name);
		pop_alterView.map_pop_stationId = (TextView) itemPopView
				.findViewById(R.id.map_pop_stationId_Name);
		pop_alterView.map_pop_stationId.setText(String.format(stationId,
				itemClicked.getBikeId() + " "));

		pop_stationRealMsg = (LinearLayout) itemPopView
				.findViewById(R.id.map_pop_stationReal_Msg_Layout);
		pop_stationRealMsgStr = (TextView) itemPopView
				.findViewById(R.id.map_pop_stationReal_Msg);
		pop_stationReal = (LinearLayout) itemPopView
				.findViewById(R.id.map_pop_stationReal_Layout);

		pop_stationClose = (RelativeLayout) itemPopView
				.findViewById(R.id.map_pop_stationClose);
		pop_stationClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeDetail();
			}
		});
		pop_alterView.firstBar = (ProgressBar) itemPopView
				.findViewById(R.id.map_pop_stationReal_usebike_progress);
		pop_alterView.secondBar = (ProgressBar) itemPopView
				.findViewById(R.id.map_pop_stationReal_usepos_progress);
		pop_alterView.map_pop_stationReal_usebike = (TextView) itemPopView
				.findViewById(R.id.map_pop_stationReal_usebike);
		pop_alterView.map_pop_stationReal_usepos = (TextView) itemPopView
				.findViewById(R.id.map_pop_stationReal_usepos);
		loadBikeRemain.execute(itemClicked.getBikeId());

		pop_alterView.map_pop_station_focus = (ImageView) itemPopView
				.findViewById(R.id.map_pop_station_focus);
		pop_alterView.map_pop_station_line = (ImageView) itemPopView
				.findViewById(R.id.map_pop_station_line);
		pop_alterView.map_pop_station_search = (ImageView) itemPopView
				.findViewById(R.id.map_pop_station_search);
		pop_alterView.map_pop_station_error = (ImageView) itemPopView
				.findViewById(R.id.map_pop_station_error);

		pop_stationRealMsg.setVisibility(View.GONE);
		pop_stationReal.setVisibility(View.VISIBLE);
		/**
		 * 点击弹出层上的按钮
		 */
		pop_alterView.map_pop_station_focus
				.setOnClickListener(new onClickListener());
		pop_alterView.map_pop_station_line
				.setOnClickListener(new onClickListener());
		pop_alterView.map_pop_station_search
				.setOnClickListener(new onClickListener());
		pop_alterView.map_pop_station_error
				.setOnClickListener(new onClickListener());

		// Message msg = new Message();
		// msg.what = 100;
		// mMapController.animateTo(point, msg);

		GeoPoint pt = new GeoPoint(point.getLatitudeE6(),
				point.getLongitudeE6());
		// 创建布局参数
		layoutParam = new MapView.LayoutParams(
		// 控件宽,继承自ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// 控件高,继承自ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// 使控件固定在某个地理位置
				pt, 0, -55,
				// 控件对齐方式
				MapView.LayoutParams.BOTTOM_CENTER);
		// 添加View到MapView中
		mMapView.addView(itemPopView, layoutParam);

		GeoPoint newGeoPoint = this.covertScreenPoint(point);
		// mMapController.animateTo(newGeoPoint);
		mMapController.setCenter(newGeoPoint);
	}

	private GeoPoint covertScreenPoint(GeoPoint geoPoint) {
		Point screenpoint = new Point();
		mMapView.getProjection().toPixels(geoPoint, screenpoint);
		int x = screenpoint.x;
		int y = screenpoint.y;
		return mMapView.getProjection().fromPixels(x,
				y - CommonVariable.STAION_MAP_OFFSET_Y);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_BIKESITEDETIAL_CODE
				&& resultCode == BikeSiteDetialActivity.RETURN_BIKESITEDETIAL_CODE) {
			String longitude = data.getStringExtra("longitude");
			String latitude = data.getStringExtra("latitude");
			bikesiteName = data.getStringExtra("bikesiteName");
			bikesiteId= data.getStringExtra("bikesiteId");
			// 定位
			Double dlongitude = Double.parseDouble(longitude) * 1E6;
			Double dlatitude = Double.parseDouble(latitude) * 1E6;
			requestPoint = new GeoPoint(dlatitude.intValue(),
					dlongitude.intValue());
			closeDetail(); // 隐藏站点浮动|显示指针
			locate();
		}
	}

	/**
	 * 控制指针显示或隐藏
	 */
	public static void setMyIconVisibility(String str, int visible) {
		if (str.equalsIgnoreCase("all")) {
			mi.setVisibility(visible);
			popview.setVisibility(visible);
		}
		if (str.indexOf("mi") != -1) {
			mi.setVisibility(visible);
		}
		if (str.indexOf("popview") != -1) {
			popview.setVisibility(visible);
		}
	}

	public class onClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.map_pop_station_search:
				if (bsv == null) {
					Util.t("正在加载站点详细信息，请稍后重试");
				} else {
					Intent intent_search = new Intent();
					intent_search.setClass(iContext,
							BikeSiteDetialActivity.class);
					intent_search.putExtra("bsv", bsv);
					startActivityForResult(intent_search,
							REQUEST_BIKESITEDETIAL_CODE);
				}

				break;
			case R.id.map_pop_station_line:
				if (bsv == null) {
					Util.t("正在加载站点详细信息，请稍后重试");
				} else {
					value = bsv.getId().toString();
					searchBikeSite();
				}
				break;

			case R.id.map_pop_station_focus:
				int login_flag = Cons.LOGIN_FLAG;
				User user = Util.getLoginUser();
				if (login_flag == 0 || user == null) {
					Util.t("该功能登录用户方可使用，请先登录。");
					break;
				}
				if (itemClicked != null) {
					bikesiteId = itemClicked.getBikeId();
					// showProgressDialog1();
					saveUserAttention(user.getApnUserId(), bikesiteId);
				}
				break;
			case R.id.map_pop_station_error:
				if (bsv == null) {
					Util.t("正在加载站点详细信息，请稍后重试");
				} else {
					value = bsv.getId().toString();
					Intent intent = new Intent(iContext,
							BikeSiteErrActivity.class);
					intent.putExtra("bikesiteId", value);
					startActivity(intent);
				}
				break;
			}
		}
	}

	/**
	 * 保存用户关注站点信息
	 * 
	 * @param userId
	 * @param bikesiteId
	 */
	private void saveUserAttention(final Long userId, final String bikesiteId) {
		// 保存服务器端
		bikeSiteUserService.saveUserFocus(userId.toString(), bikesiteId,
				new AsyncOperator() {
					@Override
					public void onSuccess(Object obj) {
						Boolean flag = (Boolean) obj;
						if (flag) {
							pop_alterView.map_pop_station_focus
									.setImageResource(R.drawable.map_pop_station_focus_down);
							bikesiteIdBuffer.append(bikesiteId + "#");
							// saveLocalUserAttention(userId,bikesiteId);//保存本地关注表
						} else {
							Util.t("网络提交失败，请稍后再试。");
						}
						super.onSuccess(obj);
					}

					@Override
					public void onFail(String message) {
						Util.t("网络提交失败，请稍后再试。");
					}
				});
	}

	// private void saveLocalUserAttention(Long userId,String bikesiteId){
	// //保存服务器端
	// bikeSiteLocalUserService.saveLocalUserFocus(userId.toString(),bikesiteId,new
	// AsyncOperator() {
	// @Override
	// public void onSuccess(Object obj) {
	// CreateOrUpdateStatus state = (CreateOrUpdateStatus)obj;
	// if (state.isCreated()) {
	// pop_alterView.map_pop_station_focus.setImageResource(R.drawable.map_pop_station_focus_down);
	// //bikesiteIdBuffer.append(bikesiteId + "#");
	// Util.t("关注成功");
	// } else {
	// Util.t("关注失败");
	// }
	// super.onSuccess(obj);
	// }
	// });
	// }

	/**
	 * 获取移动点位置信息
	 * 
	 * @param g
	 */
	public static void getPosition(GeoPoint g) {
		// position_titileName.setText("获取位置中..");
		mkSerach.reverseGeocode(g);
	}

	public void initListener() {
		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
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
				curCenterPoint = mMapView.getProjection().fromPixels(MyIconw,
						MyIconh);
				// getPosition(curCenterPoint);

			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过：
				 * mMapController.enableClick(true); 时，此回调才能被触发
				 * 
				 */
				// String title = "";
				// if (mapPoiInfo != null) {
				// title = mapPoiInfo.strText;
				// Toast.makeText(IndexMainActivity.this, title,
				// Toast.LENGTH_SHORT).show();
				// // mMapController.animateTo(mapPoiInfo.geoPt);
				// }
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
				
			}
		};
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
	 * 监听位置函数. 定位类调用requestLocation()返回的结果在onReceiveLocation中做处理
	 * 
	 * @author Administrator
	 * 
	 */
	class MyLocationListener implements BDLocationListener {
		LocationData locData = new LocationData();

		@Override
		public void onReceiveLocation(BDLocation location) {
			GeoPoint pt = null;
			if (location == null) {
				Log.d("location", "定位返回结果为空");
				pt = new GeoPoint((int) (37.851969 * 1E6),
						(int) (112.56205 * 1E6));
				// return;
			} else {
				System.out
						.println("定位开始.........................................");
				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				// locData.accuracy = location.getRadius(); // 精度
				// locData.direction = 20;location.getDerect();
				myLocationOverlay.setData(locData);
				mMapView.refresh();
				pt = new GeoPoint((int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6));
			}
			Cons.locPoint = pt;

			if (requestPoint != null)
				pt = requestPoint; // 用户请求定位位置 替换请求坐标
			if (pt != null) {
				mkSerach.reverseGeocode(pt);
				mMapView.refresh();

				Message msg = new Message();
				msg.what = MAP_BIKESITE_SEARCH;
				msg.obj = pt;
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {

		}
	}

	/**
	 * 得到当前查询区域范围
	 * 
	 * @return
	 */
	private int getCurSearchArea() {
		int area = 1500;
		// 0:全城 1:500米 2:1000米 3:1500米
		if (mapAreaState == 3)
			area = 1500;
		if (mapAreaState == 2)
			area = 1000;
		if (mapAreaState == 1)
			area = 500;
		if (mapAreaState == 0)
			area = 0;
		return area;
	}

	/**
	 * 根据中心点位置和范围得到站点数据
	 * 
	 * @param point
	 * @param area
	 */
	private void queryBikeSiteByPoint(GeoPoint point, int area) {
		int num = -300; // 纠偏参数
		if (bikeSites != null)
			bikeSites.clear();
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
					bikeSites.add(bikeSite_Lite);
				}
			}
			// bikeSites = bikeOverlayDao.queryBikeSitesByBounds(bounds);
		} else {
			bikeSites = bikeOverlayDao.queryBikeSites();
		}

		String str = "";
		switch (mapAreaState) {
		case 3:
			str = "1500米";
			break;
		case 2:
			str = "1000米";
			break;
		case 1:
			str = "500米";
			break;
		case 0:
			str = "全城";
			break;
		}
		if (!str.equals(""))
			str = "搜索“" + str + "”范围，";
		Util.t(str + "找到站点：" + String.valueOf(bikeSites.size()) + "个");
		if(requestPoint!=null){
			MyOverlayItem myOverlayItem = new MyOverlayItem(requestPoint,
					bikesiteName, bikesiteId);
			showDetail(myOverlayItem);
		}
	}

	/**
	 * 取得中心点 并搜索站点
	 */
	public synchronized void loadBikeSite(GeoPoint pt) {
		if (bikeOverLayItem != null || areaGraphicsOverlay != null) {
			if (bikeOverLayItem != null)
				mMapView.getOverlays().remove(bikeOverLayItem);
			if (areaGraphicsOverlay != null)
				mMapView.getOverlays().remove(areaGraphicsOverlay);
			mMapView.refresh();
		}
		bikeOverLayItem = new BikeOverLayItem(null, mMapView,
				IndexMainActivity.this, true);
		mMapView.getOverlays().add(bikeOverLayItem);

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
		int area = getCurSearchArea();
		areaGraphicsOverlay.removeAll();
		Graphic graphicArea = drawCircle(curCenterPoint, area);// 添加圆
		Graphic graphicLine = drawCircleLine(curCenterPoint, area);// 添加圆边
		if (graphicArea != null) {
			// areaGraphicsOverlay.setData(drawCircleCenter(curCenterPoint,
			// 20)); // 添加中心参照物
			areaGraphicsOverlay.setData(graphicArea);
			areaGraphicsOverlay.setData(graphicLine);
			mMapView.getOverlays().add(areaGraphicsOverlay);
		}
		// 加载站点覆盖物
		queryBikeSiteByPoint(curCenterPoint, area);

		if (bikeSites != null) {
			addOverlay(); // 描点
		} else {
			mMapView.refresh();
		}
	}

	/**
	 * 搜索监听
	 * 
	 * @author Administrator
	 * 
	 */
	class AreaSearchListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			requestPoint=null;
			Message msg = new Message();
			msg.what = MAP_BIKESITE_SEARCH;
			msg.obj = null;
			handler.sendMessage(msg);
			// loadBikeSite(null);
		}
	}

	/**
	 * 检索监听
	 */
	class MySearchListener implements MKSearchListener {
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int arg1) {
			if (result == null) {
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(
					IndexMainActivity.this, mMapView);
			routeOverlay.setData(result.getPlan(0).getRoute(0));
			mMapView.getOverlays().add(routeOverlay);
			mMapView.refresh();
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int arg1, int arg2) {
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		}

		@Override
		public void onGetAddrResult(MKAddrInfo info, int arg1) {
			// position_titileName.setText(info.strAddr);
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
				int arg2) {
		}
	}

	/**
	 * 区域选择域
	 * 
	 * @author Administrator mapAreaState 0:全城 1:500米 2:1000米 3:1500米
	 */
	class MyAreaListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.map_area_1500:
				mapAreaState = 3;
				topBarTv.setText("1500米");
				// mapAreaCur.setImageResource(R.drawable.map_area_1500);
				break;
			case R.id.map_area_1000:
				mapAreaState = 2;
				topBarTv.setText("1000米");
				// mapAreaCur.setImageResource(R.drawable.map_area_1000);
				break;
			case R.id.map_area_500:
				mapAreaState = 1;
				topBarTv.setText("500米");
				// mapAreaCur.setImageResource(R.drawable.map_area_500);
				break;
			case R.id.map_area_all:
				mapAreaState = 0;
				topBarTv.setText("全城");
				// mapAreaCur.setImageResource(R.drawable.map_area_all);
				break;
			}
			mapAreaLayout.setVisibility(View.GONE);
			mapAreaCurLayout.setVisibility(View.VISIBLE);
			rlOpenBar.setVisibility(View.VISIBLE);

			showProgressDialog();
			// loadBikeSite(curCenterPoint); // 根据当前位置进行搜索
			// mMapController.animateTo(curCenterPoint);
			// closeProgressDialog();

			Message msg = new Message();
			msg.what = MAP_BIKESITE_SEARCH;
			msg.obj = curCenterPoint;
			handler.sendMessage(msg);
		}
	}

	class MySensorEventListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 方向传感器
			if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
				// x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西
				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				float z = event.values[SensorManager.DATA_Z];
				System.out.println("Orientation:" + x + "," + y + "," + z);
			}
		}
	}

	/**
	 * 离线地图
	 * 
	 * @author Administrator
	 * 
	 */
	class MyOfflineMapListener implements MKOfflineMapListener {
		@Override
		public void onGetOfflineMapState(int type, int state) {
			switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				MKOLUpdateElement update = mOffline.getUpdateInfo(state);
				Log.d("OfflineDemo", String.format("%s : %d%%",
						update.cityName, update.ratio));
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

	/**
	 * 列表模式
	 * 
	 * @param v
	 */

	public void doList(View v) {
		if (bikeSites.size() > 0) {
			topflag++;
			if (topflag % 2 == 0) {
				//把底下的radiobutton 点击状态改为未选中
				radioSecondPage.setChecked(true);
				radioGroup.clearCheck();
				radioFirstPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioSecondPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioThirdPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				radioFourPage.setTextColor(getResources().getColor(
						R.color.bottom_text_bg_hui));
				currentFragment = new BikeSiteListFragment(iContext);
				Drawable drawable = getResources().getDrawable(
						R.drawable.top_main_list);
				topRight.setBackgroundDrawable(drawable);
				topRight.setVisibility(View.VISIBLE);
				infaltView();
				
				
				
				// layout_above_map.setVisibility(View.GONE);
				// mMapView.onTouchEvent(null);

			} else {
				Intent intent_main = new Intent(IndexMainActivity.this,
						IndexMainActivity.class);
				startActivity(intent_main);
				finish();
			}
		} else {
			Util.t("当前范围内没有附近站点,没有可显示站点的列表信息");
		}
	}

	private static class AlterView {
		public TextView map_pop_stationId; // 站点ID
		public TextView map_pop_stationName; // 站点名称
		public ProgressBar firstBar; //
		public ProgressBar secondBar; //
		public TextView map_pop_stationReal_usebike; // 可用车辆
		public TextView map_pop_stationReal_usepos; // 可还车辆
		public ImageView map_pop_station_line;
		public ImageView map_pop_station_search;
		public ImageView map_pop_station_focus;
		public ImageView map_pop_station_error;
	}

	@Override
	public void onBackPressed() {
		// if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
		// getSupportFragmentManager().popBackStackImmediate();
		// return;
		// }else{
		// Intent intent = new Intent(this, ShowDialogActivity.class);
		// startActivity(intent);
		// }
		// super.onBackPressed();
		if (leftMenuFlag) {
			new AsynMove().execute(-SPEED);
		} else {
			exitBy2Click();
		}
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExitFlag = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExitFlag == false) {
			isExitFlag = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExitFlag = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			BikeApplication app = (BikeApplication) getApplication();
			HashMap<String, Activity> temps = app.getTempContext();
			Set<Entry<String, Activity>> sets = temps.entrySet();
			for (Entry<String, Activity> entry : sets) {
				entry.getValue().finish();
			}
			temps.clear();
			finish();
			System.exit(-1);
		}
	}

	/**
	 * 滚动条消息处理
	 */
	// private Handler handlerProgress = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	// case MAIN_LOAD_FINISH:
	// locClient(); // 定位设置
	// //locate(); // 定位请求
	// closeProgressDialog();
	// break;
	// }
	// }
	// };

	// 当进准度发生改变时
	// sensor->传感器
	// accuracy->精准度
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	// 当传感器在被改变时触发
	@Override
	public void onSensorChanged(SensorEvent event) {
		// 方向传感器
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			// x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西
			float x = event.values[SensorManager.DATA_X];
			// float y = event.values[SensorManager.DATA_Y];
			// /float z = event.values[SensorManager.DATA_Z];
			// System.out.println("Orientation:"+x+","+y+","+z);

			LocationData locData = myLocationOverlay.getMyLocation();
			if (locData != null) {
				locData.direction = x;
				myLocationOverlay.setData(locData);
				mMapView.refresh();
			}
		}
	}

	/**
	 * 查询站点
	 * 
	 */
	private void searchBikeSite() {
		new Thread() {
			private BikeOverlayDao dao = new BikeOverlayDao();
			String[] colums = { "bikesite_id" };

			public void run() {
				if (null != value) {
					if (!("".equals(value.trim()))) {
						Util.l("----------------" + value);
						String likeValue = "%" + value + "%";
						List<BikeSite_Lite> sites = dao.query4Like(colums,
								likeValue);
						if (null != sites && 0 != sites.size()) {
							bikeSite_Lite = sites.get(0);
							Double latitude = Double.parseDouble(bikeSite_Lite
									.getSign4().trim()) * 1E6;
							Double longitude = Double.parseDouble(bikeSite_Lite
									.getSign3().trim()) * 1E6;
							point = new GeoPoint(longitude.intValue(),
									latitude.intValue());
							Message msg = new Message();
							msg.what = LOAD_ONE_FINISH;
							msg.obj = point;
							handler.sendMessage(msg);
						}

					}
				}
			}

		}.start();

	}

	private void loadBaiDuOffline() {
		boolean flag = false;
		int tyCityId = 176;
		ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
		if (localMapList != null) {
			for (MKOLUpdateElement mkolUpdateElement : localMapList) {
				int cityid = mkolUpdateElement.cityID;
				boolean cityupdate = mkolUpdateElement.update;

				if (cityid == tyCityId) {// 安装过
					flag = true;
					if (cityupdate)
						flag = false; // 可更新下载
					break;
				}
			}
		}

		if (!flag) {// 没有安装过 下载
			mOffline.start(tyCityId);
		}
	}

	
}

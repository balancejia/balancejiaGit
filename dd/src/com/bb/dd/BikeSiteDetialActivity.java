package com.bb.dd;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.RuteView;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;
import com.topdt.application.entity.BikeSiteRealView;
import com.topdt.coal.entity.User;

public class BikeSiteDetialActivity extends Activity {
	private static final String TAG = BikeSiteDetialActivity.class.getName();
	public final static int RETURN_BIKESITEDETIAL_CODE = 1;
	private Context context;
	private BikeSiteRealView bsv;
	private BikeSite_Lite bikeSite_Lite;
	private TextView topTitle, bikeSiteName, bikeSiteCode, bsUserBike,
			bsUserPos;
	private ProgressBar progressBar;
	private LinearLayout bsEnd, bsStart, bsNearBy;
	private ImageView ivStart, ivEnd, ivNearby;
	private String value;
	private RuteView ruteView;
	private GeoPoint point;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.bikesite_detial);
		context = BikeSiteDetialActivity.this;
		Intent intent = getIntent();
		bsv = (BikeSiteRealView) intent.getSerializableExtra("bsv");
		value=bsv.getId().toString();
		searchBikeSite();
		initView();
		initProgress();
	}

	private void initView() {
		topTitle = (TextView) findViewById(R.id.top_tv_center);
		topTitle.setText("站点详情");
		bikeSiteName = (TextView) findViewById(R.id.tv_site_name);
		bikeSiteCode = (TextView) findViewById(R.id.tv_site_code);
		bsUserBike = (TextView) findViewById(R.id.tv_site_can_borrow_num);
		bsUserPos = (TextView) findViewById(R.id.tv_site_can_send_num);
		progressBar = (ProgressBar) findViewById(R.id.progress_sitedetail);
		ivStart = (ImageView) findViewById(R.id.iv_start);
		ivEnd = (ImageView) findViewById(R.id.iv_end);
		ivNearby = (ImageView) findViewById(R.id.iv_nearby);
		bsEnd = (LinearLayout) findViewById(R.id.iv_site_end);
		bsStart = (LinearLayout) findViewById(R.id.iv_site_start);
		bsNearBy = (LinearLayout) findViewById(R.id.iv_site_nearby);
		bikeSiteName.setText(bsv.getName());
		bikeSiteCode.setText(bsv.getId());
		bsUserBike.setText(bsv.getCanusebikecount() + "");
		bsUserPos.setText(bsv.getCanuseposcount() + "");
		/**
		 * 点击到这里去
		 */
		bsEnd.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == event.ACTION_DOWN) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.bsd_end_down);
					ivEnd.setImageDrawable(drawable);
				}
				if (event.getAction() == event.ACTION_UP) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.bsd_end);
					ivEnd.setImageDrawable(drawable);
				}

				return false;
			}
		});
		bsEnd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ruteView=new RuteView();
				ruteView.setEndAddress(bikeSite_Lite.getBikesiteName());
				ruteView.setEndlatitude(Double.valueOf(point.getLatitudeE6()));
				ruteView.setEndlongitude(Double.valueOf(point.getLongitudeE6()));
				ruteView.setFlag(2);
				Intent intent_end = new Intent();
				intent_end.setClass(context, IndexMainActivity.class);
				intent_end.putExtra("ruteView", ruteView);
				startActivity(intent_end);
				finish();
			}
		});
		/**
		 * 点击从这出发
		 */
		bsStart.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == event.ACTION_DOWN) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.bsd_start_down);
					ivStart.setImageDrawable(drawable);
				}
				if (event.getAction() == event.ACTION_UP) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.bsd_start);
					ivStart.setImageDrawable(drawable);
				}

				return false;
			}
		});
		bsStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ruteView=new RuteView();
				ruteView.setStartAddress(bikeSite_Lite.getBikesiteName());
				ruteView.setStartlatitude(Double.valueOf(point.getLatitudeE6()));
				ruteView.setStartlongitude(Double.valueOf(point.getLongitudeE6()));
				ruteView.setFlag(1);
				Intent intent_end = new Intent();
				intent_end.setClass(context,IndexMainActivity.class);
				intent_end.putExtra("ruteView", ruteView);
				startActivity(intent_end);
				finish();
			}
		});
		/**
		 * 点击找附近站点
		 */
		bsNearBy.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == event.ACTION_DOWN) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.bsd_nearby_down);
					ivNearby.setImageDrawable(drawable);
				}
				if (event.getAction() == event.ACTION_UP) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.bsd_nearby);
					ivNearby.setImageDrawable(drawable);
				}

				return false;
			}
		});
		bsNearBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_index = new Intent();
				intent_index.putExtra("longitude", bikeSite_Lite.getSign4().trim());
				intent_index.putExtra("latitude", bikeSite_Lite.getSign3().trim());
				intent_index.putExtra("bikesiteName", bikeSite_Lite.getBikesiteName()
						.trim());
				intent_index.putExtra("bikesiteId", bikeSite_Lite.getBikesiteId()
						.trim());
				setResult(RETURN_BIKESITEDETIAL_CODE,intent_index);
				finish();
			}
		});
	}

	/**
	 * 初始化站点详情进度条
	 */
	private void initProgress() {
		Double useBikecount = Double.valueOf(bsv.getCanusebikecount());
		Double usePosCount = Double.valueOf(bsv.getCanuseposcount());
		Double totalCount = useBikecount + usePosCount;
		if(totalCount!=0){
			Double count = (useBikecount / totalCount) * 100;
			DecimalFormat df = new DecimalFormat("0");
			int useBikePro = Integer.parseInt(df.format(count));
			progressBar.setProgress(useBikePro);
		}
		
	}

	/**
	 * 返回
	 */
	public void doBack(View v) {
		finish();
	}

	/**
	 * 站点纠错
	 */
	public void doCaution(View v) {
		Intent intent=new Intent(this,BikeSiteErrActivity.class);
		intent.putExtra("bikesiteId", value);
		startActivity(intent);
	}

	/**
	 * 分享
	 */
	public void doShare(View v) {
		Intent intent = new Intent(context,
				ShareActivity.class);
		startActivity(intent);

	}

	/**
	 * 关注
	 */
	public void doFocus(View v) {
		int login_flag = Cons.LOGIN_FLAG;
		User user = Util.getLoginUser();
		if (login_flag == 0 || user == null) {
			Util.t("该功能登录用户方可使用，请先登录。");
			Intent intent=new Intent(context, LoginActivity.class);
			startActivity(intent);
			return;
		}
		if (bikeSite_Lite != null) {
			String bikesiteId = bikeSite_Lite.getBikesiteId();
			// showProgressDialog1();
			saveUserAttention(user.getApnUserId(), bikesiteId);
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
		new BikeSiteUserService().saveUserFocus(userId.toString(), bikesiteId,
				new AsyncOperator() {
					@Override
					public void onSuccess(Object obj) {
						Boolean flag = (Boolean) obj;
						if (flag) {
							Util.t("关注成功");
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
	
	public void onMapPoint(View v) {
		Intent intent_point = new Intent();
		intent_point.setClass(context, MapPointActivity.class);
		intent_point.putExtra("bikesiteId", value);
		startActivity(intent_point);
	}
	
	/**
	 * 查询站点
	 *
	 */
	private void searchBikeSite() {
		new Thread() {
			private BikeOverlayDao dao=new BikeOverlayDao();
			String[] colums = { "bikesite_id" };
			public void run() {
				if (null != value) {
					if (!("".equals(value.trim()))) {

						String likeValue = "%" + value + "%";
						List<BikeSite_Lite> sites = dao.query4Like(colums,
								likeValue);
						if (null != sites && 0 != sites.size()) {
							bikeSite_Lite=sites.get(0);
							Double latitude = Double.parseDouble(bikeSite_Lite.getSign4().trim()) * 1E6;
							Double longitude = Double.parseDouble(bikeSite_Lite.getSign3().trim()) * 1E6;
							point = new GeoPoint(longitude.intValue(), latitude
									.intValue());
						}
						

					}
				}
			}
			
		}.start();

	}
}

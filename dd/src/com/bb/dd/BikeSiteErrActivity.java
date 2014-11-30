package com.bb.dd;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bb.dd.dao.BikeOverlayDao;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.service.BikeSiteInforService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Util;
import com.bb.dd.view.EditTextMaxLengthWatcher;
import com.topdt.coal.entity.BikeSiteException;

/**
 * 退出程序
 * 
 * @author Administrator
 * 
 */
public class BikeSiteErrActivity extends Activity {
	private static final String TAG = BikeSiteErrActivity.class.getName();
	private final static int REQUEST_CODE = 1;
	private final static int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	private static final int REGIST_ISNULL = 0;
	private static final int REGIST_SUCCESS = 1;
	private static final int REGIST_ERROR = 2;
	private static final int REGIST_DATAISNULL = 3;

	private RelativeLayout err_position_layout;
	private LinearLayout err_point_layout;
	private EditText err_desc;
	private TextView err_longitude, err_latitude;
	private Spinner err_type;
	private ProgressDialog progress;
	private Context context;
	private String bikesiteId;
	private BikeSite_Lite bikeSite_Lite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.bikesite_err_dialog);
		context = BikeSiteErrActivity.this;

		Intent intent = getIntent();
		bikesiteId = intent.getStringExtra("bikesiteId");
		initView();
	}

	private void initView() {
		progress = new ProgressDialog(context);
		progress.setMax(100);
		progress.setTitle("请稍候");
		progress.setMessage("正在提交......");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(true);

		err_position_layout = (RelativeLayout) findViewById(R.id.bikesite_err_position_layout);
		err_point_layout = (LinearLayout) findViewById(R.id.bikesite_err_point_layout);
		err_type = (Spinner) findViewById(R.id.bikesite_err_type);
		err_longitude = (TextView) findViewById(R.id.bikesite_err_Longitude);
		err_latitude = (TextView) findViewById(R.id.bikesite_err_Latitude);
		err_desc = (EditText) findViewById(R.id.bikesite_err_desc);
		err_desc.addTextChangedListener(new EditTextMaxLengthWatcher(200,
				err_desc));

		err_type.setOnItemSelectedListener(selectListener);
		err_point_layout.setOnClickListener(selectOnClickListener);
	}

	private OnClickListener selectOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 得到当前站点的位置
			String longitude = err_longitude.getText().toString();
			String latitude = err_latitude.getText().toString();
			String longitude_old = "";
			String latitude_old = "";

			BikeOverlayDao bikeOverlayDao = new BikeOverlayDao();
			List<BikeSite_Lite> sites = bikeOverlayDao
					.queryBikeSitesById(bikesiteId);
			if (null != sites && 0 != sites.size()) {
				bikeSite_Lite = sites.get(0);
				if (longitude.equals("") || latitude.equals("")) {
					longitude = bikeSite_Lite.getSign4().trim();
					latitude = bikeSite_Lite.getSign3().trim();
				}
				longitude_old = bikeSite_Lite.getSign4().trim();
				latitude_old = bikeSite_Lite.getSign3().trim();
			}

			Intent intent = new Intent(context, MapPointSelActivity.class);
			intent.putExtra("bikesiteId", bikesiteId);
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			intent.putExtra("latitude_old", latitude_old);
			intent.putExtra("longitude_old", longitude_old);
			startActivityForResult(intent, REQUEST_CODE);

			// startVoiceRecognitionActivity();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE
				&& resultCode == MapPointSelActivity.RETURN_CODE) {
			String longitude = data.getStringExtra("longitude");
			String latitude = data.getStringExtra("latitude");
			err_longitude.setText(longitude);
			err_latitude.setText(latitude);
		}

		// if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode ==
		// RESULT_OK) {
		// ArrayList<String> matches =
		// data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		// for (Iterator<String> iterator = matches.iterator();
		// iterator.hasNext();) {
		// String obj = (String) iterator.next();
		// System.out.println(obj);
		// }
		// }
	}

	private OnItemSelectedListener selectListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {
			String err_type_str = err_type.getSelectedItem().toString();
			if (err_type_str.equals("站点位置")) {
				err_position_layout.setVisibility(View.VISIBLE);
			} else {
				err_position_layout.setVisibility(View.GONE);
				err_longitude.setText("");
				err_latitude.setText("");
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	public void doExit(View v) {

	}

	public void doCancle(View v) {
		finish();
	}

	/**
	 * 提交故障信息
	 */
	public void doSubmit(View v) {
		String err_type_str = err_type.getSelectedItem().toString();
		String err_longitude_str = err_longitude.getText().toString();
		String err_latitude_str = err_latitude.getText().toString();
		String err_descstr_str = err_desc.getText().toString();
		if ("".equalsIgnoreCase(err_descstr_str)) {
			handler.sendEmptyMessage(REGIST_ISNULL);
		} else if (err_type_str.equals("站点位置")
				&& ("".equalsIgnoreCase(err_longitude_str) || ""
						.equalsIgnoreCase(err_latitude_str))) {
			handler.sendEmptyMessage(REGIST_DATAISNULL);
		} else {
			progress.show();
			final BikeSiteException obj = new BikeSiteException();
			obj.setBikesiteId(bikesiteId);
			obj.setExceptionType(err_type_str);
			obj.setLongitude(err_longitude_str);
			obj.setLatitude(err_latitude_str);
			obj.setExceptionDesc(err_descstr_str);
			obj.setImei(Cons.userInfor.getDeviceId());
			obj.setPhoneNumber("");
			obj.setMobileType(Cons.userInfor.getMobileType());
			new Thread() {
				public void run() {
					boolean registFlag = new BikeSiteInforService()
							.saveBikeSitesException(obj);
					if (registFlag == true) {
						handler.sendEmptyMessage(REGIST_SUCCESS);
					} else {
						handler.sendEmptyMessage(REGIST_ERROR);
					}
				}
			}.start();

		}
	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REGIST_ISNULL:
				progress.dismiss();
				Util.t("备注描述信息不能为空");
				break;
			case REGIST_DATAISNULL:
				progress.dismiss();
				Util.t("请对站点重新进行定位");
				break;
			case REGIST_SUCCESS:
				progress.dismiss();
				Util.t("站点故障信息提交成功,感谢您的支持！");
				finish();
				break;
			case REGIST_ERROR:
				progress.dismiss();
				Util.t("站点故障信息提交失败");
				break;
			}
		}
	};

	private void startVoiceRecognitionActivity() {
		try {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "请说出您要搜索的站点名称");
			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("语音识别");
			builder.setMessage("您的手机暂不支持语音搜索功能，点击确定下载安装Google语音搜索软件。您也可以在各应用商店搜索“语音搜索”进行下载安装。");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 跳转到下载语音网页
						}
					});
			builder.setNegativeButton("取消", null);
			builder.show();
		}
	}
}

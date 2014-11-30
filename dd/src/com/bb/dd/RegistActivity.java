package com.bb.dd;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
import com.bb.dd.util.PreferencesUtil;
import com.bb.dd.util.Util;
import com.bb.dd.util.WebCons;
import com.bb.dd.util.WebHelper;
import com.topdt.coal.entity.User;

public class RegistActivity extends Activity {
	private static final String TAG = RegistActivity.class.getName();
	private Context context;
	private EditText phoneNum, userPassword, password;
	private EditText et_validnum;
	private TextView tv_getValid;
	private static final int REGIST_ET_ISNULL = 0;
	private static final int REGIST_SUCCESS = 1;
	private static final int REGIST_ERROR = 2;
	private static final int PHONE_ISREGIST = 3;
	private static final int PASSWORD_NOT_SAME = 5;
	private static final int LOGIN_ERROR = 7;
	private static final int LOGIN_CONNECT_ERROR = 8;
	private static final int CODE_WUXIAO = 9;
	private static final int NUMBER_WUXIAO = 10;
	private static final int CODE_SUCCESS = 12;
	private boolean phoneNumFlag = false;
	private ProgressDialog progress;
	private Chronometer ch;
	private static final int DAO_JI_SHI = 11;
	private boolean threadFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.regist);
		context = RegistActivity.this;
		initView();
	}

	private void initView() {
		ch = (Chronometer) findViewById(R.id.test);

		progress = new ProgressDialog(context);
		progress.setMax(100);
		progress.setTitle("请稍候");
		progress.setMessage("正在注册 ...");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		TextView toptitle = (TextView) findViewById(R.id.top_tv_center);
		toptitle.setText("注册");
		phoneNum = (EditText) findViewById(R.id.phone_num);
		userPassword = (EditText) findViewById(R.id.user_password);
		password = (EditText) findViewById(R.id.password);
		et_validnum = (EditText) findViewById(R.id.reg_et_code);
		tv_getValid = (TextView) findViewById(R.id.reg_tv_getCode);
		tv_getValid.setEnabled(false);
		tv_getValid.setBackgroundResource(R.drawable.gengxin);
		phoneNum.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (phoneNum.hasFocus() == false) {

					String userPhoneEt = Util.getEditValue(phoneNum);
					if (!("".equalsIgnoreCase(userPhoneEt))) {
						boolean flag = Util.isMobileNO(phoneNum.getText()
								.toString());
						if (flag) {
							phoneNumFlag = true;
							if (threadFlag == false) {
								tv_getValid.setEnabled(true);
								tv_getValid
										.setBackgroundResource(R.drawable.huoqu_weixuan);
								tv_getValid.setText("获取验证码");
								ch.stop();
							}
						} else {
							handler.sendEmptyMessage(NUMBER_WUXIAO);
						}

					} else {
						handler.sendEmptyMessage(NUMBER_WUXIAO);
					}

				}

			}
		});
		tv_getValid.setOnClickListener(new onClickListener());

		ch.setFormat("%s");
		ch.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer ch) {
				// 如果从开始计时到现在超过了30s。
				if (SystemClock.elapsedRealtime() - ch.getBase() > 60 * 1000)
				/*
				 * if(ch.getBase()- SystemClock.elapsedRealtime() >0)
				 */
				{

					tv_getValid.setEnabled(true);
					tv_getValid.setBackgroundResource(R.drawable.huoqu_weixuan);
					tv_getValid.setText("获取验证码");
					threadFlag = false;
					ch.stop();
				}
			}
		});
	}

	public class onClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.reg_tv_getCode:
				final String userPhoneEt = Util.getEditValue(phoneNum);
				if ("".equalsIgnoreCase(userPhoneEt)) {
					Util.t("请输入手机号");
					return;
				}
				tv_getValid.setEnabled(false);
				tv_getValid.setBackgroundResource(R.drawable.gengxin);
				// 设置开始计时时间
				ch.setBase(SystemClock.elapsedRealtime());
				// ch.setBase(3);
				// 启动计时器
				ch.start();
				new Thread() {
					public void run() {
						threadFlag = true;
						for (int i = 60; i > 0; i--) {
							Message message = new Message();
							message.what = DAO_JI_SHI;
							message.obj = i;
							handler.sendMessage(message);
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
				}.start();

				new Thread() {
					public void run() {
						String url = WebCons.REQUEST_PHONO_CODE + userPhoneEt;
						WebHelper help = new WebHelper();
						String result = help.getResults(url, null);
						try {
							JSONObject json = new JSONObject(result);
							int num = Integer.parseInt(json.get("code")
									.toString());
							if (num == 2) {
								handler.sendEmptyMessage(NUMBER_WUXIAO);
							} else {
								handler.sendEmptyMessage(CODE_SUCCESS);

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};

				}.start();
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 注册
	 */
	public void goRegist(View v) {
		final String phoneNumEt = Util.getEditValue(phoneNum);
		String userPasswordEt = Util.getEditValue(userPassword);
		final String passwordEt = Util.getEditValue(password);
		final String code = Util.getEditValue(et_validnum);
		if ("".equalsIgnoreCase(phoneNumEt) || "".equalsIgnoreCase(passwordEt)
				|| "".equalsIgnoreCase(userPasswordEt)
				|| "".equalsIgnoreCase(code)) {
			handler.sendEmptyMessage(REGIST_ET_ISNULL);

		} else {
			if (!(userPasswordEt.equals(passwordEt))) {
				handler.sendEmptyMessage(PASSWORD_NOT_SAME);
			} else {
				progress.show();
				new Thread() {

					@Override
					public void run() {
						if (phoneNumFlag) {
							String url = WebCons.REQUEST_REGISTER_URL
									+ phoneNumEt + "&pswd=" + passwordEt
									+ "&valid=" + code;
							WebHelper help = new WebHelper();
							String result = help.getResults(url, null);
							try {
								JSONObject json = new JSONObject(result);
								int num = Integer.parseInt(json.get("code")
										.toString());
								String info = json.get("rspdesc").toString();
								switch (num) {
								case 0:
									new Thread() {
										@Override
										public void run() {
											String url = WebCons.REQUEST_LOGIN_URL
													+ phoneNumEt
													+ "&pswd="
													+ passwordEt;
											WebHelper help = new WebHelper();
											String result = help.getResults(
													url, null);
											try {
												JSONObject json = new JSONObject(
														result);
												int num = Integer
														.parseInt(json.get(
																"code")
																.toString());
												switch (num) {
												case 0:
													Long id = (long) Integer
															.parseInt(json.get(
																	"id")
																	.toString());
													int sex = Integer
															.parseInt(json.get(
																	"sex")
																	.toString());
													int age = Integer
															.parseInt(json.get(
																	"age")
																	.toString());
													String photo = json.get(
															"photo").toString();
													String name = json.get(
															"name").toString();
													User userLogin = new User();
													userLogin
															.setPhoneNumber(phoneNumEt);
													userLogin.setApnUserId(id);
													userLogin
															.setPortrait(photo);
													userLogin.setSex(sex);
													userLogin.setAge(age);
													userLogin.setUsername(name);
													Cons.LOGIN_FLAG = 1;

													PreferencesUtil
															.setStr(PreferencesUtil.USER_PHOTO,
																	"");
													PreferencesUtil
															.setLong(
																	PreferencesUtil.USER_ID,
																	userLogin
																			.getApnUserId());
													PreferencesUtil
															.setStr(PreferencesUtil.USER_NAME,
																	userLogin
																			.getUsername());
													PreferencesUtil
															.setStr(PreferencesUtil.USER_PHONE,
																	userLogin
																			.getPhoneNumber());
													PreferencesUtil
															.setValue(
																	PreferencesUtil.USER_SEX,
																	userLogin
																			.getSex());
													PreferencesUtil
															.setValue(
																	PreferencesUtil.USER_AGE,
																	userLogin
																			.getAge());

													handler.sendEmptyMessage(REGIST_SUCCESS);
													break;
												case 9:
													handler.sendEmptyMessage(LOGIN_ERROR);
													break;
												case -1:
													handler.sendEmptyMessage(LOGIN_CONNECT_ERROR);
													break;
												}
											} catch (Exception e) {
												handler.sendEmptyMessage(LOGIN_CONNECT_ERROR);
												e.printStackTrace();
											}
										}
									}.start();
									PreferencesUtil.setStr(
											PreferencesUtil.USER_PWD,
											passwordEt);
									break;
								case -1:
									handler.sendEmptyMessage(LOGIN_CONNECT_ERROR);
									break;
								case 2:
									if ("验证码无效".equals(info)) {
										handler.sendEmptyMessage(CODE_WUXIAO);
									} else {
										handler.sendEmptyMessage(NUMBER_WUXIAO);
									}
									break;
								case 109:
									handler.sendEmptyMessage(PHONE_ISREGIST);
									break;
								}
							} catch (Exception e) {
								handler.sendEmptyMessage(REGIST_ERROR);
								e.printStackTrace();
							}
						} else {
							handler.sendEmptyMessage(NUMBER_WUXIAO);
						}
					}
				}.start();

			}
		}

	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REGIST_ET_ISNULL:
				progress.dismiss();
				Util.t("手机号,密码或验证码不能为空");
				break;
			case REGIST_SUCCESS:
				progress.dismiss();
				Util.t("恭喜你,注册成功,自动登录");
				finish();
				Intent intent_login = new Intent(context,
						IndexMainActivity.class);
				startActivity(intent_login);
				break;
			case REGIST_ERROR:
				progress.dismiss();
				Util.t("注册失败");
				break;
			case PHONE_ISREGIST:
				progress.dismiss();
				Util.t("该手机号已经注册过");
				break;
			case PASSWORD_NOT_SAME:
				progress.dismiss();
				Util.t("两次密码输入不一致");
				break;
			case LOGIN_ERROR:
				progress.dismiss();
				Util.t("用户名或密码错误");
				break;
			case LOGIN_CONNECT_ERROR:
				Util.t("请求网络失败");
				progress.dismiss();
				break;
			case CODE_WUXIAO:
				Util.t("验证码无效");
				progress.dismiss();
				break;
			case NUMBER_WUXIAO:
				tv_getValid.setEnabled(false);
				tv_getValid.setBackgroundResource(R.drawable.gengxin);
				Util.t("手机号码只能以139|138|137|136|135|134|159|158|152|151|150|157|188|187|183|147|182开头11位");
				progress.dismiss();
				break;
			case DAO_JI_SHI:
				int i = (Integer) msg.obj;
				tv_getValid.setText("点击重发(" + i + ")");
				break;
			case CODE_SUCCESS:
				Util.t("短信验证码已经发送，请查收");
				break;
			}
		}
	};

	/**
	 * 登录
	 */
	public void doCancle(View v) {
		Intent intent_login = new Intent(context, LoginActivity.class);
		startActivity(intent_login);
		finish();
	}

	/**
	 * 返回
	 */
	public void doBack(View v) {
		Intent intent_login = new Intent(context, LoginActivity.class);
		startActivity(intent_login);
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent_login = new Intent(context, LoginActivity.class);
			startActivity(intent_login);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

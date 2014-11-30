package com.bb.dd;

import java.io.File;
import java.io.FileOutputStream;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Base64Coder;
import com.bb.dd.util.Cons;
import com.bb.dd.util.PreferencesUtil;
import com.bb.dd.util.Util;
import com.bb.dd.util.WebCons;
import com.bb.dd.util.WebHelper;
import com.topdt.coal.entity.User;

public class LoginActivity extends Activity {
	private static final String TAG = LoginActivity.class.getName();
	private EditText phoneNum, password;
	private ProgressDialog progress,progressPwd;
	private Context context;
	private CheckBox remPwd, autoLogin;
	private static final int LOGIN_ET_ISNULL = 0;
	private static final int LOGIN_SUCCESS = 1;
	private static final int LOGIN_ERROR = 2;
	private static final int LOGIN_CONNECT_ERROR = 3;
	private static final int NUMBER_WUXIAO = 4;
	private static final int CODE_SUCCESS = 5;
	private TextView regist;
	private TextView forgetPwd;
	private String focus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.login);
		context = LoginActivity.this;
		focus = getIntent().getStringExtra("focus");
		initView();
	}

	private void initView() {
		progress = new ProgressDialog(context);
		progress.setMax(100);
		progress.setTitle("请稍候");
		progress.setMessage("正在登录 ...");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(true);
		progressPwd = new ProgressDialog(context);
		progressPwd.setMax(100);
		progressPwd.setTitle("请稍候");
		progressPwd.setMessage("正在获取静态密码 ...");
		progressPwd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressPwd.setCancelable(true);
		TextView toptitle = (TextView) findViewById(R.id.top_tv_center);
		toptitle.setText("登录");
		phoneNum = (EditText) findViewById(R.id.phone_num);
		password = (EditText) findViewById(R.id.password);
		remPwd = (CheckBox) findViewById(R.id.rem_psw);
		autoLogin = (CheckBox) findViewById(R.id.auto_login);
		/*lostPassword=(TextView)findViewById(R.id.lose_password);
		lostPassword.setText(Html.fromHtml("<u>忘记密码？</u>"));
		lostPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//忘记密码？
				Intent intent=new Intent(context, ResetPasswordActivity.class);
				startActivity(intent);
			}
		});*/
		regist=(TextView)findViewById(R.id.tv_regist);
		regist.setText(Html.fromHtml("<u>没有账号？赶紧来注册</u>"));
		forgetPwd=(TextView)findViewById(R.id.tv_forget_pwd);
		forgetPwd.setText(Html.fromHtml("<u>忘记密码？</u>"));
		if (PreferencesUtil.getValue(PreferencesUtil.REMEMBER_PASSWORD) == 1) {
			remPwd.setChecked(true);
			phoneNum.setText(PreferencesUtil.getStr(PreferencesUtil.USER_PHONE));
			password.setText(PreferencesUtil.getStr(PreferencesUtil.USER_PWD));
		}
		if (PreferencesUtil.getValue(PreferencesUtil.AUTO_LOGIN) == 1) {
			autoLogin.setChecked(true);
		}
		regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_login = new Intent(context, RegistActivity.class);
				startActivity(intent_login);
				finish();
				
			}
		});
		forgetPwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String userPhoneEt = Util.getEditValue(phoneNum);
				if ("".equalsIgnoreCase(userPhoneEt)) {
					Util.t("请输入手机号");
					return;
				}
				progressPwd.show();
				new Thread() {
					public void run() {
						String url = WebCons.REQUEST_STATIC_PWD + userPhoneEt;
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
			}
		});
	}
	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOGIN_ET_ISNULL:
				progress.dismiss();
				Util.t("手机号或密码不能为空");
				break;
			case LOGIN_SUCCESS:
				progress.dismiss();
				if (!TextUtils.isEmpty(focus)) {
					finish();
				} else {
					Intent intent_login = new Intent(context, IndexMainActivity.class);
					startActivity(intent_login);
				}
				
				finish();
				break;
			case LOGIN_ERROR:
				progress.dismiss();
				Util.t("手机号或密码错误");
				break;
			case LOGIN_CONNECT_ERROR:
				Util.t("请求网络失败");
				progress.dismiss();
				break;
			case NUMBER_WUXIAO:
				Util.t("手机号码只能以139|138|137|136|135|134|159|158|152|151|150|157|188|187|183|147|182开头11位");
				progressPwd.dismiss();
				break;
			case CODE_SUCCESS:
				progressPwd.dismiss();
				Util.t("静态登录密码已经发送，请查收");
				break;
			}
		}
	};

	/**
	 * 登录
	 */
//	public void goLogin(View v) {
//		String phoneNumEt = Util.getEditValue(phoneNum);
//		String passwordEt = Util.getEditValue(password);
//		if ("".equalsIgnoreCase(phoneNumEt) || "".equalsIgnoreCase(passwordEt)) {
//			handler.sendEmptyMessage(LOGIN_ET_ISNULL);
//		} else {
//			progress.show();
//			User user = new User();
//			user.setPhoneNumber(phoneNumEt);
//			user.setPassword(passwordEt);
//			new BikeSiteUserService().loginUser(user, new AsyncOperator() {
//
//				@Override
//				public void onSuccess(Object obj) {
//					User userLogin = (User) obj;
//					if (userLogin != null) {
//						if (!userLogin.getPhoneNumber().equals(
//								PreferencesUtil
//										.getStr(PreferencesUtil.USER_PHONE))) {
////							Dao<UserFocusBikesite, Integer> userAttentionBikesiteDao = DaoFactory
////									.instant().getUserFocusBikesiteDao();
////							List<UserAttentionBikesite> userAttentionBikesites = new BikeSiteUserService()
////									.loadUserFocus(String.valueOf(userLogin
////											.getApnUserId()));
////							for (UserAttentionBikesite userAttentionBikesite : userAttentionBikesites) {
////								try {
////									UserFocusBikesite userFocusBikesite = new UserFocusBikesite();
////									BeanUtils.copyProperties(userFocusBikesite,
////											userAttentionBikesite);
////									userAttentionBikesiteDao
////											.createOrUpdate(userFocusBikesite);
////								} catch (SQLException e) {
////									e.printStackTrace();
////								} catch (Exception e) {
////									e.printStackTrace();
////								}
////							}
//							Cons.LOGIN_FLAG = 1;
//							if (userLogin.getPortrait() != null
//									&& userLogin.getPortrait().trim() != "") {
//								String imgPhoto = new BikeSiteUserService()
//										.downloadPortrait(userLogin);
//								if (imgPhoto != null) {
//									String protrait = userLogin.getPortrait();
//									int start = protrait.lastIndexOf("/");
//									int end = protrait.indexOf(".");
//									String imageName = protrait.substring(
//											start + 1, end);
//									PreferencesUtil.setStr(
//											PreferencesUtil.USER_PHOTO,
//											imageName);
//									Util.l(imageName);
//									FileOutputStream out;
//									try {
//										String path = Environment
//												.getExternalStorageDirectory()
//												.getAbsolutePath()
//												+ "/_TAIYUAN_PBIKE/userPhoto/";
//										File dir = new File(path);
//										if (!dir.exists()) {
//											dir.mkdirs();
//										}
//										out = new FileOutputStream(Environment
//												.getExternalStorageDirectory()
//												.getAbsolutePath()
//												+ "/_TAIYUAN_PBIKE/userPhoto/"
//												+ imageName);
//										byte[] bytes = Base64Coder
//												.decodeLines(imgPhoto);
//										out.write(bytes);
//										out.flush();
//										out.close();
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								} else {
//									PreferencesUtil.setStr(
//											PreferencesUtil.USER_PHOTO, "");
//								}
//
//							} else {
//								PreferencesUtil.setStr(
//										PreferencesUtil.USER_PHOTO, "");
//							}
//							PreferencesUtil.setLong(PreferencesUtil.USER_ID,
//									userLogin.getApnUserId());
//							PreferencesUtil.setStr(PreferencesUtil.USER_NAME,
//									userLogin.getUsername());
//							PreferencesUtil.setStr(PreferencesUtil.USER_PHONE,
//									userLogin.getPhoneNumber());
//							PreferencesUtil.setValue(PreferencesUtil.USER_SEX,
//									userLogin.getSex());
//							PreferencesUtil.setValue(PreferencesUtil.USER_AGE,
//									userLogin.getAge());
//							PreferencesUtil.setStr(PreferencesUtil.USER_PWD,
//									userLogin.getPassword());
//							handler.sendEmptyMessage(LOGIN_SUCCESS);
//						} else {
//							Cons.LOGIN_FLAG = 1;
//							handler.sendEmptyMessage(LOGIN_SUCCESS);
//						}
//						if (remPwd.isChecked()) {
//							PreferencesUtil.setValue(
//									PreferencesUtil.REMEMBER_PASSWORD, 1);
//						} else {
//							PreferencesUtil.setValue(
//									PreferencesUtil.REMEMBER_PASSWORD, 0);
//						}
//						if (autoLogin.isChecked()) {
//							PreferencesUtil.setValue(
//									PreferencesUtil.AUTO_LOGIN, 1);
//						} else {
//							PreferencesUtil.setValue(
//									PreferencesUtil.AUTO_LOGIN, 0);
//						}
//					} else {
//						handler.sendEmptyMessage(LOGIN_ERROR);
//					}
//
//					super.onSuccess(obj);
//				}
//
//				@Override
//				public void onFail(String message) {
//					handler.sendEmptyMessage(LOGIN_CONNECT_ERROR);
//					super.onFail(message);
//				}
//
//			});
//
//		}
//	}

	/**
	 * 返回
	 */
	public void doBack(View v) {
		finish();
	}
	public void goLogin(View v) {
		progress.show();
		final String phoneNumEt = Util.getEditValue(phoneNum);
		final String passwordEt = Util.getEditValue(password);
		if ("".equalsIgnoreCase(phoneNumEt) || "".equalsIgnoreCase(passwordEt)) {
			handler.sendEmptyMessage(LOGIN_ET_ISNULL);
		} else {
			new Thread() {
				@Override
				public void run() {
						String url = WebCons.REQUEST_LOGIN_URL + phoneNumEt + "&pswd="
								+ passwordEt;
						WebHelper help = new WebHelper();
						String result = help.getResults(url, null);
						try {
							JSONObject json = new JSONObject(result);
							int num = Integer.parseInt(json.get("code").toString());
							Util.l("-----"+num);
							switch (num) {
							case 0:
								Long id=Long.valueOf(json.get("id").toString());
								int sex=Integer.parseInt(json.get("sex").toString());
								int age=Integer.parseInt(json.get("age").toString());
								String photo=json.get("photo").toString();
								String name=json.get("name").toString();
								User userLogin=new User();
								userLogin.setPhoneNumber(phoneNumEt);
								userLogin.setApnUserId(id);
								userLogin.setPortrait(photo);
								userLogin.setSex(sex);
								userLogin.setAge(age);
								userLogin.setUsername(name);
								if (!userLogin.getPhoneNumber().equals(
										PreferencesUtil
												.getStr(PreferencesUtil.USER_PHONE))) {
									Cons.LOGIN_FLAG = 1;
									if (userLogin.getPortrait() != null
											&& userLogin.getPortrait().trim() != "") {
										String imgPhoto = new BikeSiteUserService()
												.downloadPortrait(userLogin);
										if (imgPhoto != null) {
											String protrait = userLogin.getPortrait();
											Util.l(protrait);
											int start = protrait.lastIndexOf("/");
											int end = protrait.indexOf(".");
											String imageName = protrait.substring(
													start + 1, end);
											PreferencesUtil.setStr(
													PreferencesUtil.USER_PHOTO,
													imageName);
											Util.l(imageName);
											FileOutputStream out;
											try {
												String path = Environment
														.getExternalStorageDirectory()
														.getAbsolutePath()
														+ "/_TAIYUAN_PBIKE/userPhoto/";
												File dir = new File(path);
												if (!dir.exists()) {
													dir.mkdirs();
												}
												out = new FileOutputStream(Environment
														.getExternalStorageDirectory()
														.getAbsolutePath()
														+ "/_TAIYUAN_PBIKE/userPhoto/"
														+ imageName);
												byte[] bytes = Base64Coder
														.decodeLines(imgPhoto);
												out.write(bytes);
												out.flush();
												out.close();
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											PreferencesUtil.setStr(
													PreferencesUtil.USER_PHOTO, "");
										}

									} else {
										PreferencesUtil.setStr(
												PreferencesUtil.USER_PHOTO, "");
									}
									PreferencesUtil.setLong(PreferencesUtil.USER_ID,
											userLogin.getApnUserId());
									PreferencesUtil.setStr(PreferencesUtil.USER_NAME,
											userLogin.getUsername());
									PreferencesUtil.setStr(PreferencesUtil.USER_PHONE,
											userLogin.getPhoneNumber());
									PreferencesUtil.setValue(PreferencesUtil.USER_SEX,
											userLogin.getSex());
									PreferencesUtil.setValue(PreferencesUtil.USER_AGE,
											userLogin.getAge());
									handler.sendEmptyMessage(LOGIN_SUCCESS);
								} else {
									Cons.LOGIN_FLAG = 1;
									handler.sendEmptyMessage(LOGIN_SUCCESS);
								}
								if (remPwd.isChecked()) {
									PreferencesUtil.setValue(
											PreferencesUtil.REMEMBER_PASSWORD, 1);
								} else {
									PreferencesUtil.setValue(
											PreferencesUtil.REMEMBER_PASSWORD, 0);
								}
								if (autoLogin.isChecked()) {
									PreferencesUtil.setValue(
											PreferencesUtil.AUTO_LOGIN, 1);
								} else {
									PreferencesUtil.setValue(
											PreferencesUtil.AUTO_LOGIN, 0);
								}
								PreferencesUtil
								.setStr(PreferencesUtil.USER_PWD,
										passwordEt);
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
			
		}
		
	}


}

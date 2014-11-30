package com.bb.dd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Cons;
import com.bb.dd.util.PreferencesUtil;
import com.bb.dd.util.Util;
import com.topdt.coal.entity.User;

public class ChangePasswordActivity extends Activity {
	private static final String TAG = ChangePasswordActivity.class.getName();
	private Context context;
	private EditText phoneNum, userOldPassword, userPassword, password;
	private static final int SAVE_ET_ISNULL = 0;
	private static final int SAVE_SUCCESS = 1;
	private static final int SAVE_ERROR = 2;
	private static final int PASSWORD_NOT_SAME = 5;
	private static final int LOGIN_ERROR = 6;
	private static final int ERROR = 7;
	private ProgressDialog progress;
	private String passwordEt; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.change_password);
		context = ChangePasswordActivity.this;
		initView();
	}

	private void initView() {
		progress = new ProgressDialog(context);
		progress.setMax(100);
		progress.setTitle("请稍候");
		progress.setMessage("正在修改 ......");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(true);
		TextView toptitle = (TextView) findViewById(R.id.top_tv_center);
		toptitle.setText("修改密码");
		phoneNum = (EditText) findViewById(R.id.phone_num);
		phoneNum.setText(PreferencesUtil.getStr(PreferencesUtil.USER_PHONE));
		phoneNum.setFocusable(false);
		phoneNum.setFocusableInTouchMode(false);
		userOldPassword = (EditText) findViewById(R.id.user_old_password);
		userPassword = (EditText) findViewById(R.id.user_password);
		password = (EditText) findViewById(R.id.password);
	}

	public void goSavePassword(View v) {
		final String phoneNumEt = Util.getEditValue(phoneNum);
		String userOldPasswordEt = Util.getEditValue(userOldPassword);
		String userPasswordEt = Util.getEditValue(userPassword);
		passwordEt= Util.getEditValue(password);
		if ("".equalsIgnoreCase(phoneNumEt)
				|| "".equalsIgnoreCase(userOldPasswordEt)
				|| "".equalsIgnoreCase(passwordEt)
				|| "".equalsIgnoreCase(userPasswordEt)) {
			handler.sendEmptyMessage(SAVE_ET_ISNULL);

		} else {
			if (!(userPasswordEt.equals(passwordEt))) {
				handler.sendEmptyMessage(PASSWORD_NOT_SAME);
			} else {
				progress.show();
				User userCheckLogin = new User();
				userCheckLogin.setPhoneNumber(phoneNumEt);
				userCheckLogin.setPassword(userOldPasswordEt);

				new BikeSiteUserService().loginUser(userCheckLogin,
						new AsyncOperator() {

							@Override
							public void onSuccess(Object obj) {
								User userLogin = (User) obj;
								if (userLogin != null) {
									Cons.LOGIN_FLAG = 1;
									User users=new User();
									users.setApnUserId(userLogin.getApnUserId());
									users.setAge(userLogin.getAge());
									users.setSex(userLogin.getSex());
									users.setUsername(userLogin.getUsername());
									users.setPortrait(userLogin.getPortrait());
									users.setState(userLogin.getState());
									users.setPhoneNumber(phoneNumEt);
									users.setPassword(passwordEt);
									new BikeSiteUserService().changePassword(
											users, new AsyncOperator() {
												@Override
												public void onSuccess(Object obj) {
													Boolean flag = (Boolean) obj;
													if (flag == true) {
														handler.sendEmptyMessage(SAVE_SUCCESS);
													} else {
														handler.sendEmptyMessage(SAVE_ERROR);
													}
													super.onSuccess(obj);
												}

												@Override
												public void onFail(
														String message) {
													handler.sendEmptyMessage(ERROR);
													super.onFail(message);
												}

											});
									super.onSuccess(obj);
								} else {
									handler.sendEmptyMessage(LOGIN_ERROR);
								}
							}

							@Override
							public void onFail(String message) {
								handler.sendEmptyMessage(ERROR);
								super.onFail(message);
							}

						});

			}

		}
	}

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SAVE_ET_ISNULL:
				progress.dismiss();
				Util.t("手机号或密码不能为空");
				break;
			case SAVE_SUCCESS:
				progress.dismiss();
				Util.t("恭喜你,密码修改成功");
				break;
			case SAVE_ERROR:
				progress.dismiss();
				Util.t("密码修改失败");
				break;
			case PASSWORD_NOT_SAME:
				progress.dismiss();
				Util.t("两次密码输入不一致");
				break;
			case LOGIN_ERROR:
				progress.dismiss();
				Util.t("旧密码验证失败,请重新输入");
				userOldPassword.setText("");
				break;
			case ERROR:
				progress.dismiss();
				break;
			}
		}
	};

	/**
	 * 取消
	 */
	public void doCancle(View v) {
		finish();
	}

	/**
	 * 返回
	 */
	public void doBack(View v) {
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

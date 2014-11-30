package com.bb.dd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.PreferencesUtil;
import com.bb.dd.util.Util;
import com.topdt.coal.entity.User;

/**
 * 编辑用户信息
 */
public class EditUserMsgActivity extends Activity {
	private static final String TAG = EditUserMsgActivity.class.getName();
	private ProgressDialog progress;
	private Context context;
	private EditText userPhone, userName, userAge;
	private RadioGroup radioGroup;
	private User user;
	private boolean sexFlag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.edit_user_msg);
		context = EditUserMsgActivity.this;
		initView();

	}

	private void initView() {
		user = new User();
		progress = new ProgressDialog(context);
		progress.setMax(100);
		progress.setTitle("请稍候");
		progress.setMessage("正在保存 ......");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(true);
		TextView toptitle = (TextView) findViewById(R.id.top_tv_center);
		toptitle.setText("个人资料");
		userPhone = (EditText) findViewById(R.id.phone_num);
		userName = (EditText) findViewById(R.id.user_name);
		userAge = (EditText) findViewById(R.id.user_age);
		userPhone.setText(PreferencesUtil.getStr(PreferencesUtil.USER_PHONE));
		userName.setText(PreferencesUtil.getStr(PreferencesUtil.USER_NAME));
		userAge.setText(PreferencesUtil.getValue(PreferencesUtil.USER_AGE)+"");
		userPhone.setFocusable(false);
		userPhone.setFocusableInTouchMode(false);
		radioGroup = (RadioGroup) findViewById(R.id.user_sex);
		RadioButton buttonBoy=(RadioButton)findViewById(R.id.sex_boy);
		RadioButton buttonGirl=(RadioButton)findViewById(R.id.sex_girl);
		buttonBoy.setChecked(true);
		int sexI=PreferencesUtil.getValue(PreferencesUtil.USER_SEX);
		if(sexI==0){
			buttonGirl.setChecked(true);
		}else{
			buttonBoy.setChecked(true);
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				sexFlag=true;
				int radioButtonId = arg0.getCheckedRadioButtonId();
				RadioButton radioButton = (RadioButton) findViewById(radioButtonId);
				if (radioButton.getText().equals("男")) {
					user.setSex(1);
				} else {
					user.setSex(0);
				}
			}
		});
		userAge.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (userAge.hasFocus() == false) {
					String userAgeEt = Util.getEditValue(userAge);
					if (!("".equalsIgnoreCase(userAgeEt))) {
						boolean flag = Util.isAgeNo(userAge.getText()
								.toString());
						if (flag == false) {
							Util.t("请输入合适的年龄");
							userAge.setText("");
						} 
					}
				}
			}
		});

	}

	/**
	 * 保存
	 */
	public void doSave(View v) {

		String userNameEt = Util.getEditValue(userName);
		String userAgeEt = Util.getEditValue(userAge);

		user.setApnUserId(PreferencesUtil.getLong(PreferencesUtil.USER_ID));
		user.setPhoneNumber(PreferencesUtil
				.getStr(PreferencesUtil.USER_PHONE));
		if (sexFlag==false) {
			user.setSex(1);
		}
		if ("".equalsIgnoreCase(userNameEt)) {
			Util.t("昵称不能为空");
		} else {
			if ("".equalsIgnoreCase(userAgeEt)) {
				user.setAge(PreferencesUtil.getValue(PreferencesUtil.USER_AGE));
				progress.show();
				user.setUsername(userNameEt);
				user.setPassword(PreferencesUtil
						.getStr(PreferencesUtil.USER_PWD));
				new BikeSiteUserService().saveUserInfor(user,new AsyncOperator(){

					@Override
					public void onSuccess(Object obj) {
						Boolean registFlag = (Boolean) obj;
						if (registFlag == true) {
							progress.dismiss();
							PreferencesUtil.setLong(PreferencesUtil.USER_ID,
									user.getApnUserId());
							PreferencesUtil.setStr(PreferencesUtil.USER_NAME,
									user.getUsername());
							PreferencesUtil.setStr(PreferencesUtil.USER_PHONE,
									user.getPhoneNumber());
							PreferencesUtil.setValue(PreferencesUtil.USER_SEX,
									user.getSex());
							PreferencesUtil.setValue(PreferencesUtil.USER_AGE,
									user.getAge());
							PreferencesUtil.setStr(PreferencesUtil.USER_PWD,
									user.getPassword());
							Util.t("保存成功");
						} else {
							progress.dismiss();
							Util.t("保存失败,请重试");
						}
						super.onSuccess(obj);
					}

					@Override
					public void onFail(String message) {
						super.onFail(message);
					}
							
				});
				
			} else {
				boolean flag = Util.isAgeNo(userAge.getText().toString());
				if (flag == false) {
					Util.t("请输入合适的年龄");
					userAge.setText("");
				} else {
					user.setAge(Integer.parseInt(userAgeEt));
					progress.show();
					user.setUsername(userNameEt);
					user.setPassword(PreferencesUtil
							.getStr(PreferencesUtil.USER_PWD));
					new BikeSiteUserService().saveUserInfor(user,new AsyncOperator(){

						@Override
						public void onSuccess(Object obj) {
							Boolean registFlag = (Boolean) obj;
							if (registFlag == true) {
								progress.dismiss();
								PreferencesUtil.setLong(PreferencesUtil.USER_ID,
										user.getApnUserId());
								PreferencesUtil.setStr(PreferencesUtil.USER_NAME,
										user.getUsername());
								PreferencesUtil.setStr(PreferencesUtil.USER_PHONE,
										user.getPhoneNumber());
								PreferencesUtil.setValue(PreferencesUtil.USER_SEX,
										user.getSex());
								PreferencesUtil.setValue(PreferencesUtil.USER_AGE,
										user.getAge());
								PreferencesUtil.setStr(PreferencesUtil.USER_PWD,
										user.getPassword());
								Util.t("保存成功");
							} else {
								progress.dismiss();
								Util.t("保存失败,请重试");
							}
							super.onSuccess(obj);
						}

						@Override
						public void onFail(String message) {
							super.onFail(message);
						}
								
					});
				}
			}
		}
	}
	public void goChangePassword(View v) {
		Intent intent=new Intent();
		intent.setClass(context, ChangePasswordActivity.class);
		startActivity(intent);
	}

	/**
	 * 返回
	 */
	public void doBack(View v) {
		finish();
	}

	public void doCancle(View v) {
		finish();
	}

}

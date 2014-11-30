package com.bb.dd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.Util;

public class ResetPasswordActivity extends Activity{
	private EditText phoneNum,userCode;
	private Button btGetUserCode;
	private String phoneNumEt;
	private static final int OUT_OF_TIMES=0;
	private static final int GET_SUCCESS=1;
	private static final int GET_ERROR=2;
	private static final int PHONENUM_IS_NULL=4;
	private static final int ET_IS_NULL=5;
	private static final int OUT_OF_DATE=6;
	private static final int PHONE_NOMOBILE =7;
	private ProgressDialog progress;
	private static final int ERROR =8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password);
		initView();
	}

	private void initView() {
		progress = new ProgressDialog(ResetPasswordActivity.this);
		progress.setMax(100);
		progress.setTitle("请稍候");
		progress.setMessage("正在加载 ......");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(true);
		phoneNum=(EditText)findViewById(R.id.phone_num);
		userCode=(EditText)findViewById(R.id.user_code);
		btGetUserCode=(Button)findViewById(R.id.bt_get_user_code);
		phoneNum.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (phoneNum.hasFocus() == false) {
					boolean flag = Util.isMobileNO(phoneNum.getText()
							.toString());
					if(flag==false){
						handler.sendEmptyMessage(PHONE_NOMOBILE);
					}
				}
				
			}
		});
		btGetUserCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				phoneNumEt=Util.getEditValue(phoneNum);
				if("".equalsIgnoreCase(phoneNumEt)){
					handler.sendEmptyMessage(PHONENUM_IS_NULL);
				}else{
					progress.show();
					new BikeSiteUserService().getUserCode(phoneNumEt, new AsyncOperator(){

						@Override
						public void onSuccess(Object obj) {
							String  result = (String) obj;
							if(result.equals("out of times")){
								handler.sendEmptyMessage(OUT_OF_TIMES);
							}
							else if(result.equals("true")){
								handler.sendEmptyMessage(GET_SUCCESS);
							}
							else if(result.equals("false")){
								handler.sendEmptyMessage(GET_ERROR);
								
							}
							
							super.onSuccess(obj);
						}

						@Override
						public void onFail(String message) {
							handler.sendEmptyMessage(ERROR);
							super.onFail(message);
						}
					});
					
				}
				
			}
		});
		
		
	}
	/**
	 * 提交
	 */
	public void doResetPassword(View v) {
		progress.show();
		String userCodeEt=Util.getEditValue(userCode);
		if("".equalsIgnoreCase(phoneNumEt)||"".equalsIgnoreCase(userCodeEt)){
			handler.sendEmptyMessage(ET_IS_NULL);
		}else{
			new BikeSiteUserService().resetPassword(phoneNumEt,userCodeEt,new AsyncOperator(){

				@Override
				public void onSuccess(Object obj) {
					String  result = (String) obj;
					if(result.equals("out of date")){
						handler.sendEmptyMessage(OUT_OF_DATE);
						
						
					}
					else if(result.equals("true")){
						handler.sendEmptyMessage(GET_SUCCESS);
					}
					else if(result.equals("false")){
						handler.sendEmptyMessage(GET_ERROR);
						
					}
					
					super.onSuccess(obj);
				}

				@Override
				public void onFail(String message) {
					handler.sendEmptyMessage(ERROR);
					super.onFail(message);
				}
			});
			
		}

	}
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

	/**
	 * 消息处理
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OUT_OF_TIMES:
				Util.t("获取验证码次数超过三次");
				progress.dismiss();
				break;
			case GET_SUCCESS:
				Util.t("获取成功,请查看短信");
				progress.dismiss();
				break;
			case GET_ERROR:
				Util.t("获取失败");
				progress.dismiss();
				break;
			case PHONENUM_IS_NULL :
				Util.t("请输入手机号");
				progress.dismiss();
			break;
			case ET_IS_NULL :
				Util.t("手机号或验证码为空");
				progress.dismiss();
			break;
			case OUT_OF_DATE :
				Util.t("验证码无效");
				progress.dismiss();
				break;
			case PHONE_NOMOBILE :
				Util.t("请输入正确的手机号");
				phoneNum.setText("");
				progress.dismiss();
				break;
			case ERROR :
				progress.dismiss();
				break;
			}
		}
	};


}

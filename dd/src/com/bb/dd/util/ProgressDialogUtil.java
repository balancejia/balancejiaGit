package com.bb.dd.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {
public static ProgressDialog progressDialog;
	public static void showProgressDialog(Context context,String info) {
		if(null==progressDialog){
			progressDialog=ProgressDialog.show(context,"请稍候" , info, true);
			progressDialog.closeOptionsMenu();
		}else{
			progressDialog.show();
		}
	}
	public static void closedProgressDialog(){
		if(null!=progressDialog)progressDialog.dismiss();
	}	
		
}

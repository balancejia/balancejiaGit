package com.bb.dd.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class AbstractFragment implements FragmentView{


protected Context _context;
	
	private ProgressDialog progressDialog;
	
	protected View rootView;
	
	public AbstractFragment(Context context){
		_context = context;
	}
	protected View inflat(int resouceId) {
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(resouceId, null);
	}
	protected void showProgressDialog() {
		if(null==progressDialog){
			progressDialog=ProgressDialog.show(_context, "请稍候", "正在加载...", true);
			progressDialog.closeOptionsMenu();
		}else{
			progressDialog.show();
		}
	}
	protected void closedProgressDialog(){
		if(null!=progressDialog)progressDialog.dismiss();
	}

}

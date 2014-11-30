package com.bb.dd.impl;

import android.util.Log;

import com.bb.dd.inter.AsyncOperatorListener;
import com.bb.dd.util.Util;

public class AsyncOperator implements AsyncOperatorListener{
	private static final String TAG = AsyncOperator.class.getName();
	@Override
	public void onSuccess(Object obj) {
		
	}

	@Override
	public void onFail(String message) {
		Log.d(TAG, message);
		Util.t(message);
	}

	@Override
	public void onProgressUpdate(int progress) {
		
	}
}

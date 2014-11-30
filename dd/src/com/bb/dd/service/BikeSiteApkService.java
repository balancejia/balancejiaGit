package com.bb.dd.service;

import android.os.AsyncTask;

import com.bb.dd.inter.AsyncOperatorListener;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.WebHelper;

public class BikeSiteApkService {
	/**
	 * 接收APKupdate_JSON信息
	 * 
	 * @return
	 */
	public void loadUpdateJson(AsyncOperatorListener callback) {
		new AsyBikeLoadUpdateJson(callback).execute();
	}

	/**
	 * 接收OFFLINE_MAP信息
	 * 百度离线地图
	 * @return
	 */
	public void loadOfflineMapJson(AsyncOperatorListener callback) {
		new AsyBikeLoadOfflineMapJson(callback).execute();
	}

	private class AsyBikeLoadOfflineMapJson extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		String result;
		String message;

		AsyBikeLoadOfflineMapJson(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				WebHelper helper = new WebHelper();
				result = helper.getResult(CommonVariable.OFFLINE_JSON, null);
				return true;
			} catch (Exception e) {
				message = "网络环境太差,获取数据失败";
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			if (flag) {
				listener.onSuccess(result);
			} else {
				listener.onFail(message);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}

	private class AsyBikeLoadUpdateJson extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		String result;
		String message;

		AsyBikeLoadUpdateJson(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			WebHelper helper = new WebHelper();
			try {
				result = helper.getResult(CommonVariable.UPDATE_JSON, null);
				return true;
			} catch (Exception e) {
				message = "网络环境太差,获取数据失败";
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			if (flag) {
				listener.onSuccess(result);
			} else {
				listener.onFail(message);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}
}

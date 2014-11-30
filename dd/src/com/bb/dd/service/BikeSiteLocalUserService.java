package com.bb.dd.service;

import java.util.List;

import android.os.AsyncTask;

import com.bb.dd.dao.DaoFactory;
import com.bb.dd.inter.AsyncOperatorListener;
import com.bb.dd.modle.UserFocusBikesite;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

public class BikeSiteLocalUserService {

	/**
	 * 用户关注取消接口
	 * 取消用户关注站点信息
	 * userRegistId	用户注册id
	 * bikesiteId	站点ID
	 * @return
	 */
	public Boolean delUserFocus(String userRegistId,String bikesiteId){
//		boolean boolean1;
//		BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
//		try {
//			boolean1 = bikeSiteUser.delUserFocus(userRegistId, bikesiteId);
//		} finally {
//			client.closeProxy(bikeSiteUser);
//		}
		return true;
	}
	/**
	 * 用户关注接口
	 * 提交用户关注站点信息
	 * userRegistId	用户注册id
	 * bikesiteId	站点ID
	 * @return
	 */
	public void saveLocalUserFocus(String userRegistId,String bikesiteId, AsyncOperatorListener callback){
		new AsySaveLocalUserFocus(callback).execute(userRegistId,bikesiteId);
	}
	/**
	 * 用户关注信息同步
	 * 用户重新安装后同步用户关注信息
	 * parameter	手机号码
	 * @return
	 */
	public void loadLocalUserFocus(String userRegistId, AsyncOperatorListener callback){
		new AsySaveLoadLocalUserFocus(callback).execute(userRegistId);
	}
	
	private class AsySaveLoadLocalUserFocus extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		List<UserFocusBikesite> result;
		String message;

		AsySaveLoadLocalUserFocus(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Dao<UserFocusBikesite, Integer> userAttentionBikesiteDao = DaoFactory.instant().getUserFocusBikesiteDao();
			try {
				result = userAttentionBikesiteDao.queryBuilder().where().eq("apn_user_id", params[0]).query();
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "本地存储异常";
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
	}
	
	private class AsySaveLocalUserFocus extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		CreateOrUpdateStatus result;
		String message;

		AsySaveLocalUserFocus(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Dao<UserFocusBikesite, Integer> userAttentionBikesiteDao = DaoFactory.instant().getUserFocusBikesiteDao();
			try {
				UserFocusBikesite focusBikesite = new UserFocusBikesite();
				focusBikesite.setApnUserId(params[0]);
				focusBikesite.setBikesiteId(params[1]);
				CreateOrUpdateStatus state = userAttentionBikesiteDao.createOrUpdate(focusBikesite);
				result = state;
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "本地存储异常";
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
	}
}

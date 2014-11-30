package com.bb.dd.service;

import java.util.ArrayList;
import java.util.List;

import mixedserver.protocol.jsonrpc.client.Client;
import android.os.AsyncTask;

import com.bb.dd.inter.AsyncOperatorListener;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.Cons;
import com.topdt.application.BikeSiteUser;
import com.topdt.application.entity.BikeSiteRealView;
import com.topdt.coal.entity.Opinion;
import com.topdt.coal.entity.User;
import com.topdt.coal.entity.UserAttentionBikesite;

public class BikeSiteUserService {
	private Client client;
	
	public BikeSiteUserService(){
		client =  Client.getClient(CommonVariable.REQUEST_RPCPATH);
		client.setDencryptMessage(true);
	}
	
	/**
	 * 用户关注列表
	 * 根据后台给站点地理位置的标签，返回站点id和名称
	 * parameter	地理位置关键字
	 * @param info
	 * @return
	 */
	public List<BikeSiteRealView> loadUserFocusList(String userRegistId){
		List<BikeSiteRealView> list = new ArrayList<BikeSiteRealView>();
		BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
		try {
			list = bikeSiteUser.loadUserFocusList(userRegistId,Cons.getUserInfor());
		} finally {
			client.closeProxy(bikeSiteUser);
		}
		return list;
	}
	/**
	 * 用户关注取消接口
	 * 取消用户关注站点信息
	 * userRegistId	用户注册id
	 * bikesiteId	站点ID
	 * @return
	 */
	public void delUserFocus(String userRegistId,String bikesiteId, AsyncOperatorListener callback){
		new AsyDelUserFocus(callback).execute(userRegistId,bikesiteId);
//		boolean boolean1;
//		BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
//		try {
//			boolean1 = bikeSiteUser.delUserFocus(userRegistId, bikesiteId);
//		} finally {
//			client.closeProxy(bikeSiteUser);
//		}
//		return boolean1;
	}
	/**
	 * 用户关注接口
	 * 提交用户关注站点信息
	 * userRegistId	用户注册id
	 * bikesiteId	站点ID
	 * @return
	 */
	public void saveUserFocus(String userRegistId,String bikesiteId, AsyncOperatorListener callback){
		new AsySaveUserFocus(callback).execute(userRegistId,bikesiteId);
	}
	/**
	 * 用户关注信息同步
	 * 用户重新安装后同步用户关注信息
	 * parameter	手机号码
	 * @return
	 */
	public void loadUserFocus(String userRegistId, AsyncOperatorListener callback){
		new AsyLoadUserFocus(callback).execute(userRegistId);
//		List<UserAttentionBikesite> list;
//		BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
//		try {
//			list = bikeSiteUser.loadUserFocus(userRegistId);
//		} finally {
//			client.closeProxy(bikeSiteUser);
//		}
//		return list;
	}
	/**
	 * 用户注册手机号查询
	 * 客户端根据用户注册手机号码，到服务器端判断手机号码是否已经注册
	 * parameter	手机号码
	 * @return
	 */
	public void loadUserSearch(String phoneNumber,AsyncOperatorListener callback){
		new AsyLoadUserSearch(callback).execute(phoneNumber);
	}
	/**
	 * 用户注册接口
	 * 服务器保存注册用户信息，返回注册是否成功
	 * parameter	手机号码
	 * @return
	 */
	public void saveUserInfor(User user,AsyncOperatorListener callback){
		new AsySaveUserInfor(callback).execute(user);
	}
	/**
	 * 用户修改密码
	 * @param user
	 * @param callback
	 */
	public void changePassword(User user,AsyncOperatorListener callback){
		new AsyChangePassword(callback).execute(user);
	}
	/**
	 * 用户获得短信验证码
	 * @param user
	 * @param callback
	 */
	public void getUserCode(String phoneNum,AsyncOperatorListener callback){
		new AsyGetUserCode(callback).execute(phoneNum);
	}
	/**
	 * 用户重置密码
	 * @param user
	 * @param callback
	 */
	public void resetPassword(String phoneNum,String usercode,AsyncOperatorListener callback){
		new AsyResetPassword(callback).execute(phoneNum,usercode);
	}
	/**
	 * 用户登录接口
	 * 客户端用户登录，验证用户手机号密码，返回用户注册信息
	 * parameter	手机号码
	 * @return
	 */
	public void loginUser(User user,AsyncOperatorListener callback){
		new AsyLoginUser(callback).execute(user);
	}
	/**
	 * 头像上传
	 * @param filename
	 * @param fileString
	 * @param user
	 * @return
	 */
	public boolean	uploadPortrait(String filename,String fileString,User user) {
		boolean boolean1;
		BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
		try {
			boolean1 = bikeSiteUser.uploadPortrait(filename,fileString,user,Cons.getUserInfor());
		} finally {
			client.closeProxy(bikeSiteUser);
		}
		return boolean1;
	}
	public String downloadPortrait(User user) {
		String fileString;
		BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
		try {
			fileString = bikeSiteUser.downloadPortrait(user,Cons.getUserInfor());
		} catch (Exception e) {
			return null;
		}finally {
			client.closeProxy(bikeSiteUser);
		}
		return fileString;
	}
	/**
	 * 意见反馈
	 */
	public void saveOpinion(Opinion opinion,AsyncOperatorListener callback){
		new AsySaveOpinion(callback).execute(opinion);
	}
	
	private class AsySaveUserFocus extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		boolean result;
		String message;

		AsySaveUserFocus(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.saveUserFocus(params[0], params[1],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	private class AsyLoginUser extends AsyncTask<User,Integer, Boolean> {
		AsyncOperatorListener listener;
		User result;
		String message;

		AsyLoginUser(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(User... user) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.loginUser(user[0],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	private class AsyLoadUserSearch extends AsyncTask<String,Integer, Boolean> {
		AsyncOperatorListener listener;
		User result;
		String message;

		AsyLoadUserSearch(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.loadUserSearch(params[0],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	private class AsySaveUserInfor extends AsyncTask<User,Integer, Boolean> {
		AsyncOperatorListener listener;
		boolean result;
		String message;

		AsySaveUserInfor(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(User... user) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.saveUserInfor(user[0],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	private class AsyChangePassword extends AsyncTask<User,Integer, Boolean> {
		AsyncOperatorListener listener;
		boolean result;
		String message;

		AsyChangePassword(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(User... user) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.changePassword(String.valueOf(user[0].getApnUserId()),user[0].getPassword(),Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	private class AsyGetUserCode extends AsyncTask<String,Integer, Boolean> {
		AsyncOperatorListener listener;
		String result;
		String message;

		AsyGetUserCode(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.sendMassage(params[0],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	private class AsyResetPassword extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		String result;
		String message;

		AsyResetPassword(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.submitCode(params[0],Integer.valueOf(params[1]),Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	private class AsySaveOpinion extends AsyncTask<Opinion,Integer, Boolean> {
		AsyncOperatorListener listener;
		boolean result;
		String message;

		AsySaveOpinion(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(Opinion... opinions) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.saveOpinion(opinions[0],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	
	
	private class AsyLoadUserFocus extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		List<UserAttentionBikesite> result;
		String message;

		AsyLoadUserFocus(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.loadUserFocus(params[0],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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
	
	private class AsyDelUserFocus extends AsyncTask<String, Integer, Boolean> {
		AsyncOperatorListener listener;
		Boolean result;
		String message;

		AsyDelUserFocus(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
			try {
				result = bikeSiteUser.delUserFocus(params[0],params[1],Cons.getUserInfor());
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(bikeSiteUser);
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

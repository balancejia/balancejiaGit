package com.bb.dd.service;

import mixedserver.protocol.jsonrpc.client.Client;
import android.os.AsyncTask;

import com.bb.dd.inter.AsyncOperatorListener;
import com.bb.dd.util.CommonVariable;
import com.topdt.application.LostAndFoundInter;
import com.topdt.application.entity.LostAndFoundView;



public class LostAndFoundService {

	private Client client;
	/**
	 * 失物招领
	 */
	public LostAndFoundService(){
		client =  Client.getClient(CommonVariable.REQUEST_RPCPATH);
		client.setDencryptMessage(true);
	}
	
	public void lostAndFound(LostAndFoundView lostAndfound,AsyncOperatorListener callback){
		new AsyLostAndFound(callback).execute(lostAndfound);
	}
	private class AsyLostAndFound extends AsyncTask<LostAndFoundView,Integer, Boolean> {
		AsyncOperatorListener listener;
		boolean result;
		String message;

		AsyLostAndFound(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(LostAndFoundView... lostAndfounds) {
			LostAndFoundInter lostAndFoundInter = (LostAndFoundInter) client.openProxy("lostAndFoundInter",LostAndFoundInter.class);
			try {
				result = lostAndFoundInter.saveLostAndFound(lostAndfounds[0]);
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "网络环境太差,获取数据失败";
				return false;
			} finally {
				client.closeProxy(lostAndFoundInter);
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

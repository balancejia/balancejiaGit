package com.bb.dd.service;

import mixedserver.protocol.jsonrpc.client.Client;
import android.os.AsyncTask;

import com.bb.dd.inter.AsyncOperatorListener;
import com.bb.dd.util.CommonVariable;
import com.topdt.application.WeatherInter;
import com.topdt.coal.entity.Weather;

public class WeatherTodayService {
	private Client client;
	
	public WeatherTodayService(){
		client =  Client.getClient(CommonVariable.REQUEST_RPCPATH);
		client.setDencryptMessage(true);
	}
	
	
	public void getWeather(AsyncOperatorListener callback){
		new AsyGetWeather(callback).execute();
	}

	private class AsyGetWeather extends AsyncTask<Weather,Integer, Boolean> {
		AsyncOperatorListener listener;
		Weather result;
		String message;

		AsyGetWeather(AsyncOperatorListener callback) {
			this.listener = callback;
		}

		@Override
		protected Boolean doInBackground(Weather... weathers) {
			WeatherInter weatherInter = (WeatherInter) client.openProxy("weatherInter",WeatherInter.class);
			try {
				result = weatherInter.getTodayWeather();
				return true;
			} catch (Exception e) { // 改为rpc异常
				message = "获取实时天气数据异常";
				return false;
			} finally {
				client.closeProxy(weatherInter);
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

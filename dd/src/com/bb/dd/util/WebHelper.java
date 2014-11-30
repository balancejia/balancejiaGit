package com.bb.dd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.alibaba.fastjson.JSON;

/**
 * @author HuangF
 * @version 创建时间：2012-11-30 上午11:15:46 类说明
 */
public class WebHelper<T> {

	private static final int REQUEST_TIME = 20*1000;
	private boolean net = true;
	private Class<T> mt;

	public WebHelper() {

	}

	public WebHelper(Class<T> t) {
		mt = t;
	}
	private String requestHttp(String urlStr){
		String result="";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIME);
		HttpGet get = new HttpGet(urlStr);
		try {
			 HttpResponse response = httpClient.execute(get);
			 if (response.getStatusLine().getStatusCode() == 200) {
				 result = EntityUtils.toString(response.getEntity());
			 }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// Post请求Http
	private String requestPostHttp(String urlStr, List<NameValuePair> parameters) {
		String result = "";
		if (net) {
			HttpClient httpClient = null;
			try {
				httpClient = new DefaultHttpClient();
				httpClient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
				HttpPost post = new HttpPost(urlStr);
				if (null != parameters)
					post.setEntity(new UrlEncodedFormEntity(parameters,
							HTTP.UTF_8));
				HttpResponse response = httpClient.execute(post);
				if (response.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(response.getEntity());
				}
			} catch (SocketException e) {
				return null;
			} catch (MalformedURLException e) {
				return null;
			} catch (IOException e) {
				return null;
			} finally {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return result;

	}

	public int requestPostHttpCode(String urlStr, List<NameValuePair> parameters) {
		if (net) {
			int result = 0;
			HttpClient httpClient = null;
			BufferedReader br = null;
			try {
				httpClient = new DefaultHttpClient();
				httpClient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
				HttpPost post = new HttpPost(urlStr);
				if (null != parameters)
					post.setEntity(new UrlEncodedFormEntity(parameters,
							HTTP.UTF_8));
				HttpResponse response = null;
				response = httpClient.execute(post);
				int statusCode = response.getStatusLine().getStatusCode();
				result = statusCode;

				InputStream in = response.getEntity().getContent();
				br = new BufferedReader(new InputStreamReader(in));
			} catch (SocketException e) {
				return result;
			} catch (MalformedURLException e) {
				return result;
			} catch (IOException e) {
				return result;
			} finally {
				httpClient.getConnectionManager().shutdown();
				try {
					if (br != null) {
						br.close();
						br = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		return 0;

	}

	/**
	 * 
	 * @author HuangF
	 * @version 创建时间：2012-12-2 下午4:02:50 方法说明:
	 * @param url
	 *            请求地址
	 * @param parameters
	 *            请求参数 为null 使用Get请求
	 * @return
	 */
	public String getResult(String url, List<NameValuePair> parameters) {
		String result = "";
		if (null == parameters) {
			result = requestHttp(url);
		} else {
			result = requestPostHttp(url, parameters);
		}
		result =EncrpytionTool.dencryptFromBase64_3DES(result);
		return result;
	}
	public String getResults(String url, List<NameValuePair> parameters) {
		String result = "";
		if (null == parameters) {
			result = requestHttp(url);
		} else {
			result = requestPostHttp(url, parameters);
		}
		return result;
	}

	/**
	 * 
	 * @author HuangF
	 * @version 创建时间：2012-10-23 下午3:44:19 方法说明:get 把Json转成想要的字符
	 * @param url
	 * @param addTag
	 *            分割的标识 如:["a","b"] ->a,b
	 * @return
	 */
	public String getJson2String(String url, String addTag) {
		StringBuilder sb = new StringBuilder();
		try {
			JSONArray jsArray = new JSONArray(getResult(url, null));
			for (int i = 0, size = jsArray.length(); i < size; i++) {
				if (i != 0)
					sb.append(addTag);
				sb.append(jsArray.getString(i));
			}
		} catch (JSONException json) {
			return null;
		}
		return sb.toString();
	}

	public List<T> getArray(String url, List<NameValuePair> parameters) {
		String result = getResult(url, parameters);
		if (result != null){
			try{
				return JSON.parseArray(result,mt);
			}catch(Exception e){
				return new ArrayList<T>();
			}
			
		}
		return new ArrayList<T>();
	}
	public T getObject(String url, List<NameValuePair> parameters) {
		String result = getResult(url, parameters);
		if (result != null&&!result.equals("")){
			Util.l(result);
			return JSON.parseObject(result,mt);
		}
		return null;
	}

	/**
	 * @author HuangF
	 * @version 创建时间：2013-1-16 上午10:49:24 方法说明: 提交数据
	 */

	public boolean submit(String urlStr,
			List<? extends NameValuePair> parameters) {
		if (net) {
			DefaultHttpClient submitClient = new DefaultHttpClient();
			submitClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			HttpPost post = new HttpPost(urlStr);
			if (null != parameters)
				try {
					post.setEntity(new UrlEncodedFormEntity(parameters,
							HTTP.UTF_8));
					HttpResponse response = submitClient.execute(post);
					if (response.getStatusLine().getStatusCode() == 200) {
						return true;
					}
				} catch (UnsupportedEncodingException e) {
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				}
		}
		return false;

	}

}
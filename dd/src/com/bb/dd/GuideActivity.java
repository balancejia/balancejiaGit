package com.bb.dd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.bb.dd.impl.AsyncOperator;
import com.bb.dd.modle.UploadApk;
import com.bb.dd.service.BikeSiteApkService;
import com.bb.dd.service.BikeSiteInforService;
import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Base64Coder;
import com.bb.dd.util.CommonVariable;
import com.bb.dd.util.Cons;
import com.bb.dd.util.Dev_MountInfo;
import com.bb.dd.util.Dev_MountInfo.DevInfo;
import com.bb.dd.util.FileUtil;
import com.bb.dd.util.PreferencesUtil;
import com.bb.dd.util.Util;
import com.bb.dd.util.WebCons;
import com.bb.dd.util.WebHelper;
import com.topdt.application.entity.BikeSitesView;
import com.topdt.coal.entity.User;

public class GuideActivity extends Activity {
	private static final String TAG = GuideActivity.class.getName();
	private ProgressBar guide_progress;
	private Context context;

	private BikeSiteInforService bikeSiteInforService = new BikeSiteInforService();
	private BikeSiteApkService bikeSiteApkService = new BikeSiteApkService();

	protected static final int LOAD_NAVITE_VERSION = 1;
	// protected static final int ERROR4UPDATE = 2;
	protected static final int HAVE_NEW_VERSION = 3;
	// protected static final int HAVE_NO_VERSION = 4;
	protected static final int LOAD_BIKESITE = 5;
	protected static final int LOAD_APK_FINISH = 6;
	protected static final int HAVE_NEW_OFFLINEMAP = 7;
	protected static final int LOGIN_AUTO_SUCCESS = 8;
	protected static final int LOGIN_AUTO_ERROR = 9;
	private UploadApk newSuj;
	private UploadApk locSuj;
	private UploadApk newOfflineMap;
	private UploadApk locOfflineMap;
	private ProgressBar jindutiao;
	private MyHandler handler = new MyHandler();
	private AlertDialog dialog; // 根新提示信息
	private AlertDialog builder; // 更新下载提示
	private DownSoft downSoft; // APK下载
	private DownOfflineMap downOfflineMap; // 离线地图下载
	private TextView guide_tv;

	private MapView mMapView = null;
	private MKOfflineMap mOffline = null;
	private MapController mMapController = null;
	private MyMKOfflineMapListener myMKOfflineMapListener = new MyMKOfflineMapListener();
	private int widthPixels, heightPixels;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.guide);
		context = GuideActivity.this;
		//initViewPager();
		if (PreferencesUtil.getValue(PreferencesUtil.INIT_DB) == -1) {
			//initViewPager();
		}
		initView();
		getLoadEntity();// 初始化Apk对象
	}

	
	private void initView() {
		guide_progress = (ProgressBar) findViewById(R.id.guide_progress);
		guide_tv = (TextView) findViewById(R.id.guide_tv);

		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPixels = dm.widthPixels;
		heightPixels = dm.heightPixels;
	}
	/**
	 * 跳到引导
	 */
	private void goViewPager() {
			Intent intent = new Intent(context, ViewPagerActivity.class);
			startActivity(intent);
			finish();
	}
	/**
	 * 跳到首页
	 */
	private void goIndex() {
		if (PreferencesUtil.getValue(PreferencesUtil.AUTO_LOGIN) == 1) {
			autoLogin();
		} else {
			Intent intent_index = new Intent(context, IndexMainActivity.class);
			startActivity(intent_index);
			finish();
		}
	}

	/**
	 * 下载百度地图
	 */
	private void loadBaiDuMap() {
		guide_tv.setText("正在检查离线地图版本");
		bikeSiteApkService.loadOfflineMapJson(new AsyncOperator() {
			@Override
			public void onSuccess(Object obj) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String verjson = (String) obj;
				newOfflineMap = new UploadApk();
				boolean flag = false;
				try {
					if (null != verjson) {
						JSONArray array = new JSONArray(verjson);
						JSONObject jsonobj = array.getJSONObject(0);
						newOfflineMap.verCode = Integer.parseInt(jsonobj
								.getString("verCode"));
						newOfflineMap.verName = jsonobj.getString("verName");
						newOfflineMap.apkUrl = jsonobj.getString("apkUrl");
						newOfflineMap.apkname = jsonobj.getString("apkname");
						newOfflineMap.verContent = jsonobj
								.getString("verContent");
						newOfflineMap.updateTime = jsonobj
								.getString("updateTime");

						try {
							Date locDate = df.parse(locOfflineMap.updateTime);
							Date newDate = df.parse(newOfflineMap.updateTime);
							if (newDate.after(locDate)) {
								flag = true;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (flag) {
					handler.sendEmptyMessage(HAVE_NEW_OFFLINEMAP); // 请求版本更新
				} else {
					handler.sendEmptyMessage(LOAD_BIKESITE); // 请求百度离线地图
				}
			}

			@Override
			public void onFail(String message) {
				// Util.t("网络连接失败,请检查网络连接...");
				guide_tv.setText("网络连接失败,请检查网络连接...");
				handler.sendEmptyMessage(LOAD_BIKESITE); // 请求百度离线地图
			}
		});
	}

	private void loadBaiDuMap_Offline() {
		guide_tv.setText("正在检查离线地图版本");

		mMapView = new MapView(this);
		mMapController = mMapView.getController();
		mOffline = new MKOfflineMap();
		mOffline.init(mMapController, myMKOfflineMapListener);

		boolean flag = false;
		int tyCityId = 176;
		ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
		if (localMapList != null) {
			for (MKOLUpdateElement mkolUpdateElement : localMapList) {
				int cityid = mkolUpdateElement.cityID;
				boolean cityupdate = mkolUpdateElement.update;

				if (cityid == tyCityId) {// 安装过
					flag = true;
					if (cityupdate)
						mOffline.start(cityid); // 可更新
				}
			}
		}

		if (!flag) {
			// int num = mOffline.scan(); //离线地图导入离线包
			mOffline.start(tyCityId);
		}
		// ArrayList<MKOLSearchRecord> records = mOffline.searchCity("太原");
		// if (records == null || records.size() != 1) {
		//
		// }else{
		// int cityid = records.get(0).cityID;
		//
		// //int num = mOffline.scan(); //离线地图导入离线包
		// }
		// int num = mOffline.scan(); //离线地图导入离线包
	}

	private void downOfflineMap() {
		downOfflineMap = new DownOfflineMap();
		downOfflineMap.execute();
	}

	/**
	 * 远程加载站点信息，写入本地数据库表
	 */
	private void loadBikeSite() {
		int init = PreferencesUtil.getValue(PreferencesUtil.INIT_DB);// 判断第几次运行程序.第一次全部加载.第二次加载

		guide_progress.setProgress(10);
		if (init == -1) {
			guide_tv.setText("正在加载站点数据");
			PreferencesUtil.init();
			PreferencesUtil.initIndexMain();
			guide_progress.setProgress(20);
			bikeSiteInforService.loadBikeSites("all", new AsyncOperator() {
				@Override
				public void onSuccess(Object bikesitesview) {
					BikeSitesView view = (BikeSitesView) bikesitesview;
					if (view.getBikeSites() != null
							&& view.getBikeSites().size() > 0) {
						// 保存数据。用来判断第几次加载程序
						PreferencesUtil.add(PreferencesUtil.INIT_DB, 1);
						// 保存最后更新时间
						PreferencesUtil.setStr(
								PreferencesUtil.LAST_UPDATE_TIME,
								view.getUpdateDate());// Util.getNow()
					}
					guide_progress.setProgress(100);
					updateSynchroCount(0);
					goViewPager();
				}

				@Override
				public void onFail(String message) {
					guide_tv.setText("网络连接失败,请检查网络连接...");
					// guide_progress.setProgress(80);
					// goIndex();
					// super.onFail(message);
				}

				@Override
				public void onProgressUpdate(int progress) {
					guide_progress.setProgress(progress);
				}
			});

		} else {
			PreferencesUtil.initIndexMain();
			guide_tv.setText("正在检查站点数据更新");
			guide_progress.setProgress(20);
			try {
				String updateTime = PreferencesUtil
						.getStr(PreferencesUtil.LAST_UPDATE_TIME);
				bikeSiteInforService.loadBikeSitesUpdateCount(updateTime,
						new AsyncOperator() {
							@Override
							public void onSuccess(Object obj) {
								String countStr = (String) obj;
								int count = 0;
								if (null != countStr && !"".equals(countStr)) {
									count = Integer.parseInt(countStr.trim());
								} else {
									count = 0;
								}
								updateSynchroCount(count);
								guide_progress.setProgress(80);
								goIndex();
							}

							@Override
							public void onFail(String message) {
								guide_tv.setText("网络连接失败,请检查网络连接...");
								guide_progress.setProgress(80);
								goIndex();
								super.onFail(message);
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updateSynchroCount(int num) {
		BikeApplication bikeApp = (BikeApplication) getApplication();
		bikeApp.setSynchroCount(num);
	}

	private void autoLogin() {
//		User user = new User();
//		user.setPhoneNumber(PreferencesUtil.getStr(PreferencesUtil.USER_PHONE));
//		user.setPassword(PreferencesUtil.getStr(PreferencesUtil.USER_PWD));
//		new BikeSiteUserService().loginUser(user, new AsyncOperator() {
//
//			@Override
//			public void onSuccess(Object obj) {
//				User userLogin = (User) obj;
//				if (userLogin != null) {
//					Cons.LOGIN_FLAG = 1;
//					Util.l("用户自动登录成功");
//				} else {
//					Util.l("用户自动登录失败");
//				}
//				Intent intent_index = new Intent(context,
//						IndexMainActivity.class);
//				startActivity(intent_index);
//				finish();
//				super.onSuccess(obj);
//			}
//
//			@Override
//			public void onFail(String message) {
//				super.onFail(message);
//			}
//		});
		final String phoneNumEt=PreferencesUtil.getStr(PreferencesUtil.USER_PHONE);
		final String passwordEt=PreferencesUtil.getStr(PreferencesUtil.USER_PWD);
		new Thread() {
			@Override
			public void run() {
					String url = WebCons.REQUEST_LOGIN_URL + phoneNumEt + "&pswd="
							+ passwordEt;
					WebHelper help = new WebHelper();
					String result = help.getResults(url, null);
					try {
						JSONObject json = new JSONObject(result);
						int num = Integer.parseInt(json.get("code").toString());
						Util.l("-----"+num);
						switch (num) {
						case 0:
							Long id=Long.valueOf(json.get("id").toString());
							int sex=Integer.parseInt(json.get("sex").toString());
							int age=Integer.parseInt(json.get("age").toString());
							String photo=json.get("photo").toString();
							String name=json.get("name").toString();
							User userLogin=new User();
							userLogin.setPhoneNumber(phoneNumEt);
							userLogin.setApnUserId(id);
							userLogin.setPortrait(photo);
							userLogin.setSex(sex);
							userLogin.setAge(age);
							userLogin.setUsername(name);
							if (!userLogin.getPhoneNumber().equals(
									PreferencesUtil
											.getStr(PreferencesUtil.USER_PHONE))) {
								Cons.LOGIN_FLAG = 1;
								if (userLogin.getPortrait() != null
										&& userLogin.getPortrait().trim() != "") {
									String imgPhoto = new BikeSiteUserService()
											.downloadPortrait(userLogin);
									if (imgPhoto != null) {
										String protrait = userLogin.getPortrait();
										int start = protrait.lastIndexOf("/");
										int end = protrait.indexOf(".");
										String imageName = protrait.substring(
												start + 1, end);
										PreferencesUtil.setStr(
												PreferencesUtil.USER_PHOTO,
												imageName);
										Util.l(imageName);
										FileOutputStream out;
										try {
											String path = Environment
													.getExternalStorageDirectory()
													.getAbsolutePath()
													+ "/_TAIYUAN_PBIKE/userPhoto/";
											File dir = new File(path);
											if (!dir.exists()) {
												dir.mkdirs();
											}
											out = new FileOutputStream(Environment
													.getExternalStorageDirectory()
													.getAbsolutePath()
													+ "/_TAIYUAN_PBIKE/userPhoto/"
													+ imageName);
											byte[] bytes = Base64Coder
													.decodeLines(imgPhoto);
											out.write(bytes);
											out.flush();
											out.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else {
										PreferencesUtil.setStr(
												PreferencesUtil.USER_PHOTO, "");
									}

								} else {
									PreferencesUtil.setStr(
											PreferencesUtil.USER_PHOTO, "");
								}
								PreferencesUtil.setLong(PreferencesUtil.USER_ID,
										userLogin.getApnUserId());
								PreferencesUtil.setStr(PreferencesUtil.USER_NAME,
										userLogin.getUsername());
								PreferencesUtil.setStr(PreferencesUtil.USER_PHONE,
										userLogin.getPhoneNumber());
								PreferencesUtil.setValue(PreferencesUtil.USER_SEX,
										userLogin.getSex());
								PreferencesUtil.setValue(PreferencesUtil.USER_AGE,
										userLogin.getAge());
							} else {
								Cons.LOGIN_FLAG = 1;
							}
							handler.sendEmptyMessage(LOGIN_AUTO_SUCCESS);
							break;
						case 9:
							handler.sendEmptyMessage(LOGIN_AUTO_ERROR);
							break;
						case -1:
							handler.sendEmptyMessage(LOGIN_AUTO_ERROR);
							break;
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(LOGIN_AUTO_ERROR);
						e.printStackTrace();
					}
			}
		}.start();
	}

	public class DownSoft extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			if ("".equals(newSuj.apkUrl))
				return null;
			HttpGet get = new HttpGet(newSuj.apkUrl);
			HttpResponse response;
			try {
				response = client.execute(get);
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				FileOutputStream fileOutputStream = null;
				if (is != null) {
					File file = new File(
							Environment.getExternalStorageDirectory(),
							newSuj.apkname);
					if (file.exists()) {
						file.deleteOnExit();
					}
					fileOutputStream = new FileOutputStream(file);

					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
						count += ch;

						if (length > 0) {
							int d = (int) (count * 100 / length);
							onProgressUpdate(d);
						}

					}

				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), newSuj.apkname)),
					"application/vnd.android.package-archive");
			startActivity(intent);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			jindutiao.setProgress(values[0]);
		}

	}

	/**
	 * 下载离线地图
	 * 
	 * @author Administrator
	 */
	public class DownOfflineMap extends AsyncTask<Void, Integer, Void> {
		private String getBaiDuMapSDPath() {
			String str = CommonVariable.BaiDuMap_HVSDPath;
			// 低分屏包 480*800以下
			// 高分屏包480*800及以上机型
			if (widthPixels < 480 && heightPixels < 800) {
				str = CommonVariable.BaiDuMap_LVSDPath;
			}
			return str;
		}

		@Override
		protected void onPreExecute() {
			guide_tv.setText("正在加载离线地图");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String apkUrl = newOfflineMap.apkUrl;
			int startnum = apkUrl.lastIndexOf("/");
			String filepath = apkUrl.substring(0, startnum);
			String filename = apkUrl.substring(startnum + 1, apkUrl.length());

			String baiDuMapSDPath = getBaiDuMapSDPath();
			String newApkUrl = filepath + "/" + baiDuMapSDPath + filename;

			HttpClient client = new DefaultHttpClient();
			if ("".equals(newOfflineMap.apkUrl))
				return null;
			HttpGet get = new HttpGet(newApkUrl);
			HttpResponse response;
			try {
				response = client.execute(get);
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				FileOutputStream fileOutputStream = null;
				if (is != null) {
					String src = "";
					if (Util.haveSd())
						src = Util.getSdRootFile().getPath();
					else
						src = "/"; // Util.getDownloadCacheDirectory().getPath()

					File file = new File(src, filename);
					if (file.exists()) {
						file.deleteOnExit();
					}
					fileOutputStream = new FileOutputStream(file);

					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
						count += ch;

						if (length > 0) {
							int d = (int) (count * 100 / length);
							onProgressUpdate(d);
						}

					}
				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) {
			// CommonVariable.BaiDu_MapLocalPath+ CommonVariable.BaiDu_MapName;
			String apkUrl = newOfflineMap.apkUrl;
			int startnum = apkUrl.lastIndexOf("/");
			String filename = apkUrl.substring(startnum + 1, apkUrl.length());

			String baiDuMapSDPath = getBaiDuMapSDPath();

			String src = "";
			if (Util.haveSd())
				src = Util.getSdRootFile().getPath() + "/" + filename;
			else
				src = "/" + filename; // Util.getDownloadCacheDirectory().getPath()
										// + "/" + filename;

			String dest = "";
			if (Util.haveSd())
				dest = Util.getSdRootFile().getPath() + "/" + baiDuMapSDPath;
			else
				dest = "/" + baiDuMapSDPath; // Util.getDownloadCacheDirectory().getPath()
												// + "/" +
												// CommonVariable.BaiDu_MapSDPath;

			try {
				FileUtil.createDir(dest);
				if (FileUtil.isValidateFile(dest + filename)) {
					FileUtil.delFile(dest, filename);
				}
				FileUtil.copyFile(src, dest, filename);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Dev_MountInfo dev = Dev_MountInfo.getInstance();
				DevInfo info = dev.getExternalInfo();// External SD Card
														// Informations
				// DevInfo info2 = dev.getInternalInfo();//Internal SD Card
				// Informations

				if (info != null && info.getPath().length() > 0) {// 是否存在外部存储卡
					dest = info.getPath() + "/" + baiDuMapSDPath;
					FileUtil.createDir(dest);
					if (FileUtil.isValidateFile(dest + filename)) {
						FileUtil.delFile(dest, filename);
					}
					FileUtil.copyFile(src, dest, filename);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			PreferencesUtil.setStr(PreferencesUtil.OFFLINE_MAP,
					newOfflineMap.updateTime);
			handler.sendEmptyMessage(LOAD_BIKESITE);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			guide_progress.setProgress(values[0]);
		}

	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_NAVITE_VERSION:
				loadUpdateJson();
				break;
			case LOAD_APK_FINISH:
				loadBaiDuMap();
				// loadBaiDuMap_Offline();
				break;
			// case ERROR4UPDATE:
			// Util.t("网络连接失败,请检查网络连接...");
			// loadBaiDuMap();
			// break;
			case HAVE_NEW_VERSION:
				showUpdataDialog();
				break;
			case HAVE_NEW_OFFLINEMAP:
				downOfflineMap();
				break;
			// case HAVE_NO_VERSION:
			// loadBaiDuMap();
			// break;
			case LOAD_BIKESITE:
				loadBikeSite();
				break;
			case LOGIN_AUTO_SUCCESS:
				Util.l("自动登录成功");
				Intent intent_index = new Intent(context,
						IndexMainActivity.class);
				startActivity(intent_index);
				finish();
				break;

			case LOGIN_AUTO_ERROR:
				Util.l("自动登录失败");
				Intent intent = new Intent(context,
						IndexMainActivity.class);
				startActivity(intent);
				finish();
				break;

			}
		}
	};

	/*
	 * 
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog() {
		// AlertDialog.Builder builer = new Builder(this);
		// builer.setTitle("版本升级");
		// builer.setMessage("发现最新版本(" + newSuj.verName + ")\n"
		// + newSuj.verContent + "\n是否升级最新版?");
		// // 当点确定按钮时从服务器上下载 新的apk 然后安装
		// builer.setPositiveButton("确定", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int which) {
		// PreferencesUtil.addUpdate("update", "new");
		// showLoadDialog();
		// doUpdate();
		// }
		// });
		// builer.setOnCancelListener(new DialogInterface.OnCancelListener() {
		// public void onCancel(DialogInterface dialog) {
		// handler.sendEmptyMessage(LOAD_APK_FINISH);
		// }
		// });
		//
		// builer.setNegativeButton("取消", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int which) {
		// PreferencesUtil.addUpdate("update", "old");
		// dialog.dismiss();
		// handler.sendEmptyMessage(LOAD_APK_FINISH);
		// }
		// });
		// dialog = builer.create();
		// dialog.show();

		// 强制升级
		guide_tv.setText("发现最新版本,努力下载中。请稍后...");
		showLoadDialog();
		doUpdate();
	}

	// 初始化Apk对象
	private void getLoadEntity() {
		locSuj = new UploadApk();
		locSuj.verCode = getVerCode();
		locOfflineMap = new UploadApk();
		String updateTime = PreferencesUtil.getStr(PreferencesUtil.OFFLINE_MAP);
		locOfflineMap.updateTime = updateTime.equals("") ? "2010-09-01"
				: updateTime;
		handler.sendEmptyMessage(LOAD_NAVITE_VERSION);
	}

	// 获取本机版本
	public int getVerCode() {
		int verCode = -1;
		try {
			verCode = BikeApplication.APP_CONTEXT.getPackageManager()
					.getPackageInfo(CommonVariable.APP_NAME, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.getMessage();
		}
		return verCode;
	}

	public void loadUpdateJson() {
		guide_tv.setText("正在检查软件版本");
		bikeSiteApkService.loadUpdateJson(new AsyncOperator() {

			@Override
			public void onSuccess(Object obj) {
				String verjson = (String) obj;
				newSuj = new UploadApk();
				boolean flag = false;
				try {
					if (null != verjson) {
						JSONArray array = new JSONArray(verjson);
						JSONObject jsonobj = array.getJSONObject(0);
						newSuj.verCode = Integer.parseInt(jsonobj
								.getString("verCode"));
						newSuj.verName = jsonobj.getString("verName");
						newSuj.apkUrl = jsonobj.getString("apkUrl");
						newSuj.apkname = jsonobj.getString("apkname");
						newSuj.verContent = jsonobj.getString("verContent");

						// doCompare();
						if (locSuj.verCode < newSuj.verCode) {
							flag = true;
						}
					}
				} catch (JSONException e) {
					guide_tv.setText("正在检查软件版本");
					e.printStackTrace();
				}

				if (flag) {
					handler.sendEmptyMessage(HAVE_NEW_VERSION); // 请求版本更新
				} else {
					handler.sendEmptyMessage(LOAD_APK_FINISH); // 请求百度离线地图
				}
			}

			@Override
			public void onFail(String message) {
				// Util.t("网络连接失败,请检查网络连接...");
				guide_tv.setText("网络连接失败,请检查网络连接...");
				handler.sendEmptyMessage(LOAD_APK_FINISH); // 请求百度离线地图
			}
		});
	}

	/**
	 * 版本比较
	 */
	// private void doCompare() {
	// if (locSuj.verCode < newSuj.verCode) {
	// handler.sendEmptyMessage(HAVE_NEW_VERSION);
	// } else {
	// handler.sendEmptyMessage(HAVE_NO_VERSION);
	// }
	// }

	private void showLoadDialog() {
		builder = new AlertDialog.Builder(this).create();
		builder.show();
		Window win = builder.getWindow();
		win.setContentView(R.layout.update);
		TextView version_content = (TextView) win
				.findViewById(R.id.version_content);
		// version_content.setVisibility(View.GONE);
		version_content.setText("发现最新版本(" + newSuj.verName + ")\n"
				+ newSuj.verContent);

		jindutiao = (ProgressBar) win.findViewById(R.id.progressBar1);
		jindutiao.setVisibility(View.VISIBLE);
		TextView update_title = (TextView) win.findViewById(R.id.update_title);
		update_title.setText("正在下载...");
		TextView do_update = (TextView) win.findViewById(R.id.do_update);
		TextView do_cancle = (TextView) win.findViewById(R.id.b_cancel_text);
		do_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doCancle();
			}
		});
		do_update.setTextColor(Util.getColor(R.color.darkgrey));
		ImageView update_logo = (ImageView) win.findViewById(R.id.update_logo);
		update_logo.setImageResource(R.drawable.update_logo);
	}

	public void doUpdate() {
		downSoft = new DownSoft();
		downSoft.execute();
	}

	public void doCancle() {
		builder.dismiss();
		downSoft.cancel(true);

		guide_tv.setText("发现最新版本,请更新至最新版本...");
		// 强制更新
		// handler.sendEmptyMessage(LOAD_APK_FINISH);
	}

	private class MyMKOfflineMapListener implements MKOfflineMapListener {
		@Override
		public void onGetOfflineMapState(int type, int state) {
			switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				MKOLUpdateElement update = mOffline.getUpdateInfo(state);
				// 处理下载进度更新提示
				if (update != null) {
					guide_tv.setText(String.format("%s(%d%%)", "正在下载离线地图",
							update.ratio));
					guide_progress.setProgress(update.ratio);
				}
			}
				break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				// 有新离线地图安装
				Log.d("OfflineDemo",
						String.format("add offlinemap num:%d", state));
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				// 版本更新提示
				MKOLUpdateElement e = mOffline.getUpdateInfo(state);
				if (e.update) {

				}
				break;
			}
		}
	}
}

package com.bb.dd.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.bb.dd.BikeApplication;
import com.bb.dd.R;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Util;

public class ShareFragment extends AbstractFragment {
	private static final String TAG = ShareFragment.class.getName();
	private static final String FILE_NAME = "/logo.png";
	public static String TEST_IMAGE;
	protected Handler handler;
	private LinearLayout shareMsg, shareRenren, shareTenxun, shareSina;

	public ShareFragment(Context context) {
		super(context);
	}

	@Override
	public View getView() {
		BadHandler.getInstance().init(_context);
		rootView = inflat(R.layout.share);
		handler = new Handler();
		initImagePath();
		/**
		 * 初始化分享组件
		 */
		ShareSDK.initSDK(_context);
		initView();
		return rootView;
	}

	private void initView() {
		shareMsg = (LinearLayout) rootView.findViewById(R.id.shareMsg);
		shareRenren = (LinearLayout) rootView.findViewById(R.id.shareRenren);
		shareTenxun = (LinearLayout) rootView.findViewById(R.id.shareTenxun);
		shareSina = (LinearLayout) rootView.findViewById(R.id.shareSina);
		shareMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itMsg = new Intent(Intent.ACTION_VIEW);
				String SMSContent = _context.getString(R.string.shareContent);
				itMsg.putExtra("sms_body", SMSContent);
				itMsg.setType("vnd.android-dir/mms-sms");
				_context.startActivity(itMsg);
			}
		});
		shareRenren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.l("// 分享到人人网");
				// 分享到人人网
			}
		});
		shareTenxun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 分享到腾讯微博
				showShare(true, TencentWeibo.NAME);
			}
		});
		shareSina.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 分享到新浪微博
				showShare(true, SinaWeibo.NAME);
			}
		});
	}

	@Override
	public void closed() {

	}

	/**
	 * 点击分享按钮
	 * 
	 * @param v
	 */
	public void shareMsg(View v) {
		Intent itMsg = new Intent(Intent.ACTION_VIEW);
		String SMSContent = _context.getString(R.string.shareContent);
		itMsg.putExtra("sms_body", SMSContent);
		itMsg.setType("vnd.android-dir/mms-sms");
		_context.startActivity(itMsg);
	}

	public void shareRenren(View v) {
		Util.l("// 分享到人人网");
		// 分享到人人网
	}

	public void shareTenxun(View v) {
		// 分享到腾讯微博
		showShare(true, TencentWeibo.NAME);
	}

	public void shareSina(View v) {
		// 分享到新浪微博
		showShare(true, SinaWeibo.NAME);
	}

	private void showShare(boolean silent, String platform) {
		OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.logo,
				_context.getString(R.string.app_name));
		oks.setAddress("12345678901");
		oks.setTitle(_context.getString(R.string.share));
		oks.setTitleUrl("http://sharesdk.cn");
		oks.setText(_context.getString(R.string.shareContent));
		oks.setImagePath(TEST_IMAGE);
		oks.setUrl("http://www.sharesdk.cn");
		oks.setAppPath(TEST_IMAGE);
		oks.setComment(_context.getString(R.string.share));
		oks.setSite(_context.getString(R.string.app_name));
		oks.setSiteUrl("http://sharesdk.cn");
		oks.setVenueName("Southeast in China");
		oks.setVenueDescription("This is a beautiful place!");
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		oks.show(_context);
	}

	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + FILE_NAME;
			} else {
				TEST_IMAGE = BikeApplication.getInstance().getFilesDir()
						.getAbsolutePath()
						+ FILE_NAME;
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(
						_context.getResources(), R.drawable.logo);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {

		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	public void onCancel(Platform palt, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = palt;
		handler.sendMessage(msg);
	}

	public void onError(Platform palt, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = palt;
		handler.sendMessage(msg);
	}

	/** 处理操作结果 */
	public boolean handleMessage(Message msg) {
		Platform plat = (Platform) msg.obj;
		String text = actionToString(msg.arg2);
		switch (msg.arg1) {
		case 1: { // 成功
			text = plat.getName() + " completed at " + text;
		}
			break;
		case 2: { // 失败
			text = plat.getName() + " caught error at " + text;
		}
			break;
		case 3: { // 取消
			text = plat.getName() + " canceled at " + text;
		}
			break;
		}

		Util.t(text);
		return false;
	}

	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}
}

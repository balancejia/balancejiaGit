package com.bb.dd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bb.dd.service.BikeSiteUserService;
import com.bb.dd.util.BadHandler;
import com.bb.dd.util.Base64Coder;
import com.bb.dd.util.Cons;
import com.bb.dd.util.PreferencesUtil;
import com.bb.dd.util.Util;
import com.topdt.coal.entity.User;
/**
 *
 */
public class AddPhotoActivity extends Activity {
	private static final String TAG = AddPhotoActivity.class.getName();
	Button fromPhoto,cramera;
	String userPhotoName;
	String tp=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BadHandler.getInstance().init(getApplicationContext());
		setContentView(R.layout.add_photo_dialog);
		userPhotoName=Util.getNowDate();
		initView();
	}
	private void initView() {
		fromPhoto=(Button)findViewById(R.id.bt_from_photo);
		cramera=(Button)findViewById(R.id.bt_cramera);
		fromPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						"image/*");
				startActivityForResult(intent, 1);
			}
		});
		cramera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								""+userPhotoName+".jpg")));
				startActivityForResult(intent, 2);
				
			}
		});
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if(data != null){
				startPhotoZoom(data.getData());
			}else{
				Intent intent_main=new Intent();
				intent_main.setClass(AddPhotoActivity.this, IndexMainActivity.class);
				startActivity(intent_main);
				finish();
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/"+userPhotoName+".jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			if(data != null){
				setPicToView(data);
			}else{
				Intent intent_main=new Intent();
				intent_main.setClass(AddPhotoActivity.this, IndexMainActivity.class);
				startActivity(intent_main);
				finish();
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			Util.savePhotoToSDCard(Environment.getExternalStorageDirectory().getAbsolutePath()+"/_TAIYUAN_PBIKE/userPhoto/", userPhotoName,photo);
			PreferencesUtil.setStr(PreferencesUtil.USER_PHOTO, userPhotoName);
			/*ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();
			// 将图片流以字符串形式存储下来
			tp = new String(Base64Coder.encodeLines(b));*/
			
			//上传头像
			String imgFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/_TAIYUAN_PBIKE/userPhoto/"+userPhotoName;// 待处理的图片
			InputStream in = null;
			byte[] data = null;
			// 读取图片字节数组
			try {
				in = new FileInputStream(imgFile);
				data = new byte[in.available()];
				in.read(data);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 对字节数组Base64编码
			String fileString = Base64Coder.encodeLines(data);
			User user=Util.getLoginUser();
			boolean boolean1 = new BikeSiteUserService().uploadPortrait(userPhotoName,fileString,user);
			Util.l("--------------"+boolean1);
			Cons.drawablePhoto=drawable;
			Intent intent_main=new Intent();
			intent_main.setClass(AddPhotoActivity.this, IndexMainActivity.class);
			/*intent_main.putExtra("tag","addphoto");*/
			startActivity(intent_main);
			finish();
		}
	}
	public void doCancle(View v){
		finish();
	}
}

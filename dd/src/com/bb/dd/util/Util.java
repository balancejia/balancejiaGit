package com.bb.dd.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.bb.dd.BikeApplication;
import com.topdt.coal.entity.User;

public class Util {

	public static final String tag = "message";

	public static void l(Object o) {
		Log.i(tag, o + "");
	}

	public static void t(Object o) {
		Toast.makeText(BikeApplication.getApplication(), o + "",
				Toast.LENGTH_SHORT).show();
	}

	public static String getNow() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd");
		return format.format(System.currentTimeMillis());
	}

	public static String getNowDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(System.currentTimeMillis());
	}

	public static String getEditValue(EditText ed) {
		return ed.getText().toString().trim();
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[4-9])|(15[0-2,7-9])|(18[2-3,7-8])|(147))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isAgeNo(String age) {
		if (age.length() > 8) {
			return false;
		} else {
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(age);
			return m.matches();
		}

	}

	// 是否含有SD卡
	public static boolean haveSd() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static File getSdRootFile() {
		return Environment.getExternalStorageDirectory();
	}
	
	public static File getRootDirectory() {
		return Environment.getRootDirectory();
	}
	
	public static File getDownloadCacheDirectory() {
		return Environment.getDownloadCacheDirectory();
	}

	public static int getColor(int colorId) {
		return BikeApplication.APP_CONTEXT.getResources().getColor(colorId);
	}

	// 保存图片到SD卡
	public static void savePhotoToSDCard(String path, String photoName,

	Bitmap photoBitmap) {

		if (android.os.Environment.getExternalStorageState().equals(

		android.os.Environment.MEDIA_MOUNTED)) {

			File dir = new File(path);

			if (!dir.exists()) {

				dir.mkdirs();

			}
			Util.l("--------------" + path);
			File photoFile = new File(path, photoName); // 在指定路径下创建文件

			FileOutputStream fileOutputStream = null;

			try {

				fileOutputStream = new FileOutputStream(photoFile);

				if (photoBitmap != null) {

					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,

					fileOutputStream)) {

						fileOutputStream.flush();

					}

				}

			} catch (FileNotFoundException e) {

				photoFile.delete();

				e.printStackTrace();

			} catch (IOException e) {

				photoFile.delete();

				e.printStackTrace();

			} finally {

				try {

					fileOutputStream.close();

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

		}

	}
	/**
	 * 获取状态栏的高度
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

	// 得到当前用户
	public static User getLoginUser() {
		User user = new User();
		user.setApnUserId(PreferencesUtil.getLong(PreferencesUtil.USER_ID));
		user.setUsername(PreferencesUtil.getStr(PreferencesUtil.USER_NAME));
		user.setPhoneNumber(PreferencesUtil.getStr(PreferencesUtil.USER_PHONE));
		user.setPassword(PreferencesUtil.getStr(PreferencesUtil.USER_PWD));
		user.setSex(PreferencesUtil.getValue(PreferencesUtil.USER_SEX));
		user.setAge(PreferencesUtil.getValue(PreferencesUtil.USER_AGE));
		user.setPortrait("file/"
				+ PreferencesUtil.getStr(PreferencesUtil.USER_PHOTO));
		return user;
	}

	// 注销当前用户
	public static void resetUserData() {
		PreferencesUtil.setStr(PreferencesUtil.USER_NAME, "");
		PreferencesUtil.setStr(PreferencesUtil.USER_PHONE, "");
		PreferencesUtil.setStr(PreferencesUtil.USER_PHOTO, "");
		PreferencesUtil.setStr(PreferencesUtil.USER_PWD, "");
		PreferencesUtil.setValue(PreferencesUtil.USER_SEX, 0);
		PreferencesUtil.setValue(PreferencesUtil.USER_AGE, 0);
		PreferencesUtil.setLong(PreferencesUtil.USER_ID, 0l);
		PreferencesUtil.setValue(PreferencesUtil.REMEMBER_PASSWORD, 0);
		PreferencesUtil.setValue(PreferencesUtil.AUTO_LOGIN, 0);
	}
}

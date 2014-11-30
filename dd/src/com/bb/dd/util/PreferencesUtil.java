package com.bb.dd.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bb.dd.BikeApplication;
/**
 * 在手机中保存一些变量。
 * @author Administrator
 *
 */
public class PreferencesUtil {
	public static final String IS_VIBRATE = "is_vibrate";
	private static final String MY_PREFERE = "config";
	private static final String CHECK_BOOLEAN = "check";
	private static final String UPDATE_NEW = "update_new";
	public static final String INIT_DB="init_db";
	public static final String INIT_TABLE_FOCUS = "init_table_focus";
	public static final int ON = 1;
	public static final int OFF = 0;
	// time 参数；
	public static final String LAST_UPDATE_TIME="last_update_time";
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String USER_PHONE = "user_phone";
	public static final String USER_SEX = "user_sex";
	public static final String USER_AGE = "user_age";
	public static final String USER_PWD = "user_pwd";
	public static final String USER_PHOTO = "user_photo";
	public static final String REMEMBER_PASSWORD="remember_password";//记住密码  0,不记住,1,记住
	public static final String AUTO_LOGIN="auto_login";//自动登录 0,不自动登录,1,自动登录
	public static final String OFFLINE_MAP = "2010-09-01";
	public static final String FIRST_INDEX_MAIN="first_index_main";

	public static void init() {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(MY_PREFERE, 0);
		Editor editor = spf.edit();
		editor.putString(LAST_UPDATE_TIME, "");
		editor.putInt(IS_VIBRATE, ON);
		editor.putString(USER_NAME, "");
		editor.putString(USER_PHONE, "");
		editor.putString(USER_PWD, "");
		editor.putInt(USER_SEX, 0);
		editor.putInt(USER_AGE, 0);
		editor.putLong(USER_ID, 0l);
		editor.putString(USER_PHOTO, "");
		editor.putInt(REMEMBER_PASSWORD, 0);
		editor.putInt(AUTO_LOGIN, 0);
		editor.commit();
	}
	public static void initIndexMain() {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(MY_PREFERE, 0);
		Editor editor = spf.edit();
		editor.putInt(FIRST_INDEX_MAIN, 0);
		editor.commit();
	}
	public static void add(String tag,int value){
		SharedPreferences spf = BikeApplication.getApplication()
				.getSharedPreferences(MY_PREFERE, 0);
		Editor editor = spf.edit();
		editor.putInt(tag, value);
		editor.commit();
	}
	public static void addStr(String tag,String Str){
		SharedPreferences spf = BikeApplication.getApplication()
				.getSharedPreferences(MY_PREFERE, 0);
		Editor editor = spf.edit();
		editor.putString(tag, Str);
		editor.commit();
	}
	
	public static void addUpdate(String tag,String Str){
		SharedPreferences spf = BikeApplication.getApplication()
				.getSharedPreferences(UPDATE_NEW, 0);
		Editor editor = spf.edit();
		editor.putString(tag, Str);
		editor.commit();
	}
	public static void addBoolean(String tag,Boolean bol){
		SharedPreferences spf = BikeApplication.getApplication()
				.getSharedPreferences(CHECK_BOOLEAN, 0);
		Editor editor = spf.edit();
		editor.putBoolean(tag, bol);
		editor.commit();
	}
	
	public static boolean getCheck(String tag) {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(
				CHECK_BOOLEAN, 0);
		return spf.getBoolean(tag, true);
	}
	public static int getValue(String tag) {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(
				MY_PREFERE, 0);
		return spf.getInt(tag, -1);
	}
	public static String getStr(String tag) {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(
				MY_PREFERE, 0);
		return spf.getString(tag, "");
	}
	public static long getLong(String tag) {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(
				MY_PREFERE, 0);
		return spf.getLong(tag, -1l);
	}
	public static String getUpate(String tag) {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(
				UPDATE_NEW, 0);
		return spf.getString(tag, "");
	}
	public static void setStr(String tag,String content){
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(
				MY_PREFERE, 0);
		Editor editor = spf.edit();
		editor.putString(tag, content);
		editor.commit();
	}
	public static void setValue(String tag, int values) {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(
				MY_PREFERE, 0);
		Editor editor = spf.edit();
		editor.putInt(tag, values);
		editor.commit();
	}
	public static void setLong(String tag, long content) {
		SharedPreferences spf = BikeApplication.getApplication().getSharedPreferences(
				MY_PREFERE, 0);
		Editor editor = spf.edit();
		editor.putLong(tag, content);
		editor.commit();
	}
}

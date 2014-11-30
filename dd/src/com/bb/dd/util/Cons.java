package com.bb.dd.util;

import java.util.List;
import java.util.Map;

import android.graphics.drawable.Drawable;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.modle.BikeSite_Lite;
import com.topdt.application.entity.BikeSiteStatusView;
import com.topdt.application.entity.UserInfor;

public class Cons {
	public static String DATABASE_NAME = "public_bike";
	public static int DATABASE_VERSION = 1;
	public static GeoPoint locPoint=null;
	public static List<String> ruteList;
	public static BikeSite_Lite bikeSite_Lite=null;
	public static int SEARCH_HISTORY=0;
	public static int LOGIN_FLAG=0;
	public static Drawable drawablePhoto;
	public static List<BikeSiteStatusView> remainBikeSites;
	public static Map<String, BikeSiteStatusView> remainBikeSitesStatus;
	
	//public static String imsi;
	//public static String deviceId;  //设备号
	//public static String mobileType;//机型
	public static UserInfor userInfor = new UserInfor(); //用户信息
	
	public static String longitude; //经度
	public static String latitude;  //纬度
	
	public static int ruteMapChoosePt=0;
	public static String ruteAddress;
	public static Double ruteLatitude;
	public static Double ruteLongitude;
	
	public static UserInfor getUserInfor(){
		if(Cons.LOGIN_FLAG == 1){
			long userId = PreferencesUtil.getLong(PreferencesUtil.USER_ID);
			userInfor.setUserId(String.valueOf(userId));
		}else{
			userInfor.setUserId("");
		}
		return userInfor;
	}
}

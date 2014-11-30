package com.bb.dd.util;




/**
 * 服务器访问接口及一些静态变量
 * @author Administrator
 *
 */
public interface CommonVariable {
	int STAION_MAP_OFFSET_X = 0;
	int STAION_MAP_OFFSET_Y = 65;
	int BIKE_MAP = 0;
	int WAKE_UP_FRGMENT = 1;
	String DATABASE_NAME = "public_bike";
	int DATABASE_VERSION = 1;
	String PATH = "http://183.203.9.23:8088/ggzxcgz/";
	//String PATH = "http://183.203.9.24:8080/ggzxcgz/";
	//String PATH = "http://183.203.9.23:8088/ggzxcgz/";
	//String PATH = "http://183.203.9.24:8080/ggzxcgz/";
	//String PATH = "http://172.16.30.12:8080/ggzxcgz2.1/";
	//String PATH = "http://172.16.30.14:8080/ggzxcgz2.1/";
	String REQUEST_RPCPATH = PATH + "JSON-RPC";
	String PATH_TEST = "http://192.168.1.35:8081/proj-coal/bikeSite!queryTest.action";
	String TEST_BIKEID = "http://192.168.1.35:8081/ggzxcgz/bikeSite!getBikeIds.action?bikesiteId=";
	String REQUEST_UPDATE_BIKE_COUNT = PATH + "bikeSite!getSiteByDateCount.action";
	String REQUEST_ALL_BIKE_SITES = PATH + "bikeSite!loadAll.action";
	String REQUEST_update_BIKE_SITES = PATH + "bikeSite!getSiteByDate.action";
	String REQUEST_update_BIKE_count = PATH + "bikeSite!getSiteByDateCount.action";
	String SUBMIT_OPINION = PATH + "_opinion!savePass.action";
	String SUBMIT_COST_LOG = PATH + "costLog!saveLog.action";
	String UPDATE_JSON = PATH + "appUpdate/TaiYuan_Pbike.json";
	String OFFLINE_JSON = PATH + "appUpdate/Tai_Yuan_Shi_176.json";
	String LOAD_IMAGE=PATH+"temp/";
	int DEFAULT_LEVEL = 13;
	String BIKE_REMAIN_PATH = PATH+"bikeSite!getNewBikeMessage.action"; 
	String BIKE_TEST = PATH+"bikeSite!getBikeSite.action"; 
	String AD_INFO = PATH+"_contentNew!_list.action";
	String SD_IMAGE_PATH=Util.getSdRootFile().getAbsolutePath()+"/TaiYuan_Pbike/temp";
	int RADIO_BIKEMAP=1;
	int RADIO_SYNCHRO=2;
	int RADIO_NEWS=3;
	int RADIO_MORE=4;
	int MAX_LEVEL = 20;
	String SD_ERROR_PATH = Util.getSdRootFile().getAbsolutePath()+"/Bike/error";
	String SD_GET_BIKESITE = Util.getSdRootFile().getAbsolutePath()+"/Bike/bikeSite";
	String APP_NAME = "com.thx.ty_publicbike";
	
	String BaiDu_MapName = "Tai_Yuan_Shi_176.dat_svc";
	String BaiDu_MapLocalPath = "map/";
	String BaiDuMap_HVSDPath = "BaiduMapSdk/vmp/h/";
	String BaiDuMap_LVSDPath = "BaiduMapSdk/vmp/l/";
	
	String AD_LOST_INFO = PATH+"lostandfound!doNewHtml5List.action?perPage=7";
	String AD_NEW_INFO = PATH+"new!doNewHtml5List.action?perPage=7";
	String AD_HELP_INFO = PATH+"content!doHelpHtml5Content.action"; 
	String AD_HELPb_INFO = PATH+"content!doHelpbHtml5Content.action"; 
}

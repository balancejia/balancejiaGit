package com.bb.dd.util;
/** 
 * @author HuangF 
 * @version 创建时间：2013-1-11 上午9:23:26 
 * 类说明  网络请求地址
 */
public interface WebCons {

	//向服务端发送用户访问次数等参数的url
	String REQUEST_PHONO_CODE = CommonVariable.PATH+"clientLogin!getVerfyNum.action?num=";
	String REQUEST_REGISTER_URL = CommonVariable.PATH+"clientLogin!register.action?num=";
	String REQUEST_LOGIN_URL = CommonVariable.PATH+"clientLogin!login.action?num=";
	String REQUEST_STATIC_PWD = CommonVariable.PATH+"clientLogin!getStaticPwd.action?num=";
}

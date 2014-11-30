package com.bb.dd.modle;

import java.io.Serializable;

/**
 * @author HuangF
 * @version 创建时间：2013-1-29 上午10:26:42 类说明
 */
public class UploadApk implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String apkname;
	public String apkUrl;
	public int verCode;
	public String verName;
	public String verContent;
	public String updateTime;
}

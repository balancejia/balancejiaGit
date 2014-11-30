package com.bb.dd.MapOverLay;

import java.io.Serializable;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;



/**
 * 自定义OverlayItem
 * 任何一个ItemizedOverlay
 * @author Administrator
 *
 */
public class MyOverlayItem extends OverlayItem implements Serializable{

	private static final long serialVersionUID = 1L;
	private String title;
	private String bikeId;
	public MyOverlayItem(GeoPoint arg0, String arg1, String arg2) {
		super(arg0, arg1, arg2);
		title=arg1;
		bikeId=arg2;
	}
	public String getTitle() {
		return title;
	}
	public String getBikeId() {
		return bikeId;
	}
	

}

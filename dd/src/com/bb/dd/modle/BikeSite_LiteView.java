package com.bb.dd.modle;

import java.io.Serializable;

import com.j256.ormlite.table.DatabaseTable;
import com.topdt.application.entity.BikeSiteRealView;

/**
 * 对应列表模式
 */
@DatabaseTable(tableName = "bike_site")
public class BikeSite_LiteView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BikeSite_Lite bikeSite_Lite;

	private BikeSiteRealView bikeSiteRealView;
	private double distance;// 距离
	public BikeSite_Lite getBikeSite_Lite() {
		return bikeSite_Lite;
	}

	public void setBikeSite_Lite(BikeSite_Lite bikeSite_Lite) {
		this.bikeSite_Lite = bikeSite_Lite;
	}

	public BikeSiteRealView getBikeSiteRealView() {
		return bikeSiteRealView;
	}

	public void setBikeSiteRealView(BikeSiteRealView bikeSiteRealView) {
		this.bikeSiteRealView = bikeSiteRealView;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}

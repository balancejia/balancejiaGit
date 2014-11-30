package com.bb.dd.modle;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * 对应SQLLITE
 * @author Administrator
 *
 */
@DatabaseTable(tableName = "bike_site")
public class BikeSite_Lite implements Serializable {

	 @DatabaseField(id=true)
	private int recId;

	 @DatabaseField(columnName="bikesite_id")
	private String bikesiteId;// 站点ID

	 @DatabaseField(columnName="bikesite_name")
	private String bikesiteName;// 站点名称

	 @DatabaseField
	private String sign3;// 百度经度

	@DatabaseField
	private String sign4;// 百度纬度
	
	@DatabaseField(columnName="update_time")
	private String updateTime;//最后更新时间
	
	@DatabaseField
	private Integer state;
	
	public BikeSite_Lite(){
		
	}

	public int getRecId() {
		return recId;
	}

	public void setRecId(int recId) {
		this.recId = recId;
	}

	public String getBikesiteId() {
		return bikesiteId;
	}

	public void setBikesiteId(String bikesiteId) {
		this.bikesiteId = bikesiteId;
	}

	public String getBikesiteName() {
		return bikesiteName;
	}

	public void setBikesiteName(String bikesiteName) {
		this.bikesiteName = bikesiteName;
	}

	public String getSign3() {
		return sign3;
	}

	public void setSign3(String sign3) {
		this.sign3 = sign3;
	}

	public String getSign4() {
		return sign4;
	}

	public void setSign4(String sign4) {
		this.sign4 = sign4;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
	
}

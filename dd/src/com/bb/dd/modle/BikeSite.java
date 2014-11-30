package com.bb.dd.modle;


public class  BikeSite{
	private Long ID;
	private String Name;//站点名称
	private String bikesiteId;//站点编号
	private String BikePosCount;//车位数
	private String CanUsePosCount;//可租车位数
	private String CanUseBikeCount;//可租车辆数
	
	public BikeSite() {
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getBikePosCount() {
		return BikePosCount;
	}
	public void setBikePosCount(String bikePosCount) {
		BikePosCount = bikePosCount;
	}
	public String getCanUsePosCount() {
		return CanUsePosCount;
	}
	public void setCanUsePosCount(String canUsePosCount) {
		CanUsePosCount = canUsePosCount;
	}
	public String getCanUseBikeCount() {
		return CanUseBikeCount;
	}
	public void setCanUseBikeCount(String canUseBikeCount) {
		CanUseBikeCount = canUseBikeCount;
	}
	public String getBikesiteId() {
		return bikesiteId;
	}
	public void setBikesiteId(String bikesiteId) {
		this.bikesiteId = bikesiteId;
	}

	
}
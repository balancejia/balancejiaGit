package com.bb.dd.modle;

import java.io.Serializable;

public class RuteView implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String startAddress,endAddress;
	private Double startlatitude,startlongitude,endlatitude,endlongitude;
	private int flag;
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getStartAddress() {
		return startAddress;
	}
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	public String getEndAddress() {
		return endAddress;
	}
	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	public Double getStartlatitude() {
		return startlatitude;
	}
	public void setStartlatitude(Double startlatitude) {
		this.startlatitude = startlatitude;
	}
	public Double getStartlongitude() {
		return startlongitude;
	}
	public void setStartlongitude(Double startlongitude) {
		this.startlongitude = startlongitude;
	}
	public Double getEndlatitude() {
		return endlatitude;
	}
	public void setEndlatitude(Double endlatitude) {
		this.endlatitude = endlatitude;
	}
	public Double getEndlongitude() {
		return endlongitude;
	}
	public void setEndlongitude(Double endlongitude) {
		this.endlongitude = endlongitude;
	}
	@Override
	public String toString() {
		return "RuteView [startAddress=" + startAddress + ",\n endAddress="
				+ endAddress + ",\n startlatitude=" + startlatitude
				+ ",\n startlongitude=" + startlongitude + ",\n endlatitude="
				+ endlatitude + ",\n endlongitude=" + endlongitude + ",\n flag="
				+ flag + "]";
	}
	

}

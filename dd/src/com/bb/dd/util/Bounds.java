package com.bb.dd.util;

/**
 * 类Bounds.java的实现描述：用户当前位置半径x米的经纬度范围
 * 
 * @author zjb 2011-3-30 下午04:56:13
 */
public class Bounds {
    /**
     * 当前位置正北方向x米处 纬度
     */
    private Double latN;
    /**
     * 当前位置正南方向x米处 纬度
     */
    private Double latS;
    /**
     * 当前位置正东方向x米处 经度
     */
    private Double lagE;
    /**
     * 当前位置正西方向x米处 经度
     */
    private Double lagW;
    
	public Double getLatN() {
		return latN;
	}
	public void setLatN(Double latN) {
		this.latN = latN;
	}
	public Double getLatS() {
		return latS;
	}
	public void setLatS(Double latS) {
		this.latS = latS;
	}
	public Double getLagE() {
		return lagE;
	}
	public void setLagE(Double lagE) {
		this.lagE = lagE;
	}
	public Double getLagW() {
		return lagW;
	}
	public void setLagW(Double lagW) {
		this.lagW = lagW;
	}
}

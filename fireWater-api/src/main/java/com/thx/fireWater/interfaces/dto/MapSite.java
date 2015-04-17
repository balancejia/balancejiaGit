package com.thx.fireWater.interfaces.dto;

/**
 * 站点信息
 *
 * @author balancejia
 *
 */
public class MapSite {

	protected String siteId;// 标号
	protected String siteName;// 名称
	protected double longitude;// 经度
	protected double latitude;// 纬度

	protected String imageURL;// 图片资源请求地址
	protected int imageNUM;// 图片资源个数，资源id范围：0~imageNUM

	protected String docURL;// 文档请求地址
	protected int docNUM;// 非图片资源个数，资源id范围：0~docNUM

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getDocURL() {
		return docURL;
	}

	public void setDocURL(String docURL) {
		this.docURL = docURL;
	}

	public int getImageNUM() {
		return imageNUM;
	}

	public void setImageNUM(int imageNUM) {
		this.imageNUM = imageNUM;
	}

	public int getDocNUM() {
		return docNUM;
	}

	public void setDocNUM(int docNUM) {
		this.docNUM = docNUM;
	}

}

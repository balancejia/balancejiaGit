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

	protected String field0;
	protected String field1;
	protected String field2;
	protected String field3;
	protected String field4;
	protected String field5;
	protected String field6;
	protected String field7;
	protected String field8;
	protected String field9;

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

	public String getField0() {
		return field0;
	}

	public void setField0(String field0) {
		this.field0 = field0;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	public String getField6() {
		return field6;
	}

	public void setField6(String field6) {
		this.field6 = field6;
	}

	public String getField7() {
		return field7;
	}

	public void setField7(String field7) {
		this.field7 = field7;
	}

	public String getField8() {
		return field8;
	}

	public void setField8(String field8) {
		this.field8 = field8;
	}

	public String getField9() {
		return field9;
	}

	public void setField9(String field9) {
		this.field9 = field9;
	}

}

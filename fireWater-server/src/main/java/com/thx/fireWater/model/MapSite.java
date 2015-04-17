/**
 *
 */
package com.thx.fireWater.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author balancejia
 *
 */
@Entity
@Table(name = "map_site")
public class MapSite implements Serializable {

	private static final long serialVersionUID = 7850128713278927932L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "assigned")
	private String siteId;// 标号

	private String siteName;// 名称
	private double longitude;// 经度
	private double latitude;// 纬度

	private String imageURL;// 图片资源请求地址
	private int imageNUM;// 图片资源个数，资源id范围：0~imageNUM

	private String docURL;// 文档请求地址
	private int docNUM;// 非图片资源个数，资源id范围：0~docNUM

	private String siteType;// 站点类型

	private String field0;
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	private String field7;
	private String field8;
	private String field9;
	private String field10;
	private String field11;
	private String field12;
	private String field13;
	private String field14;
	private String field15;
	private String field16;
	private String field17;
	private String field18;
	private String field19;

	private Date insertTime;
	private Date updateTime;

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

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
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

	public String getField10() {
		return field10;
	}

	public void setField10(String field10) {
		this.field10 = field10;
	}

	public String getField11() {
		return field11;
	}

	public void setField11(String field11) {
		this.field11 = field11;
	}

	public String getField12() {
		return field12;
	}

	public void setField12(String field12) {
		this.field12 = field12;
	}

	public String getField13() {
		return field13;
	}

	public void setField13(String field13) {
		this.field13 = field13;
	}

	public String getField14() {
		return field14;
	}

	public void setField14(String field14) {
		this.field14 = field14;
	}

	public String getField15() {
		return field15;
	}

	public void setField15(String field15) {
		this.field15 = field15;
	}

	public String getField16() {
		return field16;
	}

	public void setField16(String field16) {
		this.field16 = field16;
	}

	public String getField17() {
		return field17;
	}

	public void setField17(String field17) {
		this.field17 = field17;
	}

	public String getField18() {
		return field18;
	}

	public void setField18(String field18) {
		this.field18 = field18;
	}

	public String getField19() {
		return field19;
	}

	public void setField19(String field19) {
		this.field19 = field19;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}

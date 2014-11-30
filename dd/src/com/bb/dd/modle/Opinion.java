/* 
 * 文件名: Opinion.java
 * 包路径: com.thx.favour.entiy
 * 创建描述  
 *        创建人：zhangheng 
 *        创建日期：2012-12-3 下午7:20:16
 *        内容描述：
 * 修改描述  
 *        修改人：Administrator 
 *        修改日期：2012-12-3 下午7:20:16 
 *        修改内容:
 * 版本: V1.0   
 */
package com.bb.dd.modle;

/**
 * 类:  <code> Opinion </code>
 * 功能描述: 
 * 创建日期: 2012-12-3 下午7:20:16
 * 开发环境: JDK6.0
 */
public class Opinion {

	public Integer opinionId;
	
	public String opinionStyle;//反馈类型
	
	public String opinionContent;//建议或者意见
	
	public String contact;//联系方式
	
	public String mobileType;

	public Integer getOpinionId() {
		return opinionId;
	}

	public void setOpinionId(Integer opinionId) {
		this.opinionId = opinionId;
	}

	public String getOpinionStyle() {
		return opinionStyle;
	}

	public void setOpinionStyle(String opinionStyle) {
		this.opinionStyle = opinionStyle;
	}

	public String getOpinionContent() {
		return opinionContent;
	}

	public void setOpinionContent(String opinionContent) {
		this.opinionContent = opinionContent;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}
	
}

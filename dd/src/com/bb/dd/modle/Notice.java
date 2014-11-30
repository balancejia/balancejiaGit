package com.bb.dd.modle;
import java.io.Serializable;
/**
 * 建立日期 : 2013-1-23 上午11:30:12<br>
 * 作者 : 杨子镔<br>
 * 模块 : <br>
 * 描述 :公告实体<br>
 */
public class Notice implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private String title;// 公告标题

	private String content;// 公告内容

	private String updateTime;// 公告日期

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	private String other;// 其他

	private String imageUrl; // 图片

	private String sign1;// sign1

	private String sign2;// sign2

	private String sign3;// sign3

	private String sign4;// sign4

	public String getContent() {
		return content;
	}

	public Long getId() {
		return id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getOther() {
		return other;
	}

	public String getSign1() {
		return sign1;
	}

	public String getSign2() {
		return sign2;
	}

	public String getSign3() {
		return sign3;
	}

	public String getSign4() {
		return sign4;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}



	public void setId(Long id) {
		this.id = id;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public void setSign1(String sign1) {
		this.sign1 = sign1;
	}

	public void setSign2(String sign2) {
		this.sign2 = sign2;
	}

	public void setSign3(String sign3) {
		this.sign3 = sign3;
	}

	public void setSign4(String sign4) {
		this.sign4 = sign4;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

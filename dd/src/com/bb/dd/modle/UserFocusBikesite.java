package com.bb.dd.modle;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * 对应SQLLITE
 * @author HP
 *
 */
@DatabaseTable(tableName = "user_focus_bikesite")
public class UserFocusBikesite implements Serializable {

	/**
	 * 用户关注站点
	 */
	private static final long serialVersionUID = 3991696141365576172L;

	@DatabaseField(id=true)
	private Long bikeUserAtId;
    
	@DatabaseField(columnName="apn_user_id")
	private String apnUserId;					// 用户id
	@DatabaseField(columnName="bikesite_id")
	private String bikesiteId;					//站点id
	public Long getBikeUserAtId() {
		return bikeUserAtId;
	}
	public void setBikeUserAtId(Long bikeUserAtId) {
		this.bikeUserAtId = bikeUserAtId;
	}
	public String getApnUserId() {
		return apnUserId;
	}
	public void setApnUserId(String apnUserId) {
		this.apnUserId = apnUserId;
	}
	public String getBikesiteId() {
		return bikesiteId;
	}
	public void setBikesiteId(String bikesiteId) {
		this.bikesiteId = bikesiteId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@DatabaseField(columnName="create_time")
	private Date createTime;//创建时间
	
}

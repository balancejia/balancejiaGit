package com.bb.dd.modle;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * 对应SQLLITE
 * @author HP
 *
 */
@DatabaseTable(tableName = "search_history")
public class SearchHistory implements Serializable {

	/**
	 * 查询历史
	 */
	private static final long serialVersionUID = 8669146763023913475L;


	public int getSearchHistoryId() {
		return searchHistoryId;
	}


	public void setSearchHistoryId(int searchHistoryId) {
		this.searchHistoryId = searchHistoryId;
	}


	public String getSearchHistoryName() {
		return searchHistoryName;
	}


	public void setSearchHistoryName(String searchHistoryName) {
		this.searchHistoryName = searchHistoryName;
	}


	public String getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


	@DatabaseField(id=true)
	private int searchHistoryId;


	@DatabaseField(columnName="search_history_name")
	private String searchHistoryName;// 站点名称

	
	@DatabaseField(columnName="update_time")
	private String updateTime;//最后更新时间
	
	
	public SearchHistory(){
		
	}

	
}

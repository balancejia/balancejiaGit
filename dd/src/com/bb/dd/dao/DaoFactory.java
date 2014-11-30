package com.bb.dd.dao;

import java.sql.SQLException;

import com.bb.dd.BikeApplication;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.SearchHistory;
import com.bb.dd.modle.UserFocusBikesite;
import com.bb.dd.util.DBHelper;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
/**
 * SQLLITE
 * @author 员旭峰
 * 创建数据库
 *
 */
public class DaoFactory {

	private static DaoFactory daoFactory = null;
	private DBHelper dbHelper = null;
	private Dao<BikeSite_Lite, Integer> bikeSiteDao;
	private Dao<SearchHistory, Integer> searchHistoryDao;
	private Dao<UserFocusBikesite, Integer> userAttentionBikesiteDao;

	private DaoFactory() {

	}

	public static DaoFactory instant() {
		if (null == daoFactory) {
			daoFactory = new DaoFactory();
		}
		return daoFactory;
	}

	private DBHelper getHelper() {
		if (dbHelper == null) {
			dbHelper = OpenHelperManager.getHelper(
					BikeApplication.getApplication(), DBHelper.class);
		}
		return dbHelper;
	}

	public Dao<BikeSite_Lite, Integer> getBikeSiteDao() {
		try {
			if (null == bikeSiteDao) {
				bikeSiteDao = getHelper().getDao(BikeSite_Lite.class);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bikeSiteDao;
	}
	public Dao<SearchHistory, Integer> getSearchHistoryDao() {
		try {
			if (null == searchHistoryDao) {
				searchHistoryDao = getHelper().getDao(SearchHistory.class);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return searchHistoryDao;
	}
	public Dao<UserFocusBikesite, Integer> getUserFocusBikesiteDao() {
		try {
			if (null == userAttentionBikesiteDao) {
				userAttentionBikesiteDao = getHelper().getDao(UserFocusBikesite.class);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userAttentionBikesiteDao;
	}
	public AndroidDatabaseConnection getAndroidDataBase(){
			AndroidDatabaseConnection db = new AndroidDatabaseConnection(
					dbHelper.getWritableDatabase(), true);
			return db;
	}
}

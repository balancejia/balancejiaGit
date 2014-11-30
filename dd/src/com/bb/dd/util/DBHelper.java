package com.bb.dd.util;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.SearchHistory;
import com.bb.dd.modle.UserFocusBikesite;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "user.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
			try {
				TableUtils.createTable(connectionSource, BikeSite_Lite.class);
				TableUtils.createTable(connectionSource, SearchHistory.class);
				TableUtils.createTable(connectionSource, UserFocusBikesite.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, BikeSite_Lite.class, true);
			TableUtils.dropTable(connectionSource, SearchHistory.class,true);
			TableUtils.dropTable(connectionSource, UserFocusBikesite.class, true);
			onCreate(db,connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

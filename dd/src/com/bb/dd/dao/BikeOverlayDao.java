package com.bb.dd.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.modle.SearchHistory;
import com.bb.dd.util.Bounds;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class BikeOverlayDao {

	public ArrayList<OverlayItem> queryAll() {
		Dao<BikeSite_Lite, Integer> dao = DaoFactory.instant().getBikeSiteDao();
		try {
			List<BikeSite_Lite> bikes = dao.queryForAll();
			int size = bikes.size();
			ArrayList<OverlayItem> array = new ArrayList<OverlayItem>(size);
			for (int i = 0; i < size; i++) {
				BikeSite_Lite bike = bikes.get(i);
				Double latitude = Double.parseDouble(bike.getSign4().trim()) * 1E6;
				Double longitude = Double.parseDouble(bike.getSign3().trim()) * 1E6;
				GeoPoint point = new GeoPoint(longitude.intValue(),
						latitude.intValue());
				OverlayItem item = new OverlayItem(point, "title",
						bike.getBikesiteName());
				array.add(item);
				item = null;
				point = null;
				latitude = null;
				longitude = null;
				bike = null;
			}
			return array;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public List<BikeSite_Lite> queryBikeSites(){
		Dao<BikeSite_Lite, Integer> dao = DaoFactory.instant().getBikeSiteDao();
		List<BikeSite_Lite> bikes=null;
		try {
			 bikes = dao.queryForEq("state", 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bikes;
	}
	
	public List<BikeSite_Lite> queryBikeSitesById(String BikesiteId){
		Dao<BikeSite_Lite, Integer> dao = DaoFactory.instant().getBikeSiteDao();
		List<BikeSite_Lite> bikes=null;
		try {
			 bikes = dao.queryForEq("bikesite_id", BikesiteId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bikes;
	}
	
	public List<BikeSite_Lite> queryBikeSitesByBounds(Bounds bounds){
		Dao<BikeSite_Lite, Integer> dao = DaoFactory.instant().getBikeSiteDao();
		List<BikeSite_Lite> bikes=null;
		try {
			 bikes = dao.queryBuilder().where().eq("state", 1)
					 .and().ge("sign3", bounds.getLatS())
					 .and().le("sign3", bounds.getLatN())
					 .and().ge("sign4", bounds.getLagW())
					 .and().le("sign4", bounds.getLagE()).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bikes;
	}
	/**
	 * 
	 * @param colums 需要模糊查询多个字段
	 * @param values 需要模糊查询的字段值
	 * @return
	 */
	public List<BikeSite_Lite> query4Like(String[] colums, String... values) {
		Dao<BikeSite_Lite, Integer> dao = DaoFactory.instant().getBikeSiteDao();
		QueryBuilder<BikeSite_Lite, Integer> builder = dao.queryBuilder();
		int size=0;
		if (null!=colums&&(size= colums.length)!= 0) {
			return builderWhereForQuery(size, builder, colums, values);
		} else {
			try {
				return dao.queryForAll();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	/**
	 * 模糊查询
	 * @param size 
	 * @param builder QueryBuilder对象
	 * @param colums 字段
	 * @param values 值
	 * @return
	 */
	private List<BikeSite_Lite> builderWhereForQuery(int size,
			QueryBuilder<BikeSite_Lite, Integer> builder, String[] colums,
			String[] values) {
		if (size == 1) {
			Where<BikeSite_Lite, Integer> where = builder.where();
			try {
				where.like(colums[0], values[0]);
				builder.setWhere(where);
				return builder.query();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			try {
				Where<BikeSite_Lite,Integer> where = builder.where();
				for (int i = 0; i < size; i++) {
					if(i!=0)where.or();
					where.like(colums[i], values[i]);
				}
				builder.setWhere(where);
				return builder.query();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}

		}

	}


	public List<SearchHistory> querySearchHistory() {
		Dao<SearchHistory, Integer> dao = DaoFactory.instant().getSearchHistoryDao();
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}

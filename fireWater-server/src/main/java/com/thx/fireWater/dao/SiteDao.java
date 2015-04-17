/**
 *
 */
package com.thx.fireWater.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.thx.common.orm.HibernateBaseDao;
import com.thx.fireWater.model.MapSite;

/**
 * @author balancejia
 *
 */
@Repository
public class SiteDao extends HibernateBaseDao<MapSite> {

	public List<MapSite> getSites(Date stime, String type) {
		String hql = "from MapSite where siteType=? and updateTime>=?";
		Query query = createQuery(hql,type , stime);
		List<MapSite> sites = query.list();
		return sites;
	}

	public List<MapSite> getSites(String type) {
		String hql = "from MapSite where siteType=? ";
		Query query = createQuery(hql, type);
		List<MapSite> sites = query.list();
		return sites;
	}

}

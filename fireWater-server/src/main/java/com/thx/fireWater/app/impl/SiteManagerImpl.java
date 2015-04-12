/**
 *
 */
package com.thx.fireWater.app.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thx.fireWater.interfaces.dto.WaterSite;
import com.thx.fireWater.interfaces.jsonrpc.SiteManager;

/**
 * @author balancejia
 * 站点服务
 *
 */
public class SiteManagerImpl implements SiteManager {

	@Override
	public List<WaterSite> loadNewWaterSites(Date stime) {

		List<WaterSite> sites=new ArrayList<WaterSite>();

		WaterSite site=new WaterSite();
		site.setLatitude(323232323.323232);
		site.setLongitude(898989.43443);
		site.setSiteId("njj0999");
		site.setSiteName("和平南路");

		sites.add(site);
		return sites;
	}

	@Override
	public void addWaterSite(WaterSite site) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateWaterSite(WaterSite site) {
		// TODO Auto-generated method stub

	}



}

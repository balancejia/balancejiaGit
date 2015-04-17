/**
 *
 */
package com.thx.fireWater.app.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.thx.fireWater.interfaces.dto.WaterSite;
import com.thx.fireWater.interfaces.jsonrpc.SiteManager;

/**
 * @author balancejia
 *
 */
public class SiteManagerImplTest {

	/**
	 * Test method for
	 * {@link com.thx.fireWater.app.impl.SiteManagerImpl#loadNewWaterSites(java.util.Date)}
	 * .
	 */
	@Test
	public void testLoadNewWaterSites() {
		SiteManager test = new SiteManagerImpl();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);


		List<WaterSite> sites=test.loadNewWaterSites(c.getTime());

		System.out.println("size="+sites.size());
	}

	private void set(int date, int i) {
		// TODO Auto-generated method stub

	}

}

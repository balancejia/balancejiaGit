/**
 *
 */
package com.thx.fireWater.interfaces.jsonrpc;

import java.util.Date;
import java.util.List;

import com.thx.fireWater.interfaces.dto.WaterSite;

/**
 * @author balancejia
 *
 */
public interface SiteManager {

	/**
	 * 从stime开始有变化的点，包括修改和新增的点，若stime为null则返回全部站点数据
	 *
	 * @param stime
	 * @return
	 */
	public List<WaterSite> loadNewWaterSites(Date stime);

	/**
	 * 增加一个水源
	 *
	 * @param site
	 */
	public void addWaterSite(WaterSite site);

	/**
	 * 更新水源
	 *
	 * @param site
	 */
	public void updateWaterSite(WaterSite site);

}

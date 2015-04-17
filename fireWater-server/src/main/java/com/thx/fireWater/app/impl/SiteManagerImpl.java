/**
 *
 */
package com.thx.fireWater.app.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thx.common.spring.BeanFactory;
import com.thx.fireWater.interfaces.dto.WaterSite;
import com.thx.fireWater.interfaces.jsonrpc.SiteManager;
import com.thx.fireWater.model.MapSite;
import com.thx.fireWater.service.ISiteManager;

/**
 * @author balancejia 站点服务
 *
 */
public class SiteManagerImpl implements SiteManager {

	public static String TYPE_WATER = "w";
	public static String TYPE_FIRE = "f";
	public static String TYPE_ARMY = "a";

	private static String IMGURL = "resource/img.action";
	private static String DOCURL = "resource/doc.action";

	@Override
	public List<WaterSite> loadNewWaterSites(Date stime) {

		List<WaterSite> wSites = new ArrayList<WaterSite>();

		ISiteManager siteMgr = (ISiteManager) BeanFactory.getBean(ISiteManager.class);

		List<MapSite> sites = siteMgr.getSites(stime, TYPE_WATER);

		for (MapSite site : sites) {
			wSites.add(convertS2W(site));
		}
		return wSites;
	}

	@Override
	public void addWaterSite(WaterSite site) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateWaterSite(WaterSite site) {
		// TODO Auto-generated method stub

	}

	public static WaterSite convertS2W(MapSite site) {
		WaterSite wSite = new WaterSite();
		wSite.setLatitude(site.getLatitude());
		wSite.setLongitude(site.getLongitude());
		wSite.setSiteId(site.getSiteId());
		wSite.setSiteName(site.getSiteName());
		wSite.setImageURL(IMGURL);
		wSite.setImageNUM(site.getImageNUM());
		wSite.setDocNUM(site.getDocNUM());
		wSite.setDocURL(DOCURL);

		wSite.setUsable(str2boolean(site.getField0()));// 是否可用

		wSite.setWuShui(str2boolean(site.getField1()));// 无水
		wSite.setLouShui(str2boolean(site.getField2()));// 漏水
		wSite.setYalibuzu(str2boolean(site.getField3()));// 压力不足
		wSite.setXiushi(str2boolean(site.getField4()));// 锈蚀
		wSite.setMaiya(str2boolean(site.getField5()));// 埋压
		wSite.setQuanzhan(str2boolean(site.getField6()));// 圈占
		wSite.setHuihuai(str2boolean(site.getField7()));// 毁坏
		wSite.setTajing(str2boolean(site.getField8()));// 塌井

		wSite.setBroke(str2boolean(site.getField9()));// 部件损坏

		wSite.setJinggai(str2boolean(site.getField10()));// 井盖
		wSite.setChushuigai(str2boolean(site.getField11()));// 出水盖
		wSite.setDinggai(str2boolean(site.getField12()));// 顶盖
		wSite.setZhafa(str2boolean(site.getField13()));// 闸阀
		wSite.setChushuikou(str2boolean(site.getField14()));// 出水口
		wSite.setFangshuifa(str2boolean(site.getField15()));// 放水阀
		wSite.setShangshuifa(str2boolean(site.getField16()));// 上水阀

		wSite.setComments(site.getField17());// 备注
		return wSite;
	}

	public static MapSite convertW2S(WaterSite wSite) {

		MapSite site = new MapSite();

		site.setLatitude(wSite.getLatitude());
		site.setLongitude(wSite.getLongitude());
		site.setSiteId(wSite.getSiteId());
		site.setSiteName(wSite.getSiteName());
		site.setImageURL(wSite.getImageURL());
		site.setImageNUM(wSite.getImageNUM());
		site.setDocNUM(wSite.getDocNUM());
		site.setDocURL(wSite.getDocURL());
		site.setSiteType(TYPE_WATER);

		site.setField0(boolean2str(wSite.isUsable()));// 是否可用

		site.setField1(boolean2str(wSite.isWuShui()));// 无水
		site.setField2(boolean2str(wSite.isLouShui()));// 漏水
		site.setField3(boolean2str(wSite.isYalibuzu()));// 压力不足
		site.setField4(boolean2str(wSite.isXiushi()));// 锈蚀
		site.setField5(boolean2str(wSite.isMaiya()));// 埋压
		site.setField6(boolean2str(wSite.isQuanzhan()));// 圈占
		site.setField7(boolean2str(wSite.isHuihuai()));// 毁坏
		site.setField8(boolean2str(wSite.isTajing()));// 塌井

		site.setField9(boolean2str(wSite.isBroke()));// 部件损坏

		site.setField10(boolean2str(wSite.isJinggai()));// 井盖
		site.setField11(boolean2str(wSite.isChushuigai()));// 出水盖
		site.setField12(boolean2str(wSite.isDinggai()));// 顶盖
		site.setField13(boolean2str(wSite.isZhafa()));// 闸阀
		site.setField14(boolean2str(wSite.isChushuikou()));// 出水口
		site.setField15(boolean2str(wSite.isFangshuifa()));// 放水阀
		site.setField16(boolean2str(wSite.isShangshuifa()));// 上水阀

		site.setField17(wSite.getComments());// 备注
		return site;
	}

	public static boolean str2boolean(String s) {
		if (s == null)
			return false;
		else
			return "1".equals(s);
	}

	public static String boolean2str(boolean b) {
		if (!b)
			return null;
		else
			return "1";
	}

}

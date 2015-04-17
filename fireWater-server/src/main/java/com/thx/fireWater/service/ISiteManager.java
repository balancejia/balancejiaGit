/**
 *
 */
package com.thx.fireWater.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thx.common.plug.excel.ExcelUtil;
import com.thx.common.plug.excel.ImportResult;
import com.thx.common.service.BaseManager;
import com.thx.fireWater.dao.SiteDao;
import com.thx.fireWater.model.MapSite;

/**
 * @author balancejia
 *
 */
@Service
@Transactional(readOnly = true)
public class ISiteManager extends BaseManager<MapSite> {

	@Autowired
	private SiteDao siteDao;

	public List<MapSite> getSites(Date stime, String type) {

		List<MapSite> sites = null;

		if (stime == null)
			sites = siteDao.getSites(type);
		else
			sites = siteDao.getSites(stime, type);

		return sites;
	}

	@Transactional(readOnly = false)
	public void importSite() {
		String xmlFileName = "siteImport.xml";
		String excleExpandName = "xls";
		InputStream in = null;
		try {
			in = new FileInputStream("E:\\data\\data.xls");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImportResult result = ExcelUtil.importExcel(xmlFileName, in, excleExpandName);
		System.out.println(result.getRightNum());
		List<Object> sites = result.getObjs();

		for (Object site : sites) {
			siteDao.save(site);
		}
	}

}

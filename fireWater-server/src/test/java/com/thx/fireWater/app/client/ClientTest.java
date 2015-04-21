/**
 *
 */
package com.thx.fireWater.app.client;

import java.util.Calendar;
import java.util.List;

import mixedserver.protocol.jsonrpc.client.Client;

import com.thx.fireWater.interfaces.dto.WaterSite;
import com.thx.fireWater.interfaces.jsonrpc.SiteManager;

/**
 * @author balancejia
 *
 */
public class ClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = Client.getClient("http://172.16.10.66:9080/fireWater/JSON-RPC");

		// 设定是否解密服务端的消息
		client.setDencryptMessage(false);
		// 设定是否加密服务端的消息
		client.setEncryptMessage(false);

		// 打开一个客户端代理
		SiteManager siteMgr = client.openProxy("site", SiteManager.class);
		try {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DATE, 1);
			List<WaterSite> sites=siteMgr.loadNewWaterSites(c.getTime());
			System.out.println("size="+sites.size());
		} finally {
			// 关闭代理
			client.closeProxy(siteMgr);
		}
	}

}

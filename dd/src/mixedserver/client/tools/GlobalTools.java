package mixedserver.client.tools;

import mixedserver.protocol.jsonrpc.client.Client;

import com.bb.dd.util.CommonVariable;

/**
 * 本项目的工具类
 * 
 * @author zhangxiaohui
 * 
 */
public class GlobalTools {

	// RPC服务器接口地址
	//public static final String SERVER_URL = "http://192.168.1.35:8081/MixedServerDemoPC/JSON-RPC?debug=true";
	
	private final static String URL = CommonVariable.REQUEST_RPCPATH;// "http://localhost:8081/MixedServer_ssh/JSON-RPC";
	
	public static Client getClient(){
		return Client.getClient(URL);
	}
}

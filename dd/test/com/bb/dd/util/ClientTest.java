package com.bb.dd.util;

import java.util.List;

import mixedserver.protocol.jsonrpc.client.Client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topdt.application.BikeSiteUser;
import com.topdt.coal.entity.User;




public class ClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test2(){
		//Client client =  Client.getClient("http://localhost:8080/ggzxcgz2.1/JSON-RPC");
		Client client =  Client.getClient("http://192.168.1.88:8080/ggzxcgz2.1/JSON-RPC");
		client.setDencryptMessage(true);
		BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
		try {
			User arg0=new User();
			arg0.setUsername("1111");
			arg0.setPhoneNumber("15556565565");
			Boolean boolean1 = bikeSiteUser.saveUserInfor(arg0,null);
			System.out.println(boolean1);
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			client.closeProxy(bikeSiteUser);
		}
	}
	@Test
	public void test3(){
		//Client client =  Client.getClient("http://localhost:8080/ggzxcgz2.1/JSON-RPC");
		Client client =  Client.getClient("http://183.203.9.24:8080/ggzxcgz/JSON-RPC");
		BikeSiteUser bikeSiteUser = (BikeSiteUser) client.openProxy("bikeSiteUser",BikeSiteUser.class);
		try {
			Boolean boolean1 = bikeSiteUser.delUserFocus("33", "44",null);
			System.out.println(boolean1);
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			client.closeProxy(bikeSiteUser);
		}
	}
	
}

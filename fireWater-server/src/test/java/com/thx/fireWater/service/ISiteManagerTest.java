package com.thx.fireWater.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.thx.common.spring.BeanFactory;

public class ISiteManagerTest {

//	@Test
	public void testGetSites() {
		 String a="a    212121       .	8877457	 	 	 b";
		 System.out.println(a.replaceAll("\n|\\s|\t|\r", ""));
	}

	@Test
	public void testImportSite() {
		ISiteManager mgr=(ISiteManager)BeanFactory.getBean(ISiteManager.class);
		mgr.importSite();
	}

}

package com.bb.dd.util;

import org.junit.Test;

import com.bb.dd.modle.BikeSite_Lite;
import com.bb.dd.util.BeanUtils;
import com.topdt.coal.entity.BikeSite;

public class BeanUtilsTest {

	@Test
	public void test() throws Exception {
		BikeSite b = new BikeSite();
		b.setBikesiteId("aaa");
		b.setBikesiteName("aaa");
		BikeSite_Lite a = new BikeSite_Lite();
		BeanUtils.copyProperties(a, b);
		System.out.println(a.getBikesiteId());
		System.out.println(a.getBikesiteName());
	}

}

package com.bb.dd.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtils {
	public static void copyProperties(Object dest,Object src) throws Exception {
		Class dest_calssType = dest.getClass();
		//得到要复制对象的类型
		Class calssType = src.getClass();
		//创建一个实例
		//Object objCopy = calssType.getConstructor(new Class[] {}).newInstance(new Class[] {});
		//获取声明的方法
		Field[] fields = calssType.getDeclaredFields();
		//遍历字段
		for (Field f : fields) {
			//获取字段名
			String fieldName = f.getName();
			//生成get和set方法
			String midLetter = fieldName.substring(0, 1).toUpperCase();
			Method getMethod = calssType.getMethod("get" + midLetter + fieldName.substring(1), new Class[] {});
			Object value = getMethod.invoke(src, new Object[] {});
			
			try {
				Method setMethod = dest_calssType.getMethod("set" + midLetter + fieldName.substring(1), new Class[] { f.getType() });
				setMethod.invoke(dest, new Object[] { value });
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		//返回对象
		//return objCopy;
	}
}

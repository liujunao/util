package com.east.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanInfoTest {
	
	private String test;
	
	private String anno;

	public BeanInfoTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BeanInfoTest test = new BeanInfoTest();
		
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(test.getClass());
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
System.out.println(key);
            // 过滤class属性
            if (!key.equals("class")) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
//                Object value = getter.invoke(test);
//
//                map.put(key, value);
            }

        }
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

}

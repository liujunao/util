package com.wonders.framework.util;
//具体的比较类，实现Comparator接口

import java.util.Comparator;

import com.wonders.framework.ws.permission.bo.Menu;

@SuppressWarnings("rawtypes")
public class ComparatorOrder implements Comparator{

	/**
	 * 菜单排序
	 */
	 public int compare(Object arg0, Object arg1) {
		 Menu m0=(Menu)arg0;
		 Menu m1=(Menu)arg1;
	
		  int flag=m0.getOrder().compareTo(m1.getOrder());
		  return flag;
	 }
 
}

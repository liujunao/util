package com.wonders.framework.util;

import java.util.StringTokenizer;

/**
 * 字符串操作工具包
 * @author Administrator
 *
 */
public class StringOperateUtil {
	/**
	 * 
	 * @param from 要替换的字符
	 * @param to  要替换成的目标字符
	 * @param source 要替换的字符串
	 * @return   替换后的字符串
	 */
	 public static String strReplace(String from,String to,String source) {
		    StringBuffer bf= new StringBuffer("");
		    StringTokenizer st = new StringTokenizer(source,from,true);
		    while (st.hasMoreTokens()) {
		      String tmp = st.nextToken();
		      if(tmp.equals(from)) {
		        bf.append(to);
		      } else {
		        bf.append(tmp);
		      }
		    }
		    return bf.toString();
		  }
}

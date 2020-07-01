package com.east.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamingUtil {

	private NamingUtil() {
		// TODO Auto-generated constructor stub
	}

	public static final char UNDERLINE = '_';

	public static String toUpperCaseWithUnderscore(String input) {
	    if(input == null) {
	        throw new IllegalArgumentException();
	    }

	    StringBuilder sb = new StringBuilder();
	    for(int i = 0; i < input.length(); i++) {
	        char c = input.charAt(i);
	        if(Character.isUpperCase(c)) {
	            if(i > 0) {
	                sb.append('_');
	            }
	            sb.append(c);
	        } else {
	            sb.append(Character.toUpperCase(c));
	        }
	    }

	    return sb.toString();
	}
	
	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String underlineToCamel(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String underlineToCamel2(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		StringBuilder sb = new StringBuilder(param);
		Matcher mc = Pattern.compile("_").matcher(param);
		int i = 0;
		while (mc.find()) {
			int position = mc.end() - (i++);
			// String.valueOf(Character.toUpperCase(sb.charAt(position)));
			sb.replace(position - 1, position + 1,
					sb.substring(position, position + 1).toUpperCase());
		}
		return sb.toString();
	}
	
	
	public static String toGetterMethod(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len + 3);
		sb.append("get");
		char c = param.charAt(0);
		sb.append(Character.toUpperCase(c));
		for (int i = 1; i < len; i++) {
			c = param.charAt(i);
			sb.append(c);
		}
		return sb.toString();
	}
	
	

	public static void main(String[] args) {
		String getter = NamingUtil.toGetterMethod("appId");
		
		String underscore = NamingUtil.camelToUnderline("appId");
		
		System.out.println(underscore);
	}

}

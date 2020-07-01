package com.east.common.util;

public class CommonUtil {

	public static boolean isNull(String p) {
		if ("".equals(p) || p == null || p.equals("undefined") || p.equals("null")) {
			return true;
		}
		return false;

	}
}

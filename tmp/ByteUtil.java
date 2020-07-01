package com.east.common.util;

/**
 * Created by rui on 14-3-24.
 */
public class ByteUtil {
    public static String printByte(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
            sb.append(bytes[i]);

        return sb.toString();

    }

    public static String printHexString(byte[] b) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}

package com.east.common.util;

/**
 * Created by rui on 14-3-24.
 */
public class BytesTool {
    public static String byteToHexString(byte... bs) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            String v = Integer.toHexString(b & 0xFF).toUpperCase();
            sb.append(v.getBytes().length == 1 ? "0" + v : v);
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hex) {
        int len = hex.length() / 2;
        byte[] ba = new byte[len];
        for (int i = 0; i < len; i++) {
            String t = hex.substring(i * 2, i * 2 + 2);
            try {
                byte b = Byte.parseByte(t, 16);
                ba[i] = b;
            } catch (NumberFormatException ex) {
                byte b = (byte) (0 - (~(Integer.parseInt(t, 16) - 1)));
                ba[i] = b;
            }
        }
        return ba;
    }

    public static void main(String[] arg) {
        byte[] a = hexStringToBytes("70");

        System.out.println(byteToHexString(a));
    }
}

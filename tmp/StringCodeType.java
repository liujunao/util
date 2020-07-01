package com.east.common.util;

/**
 * Created by rui on 14-3-24.
 */
public enum StringCodeType {
    ENCODING_UNKNOWN(0), ENCODING_ASCII(1), ENCODING_UTF8(2), ENCODING_BINARY(3), ENCODING_GB(
            4), ENCODING_UTF16(5), ENCODING_JAVA(6), ENCODING_EXPRESS(7), ENCODING_MAX(
            0x1000);

    private int value;

    StringCodeType(int arg) {
        value = arg;
    }

    public int getValue() {
        return value;
    }

    public static StringCodeType parse(int arg) {
        switch (arg) {
            case 0:
                return StringCodeType.ENCODING_UNKNOWN;
            case 1:
                return StringCodeType.ENCODING_ASCII;
            case 2:
                return StringCodeType.ENCODING_UTF8;
            case 3:
                return StringCodeType.ENCODING_BINARY;
            case 4:
                return StringCodeType.ENCODING_GB;
            case 5:
                return StringCodeType.ENCODING_UTF16;
            case 6:
                return StringCodeType.ENCODING_JAVA;
            case 7:
                return StringCodeType.ENCODING_EXPRESS;
            case 0x1000:
                return StringCodeType.ENCODING_MAX;
            default:
                return null;
        }
    }

    public static StringCodeType valueOf(int value) {
        switch (value) {
            case 0:
                return StringCodeType.ENCODING_UNKNOWN;
            case 1:
                return StringCodeType.ENCODING_ASCII;
            case 2:
                return StringCodeType.ENCODING_UTF8;
            case 3:
                return StringCodeType.ENCODING_BINARY;
            case 4:
                return StringCodeType.ENCODING_GB;
            case 5:
                return StringCodeType.ENCODING_UTF16;
            case 6:
                return StringCodeType.ENCODING_JAVA;
            case 7:
                return StringCodeType.ENCODING_EXPRESS;
            case 0x1000:
                return StringCodeType.ENCODING_MAX;
            default:
                return null;
        }
    }
}

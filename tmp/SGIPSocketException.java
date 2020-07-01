package com.east.common.util;

/**
 * Created by rui on 14-3-24.
 */
public class SGIPSocketException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 95388858414451110L;

    /**
     *
     */
    public SGIPSocketException() {

    }

    /**
     * @param arg0
     */
    public SGIPSocketException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public SGIPSocketException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public SGIPSocketException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}

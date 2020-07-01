package com.east.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by wangyan on 14-3-24.
 * 通讯工具类
 */
public class SocketTool {
    private static Logger logger = LoggerFactory.getLogger(SocketTool.class);
    private String PeerIP;
    private int PeerPort;
    private Socket clientSocket = null;
    private DataOutputStream outbound = null;
    private DataInputStream inbound = null;
    private Integer readLock = new Integer(0);
    private Integer writeLock = new Integer(0);

    public SocketTool(String ip, int port) {
        setPeerIP(ip);
        setPeerPort(port);
    }

    public void setTimeOut(int millSecond) {
        if (this.clientSocket != null)
            try {
                this.clientSocket.setSoTimeout(millSecond);
            } catch (SocketException e) {
                e.printStackTrace();
            }
    }

    public SocketTool(Socket socket) throws SGIPSocketException {
        this.clientSocket = socket;

        try {
            clientSocket.setSoTimeout(0);
            this.clientSocket.setKeepAlive(true);
        } catch (SocketException e1) {
            logger.error("set time out error", e1);
            throw new SGIPSocketException("set time out error");
        }
        try {
            outbound = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.error("outbound create error", e);
            throw new SGIPSocketException("DataOutputStream error");
        }
        try {
            inbound = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.error("inbound create error", e);
            throw new SGIPSocketException("DataInputStream error");
        }
    }

    public void setPeerIP(String ip) {
        PeerIP = ip;
    }

    public void setPeerPort(int port) {
        PeerPort = port;
    }

    public void sock_connect() throws SGIPSocketException {
        try {
            clientSocket = new Socket(PeerIP, PeerPort);
        } catch (UnknownHostException e) {
            logger.error("clientSocket create error", e);
            throw new SGIPSocketException(e);
        } catch (IOException e) {
            logger.error("clientSocket create error", e);
            throw new SGIPSocketException(e);
        }
        try {
            outbound = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.error("outbound create error", e);
            throw new SGIPSocketException(e);
        }
        try {
            inbound = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.error("inbound create error", e);
            throw new SGIPSocketException(e);
        }
    }

    public byte[] sock_trans(byte[] byteBuf) throws Exception {

        if (this.outbound == null)
            throw new SGIPSocketException("read meessage error");

        byte[] bb = null;
        byte[] replybuf = null;
        int usSize = 0;
        int restLen = 0;

        synchronized (this.writeLock) {
            try {
                outbound.write(byteBuf, 0, byteBuf.length);
                usSize = inbound.readInt();
                // 剩余部分需要减去包头
                restLen = usSize - 4;
                replybuf = new byte[usSize];
                bb = StringUtil.int2byte(usSize);
                System.arraycopy(bb, 0, replybuf, 0, 4);
                inbound.readFully(replybuf, 4, restLen);
            } catch (Exception e) {
                throw e;
            } finally {
                sock_close();
            }
        }

        return replybuf;
    }

    public byte[] readwait() throws SGIPSocketException, EOFException,
            SocketTimeoutException {

        if (this.inbound == null)
            return null;

        byte[] bb = null;
        byte[] btmp = null;
        byte[] replybuf = null;
        int usSize = 0;

        bb = new byte[4];

        synchronized (this.readLock) {
            try {
                inbound.read(bb);
            } catch (EOFException e1) {
                logger.info("EOF!");
                throw new EOFException();
            } catch (SocketTimeoutException e2) {
                logger.info("time out");
                throw new SocketTimeoutException();
            } catch (SocketException e3) {
                return null;
            } catch (IOException e4) {
                logger.error("read meessage error:", e4);
                throw new SGIPSocketException();
            }

            usSize = StringUtil.byte2int(bb);
            if (usSize == 0) {
                return new byte[] {};
            } else if (usSize < 5) {
                logger.error("usSize < 5 ,throw exception");
                throw new SGIPSocketException(
                        "SGIP package invalidate, length[" + usSize + "]");
            }

            btmp = new byte[usSize - 4];

            try {
                inbound.readFully(btmp);
            } catch (EOFException e1) {
                logger.error("eof:" + e1);
                throw new EOFException();
            } catch (SocketTimeoutException e) {
                logger.warn("time out:" + e);
                throw new SGIPSocketException();
            } catch (SocketException e3) {
                logger.error("read meessage error:", e3);
                throw new SGIPSocketException();
            } catch (IOException e) {
                logger.error("read meessage error:", e);
                throw new SGIPSocketException();
            }

        }

        replybuf = new byte[usSize];
        System.arraycopy(bb, 0, replybuf, 0, 4);
        System.arraycopy(btmp, 0, replybuf, 4, btmp.length);
        logger.debug("receive bytes:" + BytesTool.byteToHexString(replybuf));
        return replybuf;
    }

    public void write(byte[] buf) throws SGIPSocketException {
        if (outbound == null)
            throw new SGIPSocketException("write meessage error");
        try {
            outbound.write(buf, 0, buf.length);
            logger.debug("write bytes:" + BytesTool.byteToHexString(buf));
            outbound.flush();

        } catch (IOException e) {
            logger.error("Write SGIP package ex[" + e.getMessage() + "]!", e);
            throw new SGIPSocketException("write meessage error");
        }
    }

    public void sock_close() {
        logger.warn("close the socket!");
        if (outbound != null) {
            try {
                outbound.close();
            } catch (IOException e) {
                logger.error("outbound close error", e);
            }
        }
        outbound = null;

        if (inbound != null) {
            try {
                inbound.close();
            } catch (IOException e) {
                logger.error("inbound close error", e);
            }
        }
        inbound = null;

        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("clientSocket close error", e);
            }
        }
        clientSocket = null;
    }
}

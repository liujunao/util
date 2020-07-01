package com.east.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: EastDayWD
 * Date: 13-11-18
 * Time: 下午5:34
 * 字符处理类
 */
public class StringUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(StringUtil.class);
    private static String[] mobileArray = null;
    private static String[] unicomArray = null;
    private static String[] telcomArray = null;
    private static String mobilesNum = "139-138-137-136-135-134-147-150-151-152-157-158-159-182-183-187-188";
    private static String unicomNum = "130-131-132-155-156-185-186-145";
    private static String telcomNum = "133-153-180-181-189-177-1700";

    static {
        mobileArray = mobilesNum.split("-");
        unicomArray = unicomNum.split("-");
        telcomArray = telcomNum.split("-");
    }

    /**
     * MD5加密算法
     *
     * @param sourceStr : 需要加密文字
     * @return 返回加密后字符串
     */
    public static String getMD5(String sourceStr) {
        String resultStr = "";
        try {
            byte[] temp = sourceStr.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(temp);
            // resultStr = new String(md5.digest());
            byte[] b = md5.digest();
            for (int i = 0; i < b.length; i++) {
                char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                        '9', 'A', 'B', 'C', 'D', 'E', 'F'};
                char[] ob = new char[2];
                ob[0] = digit[(b[i] >>> 4) & 0X0F];
                ob[1] = digit[b[i] & 0X0F];
                resultStr += new String(ob);
            }
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Document parseStringToDocument(String string) {
        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(string.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(tInputStringStream);
        } catch (Exception e) {
            LOG.error("ZhiMinServiceImpl parse is throws Exception::", e);
        } finally {
            try {
                if (null != tInputStringStream) {
                    tInputStringStream.close();
                }
            } catch (IOException e) {
                LOG.error("ZhiMinServiceImpl parse finally is throws IOException::", e);
            }
        }
        return document;
    }

    public static String bytesToHexString(byte[] data) {
        StringBuilder result = new StringBuilder();

        for (int n = 0; n < data.length; ++n) {
            String tmp = Integer.toHexString(data[n]);
            if (tmp.length() == 1)
                result.append("0" + tmp);
            else
                result.append(tmp);
        }
        return result.toString();
    }

    public static String IntToHexString(int data, int len) {
        String newData = "00000000" + Integer.toHexString(data);
        newData = newData.substring(newData.length() - len, newData.length());

        return newData;
    }

    public static String IntToString(int data, int len) {
        String newData = "000000000000" + Integer.toString(data);
        newData = newData.substring(newData.length() - len, newData.length());

        return newData;
    }

    public static int DateToSMPP(Calendar date) {
        int data = 0;
        data += date.get(Calendar.SECOND);
        data += date.get(Calendar.MINUTE) * 100;
        data += date.get(Calendar.HOUR_OF_DAY) * 10000;
        data += date.get(Calendar.DAY_OF_MONTH) * 1000000;
        data += (date.get(Calendar.MONTH) + 1) * 100000000;

        return data;
    }

    public static String DateToSMPPString(Calendar date) {
        StringBuilder data = new StringBuilder();

        data.append(IntToString(date.get(Calendar.MONTH) + 1, 2));
        data.append(IntToString(date.get(Calendar.DAY_OF_MONTH), 2));
        data.append(IntToString(date.get(Calendar.HOUR_OF_DAY), 2));
        data.append(IntToString(date.get(Calendar.MINUTE), 2));
        data.append(IntToString(date.get(Calendar.SECOND), 2));

        return data.toString();
    }

    public static Calendar SMPPDateToDate(int d) {
        byte[] data = int2byte(d);
        Calendar date = Calendar.getInstance();

        date.set(0, byte2int(data, 0, 2), byte2int(data, 2, 2),
                byte2int(data, 4, 2), byte2int(data, 6, 2),
                byte2int(data, 8, 2));

        return date;
    }

    public static Calendar SMPPDateToDate(String d) {
        Calendar date = Calendar.getInstance();

        date.set(2000 + Integer.parseInt(d.substring(0, 2)),
                Integer.parseInt(d.substring(2, 2)),
                Integer.parseInt(d.substring(4, 2)),
                Integer.parseInt(d.substring(6, 2)),
                Integer.parseInt(d.substring(8, 2)),
                Integer.parseInt(d.substring(10, 2)));
        return date;

    }

    private static String HexCode[] = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return HexCode[d1] + HexCode[d2];
    }

    public static String byteArrayToHexString(byte b[]) {
        if (b == null)
            return "";
        String result = "";
        for (int i = 0; i < b.length; i++)
            result = result + byteToHexString(b[i]);
        return result;
    }

    public static int byte2int(byte b[], int start, int len) {
        if (start + len > b.length - 1)
            len = b.length - start;

        byte[] data = new byte[4];
        System.arraycopy(b, start, data, 4 - len, len);

        return byte2int(data);
    }

    public static int byte2int(byte b[]) {
        return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16
                | (b[0] & 0xff) << 24;
    }

    public static long byte2long(byte b[]) {
        return (long) b[7] & (long) 255 | ((long) b[6] & (long) 255) << 8
                | ((long) b[5] & (long) 255) << 16
                | ((long) b[4] & (long) 255) << 24
                | ((long) b[3] & (long) 255) << 32
                | ((long) b[2] & (long) 255) << 40
                | ((long) b[1] & (long) 255) << 48 | (long) b[0] << 56;
    }

    /**
     * @param b
     * @param offset 开哪个byte开始
     * @return
     */
    public static long byte2long(byte b[], int offset) {
        return (long) b[offset + 7] & (long) 255
                | ((long) b[offset + 6] & (long) 255) << 8
                | ((long) b[offset + 5] & (long) 255) << 16
                | ((long) b[offset + 4] & (long) 255) << 24
                | ((long) b[offset + 3] & (long) 255) << 32
                | ((long) b[offset + 2] & (long) 255) << 40
                | ((long) b[offset + 1] & (long) 255) << 48
                | (long) b[offset] << 56;
    }

    public static byte[] int2byte(int n) {
        byte b[] = new byte[4];
        b[0] = (byte) (n >> 24);
        b[1] = (byte) (n >> 16);
        b[2] = (byte) (n >> 8);
        b[3] = (byte) n;
        return b;
    }

    /**
     * n 为待转数据，buf[]为转换后的数据，offset为buf[]中转换的起始点 转换后数据从低到高位
     */
    public static void int2byte(int n, byte buf[], int offset) {
        buf[offset] = (byte) (n >> 24);
        buf[offset + 1] = (byte) (n >> 16);
        buf[offset + 2] = (byte) (n >> 8);
        buf[offset + 3] = (byte) n;
    }

    public static byte[] short2byte(int n) {
        byte b[] = new byte[2];
        b[0] = (byte) (n >> 8);
        b[1] = (byte) n;
        return b;
    }

    public static void short2byte(int n, byte buf[], int offset) {
        buf[offset] = (byte) (n >> 8);
        buf[offset + 1] = (byte) n;
    }

    public static byte[] long2byte(long n) {
        byte b[] = new byte[8];
        b[0] = (byte) (int) (n >> 56);
        b[1] = (byte) (int) (n >> 48);
        b[2] = (byte) (int) (n >> 40);
        b[3] = (byte) (int) (n >> 32);
        b[4] = (byte) (int) (n >> 24);
        b[5] = (byte) (int) (n >> 16);
        b[6] = (byte) (int) (n >> 8);
        b[7] = (byte) (int) n;
        return b;
    }

    public static void long2byte(long n, byte buf[], int offset) {
        buf[offset] = (byte) (int) (n >> 56);
        buf[offset + 1] = (byte) (int) (n >> 48);
        buf[offset + 2] = (byte) (int) (n >> 40);
        buf[offset + 3] = (byte) (int) (n >> 32);
        buf[offset + 4] = (byte) (int) (n >> 24);
        buf[offset + 5] = (byte) (int) (n >> 16);
        buf[offset + 6] = (byte) (int) (n >> 8);
        buf[offset + 7] = (byte) (int) n;
    }

    public static String bytes2string(byte[] buf, int start, int len,
                                      StringCodeType code) throws UnsupportedEncodingException {
        byte[] tmp = new byte[len];

        System.arraycopy(buf, start, tmp, 0, len);

        return bytes2string(tmp, code);
    }

    /**
     * @param src
     * @param dest  目标数组
     * @param start 复制到目标数组的起始点
     * @param len   要复杂的数据长度
     * @将一个字条串复杂到目标bytes数组中去
     */
    public static void String2bytesBuf(String src, byte[] dest, int start,
                                       int len) {
        if (src.length() > len)
            System.arraycopy(src.getBytes(), 0, dest, start, len);
        else
            System.arraycopy(src.getBytes(), 0, dest, start,
                    src.getBytes().length);
    }

    public static String bytes2string(byte[] buf, StringCodeType code)
            throws UnsupportedEncodingException {
        String str = null;
        switch (code) {
            case ENCODING_ASCII: {
                char[] text = new char[buf.length];
                for (int i = 0; i < buf.length; i++) {
                    text[i] = (char) buf[i];
                }
                str = new String(text);
                break;
            }
            case ENCODING_GB: {

                str = new String(buf, "GB2312");

                break;
            }
            case ENCODING_UTF8: {

                str = new String(buf, "UTF-8");

                break;
            }
            case ENCODING_UTF16: {

                str = new String(buf, "UTF-16");

                break;
            }
            case ENCODING_BINARY:
            case ENCODING_UNKNOWN:
            default: {
                str = null;
                break;
            }
        }
        return str;
    }

    /**
     * s验证短信内容和手机号码是否合法
     *
     * @param mobile       :手机号码
     * @param content:短信内容
     * @return string
     */
    public static String validateSms(String mobile, String content) {
        if (null == mobile || "" == mobile)
            return "-106"; // 手机号不能为空
        if (",".equals(mobile.charAt(mobile.length() - 1))
                || ",".equals(mobile.charAt(0)))
            return "-107"; // 群发，第一位和最后一位不能是逗号		//格式不正确
        String[] mobiles = mobile.split(",");
        if (mobiles.length > 1) {
            Set<String> set = new HashSet<String>();
            for (String mob : mobiles) {
                set.add(mob);
            }
            if (set.size() != mobiles.length)
                return "-108";        //有重复号码
        }
        if (mobiles.length > 100)
            return "-109"; // 手机号超限

        for (String mob : mobiles) {
            boolean flag = true;
            flag = isMobileNO(mob);
            if (false == flag)
                return "-110"; // 手机号有误
        }
        if (null == content || "" == content)
            return "-111"; // 短信内容不能为空
        if (content.length() > 400)
            return "-112"; // 短信内容超长
        return null;
    }

    /**
     * 手机号码验证
     *
     * @param mobiles
     * @return
     */
    private static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private static HashMap<String, List<String>> findMobileType(String mobiles) {
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
        hashMap.put("mobile", new ArrayList<String>());
        hashMap.put("unicom", new ArrayList<String>());
        hashMap.put("telecom", new ArrayList<String>());
        String[] array = mobiles.split(",");
        if (array.length > 0) {
            for (String mobStr : array) {
                boolean flag = false;
                for (String moblesNum : mobileArray) {
                    if (mobStr.startsWith(moblesNum)) {
                        hashMap.get("mobile").add(mobStr);
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                for (String unicomNum : unicomArray) {
                    if (mobStr.startsWith(unicomNum)) {
                        hashMap.get("unicom").add(mobStr);
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                for (String telcomNum : telcomArray) {
                    if (mobStr.startsWith(telcomNum)) {
                        hashMap.get("telecom").add(mobStr);
                        break;
                    }
                }
            }
        }
        return hashMap;
    }

//    public static HashMap<String, List<String>> getSplitPhone(String opreation, String mobileStr) {
//        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
//        map.put("mainChannel", new ArrayList<String>());
//        map.put("subChannel", new ArrayList<String>());
//        HashMap<String, List<String>> phoneMap = findMobileType(mobileStr);
//        String[] opreArray = opreation.split(",");
//        boolean mobleFlag = false;
//        boolean unicomFlag = false;
//        boolean telcomFlag = false;
//        for (String id : opreArray) {
//            if (id.equalsIgnoreCase("1") && phoneMap.get("mobile").size()>0) {
//                map.get("mainChannel").addAll(phoneMap.get("mobile"));
//                mobleFlag = true;
//            } else if (id.equalsIgnoreCase("2") && phoneMap.get("unicom").size()>0) {
//                map.get("mainChannel").addAll(phoneMap.get("unicom"));
//                unicomFlag = true;
//            } else if (id.equalsIgnoreCase("3") && phoneMap.get("telcom").size()>0) {
//                map.get("mainChannel").addAll(phoneMap.get("telcom"));
//                telcomFlag = true;
//            }
//        }
//        if (!mobleFlag) {
//            map.get("subChannel").addAll(phoneMap.get("mobile"));
//        }
//        if (!unicomFlag) {
//            map.get("subChannel").addAll(phoneMap.get("unicom"));
//        }
//        if (!telcomFlag) {
//            map.get("subChannel").addAll(phoneMap.get("telecom"));
//        }
//        return map;
//    }

    public static HashMap<String, List<String>> getSplitPhone(String mobileStr) {
        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        HashMap<String, List<String>> phoneMap = findMobileType(mobileStr);
        Set<String> keySet = phoneMap.keySet();
        for (String key : keySet) {
            if (phoneMap.get(key) != null && phoneMap.get(key).size() > 0) {
                map.put(key, phoneMap.get(key));
            }
        }
        return map;
    }

    public static String getPhoneString(List<String> list) {
        String phone = "";
        for (String str : list) {
            if (phone.length() == 0) {
                phone = str;
            } else {
                phone = phone + "," + str;
            }
        }
        return phone;
    }
    
    
    public static InputStream getStringStream(String sInputString) {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
        }
        return tInputStringStream;
    }

    public static void main(String[] args){
        System.out.println(getMD5("王琰"));
    }
}

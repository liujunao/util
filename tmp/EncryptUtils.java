package com.east.common.util;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: EastDayWD
 * Date: 13-10-23
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
// Referenced classes of package com.whty.aam:
//			Base64

public class EncryptUtils {

    public static String UC_KEY = "12345";

    private static EncryptUtils instance = new EncryptUtils();

    private EncryptUtils() {
    }

    public static EncryptUtils getInstance() {
        return instance;
    }

    private String md5(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return byte2hex(md.digest(input.getBytes()));
    }


    private String base64_decode(String input) {
        try {

            return new String(Base64.decode(input.toCharArray()), "iso-8859-1");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String base64_encode(String input) {
        try {
            return new String(Base64.encode(input.getBytes("iso-8859-1")));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString();
    }

    private String substr(String input, int begin, int length) {
        return input.substring(begin, begin + length);
    }

    private String substr(String input, int begin) {
        if (begin > 0) {
            return input.substring(begin);
        } else {
            return input.substring(input.length() + begin);
        }
    }


    private String sprintf(String format, long input) {
        String temp = "0000000000" + input;
        return temp.substring(temp.length() - 10);
    }

    /**
     * 加密
     *
     * @param string
     * @param key:密钥
     * @return
     */
    public String encode(String string, String key) {
        return uc_authcode(string, "ENCODE", key);
    }

    /**
     * 解密
     *
     * @param string
     * @param key:密钥
     * @return
     */
    public String decode(String string, String key) {
        if(string!=null && string.length()>0)
            return uc_authcode(string, "DECODE", key);
        else
            return "";
    }

    private String uc_authcode(String string, String operation, String key) {
        return uc_authcode(string, operation, key, 0);
    }

    /**
     * 字符串加密以及解密函数
     *
     * @param string
     *            string 原文或者密文
     * @param string
     *            operation 操作(ENCODE | DECODE), 默认为 DECODE
     * @param string
     *            key 密钥
     * @return string 处理后的 原文或者 经过 base64_encode 处理后的密文
     *
     * @example
     *
     * a = authcode('abc', 'ENCODE', 'key'); b = authcode( a, 'DECODE', 'key'); //
     * b(abc)
     *
     * a = authcode('abc', 'ENCODE', 'key', 3600); b = authcode('abc', 'DECODE',
     * 'key'); // 在一个小时内， b(abc)，否则 b 为空
     */
    private String uc_authcode(String string, String operation, String key,
                               int expiry) {

        // ckey_length 随机密钥长度 取值 0-32;
        // ckey_length 加入随机密钥，可以令密文无任何规律，即便是原文和密钥完全相同，加密结果也会每次不同，增大破解难度。
        // ckey_length 取值越大，密文变动规律越大，密文变化 = 16 的 ckey_length 次方
        // ckey_length 当此值为 0 时，则不产生随机密钥
        int ckey_length = 1;

        key = md5(key != null ? key : UC_KEY);
        String keya = md5(substr(key, 0, 16));
        String keyb = md5(substr(key, 16, 16));
        // String keyc = ckey_length > 0? ( operation.equals("DECODE") ? substr(
        // string, 0, ckey_length): substr(md5(microtime()), - ckey_length)) :
        // "";
        String keyc = "a";

        String cryptkey = keya + md5(keya + keyc);
        int key_length = cryptkey.length();

        string = operation.equals("DECODE") ? base64_decode(substr(string,
                ckey_length)) : sprintf("%010d", expiry > 0 ? expiry : 0)
                + substr(md5(string + keyb), 0, 16) + string;
        int string_length = string.length();

        StringBuffer result1 = new StringBuffer();

        int[] box = new int[256];
        for (int i = 0; i < 256; i++) {
            box[i] = i;
        }

        int[] rndkey = new int[256];
        for (int i = 0; i <= 255; i++) {
            rndkey[i] = (int) cryptkey.charAt(i % key_length);
        }

        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + box[i] + rndkey[i]) % 256;
            int tmp = box[i];
            box[i] = box[j];
            box[j] = tmp;
        }

        j = 0;
        int a = 0;
        for (int i = 0; i < string_length; i++) {
            a = (a + 1) % 256;
            j = (j + box[a]) % 256;
            int tmp = box[a];
            box[a] = box[j];
            box[j] = tmp;

            result1
                    .append((char) (((int) string.charAt(i)) ^ (box[(box[a] + box[j]) % 256])));

        }

        if (operation.equals("DECODE")) {
            String result = result1.substring(0, result1.length());
            if ((Integer.parseInt(substr(result.toString(), 0, 10)) == 0 || Long
                    .parseLong(substr(result.toString(), 0, 10)) > 0)
                    && substr(result.toString(), 10, 16).equals(
                    substr(md5(substr(result.toString(), 26) + keyb),
                            0, 16))) {
                return substr(result.toString(), 26);
            } else {
                return "";
            }
        } else {
            return keyc + base64_encode(result1.toString()).replaceAll("=", "");
        }
    }

    // 加密
    public static String jiami(String message,String key) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("MD5=");
        sb.append(getMD5(message));
        sb.append("%");
        sb.append(message);
        return new BASE64Encoder().encode((encrypt(sb.toString(), key)));
    }

    public static String getMD5(String sourceStr) throws UnsupportedEncodingException {
        String resultStr = "";
        try {
            byte[] temp = sourceStr.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(temp);
            // resultStr = new String(md5.digest());
            byte[] b = md5.digest();
            for (int i = 0; i < b.length; i++) {
                char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                        '9', 'A', 'B', 'C', 'D', 'E', 'F' };
                char[] ob = new char[2];
                ob[0] = digit[(b[i] >>> 4) & 0X0F];
                ob[1] = digit[b[i] & 0X0F];
                resultStr += new String(ob);
            }
            return resultStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }
}

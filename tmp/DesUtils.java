package common;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DesUtils {

    private static final String ENCODE = "UTF-8";
    private static final String KEY = "we_share";

    //根据键值进行加密
    public String encrypt(String data) throws Exception {
        byte[] bytes = encrypt(data.getBytes(ENCODE), KEY.getBytes(ENCODE));
        String string = new BASE64Encoder().encode(bytes);
        return string;
    }

    private byte[] encrypt(byte[] data, byte[] key) throws Exception {
        //生成一个可信任的随机数源
        SecureRandom secureRandom = new SecureRandom();
        //从原始密钥数据创建 DESKeySpec 对象
        DESKeySpec desKeySpec = new DESKeySpec(key);
        //从创建有一个密钥工厂，然后用它把 DESKeySpec 转换成 SecretKey 对象
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        //从 Cipher 对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        //从密钥初始化 Cipher 对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);

        return cipher.doFinal(data);
    }

    //根据键值进行解密
    public String decrypt(String data) throws Exception {
        if (data == null) {
            return null;
        }
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] bytes = base64Decoder.decodeBuffer(data);
        byte[] bytes1 = encrypt(bytes, KEY.getBytes(ENCODE));

        return new String(bytes1, ENCODE);
    }
}

package common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author lenovo
 * @date 2018/7/2
 */
public class JWTUtils {

    private final static String key = "we_share";
    private final static long keep_time = 7 * 24 * 60 * 60 * 1000;//设置登录过期时间为 一周

    /**
     * 获取密钥
     *
     * @return
     */
    private static Key getKeyInstance() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    /**
     * 生成 jwt
     *
     * @param id
     * @param subject
     * @return
     */
    public static String createJWT(String id, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder() //设置jwt的body
                .setId(id)//设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)//jwt的签发时间
                .setSubject(subject)//代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串
                .signWith(SignatureAlgorithm.HS256, getKeyInstance());//设置签名使用的签名算法和签名使用的秘钥
        long ttMillis = keep_time;
        if (ttMillis >= 0) {
            long expMillis = nowMillis + ttMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);//设置过期时间
        }
        return builder.compact();
    }

    /**
     * 解析 jwt
     *
     * @param jwt
     * @return
     */
    public static Claims verifyJWT(String jwt) {
        Claims jwtClaims = Jwts.parser()//得到DefaultJwtParser
                .setSigningKey(getKeyInstance())//设置签名的秘钥
                .parseClaimsJws(jwt).getBody();////设置需要解析的jwt
        return jwtClaims;
    }

    /**
     * jwt过期时，重新生成新的 jwt
     *
     * @param token
     * @return
     */
    public String updateJWT(String token) {
        try {
            Claims claims = verifyJWT(token);
            String id = claims.getId();
            String subject = claims.getSubject();
            return createJWT(id, subject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0";
    }

}

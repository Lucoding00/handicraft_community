package com.whut.springbootshiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

import static com.whut.springbootshiro.common.Constant.EXPIRED_TIME;

/**
 * JWT工具类
 * @author melo、lh
 * @createTime 2019-10-21 13:38:52
 */

public class JWTUtil {


    /**
     * 生成签名
     * @param username 用户名
     * @param secret 密码
     * @return java.lang.String
     * @author melo、lh
     * @createTime 2019-10-21 13:41:13
     */
    public static  String sign(Integer userId , String username,String secret){
        try{
        // 过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRED_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("userId", userId)
                    .withClaim("username", username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

     /**
      * 获得token中的信息无需secret解密也能获得
      * @param token 密匙
      * @return java.lang.String
      * @author melo、lh
      * @createTime 2019-10-21 13:45:46
      */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    /**
     * @Author Lei
     * @Description 更具用户的id，来获取更多的信息
     * @Date 9:14 2022/1/6
     * @Param [token]
     * @return java.lang.String
     **/
    public static Integer getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asInt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    
    /**
     * @Author Lei
     * @Description 获取过期时间
     * @Date 20:17 2022/1/7
     * @Param [token]
     * @return java.util.Date
     **/
    public static Date getExpTime(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> claims = jwt.getClaims();
            Claim exp = claims.get("exp");
            Date date = exp.asDate();
            return date;
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    

    /**
     * 校验token是否正确
     * @param token  密钥
     * @param username 用户名
     * @param secret 密钥
     * @return boolean
     * @author melo、lh
     * @createTime 2019-10-21 13:50:21
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // JWT签名头  algorithm 加密类型
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}

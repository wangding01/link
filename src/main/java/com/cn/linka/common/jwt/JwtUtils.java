package com.cn.linka.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cn.linka.business.dao.User;
import com.cn.linka.common.exception.BusException;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

/**
 * @author admin
 */
public class JwtUtils {
    private static final String secret = "secret";//将sign设置成全局变量
    /**
     * 获取token
     * @param u user
     * @return token
     */
    public static String getToken(User u) {

        Calendar instance = Calendar.getInstance();
        //默认令牌过期时间7天
        instance.add(Calendar.DATE, 7);
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("userId", u.getUserId())
                .withClaim("username", u.getUserName());
        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 验证token合法性 成功返回token
     */
    public static DecodedJWT verify(String token) throws Exception {
        if(StringUtils.isEmpty(token)){
            throw new BusException("token不能为空");
        }
        //获取登录用户真正的密码假如数据库查出来的是123456
        JWTVerifier build = JWT.require(Algorithm.HMAC256(secret)).build();
        return build.verify(token);
    }

    public static User getUserByToken(String token){
        JWTVerifier build = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT verify = build.verify(token);
        String userName = verify.getClaim("userName").asString();
        String userId = verify.getClaim("userId").asString();
        return User.builder().userId(userId)
                .userName(userName)
                .build();
    }

}


package com.cn.linka.common.jwt;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 */
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)){
            throw new BusException(BusinessExceptionEnum.AUTHORIZE_IS_NOT_NULL);
        }
        try {
            DecodedJWT verify = JwtUtils.verify(token.substring(7));
        } catch (SignatureVerificationException e) {
            log.error("无效签名！ 错误 ->", e);
            throw new BusException(BusinessExceptionEnum.AUTHORIZE_VERIFY_FAIL);
        } catch (TokenExpiredException e) {
            log.error("token过期！ 错误 ->", e);
            throw new BusException(BusinessExceptionEnum.AUTHORIZE_VERIFY_FAIL);
        } catch (AlgorithmMismatchException e) {
            log.error("token算法不一致！ 错误 ->", e);
            throw new BusException(BusinessExceptionEnum.AUTHORIZE_VERIFY_FAIL);
        } catch (Exception e) {
            log.error("token无效！ 错误 ->", e);
            throw new BusException(BusinessExceptionEnum.AUTHORIZE_VERIFY_FAIL);
        }
        return true;
    }
}


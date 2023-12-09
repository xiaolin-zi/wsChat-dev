package com.lxg.wschat.filter;


import com.lxg.wschat.exceptionhandler.LoginException;
import com.lxg.wschat.utils.JwtUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>
 * 全局Filter，统一处理会员登录与外部不允许访问的服务
 * </p>
 *
 * @author qy
 * @since 2019-11-21
 */
@Component
public class AuthGlobalFilter implements HandlerInterceptor {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer requestURL = request.getRequestURL();
        String path = requestURL.toString();
        if(antPathMatcher.match("/ws/**/auth/**", path)) {
            String token = String.valueOf(request.getHeaders("token"));
            if (token == null) {
                throw new LoginException(10000, "未登录");
            } else {
                Boolean isCheck = JwtUtils.checkToken(token);
                if (!isCheck) {
                    throw new LoginException(10000, "登录过期");
                }
            }
        }
        return true;

    }

}

/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.config;


import cn.hutool.core.util.CharUtil;
import com.lxg.wschat.socket.WebSocketServerToken;
import com.lxg.wschat.utils.CookieUtil;
import com.lxg.wschat.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

import static com.lxg.wschat.utils.JwtUtils.APP_SECRET;


/**
 * WebSocketConfigToken
 *
 * @author linxugeng
 * @since 2023/12/8
 */

@Configuration
public class WebSocketConfigToken extends ServerEndpointConfig.Configurator {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfigToken.class);


    /**
     * 这个bean会自动注册使用了@ServerEndpoint注解声明的对象
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 这个userProperties 可以通过 session.getUserProperties()获取
        final Map<String, Object> userProperties = sec.getUserProperties();
        log.info("这是在握手阶段，建立连接之前");
        // 获取token

        //有携带cookie时
//        Map<String, List<String>> headers = request.getHeaders();
//        log.info("当前请求的headers为:{}", headers);
//        List<String> cookie = headers.get("cookie");
//        log.info("当前请求的cookie为:{}", cookie);
//        String token = "";
//        if (cookie != null) {
//            log.info("当前请求的cookie.get(0):{}", cookie.get(0));
//            token = CookieUtil.getCookie("ws_token", cookie.get(0));
//        }


        //有携带token时
        //获取请求头
        Map<String, List<String>> headers = request.getHeaders();
        log.info("当前请求的headers为:{}", headers);
        List<String> list = headers.get("Sec-WebSocket-Protocol");
        log.info("当前请求的Sec-WebSocket-Protocol为:{}", list);
        String token = "";
        if (list != null) {
            log.info("当前请求的Sec-WebSocket-Protocol.get(0):{}", list.get(0));
            token = list.get(0);
        }
        log.info("当前请求的token为:{}", token);

        //当Sec-WebSocket-Protocol请求头不为空时,需要返回给前端相同的响应
        response.getHeaders().put("Sec-WebSocket-Protocol",list);

        /**
         *获取请求头后的逻辑处理
         */
        log.info("当前用户的token为:{}", token);
        // 判断用户token是否合法，并获取用户id，入果非法，生成一个临时用户用于识别
        String id = "";
        try {
            boolean b = JwtUtils.checkToken(token);
            if(!b) {
                throw new Exception();
            }
            id = JwtUtils.getMemberIdByToken(token);
            userProperties.put("id", id);
        } catch (Exception err) {
            id = "未知用户" + System.currentTimeMillis();
            userProperties.put("unknownId", id);
        }
        log.info("当前用户的id为:{}", id);
        super.modifyHandshake(sec, request, response);
    }

    /**
     * 初始化端点对象,也就是被@ServerEndpoint所标注的对象
     */
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return super.getEndpointInstance(clazz);
    }

}

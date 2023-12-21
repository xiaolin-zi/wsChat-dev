/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.socket;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lxg.wschat.config.WebSocketConfigToken;
import com.lxg.wschat.service.GroupService;
import com.lxg.wschat.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketServerToken
 *
 * @author linxugeng
 * @since 2023/12/8
 */
@ServerEndpoint(value = "/ws",configurator = WebSocketConfigToken.class)
@Component
public class WebSocketServerToken {




//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;

    private RedisTemplate<String,Object> redisTemplate = SpringUtils.getBean("myRedisTemplate");

    /**
     * 打印日志
     */
    private static final Logger log = LoggerFactory.getLogger(WebSocketServerToken.class);

    /**
     * 记录当前在线连接数
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("有新连接正在加入，sessionID为：{}", session.getId());
        // 判断是否合法连接，如果不是，直接执行session.close()关闭连接
        final boolean isverify = openVerify(session);
        if (isverify) {
            // 获取用户账号
            String id = (String) session.getUserProperties().get("id");
            // 根据账号添加到session列表中
            sessionMap.put(id, session);
            log.info("用户ID为={}加入连接, 当前在线人数为：{}", id, sessionMap.size());

        } else {
            // 非法连接，进行释放
            log.info("非法连接，sessionID为：{}，已释放", session.getId());
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 后台收到客户端发送过来的消息
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 获取ID
        String id = (String) session.getUserProperties().get("id");
        log.info("服务端收到来自用户ID为={}的消息:{}", id, message);

        // 将JSON数据转换为对象，方便操作
        JSONObject obj = JSONUtil.parseObj(message);
        /**
         *  // 发送消息格式
         *         let obj = JSON.stringify({
         *           sendId: this.userInfo.id,
         *           acceptId: this.acceptUser.userId,
         *           sendAvatar: this.userInfo.avatar,
         *           sendNickname: this.userInfo.nickname,
         *           contentType: contentType,
         *           content: message,
         *           sendTime: moment().format('YYYY-MM-DD HH:mm:ss')
         *         })
         */
        // 储存需要发送的对象
        JSONArray users = new JSONArray();

        // 判断是否为群聊，1为否，2为是
        if (obj.getStr("type").equals("1")) {
            //判断是否是心跳或系统通知
            //"chat_"+id+"to_"+users.get(i)给每个用户都存储一份聊天记录
            if(!obj.getStr("contentType").equals("ping")&&!obj.getStr("contentType").equals("system")){
                redisTemplate.opsForList().leftPush("chat_"+obj.getStr("sendId")+"to_"+obj.getStr("acceptId"),message);
                log.info("chat_"+id+"to_"+obj.getStr("acceptId")+"的消息已存入redis");
                users.add(obj.getStr("sendId"));
                users.add(obj.getStr("acceptId"));
            }else {
                //如果是系统通知
                users.add(obj.getStr("acceptId"));
            }


        } else if (obj.getStr("type").equals("2")) {
            //将消息存储到redis中
            //如果是群聊
            //只保存一份
            redisTemplate.opsForList().leftPush("chatGroup_"+obj.getStr("acceptId"),message);
            log.info("chatGroup_"+obj.getStr("acceptId")+"的消息已存入redis");
            users = obj.getJSONArray("acceptMember");
        }




        // 判断是否存在发送对象
        if (users.size() > 0) {
            for (int i=0;i<users.size();i++){
                Session toSession = sessionMap.get(users.get(i));
                if (toSession != null) {
                    this.sendMessage(obj.toString(), toSession);
                    if(!obj.getStr("contentType").equals("ping")){
                        log.info("服务器发送消息给用户ID为={}，消息：{}", users.get(i), obj.toString());
                    }
                } else {
                    log.info("发送失败，用户ID为={}未在线", users.get(i));
                }
            }
            log.info("消息发送完毕！");
        } else {
            log.info("发送对象集合不能为空！");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        // 判断断开的连接是否是合法的
        String id = (String) session.getUserProperties().get("id");
        if (id != "" & id != null) {
            sessionMap.remove(id);
            log.info("有一连接正常关闭，移除username={}的用户session, 当前在线人数为：{}", id, sessionMap.size());
        } else {
            id = (String) session.getUserProperties().get("unknownId");
            sessionMap.remove(id);
            log.info("token验证不通过，移除username={}的用户session, 当前在线人数为：{}", id, sessionMap.size());
        }
    }

    /**
     * 异常报错
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket发生异常错误:");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }


    /**
     * 判断是否是合法用户。是就返回true，反之返回false
     * @param session
     * @return
     */
    public static boolean openVerify (Session session) {
        // 判断是否有合法的id
        final String id = (String) session.getUserProperties().get("id");
        if (id == "" | id == null) {
            return false;
        } else {
            return true;
        }

    }

}

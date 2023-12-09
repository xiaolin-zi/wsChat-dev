//package com.lxg.wschat.socket;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.util.DateUtils;
//
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
// */
//@ServerEndpoint(value = "/chat/{uid}", configurator = MySpringConfigurator.class)
//@Component
//public class WebSocketServer {
//    private static Session session;
//
//    private static String getUid(Session session){
//        WebSocketServer.session =session;
//        Map parem=session.getPathParameters();
//        String uid=parem.get("uid").toString();
//        return uid;
//    }
//
//    /**
//     * 打印日志
//     */
//    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
//
//    /**
//     * 记录当前在线连接数
//     */
//    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
//
//
//
//    //打开连接执行
//    @OnOpen
//    public void onOpw(Session session) {
//        // 判断是否合法连接，如果不是，直接执行session.close()关闭连接
//        final boolean isverify = openVerify(session);
//        if (isverify) {
//            // 获取用户账号
////            String id = (String) session.getUserProperties().get("id");
//            String id = getUid(session);
//            session.getUserProperties().put("id", id);
//            // 根据账号添加到session列表中
//            sessionMap.put(id, session);
//            log.info("用户ID为={}加入连接, 当前在线人数为：{}", id, sessionMap.size());
//
//            //将当前在线用户的id发送给所有人
//            for (String key : sessionMap.keySet()) {
//
//
//                sendMessage("["+id+"]   进入聊天室"+ DateUtil.date().toString()+"当前在线用户："+sessionMap.keySet(),sessionMap.get(key));
//            }
//
//
//
//        } else {
//            // 非法连接，进行释放
//            try {
//                session.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }
//    @OnMessage//收到消息执行
//    public void onMessage(String message,Session session) {
//        // 获取ID
////        String id = (String) session.getUserProperties().get("id");
//        String id = getUid(session);
//        log.info("服务端收到来自用户ID为={}的消息:{}", id, message);
//
//        // 将JSON数据转换为对象，方便操作
//        JSONObject obj = JSONUtil.parseObj(message);
//
//
//        // 储存需要发送的对象
//        JSONArray users = new JSONArray();
//
//        // 判断是否为群聊，1为否，2为是
//        if (obj.getStr("type").equals("1")) {
//            users.add(obj.getStr("send_id"));
//            users.add(obj.getStr("accept_id"));
//        } else if (obj.getStr("type").equals("2")) {
//            users = obj.getJSONArray("accept_group");
//        }
//
//        // 判断是否存在发送对象
//        if (users.size() > 0) {
//            for (int i=0;i<users.size();i++){
//                Session toSession = sessionMap.get(users.get(i));
//                if (toSession != null) {
//                    this.sendMessage(obj.getStr("send_id")+"  ("+ DateUtil.date().toString()+")  说: "+obj.get("content").toString(), toSession);
//                    log.info("服务器发送消息给用户ID为={}，消息：{}", users.get(i), obj.toString());
//                } else {
//                    // 用户不在线，保存离线消息，待用户上线后发送
//
//                    log.info("发送失败，用户ID为={}未在线", users.get(i));
//                }
//            }
//        } else {
//            log.info("发送对象集合不能为空！");
//        }
//
//    }
//    @OnClose//关闭连接执行
//    public void onClose(Session session) {
//        // 判断断开的连接是否是合法的
////        String id = (String) session.getUserProperties().get("id");
//        String id = getUid(session);
//
//        if (id == "" & id == null) {
//            sessionMap.remove(id);
//            log.info("有一连接正常关闭，移除username={}的用户session, 当前在线人数为：{}", id, sessionMap.size());
//        } else {
////            id = (String) session.getUserProperties().get("unknownId");
//            id = getUid(session);
//            sessionMap.remove(id);
//            log.info("token验证不通过，移除username={}的用户session, 当前在线人数为：{}", id, sessionMap.size());
//        }
//
//    }
//    @OnError//连接错误的时候执行
//    public void onError(Throwable error,Session session) {
//        log.error("websocket发生异常错误:");
//        error.printStackTrace();
//
//    }
//
//
//
//    /**
//     * 发送消息
//     * @param message
//     */
//    public void sendMessage(String message, Session toSession) {
//
//        try {
//            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
//            toSession.getBasicRemote().sendText(message);
//        } catch (Exception e) {
//            log.error("服务端发送消息给客户端失败", e);
//        }
//    }
//
//    /**
//     * 判断是否是合法用户。是就返回true，反之返回false
//     * @param session
//     * @return
//     */
//    public static boolean openVerify (Session session) {
//        // 判断是否有合法的id
////        final String id = (String) session.getUserProperties().get("id");
//        final String id = getUid(session);
//        if (id == "" | id == null) {
//            return false;
//        } else {
//            return true;
//        }
//
//    }
//
//
//}

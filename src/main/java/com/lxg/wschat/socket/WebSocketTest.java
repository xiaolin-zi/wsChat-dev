package com.lxg.wschat.socket;//package com.lxg.testwebsocket.service;
//
//import com.lxg.testwebsocket.config.MySpringConfigurator;
//import com.lxg.testwebsocket.utils.DateUtils;
//
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.util.HashMap;
//import java.util.Map;
///**
// * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
// */
//@ServerEndpoint(value = "/chat/{uid}", configurator = MySpringConfigurator.class)
//@Component
//public class WebSocketTest {
//    private Session session;
//
//
//    private String getUid(Session session){
//        this.session=session;
//        Map parem=session.getPathParameters();
//        String uid=parem.get("uid").toString();
//        return uid;
//    }
//
//    /**
//     * 当前登录人的 session 管理器
//     */
//    private static Map<String,Session> sessionMessage=new HashMap<>();
//
//    @OnOpen//打开连接执行
//    public void onOpw(Session session) {
//       String uid=getUid(session);
//        sessionMessage.put(uid,session);
//        System.out.println("["+uid+"] 进入聊天室");
//        sendMessage("["+uid+"]   进入聊天室");
//    }
//    @OnMessage//收到消息执行
//    public void onMessage(String message,Session session) {
//        String uid=getUid(session);
//            sendMessage(uid+"  ("+ DateUtils.getDate()+")  说: "+message);
//    }
//    @OnClose//关闭连接执行
//    public void onClose(Session session) {
//        System.out.println(session.getId()+"关闭连接");
//    }
//    @OnError//连接错误的时候执行
//    public void onError(Throwable error,Session session) {
//        System.out.println("错误的时候执行");
//        error.printStackTrace();
//    }
//
//
//
//    /**
//     * 发送消息
//     * @param message
//     */
//    public void sendMessage(String message) {
//        for (String uid:sessionMessage.keySet()) {
//            try {
//                sessionMessage.get(uid).getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//            }
//        }
//    }
//
//
//}

//package com.lxg.wschat.config;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//
//@Configuration
//@ConditionalOnWebApplication
//public class WebSocketConfig  {
//
//    /**
//     * ServerEndpointExporter 用于扫描和注册所有携带 ServerEndpoint 注解的实例，
//     * @return
//     */
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
//
//
//    @Bean
//    public MySpringConfigurator mySpringConfigurator() {
//        return new MySpringConfigurator();
//    }
//}

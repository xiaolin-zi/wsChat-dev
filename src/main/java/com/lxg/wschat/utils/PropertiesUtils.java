package com.lxg.wschat.utils;

import org.springframework.beans.factory.InitializingBean;

/**
 * @auther xiaolin
 * @creatr 2023/5/13 22:00
 */
//@ConfigurationProperties(prefix = "aliyun.oss.file")
public class PropertiesUtils implements InitializingBean {

    //读取配置文件内容
    private String endpoint;

    private String keyid;

    private String keysecret;

    private String bucketname;

    //定义公开静态常量
    public static String END_POINT;

    public static String ACCESS_KEY_ID;

    public static String ACCESS_KEY_SECRET;

    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyid;
        ACCESS_KEY_SECRET = keysecret;
        BUCKET_NAME = bucketname;
    }
}

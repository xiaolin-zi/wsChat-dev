/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lxg.wschat.domain.Message;
import com.lxg.wschat.domain.MessageDetail;
import com.lxg.wschat.service.MessageService;
import com.lxg.wschat.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * RedisServiceImpl
 *
 * @author linxugeng
 * @since 2023/12/9
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<MessageDetail> getAllMessageDetail() {
        //群聊
        Set<String> chatGroup_ = getCacheKeys("chatGroup_");
        List<MessageDetail> result = new ArrayList<>();
        for(String s : chatGroup_){
            List<Object> range = redisTemplate.opsForList().range(s, 0, -1);
            List<MessageDetail> chatInGroup = transform(range);
            result.addAll(chatInGroup);
        }
        //个人
        Set<String> chat_ = getCacheKeys("chat_");
        for(String s : chat_){
            List<Object> range = redisTemplate.opsForList().range(s, 0, -1);
            List<MessageDetail> chatToUser = transform(range);
            result.addAll(chatToUser);
        }
        return result;
    }

   @Override
    public void removeRedis() {
        Set<String> chatGroup_ = getCacheKeys("chatGroup_");
        for(String s : chatGroup_){
            redisTemplate.delete(s);
        }
        Set<String> chat_ = getCacheKeys("chat_");
        for(String s : chat_){
            redisTemplate.delete(s);
        }
    }

    @Override
    public List<Message> getAllMessage() {

        List<Message> result = new ArrayList<>();
        //个人
        Set<String> chatMessage_ = getCacheKeys("chatMessage_");
        for(String s : chatMessage_){
            Object o = redisTemplate.opsForValue().get(s);
            // 将JSON数据转换为对象，方便操作
            JSONObject obj = JSONUtil.parseObj(o);
            Message message = new Message();
            message.setUserId(obj.getStr("userId"));
            message.setAcceptId(obj.getStr("acceptId"));
            message.setName(obj.getStr("name"));
            message.setAvatar(obj.getStr("avatar"));
            message.setType(Integer.valueOf(obj.getStr("type")));
            message.setLastMess(obj.getStr("lastMess"));
            message.setLastTime(obj.getStr("lastTime"));
            result.add(message);
        }

        //群聊
        Set<String> chatGroupMessage_ = getCacheKeys("chatGroupMessage_");
        for(String s : chatGroupMessage_){
            Object o = redisTemplate.opsForValue().get(s);
            // 将JSON数据转换为对象，方便操作
            JSONObject obj = JSONUtil.parseObj(o);
            Message message = new Message();
            message.setUserId(obj.getStr("userId"));
            message.setAcceptId(obj.getStr("acceptId"));
            message.setName(obj.getStr("name"));
            message.setAvatar(obj.getStr("avatar"));
            message.setType(Integer.valueOf(obj.getStr("type")));
            message.setLastMess(obj.getStr("lastMess"));
            message.setLastTime(obj.getStr("lastTime"));
            result.add(message);
        }
        return result;
    }

    @Override
    public void removeMessageRedis() {
        Set<String> chatMessage_ = getCacheKeys("chatMessage_");
        for(String s : chatMessage_){
            redisTemplate.delete(s);
        }
        Set<String> chatGroupMessage_ = getCacheKeys("chatGroupMessage_");
        for(String s : chatGroupMessage_){
            redisTemplate.delete(s);
        }
    }

    private List<Message> transformToMessage(List<Object> range){
        List<Message> userMessage = new ArrayList<>();
        for(Object s : range){
            // 将JSON数据转换为对象，方便操作
            JSONObject obj = JSONUtil.parseObj(s);
            Message message = new Message();
            message.setUserId(obj.getStr("userId"));
            message.setAcceptId(obj.getStr("acceptId"));
            message.setName(obj.getStr("name"));
            message.setAvatar(obj.getStr("avatar"));
            message.setType(Integer.valueOf(obj.getStr("type")));
            message.setLastMess(obj.getStr("lastMess"));
            message.setLastTime(obj.getStr("lastTime"));
            userMessage.add(message);
        }
        return userMessage;
    }

    //获取全部的keys
    public Set<String> getCacheKeys(String pattern) {
        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern + "*")
                .build();
        return redisTemplate.execute((connection) -> {
            Cursor<byte[]> scan = connection.scan(options);
            Set<String> keys = new HashSet<>();
            while (scan.hasNext()) {
                keys.add(new String(scan.next()));
            }
            return keys;
        }, true);
    }

    private List<MessageDetail> transform(List<Object> range){
        List<MessageDetail> userToOtherRedis = new ArrayList<>();
        for(Object s : range){
            // 将JSON数据转换为对象，方便操作
            JSONObject obj = JSONUtil.parseObj(s);
            MessageDetail messageDetail = new MessageDetail();
            messageDetail.setSendId(obj.getStr("sendId"));
            messageDetail.setAcceptId(obj.getStr("acceptId"));
            messageDetail.setContentType(obj.getStr("contentType"));
            messageDetail.setContent(obj.getStr("content"));
            messageDetail.setSendAvatar(obj.getStr("sendAvatar"));
            messageDetail.setSendNickname(obj.getStr("sendNickname"));
            messageDetail.setSendTime(obj.getStr("sendTime"));
            messageDetail.setType(Integer.valueOf(obj.getStr("type")));
            userToOtherRedis.add(messageDetail);
        }
        return userToOtherRedis;
    }
}

package com.lxg.wschat.service;

import com.lxg.wschat.domain.Message;
import com.lxg.wschat.domain.MessageDetail;

import java.util.List;

public interface RedisService {
    List<MessageDetail> getAllMessageDetail();


    void removeRedis();

    List<Message> getAllMessage();

    void removeMessageRedis();
}

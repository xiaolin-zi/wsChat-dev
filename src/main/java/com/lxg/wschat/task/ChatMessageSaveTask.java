package com.lxg.wschat.task;

import com.lxg.wschat.domain.Message;
import com.lxg.wschat.domain.MessageDetail;
import com.lxg.wschat.service.MessageDetailService;
import com.lxg.wschat.service.MessageService;
import com.lxg.wschat.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
@Slf4j
public class ChatMessageSaveTask {

    @Autowired
    private RedisService redisService;

    @Autowired
    private MessageDetailService messageDetailService;

    @Autowired
    private MessageService messageService;

    /**
     * 定时任务，每天凌晨3点进行redis缓存的持久化，将聊天记录保存到数据库
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void chatMessageSave() {
        log.info("【定时任务】 开始！");

        // 获取缓存中的聊天缓存
        List<MessageDetail> allMessageDetail = redisService.getAllMessageDetail();
        // 聊天记录持久化
        if (allMessageDetail != null) {
            try {
                messageDetailService.saveBatch(allMessageDetail);
                // 清空缓存
                redisService.removeRedis();
                log.info("【定时任务】 " + allMessageDetail.size() + "条聊天记录持久化到MySQL。");
                log.info("【定时任务】 " + allMessageDetail.size() + "条聊天记录缓存已清空。");
            } catch (Exception e) {
                log.error("【定时任务】 Redis出错！");
            }
        } else {
            log.info("【定时任务】 无新聊天记录持久化到MySQL。");
        }

    }

    /**
     * 定时任务，每天凌晨3点进行redis缓存的持久化，将会话记录保存到数据库
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void chatSessionSave() {
        log.info("【定时任务】 开始！");

        // 获取缓存中的会话缓存
        List<Message> allMessage= redisService.getAllMessage();
        // 会话记录持久化
        if (allMessage != null) {
            try {
                messageService.saveAll(allMessage);
                // 清空缓存
                redisService.removeMessageRedis();
                log.info("【定时任务】 " + allMessage.size() + "条会话记录持久化到MySQL。");
                log.info("【定时任务】 " + allMessage.size() + "条会话记录缓存已清空。");
            } catch (Exception e) {
                log.error("【定时任务】 Redis出错！",e);
//                log.error("【定时任务】 Redis出错！");
            }
        } else {
            log.info("【定时任务】 无新会话记录持久化到MySQL。");
        }

    }
}

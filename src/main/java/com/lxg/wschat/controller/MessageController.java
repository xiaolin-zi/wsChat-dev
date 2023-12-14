/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.controller;

import com.lxg.wschat.dto.MessageDTO;
import com.lxg.wschat.dto.MessageRedisDTO;
import com.lxg.wschat.service.MessageService;
import com.lxg.wschat.service.UserService;
import com.lxg.wschat.utils.R;
import com.lxg.wschat.vo.MessageDetialInfoVO;
import com.lxg.wschat.vo.MessageInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息操作控制类
 *
 * @author linxugeng
 * @since 2023/12/7
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * 获取会话记录列表
     */
    @GetMapping("/getMessageList")
    public R getMessageList(HttpServletRequest request) {
        List<MessageInfoVO> messageList = messageService.getMessageList(request);
        if (messageList == null) {
            return R.error().message("会话记录列表为空");
        }
        return R.ok().data("messageList", messageList);
    }


    /**
     * 新增会话记录到mysql
     *
     * @param messageDTO
     * @return
     */
    @PostMapping("/addmessage")
    public R addMessage(@Valid @RequestBody MessageDTO messageDTO) {
        boolean flag = messageService.addMessage(messageDTO);
        if (flag) {
            return R.ok().message("新增会话记录成功");
        }
        return R.error().message("新增会话记录失败");
    }

    /**
     * 新增会话记录到redis
     */
    @PostMapping("/saveMessageToRedis")
    public R addMessageToRedis(@Valid @RequestBody MessageRedisDTO messageRedisDTO) {
        boolean flag = messageService.addMessageToRedis(messageRedisDTO);
        if (flag) {
            return R.ok().message("新增会话记录到redis成功");
        }
        return R.error().message("新增会话记录到redis失败");
    }








    /**
     * 根据acceptId和type获取详细聊天记录
     */
    @GetMapping("/getChatRecords/{acceptId}/{type}")
    public R getChatRecord(HttpServletRequest request, @PathVariable String acceptId, @PathVariable Integer type) {
        List<MessageDetialInfoVO> chatRecord = messageService.getChatRecord(request, acceptId, type);
        if (chatRecord == null) {
            return R.error().message("聊天记录为空");
        }
        return R.ok().data("chatRecord", chatRecord);
    }


    /**
     * 获取所有会话列表中的详细聊天记录
     */
    @GetMapping("/getAllChatRecords")
    public R getAllChatRecords(HttpServletRequest request) {
        Map<String,List<MessageDetialInfoVO>> allChatRecords = messageService.getAllChatRecords(request);
        if (allChatRecords == null) {
            return R.error().message("聊天记录为空");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("allRecords", allChatRecords);
        map.put("user", userService.getUserInfo(request));
        return R.ok().data("map",map);
    }


}

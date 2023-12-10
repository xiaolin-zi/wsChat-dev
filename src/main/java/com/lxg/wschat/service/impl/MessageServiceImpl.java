package com.lxg.wschat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxg.wschat.domain.Group;
import com.lxg.wschat.domain.Message;
import com.lxg.wschat.domain.MessageDetail;
import com.lxg.wschat.dto.GetChatRecordsDTO;
import com.lxg.wschat.dto.MessageDTO;
import com.lxg.wschat.dto.MessageRedisDTO;
import com.lxg.wschat.mapper.GroupMapper;
import com.lxg.wschat.mapper.MessageDetailMapper;
import com.lxg.wschat.service.MessageDetailService;
import com.lxg.wschat.service.MessageService;
import com.lxg.wschat.mapper.MessageMapper;
import com.lxg.wschat.utils.JwtUtils;
import com.lxg.wschat.vo.MessageDetialInfoVO;
import com.lxg.wschat.vo.MessageInfoVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author linxugeng
* @description 针对表【t_message】的数据库操作Service实现
* @createDate 2023-12-06 22:19:18
*/
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageDetailMapper messageDetailMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private MessageDetailService messageDetailService;

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addMessage(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        int flag = messageMapper.insert(message);
        return flag > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MessageInfoVO> getMessageList(HttpServletRequest request) {
        //根据用户id获取会话记录列表
        String userId = JwtUtils.getMemberIdByRequest(request);
        //先查询缓存中的会话记录
        //获取全部的key
        Set<String> keys = getCacheKeys("chatMessage_"+userId+"to_");
        List<Message> userMessage = new ArrayList<>();
        for(String s : keys){
            String s1 = (String) redisTemplate.opsForValue().get(s);
            // 将JSON数据转换为对象，方便操作
            JSONObject obj = JSONUtil.parseObj(s1);
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
        log.info("redis中的会话记录为：{}",userMessage);

        List<MessageInfoVO> messageList = new ArrayList<>();
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Message> messages = messageMapper.selectList(queryWrapper);
        log.info("数据库中的会话记录为：{}",messages);

        messages.addAll(userMessage);

        for (Message message : messages) {
            MessageInfoVO messageInfoVO = new MessageInfoVO();
            BeanUtils.copyProperties(message, messageInfoVO);
            //使用hutools将时间格式化为只有月日
            messageList.add(messageInfoVO);
        }
        return messageList;
    }

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


    /**
     * 根据acceptId和type获取详细聊天记录
     * @param request
     * @param acceptId
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MessageDetialInfoVO> getChatRecord(HttpServletRequest request, String acceptId, Integer type) {
        //获取用户id
        String userId = JwtUtils.getMemberIdByRequest(request);
        //查询消息记录
        List<MessageDetail> messageDetails = getMessageDetails(userId, acceptId, type);
        //排序消息记录
        messageDetails.sort(Comparator.comparing(MessageDetail::getSendTime));
        //转换为VO
        List<MessageDetialInfoVO> messageDetialInfoVOS = new ArrayList<>();
        for (MessageDetail messageDetail : messageDetails) {
            MessageDetialInfoVO messageDetialInfoVO = new MessageDetialInfoVO();
            BeanUtils.copyProperties(messageDetail, messageDetialInfoVO);
            messageDetialInfoVOS.add(messageDetialInfoVO);
        }
        return messageDetialInfoVOS;
    }


    private List<MessageDetail> getMessageDetails(String userId, String acceptId, Integer type) {
        if (type == 1) {
            //查询用户给对方发的消息
            //先查询缓存中的聊天记录
            List<MessageDetail> userToOtherRedis = getChatRecordsInRedis(userId,acceptId);
            //再查数据库
            QueryWrapper<MessageDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("send_id", userId);
            queryWrapper.eq("accept_id", acceptId);
            List<MessageDetail> messageDetails = messageDetailMapper.selectList(queryWrapper);
            //查询对方给用户发的消息
            //先查redis
            List<MessageDetail> otherToUserRedis = getChatRecordsInRedis(acceptId,userId);
            //再查缓存
            QueryWrapper<MessageDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("send_id", acceptId);
            queryWrapper1.eq("accept_id", userId);
            List<MessageDetail> messageDetails2 = messageDetailMapper.selectList(queryWrapper1);

            //合并所有聊天记录
            List<MessageDetail> mergedList = new ArrayList<>();
            mergedList.addAll(userToOtherRedis);
            mergedList.addAll(messageDetails);
            mergedList.addAll(otherToUserRedis);
            mergedList.addAll(messageDetails2);
            return mergedList;
        } else {
            //因为是群聊，所以查询所有的消息
            //先查缓存
            List<Object> range = redisTemplate.opsForList().range("chatGroup_" + acceptId, 0, -1);
            List<MessageDetail> chatInGroup = transformToMessageDetail(range);

            QueryWrapper<MessageDetail> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("accept_id", acceptId);
            List<MessageDetail> messageDetails3 = messageDetailMapper.selectList(queryWrapper2);
            messageDetails3.addAll(chatInGroup);
            return messageDetails3;
        }

    }

    //查询缓存中的聊天记录
    private List<MessageDetail> getChatRecordsInRedis(String userId, String acceptId){
        //先查缓存
        List range = redisTemplate.opsForList().range("chat_" + userId + "to_" + acceptId, 0, -1);
        /**
         *  let obj = JSON.stringify({
         *           sendId: this.userInfo.id,1
         *           type: this.acceptUser.type,
         *           acceptId: this.acceptUser.userId,1
         *           acceptMember: ids,
         *           sendAvatar: this.userInfo.avatar,1
         *           sendNickname: this.userInfo.nickname,1
         *           contentType: contentType,1
         *           content: message,1
         *           sendTime: moment().format('YYYY-MM-DD HH:mm:ss')1
         *         })
         */
        return  transformToMessageDetail(range);

    }

    private List<MessageDetail> transformToMessageDetail(List<Object> range){
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
            userToOtherRedis.add(messageDetail);
        }
        return userToOtherRedis;
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String,List<MessageDetialInfoVO>> getAllChatRecords(HttpServletRequest request) {
        List<MessageInfoVO> messageList = getMessageList(request);//该用户的所有会话记录

        Map<String,List<MessageDetialInfoVO>> map = new HashMap<>();

        for(MessageInfoVO messageInfoVO : messageList){

            String acceptId = messageInfoVO.getAcceptId();
            Integer type = messageInfoVO.getType();

            List<MessageDetialInfoVO> chatRecord = getChatRecord(request, acceptId, type);
            map.put(acceptId,chatRecord);
        }
        return map;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addMessageToRedis(MessageRedisDTO messageRedisDTO) {

        //将会话记录存入redis
        //将会话记录转换为json字符串
        Message message = new Message();
        BeanUtils.copyProperties(messageRedisDTO, message);
        String messageStr = JSONUtil.toJsonStr(message);
        redisTemplate.opsForValue().set("chatMessage_"+message.getUserId()+"to_"+message.getAcceptId(),messageStr);
        return true;
    }

    @Override
    public void saveAll(List<Message> allMessage) {
        //需要判断，如果数据库已经存在该会话记录，则需要更新
        for(Message message : allMessage){
            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", message.getUserId());
            queryWrapper.eq("accept_id", message.getAcceptId());
            Message message1 = messageMapper.selectOne(queryWrapper);
            if(message1 != null){
                //更新
                messageMapper.update(message, queryWrapper);
            }else{
                //新增
                messageMapper.insert(message);
            }
        }
    }


}





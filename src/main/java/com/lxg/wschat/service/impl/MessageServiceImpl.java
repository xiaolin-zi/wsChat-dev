package com.lxg.wschat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxg.wschat.domain.Group;
import com.lxg.wschat.domain.Message;
import com.lxg.wschat.domain.MessageDetail;
import com.lxg.wschat.dto.GetChatRecordsDTO;
import com.lxg.wschat.dto.MessageDTO;
import com.lxg.wschat.mapper.GroupMapper;
import com.lxg.wschat.mapper.MessageDetailMapper;
import com.lxg.wschat.service.MessageService;
import com.lxg.wschat.mapper.MessageMapper;
import com.lxg.wschat.utils.JwtUtils;
import com.lxg.wschat.vo.MessageDetialInfoVO;
import com.lxg.wschat.vo.MessageInfoVO;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author linxugeng
* @description 针对表【t_message】的数据库操作Service实现
* @createDate 2023-12-06 22:19:18
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageDetailMapper messageDetailMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public boolean addMessage(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        int flag = messageMapper.insert(message);
        return flag > 0;
    }

    @Override
    public List<MessageInfoVO> getMessageList(HttpServletRequest request) {
        //根据用户id获取会话记录列表
        String userId = JwtUtils.getMemberIdByRequest(request);
        List<MessageInfoVO> messageList = new ArrayList<>();
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Message> messages = messageMapper.selectList(queryWrapper);
        for (Message message : messages) {
            MessageInfoVO messageInfoVO = new MessageInfoVO();
            BeanUtils.copyProperties(message, messageInfoVO);
            //使用hutools将时间格式化为只有月日
            Date updateTime = messageInfoVO.getUpdateTime();
            String dateStr = DateUtil.format(updateTime, "MM-dd");
            messageInfoVO.setLastTime(dateStr);
            messageList.add(messageInfoVO);
        }
        return messageList;
    }

    @Override
    public List<MessageDetialInfoVO> getChatRecord(HttpServletRequest request, String acceptId, Integer type) {
        //获取用户id
        String userId = JwtUtils.getMemberIdByRequest(request);
        //查询消息记录
        List<MessageDetail> messageDetails = getMessageDetails(userId, acceptId, type);
        //排序消息记录
        messageDetails.sort(Comparator.comparing(MessageDetail::getCreateTime));
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
            QueryWrapper<MessageDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("send_id", userId);
            queryWrapper.eq("accept_id", acceptId);
            List<MessageDetail> messageDetails = messageDetailMapper.selectList(queryWrapper);

            //查询对方给用户发的消息
            QueryWrapper<MessageDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("send_id", acceptId);
            queryWrapper1.eq("accept_id", userId);
            List<MessageDetail> messageDetails2 = messageDetailMapper.selectList(queryWrapper1);
            //合并两个集合
            messageDetails.addAll(messageDetails2);
            return messageDetails;
        } else {
            //因为是群聊，所以查询所有的消息
            QueryWrapper<MessageDetail> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("accept_id", acceptId);
            List<MessageDetail> messageDetails3 = messageDetailMapper.selectList(queryWrapper2);
            return messageDetails3;
        }

    }


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

}





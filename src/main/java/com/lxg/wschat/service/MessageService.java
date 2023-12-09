package com.lxg.wschat.service;

import com.lxg.wschat.domain.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxg.wschat.dto.GetChatRecordsDTO;
import com.lxg.wschat.dto.MessageDTO;
import com.lxg.wschat.vo.MessageDetialInfoVO;
import com.lxg.wschat.vo.MessageInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
* @author linxugeng
* @description 针对表【t_message】的数据库操作Service
* @createDate 2023-12-06 22:19:18
*/
public interface MessageService extends IService<Message> {

    boolean addMessage(MessageDTO messageDTO);

    List<MessageInfoVO> getMessageList(HttpServletRequest request);

    List<MessageDetialInfoVO> getChatRecord(HttpServletRequest request, String acceptId,Integer type);

    Map<String,List<MessageDetialInfoVO>> getAllChatRecords(HttpServletRequest request);
}

package com.lxg.wschat.mapper;

import com.lxg.wschat.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author linxugeng
* @description 针对表【t_message】的数据库操作Mapper
* @createDate 2023-12-06 22:19:18
* @Entity com.lxg.wschat.domain.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}





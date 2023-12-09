package com.lxg.wschat.mapper;

import com.lxg.wschat.domain.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxg.wschat.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author linxugeng
* @description 针对表【t_group】的数据库操作Mapper
* @createDate 2023-12-06 22:19:18
* @Entity com.lxg.wschat.domain.Group
*/
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}





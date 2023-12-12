package com.lxg.wschat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxg.wschat.domain.User;
import com.lxg.wschat.mahout.MahoutDataModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author linxugeng
 * @description 针对表【t_user】的数据库操作Mapper
 * @createDate 2023-12-05 12:41:44
 * @Entity com.lxg.testwebsocket.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {


}





package com.lxg.wschat.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lxg.wschat.domain.User;
import com.lxg.wschat.mahout.MahoutDataModel;
import com.lxg.wschat.vo.UserInfoVO;
import com.lxg.wschat.utils.R;
import com.lxg.wschat.dto.LoginByEmailForm;
import com.lxg.wschat.dto.LoginDTO;
import com.lxg.wschat.dto.RegisterDTO;
import com.lxg.wschat.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author linxugeng
 * @description 针对表【t_user】的数据库操作Service
 * @createDate 2023-12-05 12:41:44
 */
public interface UserService extends IService<User> {

    String login(LoginDTO loginDTO);

    R registerUser(RegisterDTO registerDTO);

    boolean sendMail(String email);

    R loginByCode(LoginByEmailForm form);

    UserInfoVO getUserInfo(HttpServletRequest request);

    boolean addUser(User user);

    boolean followUser(String userId,HttpServletRequest request);

    boolean unfollowUser(String userId, HttpServletRequest request);

    boolean updateUserInfo(UserDTO user, HttpServletRequest request);

    List<UserInfoVO> getFollowList(HttpServletRequest request);

    boolean joinGroup(String groupId, HttpServletRequest request);

    List<UserInfoVO> getFansList(HttpServletRequest request);

    List<UserInfoVO> getFriendList(HttpServletRequest request);


    List<MahoutDataModel> getDataModel();


    List<UserInfoVO> recommend(HttpServletRequest request);

    void importFlow(List<String> userIds, String userId);

    void importGroup(List<String> groupIds, String userId);


    boolean exitGroup(String groupId, HttpServletRequest request);
}

package com.lxg.wschat.controller;

import com.lxg.wschat.vo.UserInfoVO;
import com.lxg.wschat.service.UserService;
import com.lxg.wschat.utils.R;
import com.lxg.wschat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户操作控制类
 *
 * @author linxugeng
 * @date 2023/12/05 11:34
 **/
@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/getUserInfo")
    public R getUserInfo(HttpServletRequest request) {
        UserInfoVO user = userService.getUserInfo(request);
        if (user == null) {
            return R.error().message("用户不存在");
        }
        return R.ok().data("userInfo", user);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @param request
     * @return
     */
    @PutMapping("/updateUserInfo")
    public R updateUserInfo(@Valid @RequestBody UserDTO user, HttpServletRequest request) {
        boolean flag = userService.updateUserInfo(user, request);
        if (flag) {
            return R.ok().message("修改成功");
        }
        return R.error().message("修改失败");
    }


    /**
     * 关注用户
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/follow/{userId}")
    public R follow(@PathVariable String userId, HttpServletRequest request) {
        boolean flag = userService.followUser(userId, request);
        if (flag) {
            return R.ok().message("关注成功");
        }
        return R.error().message("关注失败");
    }


    /**
     * 取消关注
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/unfollow/{userId}")
    public R unfollow(@PathVariable String userId, HttpServletRequest request) {
        boolean flag = userService.unfollowUser(userId, request);
        if (flag) {
            return R.ok().message("取消关注成功");
        }
        return R.error().message("取消关注失败");
    }


    /**
     * 获取用户关注列表
     * @param request
     * @return
     */
    @GetMapping("/getFollowList")
    public R getFollowList(HttpServletRequest request) {
        List<UserInfoVO> list = userService.getFollowList(request);
        if (list != null) {
            return R.ok().data("list", list);
        }
        return R.error().message("关注列表为空");
    }


    /**
     * 加入群聊
     * @param groupId
     * @param request
     * @return
     */
    @PostMapping("/joinGroup/{groupId}")
    public R joinGroup(@PathVariable String groupId, HttpServletRequest request) {
        boolean flag = userService.joinGroup(groupId, request);
        if (flag) {
            return R.ok().message("加入群聊成功");
        }
        return R.error().message("加入群聊失败");
    }


    /**
     * 获取粉丝列表
     * @param request
     * @return
     */
    @GetMapping("/getFansList")
    public R getFansList(HttpServletRequest request) {
        List<UserInfoVO> list = userService.getFansList(request);
        if (list != null) {
            return R.ok().data("list", list);
        }
        return R.error().message("粉丝列表为空");
    }

    /**
     * 获取好友列表
     * @param request
     * @return
     */
    @GetMapping("/getFriendList")
    public R getFriendList(HttpServletRequest request) {
        List<UserInfoVO> list = userService.getFriendList(request);
        if (list != null) {
            return R.ok().data("list", list);
        }
        return R.error().message("好友列表为空");
    }


}

/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.controller;

import com.lxg.wschat.domain.Group;
import com.lxg.wschat.domain.User;
import com.lxg.wschat.mahout.MahoutDataModel;
import com.lxg.wschat.service.GroupService;
import com.lxg.wschat.utils.R;
import com.lxg.wschat.dto.AddGroupMemberDTO;
import com.lxg.wschat.dto.GroupDTO;
import com.lxg.wschat.vo.GroupInfoVO;
import com.lxg.wschat.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 群组操作控制类
 *
 * @author linxugeng
 * @since 2023/12/6
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;
    /**
     * 获取群组信息
     * @param gourpId
     * @return
     */
    @GetMapping("/getGroupInfo/{gourpId}")
    public R getGroupInfo(@PathVariable String gourpId) {
        Group group = groupService.getGroupInfo(gourpId);
        if(group == null) {
            return R.error().message("群聊不存在");
        }
        return R.ok().data("groupInfo", group);
    }

    /**
     * 创建群组
     * @param groupDTO
     * @param request
     * @return
     */
    @PostMapping("/createGroup")
    public R createGroup(@Valid @RequestBody GroupDTO groupDTO, HttpServletRequest request) {
        boolean flag = groupService.addGroup(groupDTO,request);
        if(flag) {
            return R.ok().message("创建成功");
        }
        return R.error().message("创建失败");
    }

    /**
     * 添加群成员
     * @param addGroupMemberDTO
     * @return
     */
    @PostMapping("/addGroupMember")
    public R addGroupMember(@RequestBody AddGroupMemberDTO addGroupMemberDTO) {
        boolean flag = groupService.addGroupMember(addGroupMemberDTO.getUserIds(), addGroupMemberDTO.getGroupId());
        if(flag) {
            return R.ok().message("添加成功");
        }
        return R.error().message("添加失败");
    }


    /**
     * 删除群成员
     */
    @DeleteMapping("/deleteGroupMember")
    public R deleteGroupMember(@RequestBody AddGroupMemberDTO addGroupMemberDTO, HttpServletRequest request) {
        boolean flag = groupService.deleteGroupMember(addGroupMemberDTO.getUserIds(), addGroupMemberDTO.getGroupId(),request);
        if(flag) {
            return R.ok().message("删除成功");
        }
        return R.error().message("删除失败");
    }


    /**
     * 获取群成员
     * @param groupId
     * @return
     */
    @GetMapping("/getGroupMember/{groupId}")
    public R getGroupMember(@PathVariable String groupId,HttpServletRequest request) {
        List<UserInfoVO> groupMember = groupService.getGroupMember(groupId,request);
        if (groupMember == null) {
            return R.error().message("群聊不存在");
        }
        return R.ok().data("groupMember", groupMember);
    }

    /**
     * 删除群组
     */
    @DeleteMapping("/deleteGroup/{groupId}")
    public R deleteGroup(@PathVariable String groupId,HttpServletRequest request) {
        boolean flag = groupService.removeGroup(groupId,request);
        if(flag) {
            return R.ok().message("删除成功");
        }
        return R.error().message("删除失败");
    }

    /**
     * 获取群组列表
     */
    @GetMapping("/getGroupList")
    public R getGroupList(HttpServletRequest request) {
        List<GroupInfoVO> groupList =  groupService.getGroupList(request);
        if(groupList == null) {
            return R.error().message("获取失败");
        }
        return R.ok().data("groupList", groupList);
    }


    /**
     * 获取群成员数量
     * @param groupId
     * @return
     */
    @GetMapping("/getGroupMemberCount/{groupId}")
    public R getGroupMemberCount(@PathVariable String groupId) {
        long count = groupService.getGroupMemberCount(groupId);
        return R.ok().data("count", count);
    }


    /**
     * 查询群聊得分情况记录
     */
    @GetMapping("/getGroupDataModel")
    public R getGroupScore() {
        List<MahoutDataModel> list = groupService.getGroupDataModel();
        if (list != null) {
            return R.ok().data("list", list);
        }
        return R.error().message("暂无记录");
    }


    /**
     * 推荐群聊
     */
    @GetMapping("/recommendGroup")
    public R recommendGroup(HttpServletRequest request) {
        List<GroupInfoVO> list = groupService.recommendGroup(request);
        if (list != null) {
            return R.ok().data("list", list);
        }
        return R.error().message("暂无记录");
    }
}

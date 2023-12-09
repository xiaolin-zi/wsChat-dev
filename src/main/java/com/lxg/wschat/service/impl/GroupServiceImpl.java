package com.lxg.wschat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxg.wschat.domain.Group;
import com.lxg.wschat.domain.User;
import com.lxg.wschat.domain.UserGroup;
import com.lxg.wschat.service.GroupService;
import com.lxg.wschat.mapper.GroupMapper;
import com.lxg.wschat.service.UserGroupService;
import com.lxg.wschat.service.UserService;
import com.lxg.wschat.utils.CreateGroupIdUtils;
import com.lxg.wschat.utils.JwtUtils;
import com.lxg.wschat.dto.GroupDTO;
import com.lxg.wschat.vo.GroupInfoVO;
import com.lxg.wschat.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linxugeng
 * @description 针对表【t_group】的数据库操作Service实现
 * @createDate 2023-12-06 22:19:18
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group>
        implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Group getGroupInfo(String gourpId) {
        return groupMapper.selectById(gourpId);
    }

    @Transactional
    @Override
    public boolean addGroup(GroupDTO groupDTO, HttpServletRequest request) {
        //获取当前用户id
        String userId = JwtUtils.getMemberIdByRequest(request);
        Group group = new Group();
        group.setId(CreateGroupIdUtils.createGroupId());
        group.setMasterId(userId);
        BeanUtils.copyProperties(groupDTO, group);
        int flag = groupMapper.insert(group);
        //添加群主为群成员
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(group.getId());
        userGroup.setUserId(userId);
        userGroupService.save(userGroup);
        return flag > 0;
    }

    @Transactional
    @Override
    public boolean addGroupMember(List<String> userIds, String groupId) {
        for (String userId : userIds) {
            UserGroup userGroup = new UserGroup();
            userGroup.setGroupId(groupId);
            userGroup.setUserId(userId);
            boolean save = userGroupService.save(userGroup);
            if(!save) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public boolean deleteGroupMember(List<String> userIds, String groupId,HttpServletRequest request) {
        //获取当前用户id
        String userId = JwtUtils.getMemberIdByRequest(request);
        //判断当前用户是否为群主
        Group group = groupMapper.selectById(groupId);
        if(!group.getMasterId().equals(userId)) {
            return false;
        }

        for (String id : userIds) {
            QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
            wrapper.eq("group_id", groupId);
            wrapper.eq("user_id", id);
            boolean remove = userGroupService.remove(wrapper);
            if(!remove) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public List<UserInfoVO> getGroupMember(String groupId,HttpServletRequest request) {
        //获取当前用户id
        String userId = JwtUtils.getMemberIdByRequest(request);
        //判断当前用户是否是群成员
        QueryWrapper<UserGroup> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("group_id", groupId);
        wrapper1.eq("user_id", userId);
        UserGroup userGroup1 = userGroupService.getOne(wrapper1);
        if(userGroup1 == null) {
            return null;
        }
        //根据群组id查询群成员
        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId);
        List<UserGroup> userGroups = userGroupService.list(wrapper);
        //根据群成员id查询群成员信息
        List<String> ids  = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
           ids.add(userGroup.getUserId());
        }
        List<User> users = userService.listByIds(ids);
        List<UserInfoVO> userInfoVOS = new ArrayList<>();
        for(User user : users) {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user, userInfoVO);
            userInfoVOS.add(userInfoVO);
        }
        return userInfoVOS;
    }

    @Override
    @Transactional
    public List<GroupInfoVO> getGroupList(HttpServletRequest request) {
        //获取当前用户id
        String userId = JwtUtils.getMemberIdByRequest(request);
        //根据用户id查询群组id
        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<UserGroup> userGroups = userGroupService.list(wrapper);
        //根据群组id查询群组信息
        List<String> ids  = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            ids.add(userGroup.getGroupId());
        }
        List<Group> groups = groupMapper.selectBatchIds(ids);
        //封装群组信息
        List<GroupInfoVO> groupInfoVOS = new ArrayList<>();
        for (Group group : groups) {
            GroupInfoVO groupInfoVO = new GroupInfoVO();
            BeanUtils.copyProperties(group, groupInfoVO);
            //获取群主信息
            User user = userService.getById(group.getMasterId());
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user, userInfoVO);
            groupInfoVO.setOwner(userInfoVO);
            //获取群成员数量
            QueryWrapper<UserGroup> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("group_id", group.getId());
            long count = userGroupService.count(wrapper1);
            groupInfoVO.setMemberCount(count);
            //添加到集合中
            groupInfoVOS.add(groupInfoVO);
        }
        return groupInfoVOS;
    }

    @Override
    public boolean removeGroup(String groupId, HttpServletRequest request) {
        //获取当前用户id
        String userId = JwtUtils.getMemberIdByRequest(request);
        //判断当前用户是否是群主
        Group group = groupMapper.selectById(groupId);
        if(!group.getMasterId().equals(userId)) {
            return false;
        }
        //删除群组
        int flag = groupMapper.deleteById(groupId);
        //删除群成员
        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId);
        userGroupService.remove(wrapper);
        return flag > 0;
    }

    @Override
    public long getGroupMemberCount(String groupId) {
        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId);
        long count = userGroupService.count(wrapper);
        return count;
    }
}





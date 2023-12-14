package com.lxg.wschat.service;

import com.lxg.wschat.domain.Group;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxg.wschat.domain.User;
import com.lxg.wschat.dto.GroupDTO;
import com.lxg.wschat.mahout.MahoutDataModel;
import com.lxg.wschat.vo.GroupInfoVO;
import com.lxg.wschat.vo.UserInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author linxugeng
* @description 针对表【t_group】的数据库操作Service
* @createDate 2023-12-06 22:19:18
*/
public interface GroupService extends IService<Group> {

    Group getGroupInfo(String gourpId);

    boolean addGroup(GroupDTO groupDTO, HttpServletRequest request);

    boolean addGroupMember(List<String> userIds, String groupId);

    boolean deleteGroupMember(List<String> userIds, String groupId,HttpServletRequest request);

    List<UserInfoVO> getGroupMember(String groupId, HttpServletRequest request);

    List<GroupInfoVO> getGroupList(HttpServletRequest request);

    boolean removeGroup(String groupId, HttpServletRequest request);

    long getGroupMemberCount(String groupId);

    List<MahoutDataModel> getGroupDataModel();

    List<GroupInfoVO> recommendGroup(HttpServletRequest request);
}

package com.lxg.wschat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxg.wschat.domain.Follow;
import com.lxg.wschat.domain.Group;
import com.lxg.wschat.domain.MessageDetail;
import com.lxg.wschat.domain.User;
import com.lxg.wschat.domain.UserGroup;
import com.lxg.wschat.mahout.MahoutDataModel;
import com.lxg.wschat.mahout.MyRecommender;
import com.lxg.wschat.mahout.RecommenderConstants;
import com.lxg.wschat.service.GroupService;
import com.lxg.wschat.mapper.GroupMapper;
import com.lxg.wschat.service.MessageDetailService;
import com.lxg.wschat.service.UserGroupService;
import com.lxg.wschat.service.UserService;
import com.lxg.wschat.utils.CreateGroupIdUtils;
import com.lxg.wschat.utils.JwtUtils;
import com.lxg.wschat.dto.GroupDTO;
import com.lxg.wschat.vo.GroupInfoVO;
import com.lxg.wschat.vo.UserInfoVO;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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

    @Autowired
    private MessageDetailService messageDetailService;

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
        String groupId = String.valueOf(CreateGroupIdUtils.createGroupId());
        group.setId(groupId);
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
        Map<String, Object> map = userGroupService.getMap(wrapper1);
        if(map == null) {
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
        if(ids.size() == 0) {
            return null;
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

    @Override
    public List<MahoutDataModel> getGroupDataModel() {
        List<MahoutDataModel> list = new ArrayList<>();
        QueryWrapper<UserGroup> wrapper1 = new QueryWrapper<>();
        List<UserGroup> userGroupList = userGroupService.list(wrapper1);
        for (UserGroup userGroup : userGroupList) {
            MahoutDataModel mahoutDataModel = new MahoutDataModel();
            mahoutDataModel.setUserId(Long.valueOf(userGroup.getUserId()));
            mahoutDataModel.setItemId(Long.valueOf(userGroup.getGroupId()));
            mahoutDataModel.setScore(userGroup.getScore());
            list.add(mahoutDataModel);
        }

        QueryWrapper<MessageDetail> wrapper2 = new QueryWrapper<>();
        List<MessageDetail> messageDetailList = messageDetailService.list(wrapper2);

        for (MessageDetail messageDetail : messageDetailList) {
            if(messageDetail.getScore()==null){
                continue;
            }
            if(messageDetail.getType()==1){
                continue;
            }
            MahoutDataModel mahoutDataModel = new MahoutDataModel();
            mahoutDataModel.setUserId(Long.valueOf(messageDetail.getSendId()));
            mahoutDataModel.setItemId(Long.valueOf(messageDetail.getAcceptId()));
            mahoutDataModel.setScore(messageDetail.getScore());
            list.add(mahoutDataModel);
        }

        return list;


    }

    @Override
    public List<GroupInfoVO> recommendGroup(HttpServletRequest request) {
        Long userId = Long.valueOf(JwtUtils.getMemberIdByRequest(request));
        List<MahoutDataModel> list = this.getGroupDataModel();
        try {
            DataModel dataModel = MyRecommender.buildJdbcDataModel(list);
            UserSimilarity similarity = (UserSimilarity) MyRecommender.getSimilarity(RecommenderConstants.SIMILARITY_COSINE, dataModel);
            UserNeighborhood userNeighborhood = new ThresholdUserNeighborhood(0.3, similarity, dataModel);
//            构建推荐引擎
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

//             构建推荐器，使用基于物品的协同过滤推荐
//            ItemSimilarity itemSimilarity = (ItemSimilarity) MyRecommender.getSimilarity(RecommenderConstants.SIMILARITY_COSINE, dataModel);
//            GenericItemBasedRecommender itemRecommender = new GenericItemBasedRecommender(dataModel, (ItemSimilarity) itemSimilarity);


            //给指定用户推荐3个群聊
            List<RecommendedItem> recommend = recommender.recommend(userId, 3);
//            List<RecommendedItem> recommend = itemRecommender.recommend(userId, 5);
            List<Long> collect = recommend.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
            List<GroupInfoVO> groupInfoVOList = new ArrayList<>();
            for (Long id : collect) {
                //如果已经在群聊中，跳过
                QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
                wrapper.eq("group_id", id);
                wrapper.eq("user_id", userId);
                UserGroup userGroup = userGroupService.getOne(wrapper);
                if (userGroup != null) {
                    continue;
                }
                Group group = groupMapper.selectById(id);
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
                groupInfoVOList.add(groupInfoVO);
            }

            //如果推荐的群聊数量不足3个，随机推荐群聊
            if (groupInfoVOList.size() < 3) {
                //查找所有没有加入的群聊
                QueryWrapper<Group> wrapper = new QueryWrapper<>();
                wrapper.notInSql("id", "select group_id from t_user_group where user_id=" + userId);
                List<Group> groupList = groupMapper.selectList(wrapper);
                //随机推荐
                //可以通过随机数来获取群聊
                Random random = new Random();
                while (groupInfoVOList.size() < 3) {
                    int i = random.nextInt(groupList.size());
                    Group group = groupList.get(i);
                    GroupInfoVO groupInfoVO = new GroupInfoVO();
                    BeanUtils.copyProperties(group, groupInfoVO);
                    //如果已经推荐过，跳过
                    if (groupInfoVOList.contains(groupInfoVO)) {
                        continue;
                    }
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
                    groupInfoVOList.add(groupInfoVO);
                }
            }

            return groupInfoVOList;

        } catch (TasteException e) {
            throw new RuntimeException(e);
        }
    }
}





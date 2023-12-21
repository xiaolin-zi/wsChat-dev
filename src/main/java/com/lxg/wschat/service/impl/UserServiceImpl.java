package com.lxg.wschat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxg.wschat.domain.Follow;
import com.lxg.wschat.domain.Message;
import com.lxg.wschat.domain.MessageDetail;
import com.lxg.wschat.domain.User;
import com.lxg.wschat.domain.UserGroup;
import com.lxg.wschat.mahout.MahoutDataModel;
import com.lxg.wschat.mahout.MyRecommender;
import com.lxg.wschat.mahout.RecommenderConstants;
import com.lxg.wschat.service.MessageDetailService;
import com.lxg.wschat.service.MessageService;
import com.lxg.wschat.service.UserGroupService;
import com.lxg.wschat.vo.UserInfoVO;
import com.lxg.wschat.mapper.UserMapper;
import com.lxg.wschat.service.FollowService;
import com.lxg.wschat.service.UserService;
import com.lxg.wschat.utils.EmailCodeUtil;
import com.lxg.wschat.utils.JwtUtils;
import com.lxg.wschat.utils.MD5;
import com.lxg.wschat.utils.R;
import com.lxg.wschat.dto.LoginByEmailForm;
import com.lxg.wschat.dto.LoginDTO;
import com.lxg.wschat.dto.RegisterDTO;
import com.lxg.wschat.dto.UserDTO;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author linxugeng
 * @description 针对表【t_user】的数据库操作Service实现
 * @createDate 2023-12-05 12:41:44
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // token缓存时间 5小时（单位小时）
    final static int tokenCacheTime = 5;
    // 模板引擎
    @Autowired
    TemplateEngine templateEngine;
    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private JavaMailSender mailSender;
    // 发送邮件的邮箱
    @Value("${spring.mail.username}")
    private String sendFrom;
    // 发送邮件的昵称
    @Value("${spring.mail.nickname}")
    private String nickname;


    @Autowired
    private FollowService followService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private MessageDetailService messageDetailService;

    @Autowired
    private MessageService messageService;

    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String login(LoginDTO loginDTO) {
        // 获取登录账号和密码
        String account = loginDTO.getAccount();
        String password = loginDTO.getPassword();

        // 对密码进行加密
        String encrypt = MD5.encrypt(password);
        // 查找数据库中是否存在该用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        wrapper.eq("password", encrypt);
        User user = baseMapper.selectOne(wrapper);

        if (user == null || (user.getIsDisabled() != null && user.getIsDisabled() == 1)) {
            return null;
        }
        // 登录成功
        // 生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(user.getId(), user.getAccount());
        return jwtToken;
    }

    /**
     * 注册
     *
     * @param registerDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R registerUser(RegisterDTO registerDTO) {
        // 获取账号、密码、昵称、邮箱、验证码
        String account = registerDTO.getAccount();
        String password = registerDTO.getPassword();
        String nickname = registerDTO.getNickname();
        String email = registerDTO.getEmail();
        String code = registerDTO.getCode();

        String authCode = (String) redisTemplate.opsForValue().get(email + "_code");
        if (!code.equals(authCode)) {
            return R.error().message("验证码错误");
        }

        // 查找数据库中是否存在该用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        User user1 = baseMapper.selectOne(wrapper);
        if (user1 != null) {
            return R.error().message("该账号已存在");
        }

        User user = new User();
        user.setAccount(account);
        user.setPassword(MD5.encrypt(password));
        user.setNickname(nickname);
        user.setEmail(email);
        user.setAvatar("https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312062137300.webp");//默认头像

        int insert = baseMapper.insert(user);
        if (insert > 0) {
            //注册成功，自动加入小黑子群聊
            UserGroup userGroup = new UserGroup();
            //查询用户的id
            QueryWrapper<User> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("account", account);
            User user2 = baseMapper.selectOne(wrapper2);

            userGroup.setUserId(user2.getId());
            userGroup.setGroupId("GP1732425017943670786");
            userGroupService.save(userGroup);

            UserGroup userGroup2 = new UserGroup();
            userGroup2.setUserId(user2.getId());
            userGroup2.setGroupId("GP2");
            userGroupService.save(userGroup2);

            return R.ok().message("注册成功");

        } else {
            return R.error().message("注册失败");
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean sendMail(String email) {
        // 从redis获取验证码
        String code = (String) redisTemplate.opsForValue().get(email + "_code");
        if (StringUtils.hasLength(code)) {
            // 如果有说明现在不是第一次发送，返回新的验证码
            // 删除旧的验证码
            redisTemplate.delete(email + "_code");
        }

        // 生成随机验证码
        code = EmailCodeUtil.generateVerificationCode();

        // 创建邮件上下文
        Context context = new Context();
        // 设置验证码
        context.setVariable("verifyCode", Arrays.asList(code.split("")));
        // 设置操作类型
        context.setVariable("operate", "邮箱验证");

        // 将模板引擎内容解析成html字符串
        String emailContent = templateEngine.process("emailTemplate", context);

        // 发送邮件
        boolean b = sendVerifyCodeEmail(emailContent, email);

        if (b) {
            // 发送成功，把发送成功验证码放到redis里面
            // 设置有效时间5分钟
            redisTemplate.opsForValue().set(email + "_code", code, 5, TimeUnit.MINUTES);
            return true;
        } else {
            return false;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R loginByCode(LoginByEmailForm form) {
        String email = form.getEmail();
        String code = form.getCode();
        String authCode = (String) redisTemplate.opsForValue().get(email + "_code");
        // 验证码正确
        if (code.equals(authCode)) {
            // 验证码正确后，删除redis中的验证码
            redisTemplate.delete(email + "_code");
            // 查询数据库中是否存在该用户
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("email", email);
            User user = baseMapper.selectOne(wrapper);
            if (user == null) {
                return R.error().message("该邮箱未注册");
            }
            // 生成token字符串，使用jwt工具类
            String jwtToken = JwtUtils.getJwtToken(user.getId(), user.getAccount());
            return R.ok().data("token", jwtToken).message("登录成功");
        }
        return R.error().message("验证码错误");

    }

    /**
     * 根据token获取用户信息
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoVO getUserInfo(HttpServletRequest request) {
        //调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        //查询数据库根据用户id获取用户信息
        User user = baseMapper.selectById(memberId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(User user) {
        int insert = baseMapper.insert(user);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfollowUser(String userId, HttpServletRequest request) {
        // 获取当前用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", memberId);
        wrapper.eq("friend_id", userId);
        //删除关注记录
        boolean remove = followService.remove(wrapper);
        return remove;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(UserDTO user, HttpServletRequest request) {
        // 获取当前用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        User user1 = new User();
        user1.setId(memberId);
        BeanUtils.copyProperties(user, user1);
        int i = baseMapper.updateById(user1);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserInfoVO> getFollowList(HttpServletRequest request) {
        // 获取当前用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", memberId);
        List<Follow> list = followService.list(wrapper);
        // 获取关注列表
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        for (Follow follow : list) {
            QueryWrapper<User> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("id", follow.getFriendId());
            User user = baseMapper.selectOne(wrapper1);
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user, userInfoVO);
            userInfoVOList.add(userInfoVO);
        }
        return userInfoVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean joinGroup(String groupId, HttpServletRequest request) {
        // 获取当前用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(memberId);
        userGroup.setGroupId(groupId);
        //查询用户是否已经加入该群聊
        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", memberId);
        wrapper.eq("group_id", groupId);
        UserGroup userGroup1 = userGroupService.getOne(wrapper);
        if (userGroup1 != null) {
            return false;
        }
        //插入群聊记录
        boolean insert = userGroupService.save(userGroup);
        return insert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserInfoVO> getFansList(HttpServletRequest request) {
        // 获取当前用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.eq("friend_id", memberId);
        List<Follow> list = followService.list(wrapper);
        // 获取粉丝列表
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        for (Follow follow : list) {
            QueryWrapper<User> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("id", follow.getUserId());
            User user = baseMapper.selectOne(wrapper1);
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(user, userInfoVO);
            userInfoVOList.add(userInfoVO);
        }
        return userInfoVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserInfoVO> getFriendList(HttpServletRequest request) {
        // 获取当前用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", memberId);
        List<Follow> flowList = followService.list(wrapper);
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        for (Follow follow : flowList) {
            String id = follow.getFriendId();
            QueryWrapper<Follow> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("user_id", id);
            List<Follow> flowList1 = followService.list(wrapper1);
            for (Follow follow1 : flowList1) {
                if (follow1.getFriendId().equals(memberId)) {
                    QueryWrapper<User> wrapper2 = new QueryWrapper<>();
                    wrapper2.eq("id", id);
                    User user = baseMapper.selectOne(wrapper2);
                    UserInfoVO userInfoVO = new UserInfoVO();
                    BeanUtils.copyProperties(user, userInfoVO);
                    userInfoVOList.add(userInfoVO);
                }
            }
        }
        return userInfoVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MahoutDataModel> getDataModel() {
        List<MahoutDataModel> list = new ArrayList<>();
        //查用户关注表
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        List<Follow> followList = followService.list(wrapper);
        for (Follow follow : followList) {
            MahoutDataModel mahoutDataModel = new MahoutDataModel();
            mahoutDataModel.setUserId(Long.valueOf(follow.getUserId()));
            mahoutDataModel.setItemId(Long.valueOf(follow.getFriendId()));
            mahoutDataModel.setScore(follow.getScore());
            list.add(mahoutDataModel);
        }

        QueryWrapper<MessageDetail> wrapper2 = new QueryWrapper<>();
        List<MessageDetail> messageDetailList = messageDetailService.list(wrapper2);

        for (MessageDetail messageDetail : messageDetailList) {
            if(messageDetail.getScore()==null){
                continue;
            }
            if(messageDetail.getType()==2){
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
    @Transactional(rollbackFor = Exception.class)
    public List<UserInfoVO> recommend(HttpServletRequest request) {
        Long userId = Long.valueOf(JwtUtils.getMemberIdByRequest(request));
        List<MahoutDataModel> list = this.getDataModel();
        try {
            DataModel dataModel = MyRecommender.buildJdbcDataModel(list);
            UserSimilarity similarity = (UserSimilarity) MyRecommender.getSimilarity(RecommenderConstants.SIMILARITY_COSINE, dataModel);
            UserNeighborhood userNeighborhood = new ThresholdUserNeighborhood(0.3, similarity, dataModel);
//            构建推荐引擎
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

//             构建推荐器，使用基于物品的协同过滤推荐
//            ItemSimilarity itemSimilarity = (ItemSimilarity) MyRecommender.getSimilarity(RecommenderConstants.SIMILARITY_COSINE, dataModel);
//            GenericItemBasedRecommender itemRecommender = new GenericItemBasedRecommender(dataModel, (ItemSimilarity) itemSimilarity);

            //给指定用户推荐5个好友
            List<RecommendedItem> recommend = recommender.recommend(userId, 5);
//            List<RecommendedItem> recommend = itemRecommender.recommend(userId, 5);
            List<Long> collect = recommend.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
            List<UserInfoVO> userInfoVOList = new ArrayList<>();
            for (Long id : collect) {
                //如果是自己，跳过
                if (id.equals(userId)) {
                    continue;
                }
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("id", id);
                User user = baseMapper.selectOne(wrapper);
                UserInfoVO userInfoVO = new UserInfoVO();
                BeanUtils.copyProperties(user, userInfoVO);
                userInfoVOList.add(userInfoVO);
            }

            //如果推荐的好友不足5个，随机推荐
            if(userInfoVOList.size()<5){
                //查询所有没有关注的用户
                QueryWrapper<Follow> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", userId);
                List<Follow> followList = followService.list(wrapper);
                List<String> userIdList = new ArrayList<>();
                for (Follow follow : followList) {
                    userIdList.add(follow.getFriendId());
                }
                userIdList.add(String.valueOf(userId));
                QueryWrapper<User> wrapper2 = new QueryWrapper<>();
                wrapper2.notIn("id",userIdList);
                List<User> userList = baseMapper.selectList(wrapper2);
                //随机推荐
                //可以通过随机数来获取用户
                Random random = new Random();
                List<UserInfoVO> userInfoVOList2 = new ArrayList<>();
                while (userInfoVOList.size()<5){
                    int i = random.nextInt(userList.size());
                    User user = userList.get(i);
                    UserInfoVO userInfoVO = new UserInfoVO();
                    BeanUtils.copyProperties(user, userInfoVO);
                    userInfoVOList2.add(userInfoVO);
                    //这是为了去重
                    userInfoVOList = userInfoVOList2.stream().distinct().collect(Collectors.toList());
                }
            }
            return userInfoVOList;
        } catch (TasteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importFlow(List<String> userIds, String userId) {
        for (String id : userIds) {
            //查询用户是否已经关注
            QueryWrapper<Follow> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            wrapper.eq("friend_id", id);
            Follow follow1 = followService.getOne(wrapper);
            if (follow1 != null) {
                continue;
            }
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFriendId(id);
            followService.save(follow);

            //回关
            QueryWrapper<Follow> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("user_id", id);
            wrapper2.eq("friend_id", userId);
            Follow follow2 = followService.getOne(wrapper2);
            if (follow2 != null) {
                continue;
            }
            Follow follow3 = new Follow();
            follow3.setUserId(id);
            follow3.setFriendId(userId);
            followService.save(follow3);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importGroup(List<String> groupIds, String userId) {
        for (String groupId : groupIds) {
            //查询用户是否已经加入该群聊
            QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            wrapper.eq("group_id", groupId);
            UserGroup userGroup1 = userGroupService.getOne(wrapper);
            if (userGroup1 != null) {
                continue;
            }
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(userId);
            userGroup.setGroupId(groupId);
            userGroupService.save(userGroup);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean exitGroup(String groupId, HttpServletRequest request) {
        // 获取当前用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(memberId);
        userGroup.setGroupId(groupId);
        //查询用户是否已经加入该群聊
        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", memberId);
        wrapper.eq("group_id", groupId);
        UserGroup userGroup1 = userGroupService.getOne(wrapper);
        if (userGroup1 == null) {
            return false;
        }
        //删除群聊记录
        boolean remove = userGroupService.remove(wrapper);
        //删除该用户在该群聊的会话记录
        QueryWrapper<Message> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("accept_id", groupId);
        wrapper2.eq("user_id", memberId);
        wrapper2.eq("type", 2);
        messageService.remove(wrapper2);
        //也要删除缓存
        redisTemplate.delete("chatGroupMessage_"+memberId+"to_"+groupId);
        return remove;
    }

    @Override
    public UserInfoVO getUserInfoByAccount(String account) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        User user = baseMapper.selectOne(wrapper);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean followUser(String userId, HttpServletRequest request) {
        // 获取当前用户id
        String memberId = JwtUtils.getMemberIdByRequest(request);
        //判断用户是否存在
        QueryWrapper<User> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("id", userId);
        User user1 = baseMapper.selectOne(wrapper1);
        if (user1 == null) {
            return false;
        }
        //判断是否已经关注
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", memberId);
        wrapper.eq("friend_id", userId);
        Follow follow1 = followService.getOne(wrapper);
        if (follow1 != null) {
            return false;
        }

        Follow follow = new Follow();
        follow.setUserId(memberId);
        follow.setFriendId(userId);
        //插入关注记录
        boolean insert = followService.save(follow);
        return insert;
    }

    /**
     * 发送验证码邮件
     *
     * @param emailContent
     * @param email
     * @return
     */
    private boolean sendVerifyCodeEmail(String emailContent, String email) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(nickname + '<' + sendFrom + '>');
            helper.setTo(email);
            helper.setSubject("知识交流社区-通知邮件");
            helper.setText(emailContent, true);
            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}

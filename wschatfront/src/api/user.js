import request from '@/utils/request'
export default {


    //获取好友列表
    getFriendList() {
        return request({
            url: `/user/getFriendList`,
            method: 'get',
        })
    },

    //获取推荐用户列表
    getRecommendUserList(id) {
        return request({
            url: `/user/recommend`,
            method: 'get',
        })
    },

    //获取关注用户列表
    getFollowUserList() {
        return request({
            url: `/user/getFollowList`,
            method: 'get',
        })
    },

    //获取粉丝用户列表
    getFansUserList() {
        return request({
            url: `/user/getFansList`,
            method: 'get',
        })
    },

    //关注用户
    followUser(userId) {
        return request({
            url: `/user/follow/${userId}`,
            method: 'post',
        })
    },

    //取消关注用户
    unFollow(userId) {
        return request({
            url: `/user/unfollow/${userId}`,
            method: 'post',
        })
    },

    //退出群聊
    quitGroup(groupId) {
        return request({
            url: `/user/exitGroup/${groupId}`,
            method: 'delete',
        })
    },

}

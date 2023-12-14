import request from '@/utils/request'
export default {


    //获取好友列表
    getGroupList() {
        return request({
            url: `/group/getGroupList`,
            method: 'get',
        })
    },

    //获取群成员人数
    getGroupMemberNum(groupId) {
        return request({
            url: `/group/getGroupMemberCount`,
            method: 'get',
            params: {groupId}
        })
    },

    //获取群成员列表
    getGroupMemberList(groupId) {
        return request({
            url: `/group/getGroupMember/${groupId}`,
            method: 'get',
        })
    },

    //获取推荐群列表
    getRecommendGroupList() {
        return request({
            url: `/group/recommendGroup`,
            method: 'get',
        })
    },
}

import request from '@/utils/request'
export default {


    //获取好友列表
    getFriendList() {
        return request({
            url: `/user/getFriendList`,
            method: 'get',
        })
    },
}

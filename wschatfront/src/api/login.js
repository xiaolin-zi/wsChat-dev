import request from '@/utils/request'
export default {


    //登录
    submitLogin(userInfo) {
        return request({
            url: `/login`,
            method: 'post',
            data: userInfo
        })
    },

    //根据token获取用户信息
    getLoginUserInfo() {
        return request({
            url: `/user/getUserInfo`,
            method: 'get',
        })
    },
}

import cookie from "js-cookie";
import request from "@/utils/request";


export default {


    //获取cookie中的用户信息
    getCookieUserInfo() {
        var userStr = cookie.get("userInfo");
        //把字符串转换成json对象
        if (!userStr) {
            return JSON.parse(userStr);
        }
    },

    //获取cookie中的用户id
    getCookieUserId() {
        var userStr = cookie.get("userInfo");
        //把字符串转换成json对象
        if (!userStr) {
            return JSON.parse(userStr).get("id");
        }
    },

}

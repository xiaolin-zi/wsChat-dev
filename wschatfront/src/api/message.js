import request from '@/utils/request'
export default {


    //获取消息列表
    getMessageList() {
        return request({
            url: `/message/getMessageList`,
            method: 'get',
        })
    },

    //获取聊天记录
    getChatRecords(acceptId, type) {
        return request({
            url: `/message/getChatRecords/${acceptId}/${type}`,
            method: 'get'
        })
    },

    //获取全部聊天记录
    getAllChatRecords(){
        return request({
            url: `/message/getAllChatRecords`,
            method: 'get'
        })
    },

    //保存会话记录到redis
    saveMessageToRedis(message){
        return request({
            url: `/message/saveMessageToRedis`,
            method: 'post',
            data: message
        })
    }
}

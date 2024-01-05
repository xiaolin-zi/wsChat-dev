import request from '@/utils/request'
export default {
    //ai对话
    aiChat(messgeInfo) {
        return request({
            url: `/ai/chat/gpt`,
            method: 'post',
            data: messgeInfo
        })
    },
    aiChatStream(messgeInfo) {
        return request({
            url: `/ai/chat/gpt/stream`,
            method: 'post',
            data: messgeInfo
        })
    },
    aiChat2(messgeInfo) {
        return request({
            url: `/ai/chat/gpt2`,
            method: 'post',
            data: messgeInfo
        })
    },
    aiChatStream2(messgeInfo) {
        return request({
            url: `/ai/chat/gpt2/stream`,
            method: 'post',
            data: messgeInfo
        })
    },
    aiChatWXYY(messgeInfo) {
        return request({
            url: `/ai/chat/wxyy`,
            method: 'post',
            data: messgeInfo
        })
    },
    aiChatStreamWXYY(messgeInfo) {
        return request({
            url: `/ai/chat/wxyy/stream`,
            method: 'post',
            data: messgeInfo
        })
    }
}

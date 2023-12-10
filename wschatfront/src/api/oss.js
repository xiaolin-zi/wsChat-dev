import request from '@/utils/request'
export default {

    //上传文件到oss
    uploadFileToOss(file){
        return request({
            url: `/oss/uploadOssFile`,
            method: 'post',
            data: file
        })
    }
}

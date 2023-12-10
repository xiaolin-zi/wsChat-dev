package com.lxg.wschat.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @auther xiaolin
 * @creatr 2023/5/13 22:24
 */

public interface OssService {
    //上传头像到oss
    String uploadFileAvatar(MultipartFile file);
}

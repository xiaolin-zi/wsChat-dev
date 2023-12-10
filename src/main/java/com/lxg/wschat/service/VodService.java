package com.lxg.wschat.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @auther xiaolin
 * @create 2023/5/21 10:46
 */
public interface VodService {
    String uploadVideoAly(MultipartFile file);

    void removeMoreAlyVideo(List<String> videoIdList);

    String getPlayAuth(String id);
}

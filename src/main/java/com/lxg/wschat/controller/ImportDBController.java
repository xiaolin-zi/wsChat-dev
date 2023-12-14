/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.controller;

import com.lxg.wschat.dto.ImportFlowDTO;
import com.lxg.wschat.dto.ImportGPDTO;
import com.lxg.wschat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ImportDBController
 * 仅用于导入数据库
 * @author linxugeng
 * @since 2023/12/12
 */
@RestController
public class ImportDBController {

    @Autowired
    private UserService userService;

    // 用户批量互关 其他用户列表
    @PostMapping("/importFlow")
    public String importFlow(@RequestBody ImportFlowDTO importFlowDTO) {
        userService.importFlow(importFlowDTO.getUserIds(),importFlowDTO.getUserId());
        return "success";
    }

    //用户批量加群
    @PostMapping("/importGroup")
    public String importGroup(@RequestBody ImportGPDTO importGPDTO) {
        userService.importGroup(importGPDTO.getGroupIds(),importGPDTO.getUserId());
        return "success";
    }

}

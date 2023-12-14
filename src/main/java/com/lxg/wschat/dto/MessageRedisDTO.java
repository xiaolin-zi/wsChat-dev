/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * MessageRedisDTO
 *
 * @author linxugeng
 * @since 2023/12/10
 */
@Data
public class MessageRedisDTO {
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    @NotNull(message = "用户id不能为空")
    private String userId;

    /**
     * 聊天名称
     */
    @NotBlank(message = "聊天名称不能为空")
    @NotNull(message = "聊天名称不能为空")
    private String name;

    /**
     * 群聊还是私聊（1私聊，2群聊）
     */
    @NotNull(message = "聊天类型不能为空")
    private Integer type;


    @NotNull(message = "接收者id不能为空")
    @NotBlank(message = "接收者id不能为空")
    private String acceptId;

    /**
     * 头像
     */
    @NotNull(message = "头像不能为空")
    @NotBlank(message = "头像不能为空")
    private String avatar;

    /**
     * 最后的消息
     */
    private String lastMess;

    /**
     * 最后的消息时间
     */
    private String lastTime;


    private List<String> groupUserIds;
}

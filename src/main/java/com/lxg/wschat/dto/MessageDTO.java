/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * MessageDTO
 *
 * @author linxugeng
 * @since 2023/12/7
 */
@Data
public class MessageDTO {
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
    @NotBlank(message = "聊天类型不能为空")
    private Integer type;

    /**
     * 详细聊天记录id
     */
    @NotNull(message = "详细聊天记录id不能为空")
    @NotBlank(message = "详细聊天记录id不能为空")
    private String detailId;

    /**
     * 是否已读
     */
    @NotNull(message = "是否已读不能为空")
    @NotBlank(message = "是否已读不能为空")
    private Integer isRead;

    /**
     * 是否展示
     */
    @NotNull(message = "是否展示不能为空")
    @NotBlank(message = "是否展示不能为空")
    private Integer isShow;

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
}

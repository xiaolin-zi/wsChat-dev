/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.vo;

import lombok.Data;

import java.util.Date;

/**
 * UserInfo
 *
 * @author linxugeng
 * @since 2023/12/5
 */
@Data
public class UserInfoVO {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 是否禁用
     */
    private Integer isDisabled;
    /**
     * 逻辑删除
     */
    private Integer isDeleted;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 修改人
     */
    private String updateUser;
}

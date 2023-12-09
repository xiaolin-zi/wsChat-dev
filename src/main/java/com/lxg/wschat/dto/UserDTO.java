/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.dto;

import lombok.Data;

/**
 * UserVo
 *
 * @author linxugeng
 * @since 2023/12/6
 */
@Data
public class UserDTO {
    /**
     * 用户账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
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
}

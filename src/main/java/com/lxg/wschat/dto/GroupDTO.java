/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * GroupVo
 *
 * @author linxugeng
 * @since 2023/12/6
 */
@Data
public class GroupDTO {

    /**
     * 群名称
     */
    @NotNull(message = "群名称不能为空")
    @NotBlank(message = "群名称不能为空")
    private String name;

    /**
     * 群头像
     */
    @NotNull(message = "群头像不能为空")
    @NotBlank(message = "群头像不能为空")
    private String avatar;


}

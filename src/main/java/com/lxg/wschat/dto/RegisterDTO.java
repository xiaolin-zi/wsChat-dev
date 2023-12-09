package com.lxg.wschat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @auther xiaolin
 * @create 2023/5/28 9:51
 */
@Data
@ApiModel(value="注册对象", description="注册对象")
public class RegisterDTO {
    @ApiModelProperty(value = "账号")
    @NotNull(message = "账号不能为空")
    @NotBlank(message = "账号不能为空")
    private String account;

    @NotNull(message = "昵称不能为空")
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotNull(message = "验证码不能为空")
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String code;
}

package com.lxg.wschat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value="登录对象", description="登录对象")
public class LoginDTO {
    @ApiModelProperty(value = "账号")
    @NotNull(message = "账号不能为空")
    @NotBlank(message = "账号不能为空")
    private String account;
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
}

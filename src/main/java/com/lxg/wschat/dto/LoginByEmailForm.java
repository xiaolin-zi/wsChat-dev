package com.lxg.wschat.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author linxugeng
 * @create 2023/7/9 10:37
 */
@Data
public class LoginByEmailForm {

    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    String email;

    @NotNull(message = "验证码不能为空")
    @NotBlank(message = "验证码不能为空")
    String code;

}

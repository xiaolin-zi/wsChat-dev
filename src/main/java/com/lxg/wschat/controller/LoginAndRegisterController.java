package com.lxg.wschat.controller;


import com.lxg.wschat.service.UserService;
import com.lxg.wschat.utils.R;
import com.lxg.wschat.dto.LoginByEmailForm;
import com.lxg.wschat.dto.LoginDTO;
import com.lxg.wschat.dto.RegisterDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 登录注册控制器
 *
 * @author linxugeng
 * @date 2023/12/05 11:35
 **/
@RestController
public class LoginAndRegisterController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("login")
    public R login(@RequestBody @Valid LoginDTO loginDTO) {
        // 调用service方法实现登录
        String token = userService.login(loginDTO);
        // 判断token是否为空
        if (token == null) {
            return R.error().message("账号或密码不正确");
        }
        return R.ok().data("token", token);
    }

    /**
     * 注册
     *
     * @param registerDTO
     * @return
     */
    @PostMapping("register")
    public R registerUser(@RequestBody @Valid RegisterDTO registerDTO) {
        return userService.registerUser(registerDTO);
    }

    /**
     * 发送邮箱验证码
     */
    @ApiOperation("发送邮箱验证码")
    @GetMapping("sendEmail/{email}")
    public R sendEmail(@PathVariable String email) {
        boolean flag = userService.sendMail(email);
        if (flag) {
            return R.ok().message("邮箱验证码已发送");
        } else {
            return R.error().message("邮箱验证码已发送");
        }
    }

    /**
     * 邮箱登录
     */
    @ApiOperation("邮箱登录")
    @PostMapping("loginByEmail")
    public R loginByEmail(@Valid @RequestBody LoginByEmailForm form) {
        R r = userService.loginByCode(form);
        return r;
    }



}

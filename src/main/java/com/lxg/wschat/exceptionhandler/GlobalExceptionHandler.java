package com.lxg.wschat.exceptionhandler;

import com.lxg.wschat.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @auther xiaolin
 * @creatr 2023/5/9 9:39
 */

@RestControllerAdvice(basePackages = "com.lxg.testwebsocket.controller")
@Slf4j
public class GlobalExceptionHandler {

    // 全局异常处理
    @ExceptionHandler(Exception.class)
    public R exceptionHandler(Exception e) {
        log.error("GlobalExceptionHandler.exceptionHandler , 异常信息", e);
        return R.error().message("执行了全局异常处理");
    }

    /**
     * 处理所有RequestBody注解参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return R.error().message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 处理所有RequestParam注解数据验证异常
     */
    @ExceptionHandler(BindException.class)
    public R handleBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        log.warn("必填校验异常:{}({})", fieldError.getDefaultMessage(), fieldError.getField());
        return R.error().message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    // 自定义异常
    @ExceptionHandler(LoginException.class)
    @ResponseBody // 为了返回数据
    public R error(LoginException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        log.error(ExceptionUtil.getMessage(e));
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}

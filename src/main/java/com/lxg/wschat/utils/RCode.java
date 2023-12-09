package com.lxg.wschat.utils;

public enum RCode {


    SUCCESS(20000, "成功"),
    ERROR(20001, "失败");


    private int code;
    private String message;

    RCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    };





}

package com.lxg.wschat.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieUtil {

    public static String getCookie(String name, String cookieHeader) {
        Map<String, String> cookies = parseCookieHeader(cookieHeader);
        return cookies.get(name);
    }

    private static Map<String, String> parseCookieHeader(String cookieHeader) {
        Map<String, String> cookies = new HashMap<>();
        List<String> cookieList = Arrays.asList(cookieHeader.split(";"));
        for (String cookie : cookieList) {
            String[] parts = cookie.trim().split("=");
            if (parts.length == 2) {
                String name = parts[0];
                String value = parts[1];
                cookies.put(name, value);
            }
        }
        return cookies;
    }
}

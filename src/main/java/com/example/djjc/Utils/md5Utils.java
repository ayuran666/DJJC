package com.example.djjc.Utils;

import org.springframework.util.DigestUtils;

public class md5Utils {

    public static String md5(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
}

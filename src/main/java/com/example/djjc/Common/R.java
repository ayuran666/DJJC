package com.example.djjc.Common;

import lombok.Data;

@Data
public class R<T> {
    private Integer code;

    private String msg;

    private T data;

    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.code = 1;
        r.data = object;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.code = 0;
        r.msg = msg;
        return r;
    }
}

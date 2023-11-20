package com.personal.blog.utils;

import lombok.Builder;
import lombok.Data;

@Data
public class R<T> {
    private Integer code;
    private T data;

    public void success(T data){
        this.code = 200;
        this.data = data;
    }
    public void error(T data){
        this.code = 500;
        this.data = data;
    }

    public R() {
    }

    public R(Integer code, T data) {
        this.code = code;
        this.data = data;
    }
}

package com.george.reggie_delivery.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author George
 * @Date 2022-06-01-18:33
 * @Description TODO
 * @Version 1.0
 */
@Data
public class R<T> {

    private Integer code;
    private String msg;
    private T data;
    //动态数据
    private Map map = new HashMap();

    public static <T> R<T> success(T object){
        R r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg){
        R r = new R<T>();
        r.code = 0;
        r.msg = msg;
        return r;
    }

    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }
}

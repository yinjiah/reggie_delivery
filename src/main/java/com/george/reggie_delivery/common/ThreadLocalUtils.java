package com.george.reggie_delivery.common;

/**
 * @Author George
 * @Date 2022-06-19-10:04
 * @Description TODO
 * @Version 1.0
 */
public class ThreadLocalUtils {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setValue(Long value){
        THREAD_LOCAL.set(value);
    }

    public static Long getValue(){
        return THREAD_LOCAL.get();
    }
}

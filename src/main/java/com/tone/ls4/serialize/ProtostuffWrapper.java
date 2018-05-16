package com.tone.ls4.serialize;

/**
 * @author zhaoxiang.liu
 * @date 2018/5/16
 */
public class ProtostuffWrapper<T> {
    private T data;

    public static <T> ProtostuffWrapper<T> builder(T data) {
        ProtostuffWrapper<T> wrapper = new ProtostuffWrapper<>();
        wrapper.setData(data);
        return wrapper;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package com.tone.ls4.serialize;

import java.io.IOException;

/**
 * @author zhaoxiang.liu
 * @date 2018/5/16
 */
public class FastJsonSerialization implements Serialization {

    @Override
    public byte[] serialize(Object obj) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        return null;
    }
}

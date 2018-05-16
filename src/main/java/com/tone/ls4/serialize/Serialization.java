package com.tone.ls4.serialize;

import java.io.IOException;

/**
 * @author zhaoxiang.liu
 * @date 2018/5/16
 */
public interface Serialization {
    byte[] serialize(Object obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException;
}

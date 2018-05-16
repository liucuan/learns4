package com.tone.ls4.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zhaoxiang.liu
 * @date 2018/5/16
 */
public class Hessian2Serialization implements Serialization {
    //使用 Hessian 序列化包含 BigDecimal 字段的对象时会导致其值一直为0，
    // 不注意这个bug会导致很大的问题，在最新的4.0.51版本仍然可以复现。
    // 解决方案也很简单，指定 BigDecimal 的序列化器即可，通过添加两个文件解决这个bug
    //resources\META-INF\hessian\serializers djava.math.BigDecimal=com.caucho.hessian.io.StringValueSerializer
    //resources\META-INF\hessian\deserializers java.math.BigDecimal=com.caucho.hessian.io.BigDecimalDeserializer
    @Override
    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        out.writeObject(obj);
        out.flush();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
        return (T) input.readObject(clz);
    }
}

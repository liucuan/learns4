package com.tone.ls4.serialize;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * @author zhaoxiang.liu
 * @date 2018/5/16
 */
public class ProtostuffSerialization implements Serialization {

    /**
     * 需要使用包装类进行序列化/反序列化的class集合
     */
    private static final Set<Class<?>> WRAPPER_SET = new HashSet<>();

    /**
     * 序列化/反序列化包装类 Class 对象
     */
    private static final Class<ProtostuffWrapper> WRAPPER_CLASS = ProtostuffWrapper.class;

    /**
     * 序列化/反序列化包装类 Schema 对象
     */
    private static final Schema<ProtostuffWrapper> WRAPPER_SCHEMA = RuntimeSchema
            .createFrom(WRAPPER_CLASS);

    /**
     * 预定义一些Protostuff无法直接序列化/反序列化的对象
     */
    static {
        WRAPPER_SET.add(List.class);
        WRAPPER_SET.add(ArrayList.class);
        WRAPPER_SET.add(CopyOnWriteArrayList.class);
        WRAPPER_SET.add(LinkedList.class);
        WRAPPER_SET.add(Stack.class);
        WRAPPER_SET.add(Vector.class);

        WRAPPER_SET.add(Map.class);
        WRAPPER_SET.add(HashMap.class);
        WRAPPER_SET.add(TreeMap.class);
        WRAPPER_SET.add(Hashtable.class);
        WRAPPER_SET.add(SortedMap.class);
        WRAPPER_SET.add(Map.class);

        WRAPPER_SET.add(Object.class);
    }

    private Objenesis objenesis = new ObjenesisStd();


    @Override
    public byte[] serialize(Object obj) {
        Class clz = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = WRAPPER_SCHEMA;
            if (!WRAPPER_SET.contains(clz)) {
                schema = RuntimeSchema.createFrom(clz);
            } else {
                obj = ProtostuffWrapper.builder(obj);
            }
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw e;
        } finally {
            buffer.clear();
        }
    }


    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) {
        T message;
        if (!WRAPPER_SET.contains(clz)) {
            message = objenesis.newInstance(clz);
            Schema<T> schema = RuntimeSchema.createFrom(clz);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } else {
            ProtostuffWrapper<T> wrapper = new ProtostuffWrapper<>();
            ProtostuffIOUtil.mergeFrom(bytes, wrapper, WRAPPER_SCHEMA);
            return wrapper.getData();
        }
    }
}

package com.tone.mvc;

import com.tone.ls4.genericservice.User;
import com.tone.ls4.serialize.Hessian2Serialization;
import com.tone.ls4.serialize.KryoSerialization;
import com.tone.ls4.serialize.ProtostuffSerialization;
import com.tone.ls4.serialize.Serialization;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

/**
 * @author zhaoxiang.liu
 * @date 2018/5/16
 */
public class SerializeTest {
    private Serialization ks = new KryoSerialization();
    private Serialization hs = new Hessian2Serialization();
    private Serialization ps = new ProtostuffSerialization();
    private List<User> datas = new ArrayList<>(1000);
    static int LOOP = 10000;
    static int TIMES = 10;

    @Before
    public void fillDatas() {
        LongStream.range(1, 1001).forEach(l -> datas.add(new User("name" + l, l)));
    }

    @Test
    public void kryoSeri() throws IOException {
        long start = System.currentTimeMillis();
        long spend = 0L;
        byte[] bytes;
        for (int i = 0; i < TIMES; i++) {
            for (int j = 0; j < LOOP; j++) {
                bytes = ks.serialize(datas);
//        System.out.println("ks len=" + bytes.length);
                List<User> users = ks.deserialize(bytes, ArrayList.class);
//        System.out.println(users);
            }
        }
        spend = (System.currentTimeMillis() - start) / 10;
        System.out.println("ks=" + spend);
    }

    @Test
    public void ps() throws IOException {
        long start = System.currentTimeMillis();
        long spend = 0L;
        byte[] bytes;
        for (int i = 0; i < TIMES; i++) {
            for (int j = 0; j < LOOP; j++) {
                bytes = ps.serialize(datas);
//        System.out.println("ps len=" + bytes.length);
                List<User> users = ps.deserialize(bytes, ArrayList.class);
//        System.out.println(users);
            }
        }
        spend = (System.currentTimeMillis() - start) / 10;
        System.out.println("ps=" + spend);
    }

    @Test
    public void hs() throws IOException {

        long start = System.currentTimeMillis();
        long spend = 0L;
        byte[] bytes;
        for (int i = 0; i < TIMES; i++) {
            for (int j = 0; j < LOOP; j++) {
                bytes = hs.serialize(datas);
//        System.out.println("len=" + bytes.length);
                List<User> users = hs.deserialize(bytes, ArrayList.class);
//        System.out.println(users);
            }
        }
        spend = (System.currentTimeMillis() - start) / 10;
        System.out.println("hs=" + spend);
    }

}

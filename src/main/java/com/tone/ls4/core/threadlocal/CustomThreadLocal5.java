package com.tone.ls4.core.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

/**
 * @author zhaoxiang.liu
 * @date 2018/3/27
 */
public class CustomThreadLocal5 {
    static TransmittableThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

    static ExecutorService pool = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            int j = i;
            pool.execute(() -> {
                CustomThreadLocal5.threadLocal.set("猿天地" + j);
                new Service().call();
            });
        }
    }

    private static class Service {
        public void call() {
            CustomThreadLocal5.pool.execute(() -> new Dao().call());
        }
    }

    private static class Dao {
        public void call() {
            System.out.println("Dao:" + CustomThreadLocal5.threadLocal.get());
        }
    }
}

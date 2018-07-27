package com.tone.ls4.core.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhaoxiang.liu
 * @date 2018/3/27
 */
public class CustomThreadLocal4 {

    static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            int j = i;
            pool.execute(() -> {
                CustomThreadLocal4.threadLocal.set("猿天地" + j);
                new Service().call();
            });
        }
    }

    private static class Service {

        public void call() {
            CustomThreadLocal4.pool.execute(() -> new Dao().call());
        }
    }

    private static class Dao {

        public void call() {
            System.out.println("Dao:" + CustomThreadLocal4.threadLocal.get());
        }
    }
}

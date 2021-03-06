package com.tone.ls4.core.threadlocal;

/**
 * @author zhaoxiang.liu
 * @date 2018/3/27
 */
public class CustomThreadLocal {

    static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CustomThreadLocal.threadLocal.set("xxx");
                new Service().call();
            }
        }).start();
    }

    private static class Service {

        public void call() {
            System.out.println("Service:" + Thread.currentThread().getName());
            System.out.println("Service:" + CustomThreadLocal.threadLocal.get());
            new Dao().call();
        }
    }

    private static class Dao {

        public void call() {
            System.out.println("==========================");
            System.out.println("Dao:" + Thread.currentThread().getName());
            System.out.println("Dao:" + CustomThreadLocal.threadLocal.get());
        }
    }
}

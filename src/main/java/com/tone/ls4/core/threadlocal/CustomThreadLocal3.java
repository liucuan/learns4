package com.tone.ls4.core.threadlocal;

/**
 * @author zhaoxiang.liu
 * @date 2018/3/27
 */
public class CustomThreadLocal3 {

    /**
     * InheritableThreadLocal这个类继承了ThreadLocal，重写了3个方法，在当前线程上创建一个新的线程实例Thread时，会把这些线程变量从当前线程传递给新的线程实例。
     * <p>
     * 到此为止，通过inheritableThreadLocals我们可以在父线程创建子线程的时候将Local中的值传递给子线程，这个特性已经能够满足大部分的需求了，但是还有一个很严重的问题是如果是在线程复用的情况下就会出问题，
     * 比如线程池中去使用inheritableThreadLocals 进行传值，因为inheritableThreadLocals
     * 只是会再新创建线程的时候进行传值，线程复用并不会做这个操作，那么要解决这个问题就得自己去扩展线程类，实现这个功能。
     * </p>
     */
    static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CustomThreadLocal3.threadLocal.set("xxx");
                new Service().call();
            }
        }).start();
    }

    private static class Service {

        public void call() {
            System.out.println("Service:" + Thread.currentThread().getName());
            System.out.println("Service:" + CustomThreadLocal3.threadLocal.get());
            new Thread(() -> new Dao().call()).start();
        }
    }

    private static class Dao {

        public void call() {
            System.out.println("==========================");
            System.out.println("Dao:" + Thread.currentThread().getName());
            System.out.println("Dao:" + CustomThreadLocal3.threadLocal.get());
        }
    }
}

package com.tone.ls4.core.concur;

import java.util.concurrent.*;

public class CompletableFutureApp2 {
    static ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactory() {
        int count = 1;

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "custom-executor-" + count++);
        }
    });

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFutureApp2 c = new CompletableFutureApp2();
        long start = System.currentTimeMillis();
        c.verifyOrder(1, 1, 1);
        System.out.println("verifyOrder=" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        c.verifyOrderWithFuture(1, 1, 1);
        System.out.println("verifyOrderWithFuture=" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        c.verifyOrderCompletableFuture(1, 1, 1).get();
        System.out.println("verifyOrderCompletableFuture=" + (System.currentTimeMillis() - start));
    }

    /**
     * 验证订单是否合法
     *
     * @param userId   用户id
     * @param itemId   商品id
     * @param discount 折扣
     * @return
     */
    public boolean verifyOrder(long userId, long itemId, double discount) {

        // 验证用户能否享受这一折扣，RPC调用
        boolean verifyDiscount = discountVerify(userId, itemId, discount);

        if (!verifyDiscount) {
            // 该用户无法享受这一折扣
            return false;
        }

        // 获取商品单价，RPC调用
        double itemPrice = getPrice(itemId);

        // 用户实际应该支付的价格
        double realPrice = itemPrice * discount;

        // 获取用户账号余额，限定了只能使用余额购买，RPC调用
        double balance = getBalance(userId);

        return realPrice <= balance;
    }

    /**
     * 验证订单是否合法
     *
     * @param userId   用户id
     * @param itemId   商品id
     * @param discount 折扣
     * @return
     */
    public boolean verifyOrderWithFuture(long userId, long itemId, double discount) throws ExecutionException, InterruptedException {
        //3 个 rpc 调用可以同时进行了，系统延迟降低为之前的 1/3。不过延迟降低吞吐量的问题还是没有解决，依然需要通过增加线程数来提升吞吐量。
        // 验证用户能否享受这一折扣，RPC调用
        Future<Boolean> verifyDiscountFuture = executor.submit(() -> discountVerify(userId, itemId, discount));

        // 获取商品单价，RPC调用
        Future<Double> itemPriceFuture = executor.submit(() -> getPrice(itemId));

        // 获取用户账号余额，限定了只能使用余额购买，RPC调用
        Future<Double> balanceFuture = executor.submit(() -> getBalance(userId));

        if (!verifyDiscountFuture.get()) {
            // 该用户无法享受这一折扣
            return false;
        }

        // 用户实际应该支付的价格
        double realPrice = itemPriceFuture.get() * discount;

        // 用户账号余额
        double balance = balanceFuture.get();

        return realPrice <= balance;
    }

    /**
     * 验证订单是否合法
     *
     * @param userId   用户id
     * @param itemId   商品id
     * @param discount 折扣
     * @return
     */
    public CompletableFuture<Boolean> verifyOrderCompletableFuture(long userId, long itemId, double discount) {

        // 验证用户能否享受这一折扣，RPC调用
        CompletableFuture<Boolean> verifyDiscountFuture = CompletableFuture.supplyAsync(() -> discountVerify(userId, itemId, discount));

        // 获取商品单价，RPC调用
        CompletableFuture<Double> itemPriceFuture = CompletableFuture.supplyAsync(() -> getPrice(itemId));

        // 获取用户账号余额，限定了只能使用余额购买，RPC调用
        CompletableFuture<Double> balanceFuture = CompletableFuture.supplyAsync(() -> getBalance(userId));

        return CompletableFuture
                .allOf(verifyDiscountFuture, itemPriceFuture, balanceFuture)
                .thenApply(v -> {
                    try {
                        System.out.println("-------");
                        if (!verifyDiscountFuture.get()) {
                            // 该用户无法享受这一折扣
                            return false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    // 用户实际应该支付的价格
                    double realPrice = 0;
                    try {
                        realPrice = itemPriceFuture.get() * discount;
                        // 用户账号余额
                        double balance = balanceFuture.get();
                        System.out.println("b=" + balance);
                        return realPrice <= balance;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return false;
                });


    }


    private boolean discountVerify(long userId, long itemId, double discount) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private Double getPrice(long itemId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2.0;
    }

    private Double getBalance(long userId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 3.0;
    }
}

package com.tone.ls4;

import com.tone.ls4.genericservice.User;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * Created by jenny on 2016/8/2.
 */
public class Test {
    public static void main(String[] args) {
        AsyncRestTemplate template = new AsyncRestTemplate();
        //调用完后立即返回（没有阻塞）
        ListenableFuture<ResponseEntity<User>> future = template.getForEntity("http://localhost:8080/ls4/api", User.class);
        //设置异步回调
        future.addCallback(new ListenableFutureCallback<ResponseEntity<User>>() {
            @Override
            public void onSuccess(ResponseEntity<User> result) {
                System.out.println("======client get result : " + result.getBody());
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("======client failure : " + t);
            }
        });
        System.out.println("==no wait");
    }
}

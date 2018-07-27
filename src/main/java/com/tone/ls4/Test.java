package com.tone.ls4;

import com.tone.ls4.genericservice.User;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Created by jenny on 2016/8/2.
 */
public class Test {

    public static void main(String[] args) {
        webClient();
    }

    private static void webClient() {
        ResponseSpec responseSpec = WebClient.create("http://localhost:8080/ls4/api")
                .get().retrieve();
        User result = responseSpec
                .onStatus(e -> e.is4xxClientError(), resp -> {
                    System.out.println("statusCode:" + resp.statusCode());
                    return Mono.error(new RuntimeException(
                            resp.statusCode().value() + " : " + resp.statusCode()
                                    .getReasonPhrase()));
                })
                .bodyToMono(User.class)
                .doOnError(WebClientResponseException.class, err -> {
                    System.out.println("exception:" + err);
                })
                .doOnSuccess(user -> {
                    System.out.println("success user:" + user);
                })
                .onErrorReturn(new User())
                .block();
//                .onErrorReturn(new User());
        System.out.println("xxx");
//        User result = mono.block();
        System.out.println("user:" + result);
    }


    private static void async() {
        System.out.println();
        AsyncRestTemplate template = new AsyncRestTemplate();
        //调用完后立即返回（没有阻塞）
        ListenableFuture<ResponseEntity<User>> future = template
                .getForEntity("http://localhost:8080/ls4/api", User.class);
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

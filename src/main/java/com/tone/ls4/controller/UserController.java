package com.tone.ls4.controller;

import com.tone.ls4.genericservice.User;
import com.tone.ls4.genericservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * Created by jenny on 2016/8/2.
 */
@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        System.out.println("dddd");
        this.userService = userService;
    }

    @RequestMapping("/test")
    public User view() {
        User user = new User();
        user.setId(1L);
        user.setName("haha");
        return user;
    }

    @RequestMapping("/test2")
    public String view2() {
        return "{\"id\" : 1}";
    }
    @RequestMapping("api")
    public Callable<User> api(){
        System.out.println("api..");
        return new Callable<User>() {
            @Override
            public User call() throws Exception {
                Thread.sleep(2000L);
                User user = new User();
                user.setId(1L);
                user.setName("haha");
                return user;
            }
        };
    }
}

package com.tone.ls4.genericservice;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * Created by jenny on 2016/8/2.
 */
@Order(3)//顺序
@Lazy
@Service
public class UserService extends BaseService<User> {
    public UserService() {
        System.out.println("construct UserService.");
    }
}

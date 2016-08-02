package com.tone.mvc;

import com.tone.ls4.genericservice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by jenny on 2016/8/2.
 */
public class GenericRepsorityTest extends BaseTest {
    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
//    @Autowired
    private Map<String, BaseService> map;

//    @Autowired
    private List<BaseService> list;

    @Test
    public void usTest() {
        System.out.println("usTest");
        userService.save(new User());
    }

    @Test
    public void osTest() {
        System.out.println("osTest");
        organizationService.save(new Organization());
    }

    @Test
    public void mapTest() {
        map.entrySet().stream().forEach(s -> System.out.println(s.getValue()));
    }

    @Test
    public void listTest() {
        list.stream().forEach(System.out::println);
    }
}

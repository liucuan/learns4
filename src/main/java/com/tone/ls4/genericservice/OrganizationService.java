package com.tone.ls4.genericservice;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * Created by jenny on 2016/8/2.
 */
@Order(2)
@Service
public class OrganizationService extends BaseService<Organization> {

    public OrganizationService() {
        System.out.println("construct OrganizationService");
    }
}

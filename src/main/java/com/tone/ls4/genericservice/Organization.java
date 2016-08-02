package com.tone.ls4.genericservice;

import java.io.Serializable;

/**
 * Created by jenny on 2016/8/2.
 */
public class Organization implements Serializable {
    private static final long serialVersionUID = 4830759744439603773L;
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.tone.ls4.genericservice;

import java.io.Serializable;

/**
 * Created by jenny on 2016/8/2.
 */
public class User implements Serializable {
    private static final long serialVersionUID = -7386825545785262230L;
    private String name;
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

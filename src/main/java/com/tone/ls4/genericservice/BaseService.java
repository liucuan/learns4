package com.tone.ls4.genericservice;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by jenny on 2016/8/2.
 */
public abstract class BaseService<M extends Serializable> {
    @Autowired
    protected BaseRepository<M> repository;

    public void save(M m) {
        repository.save(m);
    }
}

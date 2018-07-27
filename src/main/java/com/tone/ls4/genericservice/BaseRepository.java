package com.tone.ls4.genericservice;

import java.io.Serializable;

/**
 * Created by jenny on 2016/8/2.
 */
public abstract class BaseRepository<M extends Serializable> {

    public void save(M m) {
        System.out.println("==========repository save:" + m);
    }
}

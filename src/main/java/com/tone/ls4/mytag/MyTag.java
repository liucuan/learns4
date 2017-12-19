package com.tone.ls4.mytag;

public class MyTag {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MyTag{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

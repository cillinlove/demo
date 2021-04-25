package com.example.demo.pattern;

import lombok.Getter;

public abstract class Daddy<T extends Daddy<T>> {

    @Getter
    private String name;

    public Daddy(String name) {
        this.name = name;
    }

    public abstract void sing();

    public void doSing(T t) {
        prepare(t);
        sing();
        thanks(t);
    }

    public void prepare(T t) {
        System.out.println(t.getName() + "登台准备");
    }

    public void thanks(T t) {
        System.out.println(t.getName() + "致谢");
    }
}

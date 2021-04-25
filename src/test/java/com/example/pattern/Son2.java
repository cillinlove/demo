package com.example.pattern;

import com.example.demo.pattern.Daddy;

public class Son2 extends Daddy<Son2> {



    public Son2() {
        super("二儿子");
    }

    @Override
    public void sing() {
        System.out.println("民族唱法");
    }

    public static void main(String[] args) {
        Son2 son2 = new Son2();
        son2.doSing(son2);
    }
}

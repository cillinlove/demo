package com.example.pattern;

import com.example.demo.pattern.Daddy;

public class Son extends Daddy<Son> {


    public Son() {
        super("大儿子");
    }

    @Override
    public void sing() {
        System.out.println("美声唱法");
    }

    public static void main(String[] args) {
        Son son = new Son();
        son.doSing(son);
    }
}

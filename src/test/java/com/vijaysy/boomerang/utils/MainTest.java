package com.vijaysy.boomerang.utils;

/**
 * Created by vijaysy on 26/04/16.
 */
public class MainTest {

    public static void main(String[] args) {
        new Thread(() -> {System.out.printf("Hi");}).start();
    }
}
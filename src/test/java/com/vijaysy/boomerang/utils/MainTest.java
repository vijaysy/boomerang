package com.vijaysy.boomerang.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by vijaysy on 26/04/16.
 */
public class MainTest {

    public static void main(String[] args) {
        //new Thread(() -> {System.out.printf("Hi");}).start();
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(i);
                    blockingQueue.put(String.format("%s",i));
                    System.out.println("Produced:" + i);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                blockingQueue.put("exit");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(() -> {
            String string;
            try {
                while ((string = blockingQueue.take()) != "exit") {
                    Thread.sleep(10);
                    System.out.println("Consumed: " + string);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }).start();

    }

}


package com.vijaysy.boomerang;

import com.vijaysy.boomerang.listeners.SubListenerThread;

/**
 * Created by vijay.yala on 02/04/16.
 */
public class BoomerangListenerTest {
    public static void main(String [] args){
        SubListenerThread subListenerThread = new SubListenerThread();
        Thread thread = new Thread(subListenerThread);
        thread.start();


    }
}

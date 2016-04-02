package com.vijaysy.boomerang;

import com.vijaysy.boomerang.listeners.SubListenerThread;

/**
 * Created by vijaysy on 02/04/16.
 */
public class BoomerangListenerTest {
    public static void main(String [] args){
        //TODO: Need to check about thread management
        SubListenerThread subListenerThread = new SubListenerThread();
        Thread thread = new Thread(subListenerThread);
        thread.start();


    }
}

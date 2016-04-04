package com.vijaysy.boomerang;

import com.vijaysy.boomerang.listeners.SubListenerThread;

/**
 * Created by vijaysy on 02/04/16.
 */
public class BoomerangListenerTest {
    public static void main(String [] args){
        //TODO: Need to check about thread management
        // TODO: 04/04/16  https://github.com/OpenHFT/Java-Thread-Affinity,
        //                 http://vanillajava.blogspot.in/2012/01/java-thread-affinity-supports-groups-of.html
        SubListenerThread subListenerThread = new SubListenerThread();
        Thread thread = new Thread(subListenerThread);
        thread.start();


    }
}

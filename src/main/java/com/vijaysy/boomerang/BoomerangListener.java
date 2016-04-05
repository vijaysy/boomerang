package com.vijaysy.boomerang;

import com.vijaysy.boomerang.listeners.SubListenerThread;
import com.vijaysy.boomerang.models.Config.ListenerConfig;
import com.vijaysy.boomerang.models.Config.ThreadConfig;
import com.vijaysy.boomerang.utils.YMLReader;

/**
 * Created by vijaysy on 05/04/16.
 */
public final class BoomerangListener {
    public static void main(String[] args) throws Exception{
        YMLReader ymlReader = new YMLReader();
        ListenerConfig listenerConfig= ymlReader.getListenerConfig();
        for (ThreadConfig threadConfig:listenerConfig.getThreadConfigs()){
            for (int i=0 ;i<threadConfig.getListenerCount();i++) {
                SubListenerThread subListenerThread = new SubListenerThread(listenerConfig.getRedisConfig().getHost(), listenerConfig.getRedisConfig().getPort(), threadConfig.getChannel());
                Thread thread = new Thread(subListenerThread);
                thread.start();
            }
        }
    }
}

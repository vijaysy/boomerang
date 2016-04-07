package com.vijaysy.boomerang;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vijaysy.boomerang.listeners.SubListenerThread;
import com.vijaysy.boomerang.models.Config.CacheConfig;
import com.vijaysy.boomerang.models.Config.ListenerConfig;
import com.vijaysy.boomerang.models.Config.ThreadConfig;
import com.vijaysy.boomerang.utils.Cache;
import com.vijaysy.boomerang.utils.YMLReader;

/**
 * Created by vijaysy on 05/04/16.
 */
public final class BoomerangListener {
    public static void main(String[] args) throws Exception{
        YMLReader ymlReader = new YMLReader();
        ListenerConfig listenerConfig= ymlReader.getListenerConfig();
        CacheConfig cacheConfig = new CacheConfig("mymaster","127.0.0.1:26379","foobared",2,0,8);
        Injector injector = Guice.createInjector(new BoomerangModule(cacheConfig));
        Cache cache = injector.getInstance(Cache.class);
        for (ThreadConfig threadConfig:listenerConfig.getThreadConfigs()){
            for (int i=0 ;i<threadConfig.getListenerCount();i++) {
                SubListenerThread subListenerThread = new SubListenerThread(cache,threadConfig.getChannel());
                Thread thread = new Thread(subListenerThread);
                thread.start();
            }
        }
    }


    /// todo add runtime shutdown hooks and call sublistenerThread.stoplistener
}

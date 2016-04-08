package com.vijaysy.boomerang.utils;

import com.vijaysy.boomerang.models.Config.ListenerConfig;

import javax.inject.Singleton;

/**
 * Created by vijaysy on 05/04/16.
 */
@Singleton
public class YMLReader {
    private ListenerConfig listenerConfig;
    public YMLReader() throws Exception {
//        YamlReader reader = new YamlReader(new FileReader("config/boomerang.yml"));
//        Object object = reader.read();
       // Map map = (Map)object;
//        listenerConfig = new ListenerConfig();
//        listenerConfig.setThreadConfigs((List<ThreadConfig>) map.get("threadConfigs"));
//        listenerConfig.setGroupName((String)map.get("groupName"));
//        listenerConfig.setCacheConfig((HashMap<String, String>) map.get("cacheConfig"));
    }

    public ListenerConfig getListenerConfig() {
        return listenerConfig;
    }
}

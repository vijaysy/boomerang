package com.vijaysy.boomerang.utils;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.vijaysy.boomerang.models.Config.ListenerConfig;
import com.vijaysy.boomerang.models.Config.ThreadConfig;

import javax.inject.Singleton;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vijaysy on 05/04/16.
 */
@Singleton
public class YMLReader {
    private ListenerConfig listenerConfig;
    public YMLReader() throws Exception {
        YamlReader reader = new YamlReader(new FileReader("config/boomerangListener.yml"));
        Object object = reader.read();
        Map map = (Map)object;
        listenerConfig = new ListenerConfig();
        listenerConfig.setThreadConfigs((List<ThreadConfig>) map.get("threadConfigs"));
        listenerConfig.setGroupName((String)map.get("groupName"));
        listenerConfig.setRedisConfig((HashMap<String, String>) map.get("redisConfig"));
    }

    public ListenerConfig getListenerConfig() {
        return listenerConfig;
    }
}

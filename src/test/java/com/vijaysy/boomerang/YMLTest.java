package com.vijaysy.boomerang;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.vijaysy.boomerang.models.Config.ListenerConfig;
import com.vijaysy.boomerang.models.Config.ThreadConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vijaysy on 04/04/16.
 */
@Slf4j
public class YMLTest {
    public static void main(String [] args) throws Exception{
        YamlReader reader = new YamlReader(new FileReader("config/boomerangListener.yml"));
        Object object = reader.read();
        Map map = (Map)object;
        //System.out.printf(map.toString());
        ListenerConfig listenerConfig = new ListenerConfig();

        listenerConfig.setThreadConfigs((List<ThreadConfig>) map.get("threadConfigs"));
        listenerConfig.setGroupName((String)map.get("groupName"));
        listenerConfig.setRedisConfig((HashMap<String, String>) map.get("redisConfig"));
        log.info("Testing log");
        log.info(""+listenerConfig);
        //System.out.printf(listenerConfig.toString());


    }
}

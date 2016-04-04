package com.vijaysy.boomerang;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.vijaysy.boomerang.models.ListenerConfig;
import com.vijaysy.boomerang.models.ThreadConfig;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

/**
 * Created by vijay.yala on 04/04/16.
 */
public class YMLTest {
    public static void main(String [] args) throws Exception{
        YamlReader reader = new YamlReader(new FileReader("/Users/vijay.yala/vijay/java/boomerang/config/boomerangListener.yml"));
        Object object = reader.read();
        Map map = (Map)object;
        System.out.printf(map.toString());
        ListenerConfig listenerConfig = new ListenerConfig();
        listenerConfig.setThreadConfigs((List<ThreadConfig>) map.get("threadConfigs"));
        listenerConfig.setGroupName((String)map.get("groupName"));
        System.out.printf(listenerConfig.toString());


    }
}

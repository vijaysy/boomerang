package com.vijaysy.boomerang.models.Config;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by vijaysy on 04/04/16.
 */
@ToString
@Getter
public class ListenerConfig {
    private String groupName;
    private List<ThreadConfig> threadConfigs = new ArrayList<ThreadConfig>();
    private RedisConfig redisConfig = new RedisConfig();

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setThreadConfigs(List<ThreadConfig> threadConfigs) {
        this.threadConfigs = threadConfigs;
    }

    public void setRedisConfig(HashMap<String,String> hashMap) {
        this.redisConfig.setHost(Objects.isNull(hashMap.get("host"))?"localhost":hashMap.get("host"));
        this.redisConfig.setDb(Objects.isNull(hashMap.get("db"))?0:Integer.valueOf(hashMap.get("db")));
        this.redisConfig.setPassword(Objects.isNull(hashMap.get("password"))?"":hashMap.get("password"));
        this.redisConfig.setPort(Objects.isNull(hashMap.get("port"))?26379:Integer.valueOf(hashMap.get("port")));
    }
}

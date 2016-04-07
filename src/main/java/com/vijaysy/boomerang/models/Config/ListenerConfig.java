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
    private CacheConfig cacheConfig = new CacheConfig();

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setThreadConfigs(List<ThreadConfig> threadConfigs) {
        this.threadConfigs = threadConfigs;
    }

    public void setCacheConfig(HashMap<String,String> hashMap) {
        this.cacheConfig.setMaster(Objects.isNull(hashMap.get("master"))?"mymaster":hashMap.get("master"));
        this.cacheConfig.setSentinels(Objects.isNull(hashMap.get("sentinels"))?"127.0.0.1:26379":hashMap.get("sentinels"));
        this.cacheConfig.setPassword(Objects.isNull(hashMap.get("password"))?"foobared":hashMap.get("password"));
        this.cacheConfig.setTimeout(Objects.isNull(hashMap.get("timeout"))?2:Integer.valueOf(hashMap.get("timeout")));
        this.cacheConfig.setDb(Objects.isNull(hashMap.get("timeout"))?2:Integer.valueOf(hashMap.get("timeout")));
    }
}

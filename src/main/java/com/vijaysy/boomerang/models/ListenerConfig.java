package com.vijaysy.boomerang.models;

import java.util.List;

/**
 * Created by vijay.yala on 04/04/16.
 */
public class ListenerConfig {
    private List<ThreadConfig> threadConfigs;
    private String groupName;
    public ListenerConfig(){}

    public List<ThreadConfig> getThreadConfigs() {
        return threadConfigs;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setThreadConfigs(List<ThreadConfig> threadConfigs) {
        this.threadConfigs = threadConfigs;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

package com.vijaysy.boomerang.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by vijaysy on 16/04/16.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class PoolStatus {
    private int activeCount;
    private int corePoolSize;
    private int poolSize;
    private long taskCount;
}

package com.vijaysy.boomerang.models.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by vijaysy on 04/04/16.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThreadConfig {
    private String name;
    private String channel;
    private int listenerCount;
}

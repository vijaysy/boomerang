package com.vijaysy.boomerang.models.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by vijaysy on 05/04/16.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisConfig {

    private String host="localhost";
    private int port;
    private int db=0;
    private String password="";
}

package com.vijaysy.boomerang.models.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Created by vijaysy on 08/04/16.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CacheConfig {

    @JsonProperty
    @NonNull
    private String master;

    @JsonProperty
    @NonNull
    private String sentinels;

    @JsonProperty
    @NonNull
    private String password;

    @JsonProperty
    private int timeout = 2;

    @JsonProperty
    private int db = 0;

    @JsonProperty
    private int maxThreads = 8;
}

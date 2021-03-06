package com.vijaysy.boomerang;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vijaysy.boomerang.models.config.CacheConfig;
import com.vijaysy.boomerang.models.config.ThreadConfig;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijaysy on 08/04/16.
 */
@Getter
@Setter
public class BoomerangConfiguration extends Configuration {

    @JsonProperty
    @NonNull
    @NotEmpty
    private String name;

    @JsonProperty
    private DataSourceFactory database;

    @Valid
    @NonNull
    @JsonProperty
    private CacheConfig cacheConfig;

    @JsonProperty
    private int threadPoolSize = 50;

    @JsonProperty
    private List<ThreadConfig> threadConfigs = new ArrayList<>();

}

package com.vijaysy.boomerang;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.models.Config.CacheConfig;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.Cache;
import com.vijaysy.boomerang.utils.RetryItemBuilder;

import java.util.Optional;

/**
 * Created by vijaysy on 02/04/16.
 */
public class BoomerangTest {
    public static void main(String args[]) throws Exception {
        Integer[] integers = new Integer[]{1, 1, 1};
        CacheConfig cacheConfig = new CacheConfig("mymaster","127.0.0.1:26379","foobared",2,0,8);
        Injector injector = Guice.createInjector(new BoomerangModule(cacheConfig));
        Cache cache = injector.getInstance(Cache.class);

        Optional<RetryItem> retryItem = Boomerang.isRetryExist("m44");
        if (retryItem.isPresent())
            Boomerang.reappear(retryItem.get(),cache);
        else
            Boomerang.reappear(RetryItemBuilder.create()
                    .withMessageId("m44")
                    .withMessage("{\"input\":\"Hi12\"}")
                    .withHttpMethod(HttpMethod.POST)
                    .withHttpUrl("http://localhost:8080/mock/post")
                    .withRetryPattern(integers)
                    .withNextRetry(0)
                    .withFallbackHttpUrl("http://localhost:8080/mock")
                    .withChannel("RT")
                    .build(),cache);
    }
}
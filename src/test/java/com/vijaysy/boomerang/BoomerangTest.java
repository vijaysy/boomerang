package com.vijaysy.boomerang;

import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.RetryItemBuilder;

/**
 * Created by vijaysy on 02/04/16.
 */
public class BoomerangTest {
    public static void main(String args[]) throws Exception {
        Integer[] integers = new Integer[]{1, 2, 3,};
        Boomerang.reappear(new RetryItem("m11", "Hi12", HttpMethod.GET, "URL1", 0, integers, "fURL1", HttpMethod.POST,"RT"));


        Boomerang.reappear(RetryItemBuilder.create()
                .withMessageId("m33")
                .withMessage("Test Message")
                .withHttpMethod(HttpMethod.POST)
                .withHttpUrl("http://localhost:8080")
                .withRetryPattern(integers)
                .withNextRetry(0)
                .withFallbackHttpMethod(HttpMethod.PUT)
                .withFallbackHttpUrl("http://localhost:8080/fallback")
                .withChannel("RT")
                .build());
    }
}
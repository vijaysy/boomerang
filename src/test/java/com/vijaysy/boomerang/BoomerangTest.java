package com.vijaysy.boomerang;

import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.utils.RetryItemBuilder;

import java.util.Optional;

/**
 * Created by vijaysy on 02/04/16.
 */
public class BoomerangTest {
    public static void main(String args[]) throws Exception {
        Integer[] integers = new Integer[]{1, 1, 1};
        //Boomerang.reappear(new RetryItem("m11", "Hi12", HttpMethod.GET, "URL1", 0, integers, "fURL1", HttpMethod.POST, "RT"));

        Optional<RetryItem> retryItem = Boomerang.isRetryExist("m44");
        if (retryItem.isPresent())
            Boomerang.reappear(retryItem.get());
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
                    .build());
    }
}
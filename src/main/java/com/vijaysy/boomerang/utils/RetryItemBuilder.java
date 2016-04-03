package com.vijaysy.boomerang.utils;

import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.models.RetryItem;

import java.util.Arrays;

/**
 * Created by vijaysy on 04/04/16.
 */
public final class RetryItemBuilder {
    private RetryItem retryItem;

    private RetryItemBuilder(){
        retryItem = new RetryItem();
    }
    public static RetryItemBuilder create(){return new RetryItemBuilder();}

    public RetryItemBuilder withMessageId(String messageId){
        this.retryItem.setMessageId(messageId);
        return this;
    }

    public RetryItemBuilder withMessage (String message){
        this.retryItem.setMessage(message);
        return this;
    }

    public RetryItemBuilder withHttpMethod(HttpMethod httpMethod){
        this.retryItem.setHttpMethod(httpMethod);
        return this;
    }

    public RetryItemBuilder withHttpUrl(String httpUrl){
        this.retryItem.setHttpUri(httpUrl);
        return this;
    }

    public RetryItemBuilder withRetryPattern(Integer[] retryPattern){
        this.retryItem.setRetryPattern(Arrays.toString(retryPattern).replaceAll("\\[|\\]||\\s", "").replace(" ",""));
        this.retryItem.setMaxRetry(retryPattern.length);
        return this;
    }

    // TODO: 04/04/16  not required, should be zero by default
    public RetryItemBuilder withNextRetry(Integer nextRetry){
        this.retryItem.setNextRetry(nextRetry);
        return this;
    }

    public RetryItemBuilder withFallbackHttpMethod(HttpMethod httpMethod){
        this.retryItem.setfHttpMethod(httpMethod);
        return this;
    }

    public RetryItemBuilder withFallbackHttpUrl(String url){
        this.retryItem.setfHttpUri(url);
        return this;
    }

    // TODO: 04/04/16  addHeader() not addHeaders()
    public RetryItem getRetryItem() {
        return retryItem;
    }
}

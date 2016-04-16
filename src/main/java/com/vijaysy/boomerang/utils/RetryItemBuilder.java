package com.vijaysy.boomerang.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.models.RetryItem;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MultivaluedHashMap;
import java.util.Arrays;

/**
 * Created by vijaysy on 04/04/16.
 */
@Slf4j
public final class RetryItemBuilder {
    private RetryItem retryItem;
    private MultivaluedHashMap multivaluedHashMap = new MultivaluedHashMap();

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
    public RetryItemBuilder withNextRetry(int nextRetry){
        this.retryItem.setNextRetry(nextRetry);
        return this;
    }


    public RetryItemBuilder withFallbackHttpUrl(String url){
        this.retryItem.setFallbackHttpUri(url);
        return this;
    }

    public RetryItemBuilder withChannel(String channel){
        this.retryItem.setChannel(channel);
        return this;
    }

    public RetryItemBuilder withHeader(String name , String value){
        this.multivaluedHashMap.putSingle(name,value);
        return this;
    }

    public RetryItemBuilder withResponse(boolean response){
        this.retryItem.setNeedResponse(response);
        return this;
    }

    public RetryItem build(){
        final ObjectMapper mapper = new ObjectMapper();
        try {
            this.retryItem.setHeaders(mapper.writeValueAsString(this.multivaluedHashMap));
        }catch (JsonProcessingException e){
           log.error(e.toString());
        }
        return this.retryItem;
    }



}

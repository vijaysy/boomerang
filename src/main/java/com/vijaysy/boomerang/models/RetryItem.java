package com.vijaysy.boomerang.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.NotNull;
import com.vijaysy.boomerang.enums.HttpMethod;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by vijaysy on 01/04/16.
 */
@Slf4j
@Entity
@ToString
@Table(name="retry_item",indexes ={
        @Index(columnList = "message_id", name = "message_id")
})
public class RetryItem implements Serializable{

    //TODO:timestamps

    public RetryItem(){}

    public RetryItem(String messageId, String message , HttpMethod httpMethod, String httpUri, int nextRetry, int[] retryPattern,String fHttpUri, String channel,String headers,boolean needResponse,int retryStatusCode){
        this.messageId=messageId;
        this.message=message;
        this.httpMethod=httpMethod;
        this.httpUri=httpUri;
        this.nextRetry=nextRetry;
        this.retryPattern=Arrays.toString(retryPattern).replaceAll("\\[|\\]||\\s", "").replace(" ","");
        this.maxRetry=retryPattern.length;
        this.fallbackHttpUri =fHttpUri;
        this.channel=channel;
        this.headers=headers;
        this.needResponse=needResponse;
        this.retryStatusCode=retryStatusCode;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true)
    private int id;

    @Column(name = "message_id",unique = true)
    @NotNull
    private String messageId;

    @Column(name = "message")
    private String message;

    @Column(name = "http_method")
    private HttpMethod httpMethod;

    @Column(name = "http_uri")
    private String httpUri;

    @Column(name = "retry_pattern")
    private String retryPattern;

    @Column(name = "next_retry")
    private int nextRetry;

    @Column(name = "max_retry")
    private int maxRetry;

    @Column(name = "channel")
    @NotNull
    private String channel;

    @Column(name = "fallback_http_uri")
    private String fallbackHttpUri;


    @Column(name = "retry_status_code")
    private int retryStatusCode;

    @Column(name = "headers")
    private String headers;

    @Column(name = "need_response")
    private boolean needResponse;


    public int getId() {
        return id;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getFallbackHttpUri() {
        return fallbackHttpUri;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public String[] getRetryPattern() {
        return retryPattern.split(",");
    }

    public int getNextRetry() {
        return nextRetry;
    }

    public String getHttpUri() {
        return httpUri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getMessage() {
        return message;
    }

    public String getChannel() {
        return channel;
    }

    public int getRetryStatusCode() {
        return retryStatusCode;
    }

    public String getHeaders() {
        return headers;
    }

    public boolean getNeedResponse() {
        return needResponse;
    }

    public void setNextRetry(int nextRetry) {
        this.nextRetry = nextRetry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o)) return false;
        if (!(o instanceof RetryItem)) return false;

        RetryItem retryItem = (RetryItem) o;

        if (!getMessageId().equals(retryItem.getMessageId())) return false;
        return getMessage() != null ? getMessage().equals(retryItem.getMessage()) : retryItem.getMessage() == null;

    }

    @Override
    public int hashCode() {
        int result = getMessageId().hashCode();
        result = 31 * result + (getMessage() != null ? getMessage().hashCode() : 0);
        return result;
    }


    public static class RetryItemBuilder {
        private String messageId;
        private String message;
        private HttpMethod httpMethod;
        private String httpUri;
        private int nextRetry;
        private int [] retryPattern;
        private String channel;
        private String fallbackHttpUri;
        private int retryStatusCode;
        private String headers;
        private boolean needResponse;
        private MultivaluedHashMap<String, String> multivaluedHashMap = new MultivaluedHashMap<String, String>();


        public RetryItemBuilder withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public RetryItemBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public RetryItemBuilder withHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public RetryItemBuilder withHttpUri(String httpUri) {
            this.httpUri = httpUri;
            return this;
        }

        public RetryItemBuilder withNextRetry(int nextRetry) {
            this.nextRetry = nextRetry;
            return this;
        }

        public RetryItemBuilder withRetryPattern(int[] retryPattern) {
            this.retryPattern = retryPattern;
            return this;
        }

        public RetryItemBuilder withFallbackHttpUri(String fallbackHttpUri) {
            this.fallbackHttpUri = fallbackHttpUri;
            return this;
        }

        public RetryItemBuilder withChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public RetryItemBuilder withHeaders(String name , String value) {
            this.multivaluedHashMap.putSingle(name,value);
            return this;
        }

        public RetryItemBuilder withRetryStatusCode(int retryStatusCode){
            this.retryStatusCode=retryStatusCode;
            return this;
        }

        public RetryItemBuilder withNeedResponse(boolean needResponse) {
            this.needResponse = needResponse;
            return this;
        }

        public RetryItem createRetryItem() {
            final ObjectMapper objectMapper=new ObjectMapper();
            try {
                headers=objectMapper.writeValueAsString(multivaluedHashMap);
            }catch (JsonProcessingException e){
                log.error(e.toString());
            }
            return new RetryItem(messageId, message, httpMethod, httpUri, nextRetry, retryPattern, fallbackHttpUri, channel, headers, needResponse,retryStatusCode);
        }
    }

}

package com.vijaysy.boomerang.models;

import com.sun.istack.internal.NotNull;
import com.vijaysy.boomerang.enums.HttpMethod;
import lombok.ToString;

import javax.persistence.*;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by vijaysy on 01/04/16.
 */
@Entity
@ToString
@Table(name="retry_item",indexes ={
        @Index(columnList = "message_id", name = "message_id")
})
public class RetryItem implements Serializable{

    //TODO: need to add headers,timestamps
    // TODO: 04/04/16  use primitive datatypes

    public RetryItem(){}

    public RetryItem(String messageId, String message , HttpMethod httpMethod, String httpUri, int nextRetry, Integer[] retryPattern,String fHttpUri,HttpMethod fHttpMethod, String channel){
        this.messageId=messageId;
        this.message=message;
        this.httpMethod=httpMethod;
        this.httpUri=httpUri;
        this.nextRetry=nextRetry;
        setRetryPattern(Arrays.toString(retryPattern).replaceAll("\\[|\\]||\\s", "").replace(" ",""));
        setMaxRetry(retryPattern.length);
        this.fallbackHttpUri =fHttpUri;
        this.channel=channel;

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

    //Post max retry
    // TODO: 04/04/16  do we really need fallback api ?? Can we hit retry api with some flag ??

    @Column(name = "fallback_http_uri")
    private String fallbackHttpUri;

    //retry config

    @Column(name = "retry_status_code")
    int retryStatusCode;

    @Column(name = "headers")
    MultivaluedMap<String, Object> headers;


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

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public void setFallbackHttpUri(String fHttpUri) {
        this.fallbackHttpUri = fHttpUri;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public void setRetryPattern(String retryPattern) {
        this.retryPattern = retryPattern;
    }

    public void setNextRetry(int nextRetry) {
        this.nextRetry = nextRetry;
    }

    public void setHttpUri(String httpUri) {
        this.httpUri = httpUri;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setRetryStatusCode(int retryStatusCode) {
        this.retryStatusCode = retryStatusCode;
    }

    public void setHeaders(MultivaluedMap<String, Object> headers) {
        this.headers = headers;
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
}

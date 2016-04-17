package com.vijaysy.boomerang.models;

import com.vijaysy.boomerang.enums.HttpMethod;

public class RetryItemBuilder {
    private String messageId;
    private String message;
    private HttpMethod httpMethod;
    private String httpUri;
    private int nextRetry;
    private Integer[] retryPattern;
    private String fHttpUri;
    private String channel;
    private String headers;
    private boolean needResponse;

    public RetryItemBuilder setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public RetryItemBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public RetryItemBuilder setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public RetryItemBuilder setHttpUri(String httpUri) {
        this.httpUri = httpUri;
        return this;
    }

    public RetryItemBuilder setNextRetry(int nextRetry) {
        this.nextRetry = nextRetry;
        return this;
    }

    public RetryItemBuilder setRetryPattern(Integer[] retryPattern) {
        this.retryPattern = retryPattern;
        return this;
    }

    public RetryItemBuilder setfHttpUri(String fHttpUri) {
        this.fHttpUri = fHttpUri;
        return this;
    }

    public RetryItemBuilder setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public RetryItemBuilder setHeaders(String headers) {
        this.headers = headers;
        return this;
    }

    public RetryItemBuilder setNeedResponse(boolean needResponse) {
        this.needResponse = needResponse;
        return this;
    }

    public RetryItem createRetryItem() {
        return new RetryItem(messageId, message, httpMethod, httpUri, nextRetry, retryPattern, fHttpUri, channel, headers, needResponse);
    }
}
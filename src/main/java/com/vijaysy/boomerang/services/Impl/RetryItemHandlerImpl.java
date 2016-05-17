package com.vijaysy.boomerang.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.vijaysy.boomerang.core.restclient.RestClient;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.enums.FallBackReasons;
import com.vijaysy.boomerang.enums.HttpMethod;
import com.vijaysy.boomerang.exception.DBException;
import com.vijaysy.boomerang.models.RetryItem;
import com.vijaysy.boomerang.services.IngestionService;
import com.vijaysy.boomerang.services.RetryItemHandler;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by vijaysy on 10/05/16.
 */
@Slf4j
public class RetryItemHandlerImpl implements RetryItemHandler {

    private final RetryItemDao retryItemDao;
    private final IngestionService ingestionService;
    private final HashMap<HttpMethod, RestClient> httpMethodRestClientHashMap;
    private final ObjectMapper objectMapper;

    @Inject
    public RetryItemHandlerImpl( RetryItemDao retryItemDao, IngestionService ingestionService, HashMap<HttpMethod, RestClient> httpMethodRestClientHashMap) {
        this.retryItemDao = retryItemDao;
        this.ingestionService = ingestionService;
        this.httpMethodRestClientHashMap = httpMethodRestClientHashMap;
        objectMapper = null;
    }

    @Override
    public void handelRetryItem(String messageId) {
        RetryItem retryItem;
        try {
            if (Objects.isNull(retryItem = retryItemDao.get(messageId))) return;
            if (retryItem.getNextRetry() >= retryItem.getMaxRetry()) {
                process(retryItem, FallBackReasons.MAX_RETRY, Response.status(400).build());
                return;
            }
            Response response = httpMethodRestClientHashMap.get(retryItem.getHttpMethod()).invoke(retryItem.getHttpUri(), retryItem.getMessage(), getHeaders(retryItem.getHeaders()), retryItem.getMessageId());
            if (Response.Status.Family.SUCCESSFUL == response.getStatusInfo().getFamily())
                process(retryItem, FallBackReasons.SUCCESSFULL, response);
            else if (response.getStatusInfo().getFamily() == Response.Status.Family.SERVER_ERROR)
                ingestionService.againProcess(retryItem);
            else
                process(retryItem, FallBackReasons.NOT_RETRY_STATUS_CODE, response);
        } catch (Exception e) {
            log.error("Exception {}",e);
        }
    }

    private void process(RetryItem retryItem, FallBackReasons fallBackReasons, Response response) throws DBException {
        retryItem.setFallBackReasons(fallBackReasons);
        retryItem.setProcessed(true);
        retryItem.setResponse(response.getEntity().toString());
        boolean flag = fallBackReasons.equals(FallBackReasons.SUCCESSFULL);
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle("retry", flag);
        headers.putSingle("reason", retryItem.getFallBackReasons());
        Object body = (retryItem.getNeedResponse() && flag) ? response : "";
        Response returnResponse = httpMethodRestClientHashMap.get(HttpMethod.PUT).invoke(retryItem.getFallbackHttpUri() + "/" + retryItem.getMessageId() + "/fallback", body, headers, "executeFallBack");
        retryItem.setReturnFlag(Response.Status.Family.SUCCESSFUL==returnResponse.getStatusInfo().getFamily());
        retryItemDao.update(retryItem);
    }

    private MultivaluedHashMap getHeaders(String headers) {
        try {
            assert objectMapper != null;
            return (headers != null && !headers.isEmpty()) ? objectMapper.readValue(headers, MultivaluedHashMap.class) : null;
        } catch (IOException e) {
            log.error("Exception while deserialization the headers");
            return null;

        }
    }
}

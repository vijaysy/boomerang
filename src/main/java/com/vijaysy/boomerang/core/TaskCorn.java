package com.vijaysy.boomerang.core;

import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Inject;
import com.vijaysy.boomerang.dao.RetryItemDao;
import com.vijaysy.boomerang.models.RetryItem;
import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by vijaysy on 25/04/16.
 */
public class TaskCorn extends Task {

    private final RetryItemDao retryItemDao;

    @Inject
    public TaskCorn(RetryItemDao retryItemDao) {
        super("dbCorn");
        this.retryItemDao=retryItemDao;
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        List<RetryItem> retryItems = retryItemDao.getAll();
        retryItems.forEach(retryItem->{
            output.write(retryItem.toString());
            output.write("\n");
        });
    }
}

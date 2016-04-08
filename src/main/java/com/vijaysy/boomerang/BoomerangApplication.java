package com.vijaysy.boomerang;

import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.vijaysy.boomerang.models.RetryItem;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.errors.EarlyEofExceptionMapper;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Created by vijaysy on 01/04/16.
 */
@Slf4j
public class BoomerangApplication extends Application<BoomerangConfiguration> {
    public static void main(String [] args)throws Exception{
        new BoomerangApplication().run(args);
    }


    @Override
    public void run(BoomerangConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new JsonProcessingExceptionMapper());
        environment.jersey().register(new EarlyEofExceptionMapper());
        environment.jersey().register(new LoggingExceptionMapper<Throwable>() {
        });

    }

    @Override
    public void initialize(Bootstrap<BoomerangConfiguration> bootstrap) {
        HibernateBundle<BoomerangConfiguration> hibernateBundle = new HibernateBundle<BoomerangConfiguration>(
                RetryItem.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(BoomerangConfiguration configuration) {
                return configuration.getDatabase();
            }

            @Override
            public void configure(Configuration configuration) {
                configuration.setNamingStrategy(new ImprovedNamingStrategy());
                configuration.addPackage("com.vijaysy.boomerang.models");
            }
        };

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(GuiceBundle.<BoomerangConfiguration>newBuilder()
                .enableAutoConfig(getClass().getPackage().getName())
                .addModule(new BoomerangModule(hibernateBundle))
                .setConfigClass(BoomerangConfiguration.class)
                .build(Stage.DEVELOPMENT));
    }

    @Override
    public String getName() {
        return "boomerang";
    }

}

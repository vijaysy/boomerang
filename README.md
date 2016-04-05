# boomerang

## Code example
   
   ```java 
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
   ```

1. Redis server should be running on your local machine
   Every time you restart redis have to set notify-keyspace-events
   Command: redis-cli config set notify-keyspace-events KEA
   
2. retry_item table 
   
   ```
   +---------------+--------------+------+-----+---------+----------------+
   | Field         | Type         | Null | Key | Default | Extra          |
   +---------------+--------------+------+-----+---------+----------------+
   | id            | int(11)      | NO   | PRI | NULL    | auto_increment |
   | f_http_method | int(11)      | YES  |     | NULL    |                |
   | f_http_uri    | varchar(255) | YES  |     | NULL    |                |
   | http_method   | int(11)      | YES  |     | NULL    |                |
   | http_uri      | varchar(255) | YES  |     | NULL    |                |
   | max_retry     | int(11)      | YES  |     | NULL    |                |
   | mesasge       | varchar(255) | YES  |     | NULL    |                |
   | message_id    | varchar(255) | YES  | UNI | NULL    |                |
   | next_retry    | int(11)      | YES  |     | NULL    |                |
   | retry_pattern | varchar(255) | YES  |     | NULL    |                |
   +---------------+--------------+------+-----+---------+----------------+
   ```
   
3. hibernate.cfg.xml is under config folder
   

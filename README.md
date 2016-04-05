# boomerang

## Code example

### Sending retry request   
   
   ```java 
    Optional<RetryItem> retryItem = Boomerang.isRetryExist("m33");
           if (retryItem.isPresent())
               Boomerang.reappear(retryItem.get());
           else
               Boomerang.reappear(RetryItemBuilder.create()
                       .withMessageId("m33")
                       .withMessage("Test Message")
                       .withHttpMethod(HttpMethod.POST)
                       .withHttpUrl("http://localhost:8080")
                       ..withRetryPattern(new Integer[]{1, 2, 3,})
                       .withNextRetry(0)
                       .withFallbackHttpMethod(HttpMethod.PUT)
                       .withFallbackHttpUrl("http://localhost:8080/fallback")
                       .withChannel("RT")
                       .build());
   ```
### Listener yml example
    
         groupName: "testGroup"
        
         threadConfigs:
               - name: "Test1"
                 channel: "RT"
                 listenerCount: 5
               - name: "Test2"
                 channel: "RT"
                 listenerCount: 6
        
         redisConfig:
                 host: "localhost"
                 port: 26379
                 db: 0
                 password: "foobared"
                            

    
1. Redis server should be running on your local machine
   Every time you restart redis have to set notify-keyspace-events
   Command: redis-cli config set notify-keyspace-events KEA
   
2. retry_item table 
   
   ```
   +----------------------+--------------+------+-----+---------+----------------+
   | Field                | Type         | Null | Key | Default | Extra          |
   +----------------------+--------------+------+-----+---------+----------------+
   | id                   | int(11)      | NO   | PRI | NULL    | auto_increment |
   | channel              | varchar(255) | YES  |     | NULL    |                |
   | fallback_http_method | int(11)      | YES  |     | NULL    |                |
   | fallback_http_uri    | varchar(255) | YES  |     | NULL    |                |
   | http_method          | int(11)      | YES  |     | NULL    |                |
   | http_uri             | varchar(255) | YES  |     | NULL    |                |
   | max_retry            | int(11)      | YES  |     | NULL    |                |
   | message              | varchar(255) | YES  |     | NULL    |                |
   | message_id           | varchar(255) | YES  | UNI | NULL    |                |
   | next_retry           | int(11)      | YES  |     | NULL    |                |
   | retry_pattern        | varchar(255) | YES  |     | NULL    |                |
   +----------------------+--------------+------+-----+---------+----------------+
   ```
   
3. hibernate.cfg.xml is under config folder
   

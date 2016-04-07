# boomerang

## Code example

### Sending retry request   
   
   ```java 
     Cache cache=new Cache("mymaster","127.0.0.1:26379",2,"foobared",0);
            Optional<RetryItem> retryItem = Boomerang.isRetryExist("m44");
            if (retryItem.isPresent())
                Boomerang.reappear(retryItem.get(),cache);
            else
                Boomerang.reappear(RetryItemBuilder.create()
                        .withMessageId("m44")
                        .withMessage("{\"input\":\"Hi12\"}")
                        .withHttpMethod(HttpMethod.POST)
                        .withHttpUrl("http://localhost:8080/mock/post")
                        .withRetryPattern(integers)
                        .withNextRetry(0)
                        .withFallbackHttpUrl("http://localhost:8080/mock")
                        .withChannel("RT")
                        .build(),cache);
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
        
         cacheConfig:
               master: mymaster
               sentinels: 127.0.0.1:26379
               password: foobared
               timeout: 2
               db: 0
               maxThreads: 8
                            

    
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
   

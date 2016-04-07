package com.vijaysy.boomerang;

import com.vijaysy.boomerang.utils.YMLReader;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by vijaysy on 04/04/16.
 */
@Slf4j
public class YMLTest {
    public static void main(String [] args) throws Exception{
        YMLReader ymlReader = new YMLReader();
        log.info("Testing log");
        log.info(""+ymlReader.getListenerConfig());
    }
}

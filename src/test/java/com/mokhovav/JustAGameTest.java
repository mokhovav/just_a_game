package com.mokhovav;

import com.mokhovav.justAGame.JustAGame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { JustAGame.class })
@SpringBootTest
public class JustAGameTest {

    @Autowired
    private Logger logger;
    @Test
    public void autowiredTest(){
        logger.debug("In JustAGameTest");
    }
}

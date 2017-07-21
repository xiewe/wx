package com.uc.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import com.framework.service.RedisService;

@WebAppConfiguration
public class RedisTest extends BaseTest {
    @Autowired
    private RedisService redisService;

    // @Test
    // public void testVset() throws Exception {
    // redisService.vset("xie", "wenbao");
    // }

    // @Test
    // public void testVdel() throws Exception {
    // redisService.vdel("xie");
    // }

//    @Test
//    public void testSet() throws Exception {
//        redisService.SET("wen", "xiebao");
//    }

     @Test
     public void testDel() throws Exception {
     redisService.DEL("wen");
     }
}

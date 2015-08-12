package com.tuotiansudai.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2015/8/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration
public class RedisWrapperClientTest {



    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Test
    public void testRedisString() {
        redisWrapperClient.set("fuck", "fuck");
        assertThat(redisWrapperClient.get("fuck"), is("fuck"));
        redisWrapperClient.append("fuck", " shit");
        assertThat(redisWrapperClient.get("fuck"), is("fuck shit"));
        redisWrapperClient.del("fuck");
        assertFalse(redisWrapperClient.exists("fuck"));
        redisWrapperClient.setex("damn",5,"damn");
        assertTrue(redisWrapperClient.exists("damn"));
            try {
            Thread.sleep(1000 * 6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(redisWrapperClient.exists("damn"));
    }

    @Test
    public void testRedisList() {
        redisWrapperClient.lpush("listFuck","0","1","2");
        assertThat(redisWrapperClient.llen("listFuck"), is(3));
        redisWrapperClient.lpush("listFuck","3");
        assertThat(redisWrapperClient.llen("listFuck"), is(4));
        List<String> list = redisWrapperClient.lrange("listFuck", 0, -1);
        for (int i=0;i<list.size();i++) {
            assertThat(Integer.valueOf(list.get(i)), is(list.size() - 1 - i));
        }
    }

    @Test
    public void testRedisMap() {
        Map<String, String> fuck = new HashMap<String, String>();
        fuck.put("fuck","0");
        fuck.put("shit","1");
        fuck.put("damn","2");
        redisWrapperClient.hmset("bitch", fuck);
        assertThat(redisWrapperClient.hmget("bitch", "fuck").get(0).toString(), is("0"));
        assertThat(redisWrapperClient.hlen("bitch"), is(3));
        redisWrapperClient.hdel("bitch","fuck","shit");
        assertThat(redisWrapperClient.hlen("bitch"), is(1));
        Iterator<String> iter = redisWrapperClient.hkeys("bitch").iterator();
        String i = "2";
        while (iter.hasNext()) {
            String hkey = iter.next();
            assertThat(redisWrapperClient.hget("bitch", hkey), is(i));
        }
        redisWrapperClient.hset("bitch","cock","3");
        assertThat(redisWrapperClient.hget("bitch","cock"), is("3"));
    }

}

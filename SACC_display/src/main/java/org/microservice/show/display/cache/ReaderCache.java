package org.microservice.show.display.cache;

import org.microservice.show.display.mapper.ReaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ReaderCache {
    @Autowired
    private ReaderMapper readerMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int threshold = 10;
    private static AtomicInteger addTimes = new AtomicInteger(0);

    public int addViewTimes(Integer userCode, Integer bookCode) {
        Boolean ifSet = redisTemplate
                .opsForValue()
                .setIfAbsent("reader:userCode:" + userCode +
                                ":bookCode:" + bookCode + ":view_times", "1");

        if (!ifSet.booleanValue()){
            redisTemplate.opsForValue()
                    .increment("reader:userCode:" + userCode +
                            ":bookCode:" + bookCode + ":view_times",1);
            addTimes.incrementAndGet();
        }

        int view_times= (int) redisTemplate.opsForValue().get("reader:userCode:" + userCode +
                ":bookCode:" + bookCode + ":view_times");

        if (addTimes.get() >= threshold) {
            return readerMapper.addViewTimes(userCode, bookCode,view_times);
        }
        return 1;
    }

}

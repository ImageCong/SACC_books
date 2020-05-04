package org.microservice.show.display.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.microservice.pub.commons.entity.RecommendBook;
import org.microservice.pub.commons.service.RecommendBookService;
import org.microservice.show.display.mapper.RecommendBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Service(version = "1.0.0")
public class RecommendBookServiceImpl implements RecommendBookService {
    @Autowired
    private RecommendBookMapper recommendBookMapper;

    @Override
    public List<RecommendBook> selectRecommend(Integer userCode) {
        List<RecommendBook> recommendBooks = recommendBookMapper.selectRecommend(userCode);

        if (recommendBooks == null){
            log.info("=================没有推荐书目===================");
        }

        return recommendBooks;
    }
}

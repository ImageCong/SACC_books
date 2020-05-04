package org.microservice.pub.commons.service;

import org.microservice.pub.commons.entity.RecommendBook;

import java.util.List;

public interface RecommendBookService {
    // 根据userCode查找推荐给他的图书
    List<RecommendBook> selectRecommend(Integer userCode);
}

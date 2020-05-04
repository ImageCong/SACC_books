package org.microservice.show.display.mapper;

import org.microservice.pub.commons.entity.RecommendBook;

import java.util.List;

public interface RecommendBookMapper {
    // 根据userCode查找推荐给他的图书
    List<RecommendBook> selectRecommend(Integer userCode);
}

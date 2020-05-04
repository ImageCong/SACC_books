package org.microservice.pub.commons.service;

import org.microservice.pub.commons.entity.Reader;

import java.io.IOException;
import java.util.List;

public interface ReaderService {
    // 查找某个用户的全部推荐书目
    List<Reader> selectByUCode(Integer userCode);

    // 查找某本书应该被哪些用户阅读过
    List<Reader> selectByBCode(Integer bookCode);

    // 增加浏览次数
    int addViewTimes(Integer userCode, Integer bookCode);

    // 增加阅读记录
    int insertRecord(Integer userCode, Integer bookCode, Integer viewTimes);

    // 查询所有记录并导出txt文件
    void exportRecords();
}

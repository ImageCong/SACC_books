package org.microservice.show.display.mapper;

import org.apache.ibatis.annotations.Param;
import org.microservice.pub.commons.entity.Reader;

import java.util.List;

public interface ReaderMapper {
    // 查找某个用户的全部推荐书目
    List<Reader> selectByUCode(Integer userCode);

    // 查找某本书应该被哪些用户阅读过
    List<Reader> selectByBCode(Integer bookCode);

    // 增加浏览次数
    int addViewTimes(@Param("userCode") Integer userCode, @Param("bookCode") Integer bookCode,@Param("times") Integer times);

    // 增加阅读记录
    int insertRecord(Reader reader);

    // 列出所有记录（不含id），用于导出txt
    List<Reader> listAllRecords();
}

package org.microservice.show.display.mapper;

import org.microservice.pub.commons.entity.Book;

import java.util.List;

public interface BookMapper {
    // 根据索书号找到书
    List<Book> findBookbyCode(Integer bookCode);

    // 根据书名找到书
    List<Book> findBookbyName(String bookName);

    // 列出所有书
    List<Book> listBook(Integer offset);

    // 新增图书
    int insertBook(Book book);
}

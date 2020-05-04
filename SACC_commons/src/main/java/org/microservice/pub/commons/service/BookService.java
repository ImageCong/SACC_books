package org.microservice.pub.commons.service;

import org.microservice.pub.commons.entity.Book;

import java.util.List;

public interface BookService {
    // 根据索书号找到图书
    List<Book>  findBookbyCode(Integer bookCode);

    // 根据书名找到图书
    List<Book>  findBookbyName(String bookName);

    // 列出所有图书
    List<Book> listBook();

    // 新增图书
    int insertBook(Book book);
}

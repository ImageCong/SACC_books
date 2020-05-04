package org.microservice.show.display.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.microservice.pub.commons.entity.Book;
import org.microservice.pub.commons.service.BookService;
import org.microservice.show.display.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Service(version = "1.0.0")
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<Book> findBookbyCode(Integer bookCode) {

        List<Book> bookbyCode = bookMapper.findBookbyCode(bookCode);
        log.info("=========findBookbyCode=========   " + bookbyCode);
        return bookbyCode;
    }

    @Override
    public List<Book> findBookbyName(String bookName) {

        List<Book> bookbyName = bookMapper.findBookbyName(bookName);
        log.info("=========findBookbyName=========   " + bookbyName);

        return bookbyName;
    }

    @Override
    public List<Book> listBook() {
        List<Book> books = bookMapper.listBook();

        log.info("=========listBook=========   ");
        for (Book b : books) {
            log.info("------ " + b + " ------");
        }

        return books;
    }

    @Override
    public int insertBook(Book book) {
        int isSuccess = bookMapper.insertBook(book);

        if (isSuccess == 1) {
            log.info("========= insertBook success =========   ");
            return 1;
        } else {
            log.info("========= insertBook fail =========   ");
            return 0;
        }
    }
}

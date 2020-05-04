package org.microservice.show.display;

import org.junit.jupiter.api.Test;
import org.microservice.pub.commons.service.BookService;
import org.microservice.pub.commons.service.ReaderService;
import org.microservice.pub.commons.service.RecommendBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class DisplayApplicationTests {
    @Autowired
    private BookService bookService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private RecommendBookService recommendBookService;

    @Test
    void contextLoads()  {
        readerService.exportRecords();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

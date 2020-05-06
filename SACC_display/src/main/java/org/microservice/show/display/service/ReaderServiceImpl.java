package org.microservice.show.display.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.microservice.pub.commons.entity.Reader;
import org.microservice.pub.commons.service.ReaderService;
import org.microservice.show.display.cache.ReaderCache;
import org.microservice.show.display.mapper.ReaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Service(version = "1.0.0")
public class ReaderServiceImpl implements ReaderService {
    @Autowired
    private ReaderMapper readerMapper;
    @Autowired
    private ReaderCache readerCache;

    @Override
    public List<Reader> selectByUCode(Integer userCode) {
        List<Reader> readerRecords = readerMapper.selectByUCode(userCode);

        if (readerRecords == null) {
            log.info("=========This user have no book to recommend=========");
        }
        for (Reader reader : readerRecords) {
            log.info("------ " + reader + " ------");
        }

        return readerRecords;
    }

    @Override
    public List<Reader> selectByBCode(Integer bookCode) {
        List<Reader> readerRecords = readerMapper.selectByBCode(bookCode);

        if (readerRecords == null) {
            log.info("=========This book have no user to recommend=========");
        }
        for (Reader reader : readerRecords) {
            log.info("------ " + reader + " ------");
        }

        return readerRecords;
    }

    @Override
    public int addViewTimes(Integer userCode, Integer bookCode) {
        int isAddSuccess = readerCache.addViewTimes(userCode, bookCode);

        if (isAddSuccess == 1) {
            log.info("========= addViewTimes success =========   ");
            return 1;
        } else {
            log.info("========= addViewTimes fail =========   ");
            return 0;
        }
    }

    @Override
    public int insertRecord(Integer userCode, Integer bookCode, Integer viewTimes) {
        Reader reader = new Reader();
        reader.setUserCode(userCode);
        reader.setBookCode(bookCode);
        reader.setViewTimes(1);

        int isInsertSuccess = readerMapper.insertRecord(reader);
        if (isInsertSuccess == 1) {
            log.info("========= insertRecord success =========   ");
            return 1;
        } else {
            log.info("========= insertRecord fail =========   ");
            return 0;
        }
    }

    @Override
    @PostConstruct
    public void exportRecords() {
        String path = "C:\\Users\\cyx_i\\IdeaProjects\\SACC_books" +
                "\\SACC_recommend\\src\\main\\resources\\static\\douban-rating.txt";

        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();

        log.info("===================数据集导出计划开始=====================");
        service.scheduleAtFixedRate(() -> {
            try {
                executeExport(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 1, 60 * 60 * 12, TimeUnit.SECONDS);
    }

    private void executeExport(String path) throws IOException {
        List<Reader> allRecords = readerMapper.listAllRecords();

        List<String> readyToWrite = new ArrayList<>();
        for (Reader r : allRecords) {
            readyToWrite.add(r.getUserCode() + "," + r.getBookCode() + "," + r.getViewTimes());
        }

        File file = new File(path);

        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        for (String s : readyToWrite) {
            writer.write(s + "\r\n");
        }
        writer.close();
        ;
    }

}

package org.microservice.ml.recommend.recommend;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.mahout.cf.taste.common.TasteException;
import org.microservice.ml.recommend.util.BookResult;

public class TaskTimer {

	// 每天执行一次
	final static int TIME = 60 * 60 * 24;

	public static void runTask(){
		Runnable runnable = new Runnable() {
            public void run() {
                try {
                	String filePath = "C:\\Users\\cyx_i\\IdeaProjects\\SACC_books\\" +
							"SACC_recommend\\src\\main\\resources\\static\\douban-rating.txt";
					/**
					 * 进入训练
					 */
                	BookResult.runBookResult(filePath);
				} catch (TasteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        };

		// 周期性执行
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.scheduleAtFixedRate(runnable, 10, TIME, TimeUnit.SECONDS);
	}

}

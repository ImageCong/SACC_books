package org.microservice.ml.recommend;

import org.microservice.ml.recommend.recommend.TaskTimer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecommendApplication {

    public static void main(String[] args) {
        TaskTimer.runTask();
        SpringApplication.run(RecommendApplication.class, args);
    }

}

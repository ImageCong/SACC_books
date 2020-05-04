package org.microservice.ml.recommend;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

@SpringBootTest
class RecommendApplicationTests {

    @Test
    void contextLoads() throws Exception {
        // 数据模型
        DataModel dataModel = new FileDataModel
                (new File("C:\\Users\\cyx_i\\IdeaProjects\\SACC_books\\SACC_recommend\\src\\main\\resources\\static\\douban-rating-300.txt"));

        // 创建用户相似度模型
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);

        // 近邻对象
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(1.0, similarity, dataModel);

        // 推荐器(基于用户的)
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

        // 开始推荐
        List<RecommendedItem> recommendedItems = recommender.recommend(2,3);

        System.out.println("======================");
        System.out.println(recommendedItems);
        System.out.println("======================");

        for (RecommendedItem item : recommendedItems){
            System.out.println(item.getItemID());
        }

    }


    @Test
    void test2(){

    }

}

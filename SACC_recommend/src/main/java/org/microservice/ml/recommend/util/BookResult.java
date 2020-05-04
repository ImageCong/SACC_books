package org.microservice.ml.recommend.util;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 为得到差异化结果，测试时使用4个算法：
 * userEuclidean,itemEuclidean，userEuclideanNoPref，itemEuclideanNoPref
 * 对推荐结果人工比较。
 *
 * 上线时只使用itemEuclideanNoPref
 */

public class BookResult {

	final static int NEIGHBORHOOD_NUM = 2;
    final static int RECOMMENDER_NUM = 3;

    // 调用链开始处，需要controller调用这个方法
//    public static void runBookResult(String filePath) throws TasteException, IOException, SQLException {
//    	String file = filePath;
//    	// 创建模型
//        DataModel dataModel = RecommendFactory.buildDataModel(file);
//
//        // 分别得到4个算法的推荐器
//        RecommenderBuilder rb1 = BookEvaluator.userEuclidean(dataModel);
//        RecommenderBuilder rb2 = BookEvaluator.itemEuclidean(dataModel);
//        RecommenderBuilder rb3 = BookEvaluator.userEuclideanNoPref(dataModel);
//        RecommenderBuilder rb4 = BookEvaluator.itemEuclideanNoPref(dataModel);
//
//        RecommendFactory.connectDatabase();
//        RecommendFactory.deleteAllrecords();
//
//        LongPrimitiveIterator iter = dataModel.getUserIDs();
//        while (iter.hasNext()) {
//            long uid = iter.nextLong();
//            System.out.print("userEuclidean       =>");
//            result(uid, rb1, dataModel,0);
//            System.out.print("itemEuclidean       =>");
//            result(uid, rb2, dataModel,1);
//            System.out.print("userEuclideanNoPref =>");
//            result(uid, rb3, dataModel,2);
//            System.out.print("itemEuclideanNoPref =>");
//            result(uid, rb4, dataModel,3);
//        }
//    }

    public static void runBookResult(String filePath) throws TasteException, IOException, SQLException {
        String file = filePath;
        // 创建模型
        DataModel dataModel = RecommendFactory.buildDataModel(file);

        // 得到 itemEuclideanNoPref 推荐器
        RecommenderBuilder rb = BookEvaluator.itemEuclideanNoPref(dataModel);

        RecommendFactory.connectDatabase();
        // 删除recommend_book数据库的全部数据
        RecommendFactory.deleteAllrecords();

        LongPrimitiveIterator iter = dataModel.getUserIDs();
        while (iter.hasNext()) {
            long uid = iter.nextLong();
            System.out.print("itemEuclideanNoPref =>");
            result(uid, rb, dataModel,3);
        }
    }


    public static void result(long uid, RecommenderBuilder recommenderBuilder, DataModel dataModel,int type) throws TasteException, SQLException {
        List<RecommendedItem> list = recommenderBuilder.buildRecommender(dataModel).recommend(uid, RECOMMENDER_NUM);
        RecommendFactory.showItemsWithSQL(uid, list, false,type);
    }

    // 测试
//    public static void main(String[] args) throws TasteException, IOException, SQLException {
//    	runBookResult("C:\\Users\\cyx_i\\IdeaProjects\\SACC_books\\SACC_recommend\\src\\main\\resources\\static\\douban-rating-300.txt");
//    }


}

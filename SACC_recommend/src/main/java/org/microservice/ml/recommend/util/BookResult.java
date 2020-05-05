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

}

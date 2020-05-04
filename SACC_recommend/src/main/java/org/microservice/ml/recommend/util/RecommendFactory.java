package org.microservice.ml.recommend.util;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.*;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public final class RecommendFactory {

    static Connection conn = null;
    static Statement sta = null;
    static ResultSet rs = null;

    public static void connectDatabase() throws SQLException {
        PooledDataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
        Properties properties = new Properties();
        properties.put("username", "root");
        properties.put("password", "root");
        properties.put("url","jdbc:mysql://localhost:3306/sacc_book?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT");
        properties.put("driver", "com.mysql.cj.jdbc.Driver");
        dataSourceFactory.setProperties(properties);
        DataSource dataSource = dataSourceFactory.getDataSource();

        if (conn == null) {
            conn = dataSource.getConnection();
            sta = conn.createStatement();
        }
    }

    /**
     * build Data model from file
     */
    public static DataModel buildDataModel(String file) throws TasteException, IOException {
        return new FileDataModel(new File(file));
    }

    public static DataModel buildDataModelNoPref(String file) throws TasteException, IOException {
        return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(new FileDataModel(new File(file))));
    }

    public static DataModelBuilder buildDataModelNoPrefBuilder() {
        return new DataModelBuilder() {
            public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
                return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(trainingData));
            }
        };
    }

    /**
     * build Data model from database
     */
    public static DataModel buildDataModelFromDatabase(String serverName, String user, String password, String databaseName) throws TasteException, IOException {
        // 获取数据库连接
        PooledDataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
        DataSource dataSource = dataSourceFactory.getDataSource();

        JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource,
                "reader_copy", "userCode",
                "bookCode", "rating", "time");

        return dataModel;

    }

    public static DataModel buildDataModelNoPrefFromDatabase(String file) throws TasteException, IOException {
        return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(new FileDataModel(new File(file))));
    }

    /**
     * similarity
     */
    public enum SIMILARITY {
        PEARSON, EUCLIDEAN, COSINE, TANIMOTO, LOGLIKELIHOOD, SPEARMAN, CITYBLOCK, FARTHEST_NEIGHBOR_CLUSTER, NEAREST_NEIGHBOR_CLUSTER
    }

    public static UserSimilarity userSimilarity(SIMILARITY type, DataModel m) throws TasteException {
        switch (type) {
            case PEARSON:
                return new PearsonCorrelationSimilarity(m);
            case COSINE:
                return new UncenteredCosineSimilarity(m);
            case TANIMOTO:
                return new TanimotoCoefficientSimilarity(m);
            case LOGLIKELIHOOD:
                return new LogLikelihoodSimilarity(m);
            case SPEARMAN:
                return new SpearmanCorrelationSimilarity(m);
            case CITYBLOCK:
                return new CityBlockSimilarity(m);
            case EUCLIDEAN:
            default:
                return new EuclideanDistanceSimilarity(m);
        }
    }

    public static ItemSimilarity itemSimilarity(SIMILARITY type, DataModel m) throws TasteException {
        switch (type) {
            case PEARSON:
                return new PearsonCorrelationSimilarity(m);
            case COSINE:
                return new UncenteredCosineSimilarity(m);
            case TANIMOTO:
                return new TanimotoCoefficientSimilarity(m);
            case LOGLIKELIHOOD:
                return new LogLikelihoodSimilarity(m);
            case CITYBLOCK:
                return new CityBlockSimilarity(m);
            case EUCLIDEAN:
            default:
                return new EuclideanDistanceSimilarity(m);
        }
    }

//    public static ClusterSimilarity clusterSimilarity(SIMILARITY type, UserSimilarity us) throws TasteException {
//        switch (type) {
//        case NEAREST_NEIGHBOR_CLUSTER:
//            return new NearestNeighborClusterSimilarity(us);
//        case FARTHEST_NEIGHBOR_CLUSTER:
//        default:
//            return new FarthestNeighborClusterSimilarity(us);
//        }
//    }

    /**
     * neighborhood
     */
    public enum NEIGHBORHOOD {
        NEAREST, THRESHOLD
    }

    public static UserNeighborhood userNeighborhood(NEIGHBORHOOD type, UserSimilarity s, DataModel m, double num) throws TasteException {
        switch (type) {
            case NEAREST:
                return new NearestNUserNeighborhood((int) num, s, m);
            case THRESHOLD:
            default:
                return new ThresholdUserNeighborhood(num, s, m);
        }
    }

    /**
     * recommendation
     */
    public enum RECOMMENDER {
        USER, ITEM
    }

    public static RecommenderBuilder userRecommender(final UserSimilarity us, final UserNeighborhood un, boolean pref) throws TasteException {
        return pref ? new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new GenericUserBasedRecommender(model, un, us);
            }
        } : new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new GenericBooleanPrefUserBasedRecommender(model, un, us);
            }
        };
    }

    public static RecommenderBuilder itemRecommender(final ItemSimilarity is, boolean pref) throws TasteException {
        return pref ? new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new GenericItemBasedRecommender(model, is);
            }
        } : new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new GenericBooleanPrefItemBasedRecommender(model, is);
            }
        };
    }

//    public static RecommenderBuilder slopeOneRecommender() throws TasteException {
//        return new RecommenderBuilder() {
//            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
//
//                return new SlopeOneRecommender(dataModel);
//            }
//
//        };
//    }

    public static RecommenderBuilder itemKNNRecommender(final ItemSimilarity is) throws TasteException {
        return new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                return new GenericItemBasedRecommender(dataModel, is);
            }
        };
    }

    public static RecommenderBuilder svdRecommender(final Factorizer factorizer) throws TasteException {
        return new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                return new SVDRecommender(dataModel, factorizer);
            }
        };
    }

//    public static RecommenderBuilder treeClusterRecommender(final ClusterSimilarity cs, final int n) throws TasteException {
//        return new RecommenderBuilder() {
//            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
//                return new TreeClusteringRecommender(dataModel, cs, n);
//            }
//        };
//    }

    public static void showItems(long uid, List<RecommendedItem> recommendations, boolean skip) {
        if (!skip || recommendations.size() > 0) {
            System.out.printf("uid:%s,", uid);
            for (RecommendedItem recommendation : recommendations) {
                System.out.printf("(%s,%f)", recommendation.getItemID(), recommendation.getValue());
            }
            System.out.println();
        }
    }

    /**
     * find bookName from database
     */
    public static void showItemsWithSQL(long uid, List<RecommendedItem> recommendations, boolean skip, int type) {
        if (!skip || recommendations.size() > 0) {
            System.out.printf("uid:%s,", uid);
            for (RecommendedItem recommendation : recommendations) {
                String bCode = recommendation.getItemID() + "";
                // 从book表拿到书名
                String bName = getBookNameByBookCode(bCode);
                System.out.printf("(%s [%s], %f)", bName, bCode, recommendation.getValue());
                String rating = String.format("%f", recommendation.getValue());
                // 计算rating，并将数据集得到的结果存入数据库
                insertRecommendBook(uid + "", bName, rating, type + "");
            }
            System.out.println();
        }
    }

    public static String getBookNameByBookCode(String bc) {
        String bookName = "";

        String sql = String.format("select distinct bookName from book where bookCode = %s ", bc);
        try {
            rs = sta.executeQuery(sql);
            while (rs.next()) {
                bookName = rs.getString("bookName");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bookName;
    }

    public static void insertRecommendBook(String userCode, String bookName, String rating, String type) {

        String sql = String.format("insert into recommendBook(userCode,recommendBook,rating,type) values(%s,'%s',%s,%s) ", userCode, bookName, rating, type);
        try {
            boolean ok = sta.execute(sql);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void deleteAllrecords() {
        String sql = String.format("delete from recommendBook");
        try {
            boolean ok = sta.execute(sql);
            System.out.println("delete from recommendBook ...." + ok);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * evaluator
     */
    public enum EVALUATOR {
        AVERAGE_ABSOLUTE_DIFFERENCE, RMS
    }

    public static RecommenderEvaluator buildEvaluator(EVALUATOR type) {
        switch (type) {
            case RMS:
                return new RMSRecommenderEvaluator();
            case AVERAGE_ABSOLUTE_DIFFERENCE:
            default:
                return new AverageAbsoluteDifferenceRecommenderEvaluator();
        }
    }

    public static void evaluate(EVALUATOR type, RecommenderBuilder rb, DataModelBuilder mb, DataModel dm, double trainPt) throws TasteException {
        System.out.printf("%s Evaluater Score:%s\n", type.toString(), buildEvaluator(type).evaluate(rb, mb, dm, trainPt, 1.0));
    }

    public static void evaluate(RecommenderEvaluator re, RecommenderBuilder rb, DataModelBuilder mb, DataModel dm, double trainPt) throws TasteException {
        System.out.printf("Evaluater Score:%s\n", re.evaluate(rb, mb, dm, trainPt, 1.0));
    }

    /**
     * statsEvaluator
     */
    public static void statsEvaluator(RecommenderBuilder rb, DataModelBuilder mb, DataModel m, int topn) throws TasteException {
        RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
        IRStatistics stats = evaluator.evaluate(rb, mb, m, null, topn, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
        System.out.printf("Recommender IR Evaluator: [Precision:%s,Recall:%s]\n", stats.getPrecision(), stats.getRecall());
    }

}

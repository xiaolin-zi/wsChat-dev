/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.mahout;

import com.lxg.wschat.service.UserService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TestUserMahout
 *
 * @author linxugeng
 * @since 2023/12/11
 */
public class TestUserMahout {
    @Autowired
    static
    UserService userService;



    public static void main(String[] args) {
//        File file = new File("D:\\testMahout.txt");
        // 实例化DataModel并将数据传入其内
        DataModel dataModel = null;
        //            dataModel = new FileDataModel(file);
//        List<MahoutDataModel> list = userService.getDataModel();
//        dataModel = buildJdbcDataModel(list);
        //余弦相似度
        try {
            UserSimilarity userSimilarity = new UncenteredCosineSimilarity(dataModel);
            //欧几里得相似度
            UserSimilarity userSimilarity1 = new EuclideanDistanceSimilarity(dataModel);
            //皮尔森相似度
            UserSimilarity userSimilarity2 = new PearsonCorrelationSimilarity(dataModel);

            //阈值相似度
            UserNeighborhood userNeighborhood = new ThresholdUserNeighborhood(0.3, userSimilarity, dataModel);
//            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2,userSimilarity2,dataModel);
            // 构建推荐器，使用基于用户的协同过滤推荐
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
            //从数据模型中获取所有用户的ID迭代器
            LongPrimitiveIterator usersIterator = dataModel.getUserIDs();
            //通过迭代器遍历所有用户ID
            while (usersIterator.hasNext()) {
                System.out.println("=======================================");
                //用户ID
                long userID = usersIterator.nextLong();
                //用户ID迭代器
                LongPrimitiveIterator otherusersIterator = dataModel.getUserIDs();
                //相当于两个for循环，遍历用户ID，计算任何两个用户的相似度
                while (otherusersIterator.hasNext()) {
                    Long otherUserID = otherusersIterator.nextLong();
                    System.out.println("用户 " + userID
                            + " 与用户 " + otherUserID + " 的相似度为 "
                            + userSimilarity2.userSimilarity(userID, otherUserID));
                }

                //userID的N-最近邻
                long[] userN = userNeighborhood.getUserNeighborhood(userID);
                //用户userID的推荐物品，最多推荐两个
                List<RecommendedItem> recommendedItems = recommender.recommend(userID, 10);
                System.out.println("用户 " + userID + " 的2-最近邻是 " + Arrays.toString(userN));
                if (recommendedItems.size() > 0) {
                    for (RecommendedItem item : recommendedItems) {
                        System.out.println("推荐的物品 " + item.getItemID()
                                + "预测评分是 " + item.getValue());
                    }
                } else {
                    System.out.println("无任何物品推荐");
                }
            }


        } catch (TasteException e) {
            throw new RuntimeException(e);
        }


    }
}

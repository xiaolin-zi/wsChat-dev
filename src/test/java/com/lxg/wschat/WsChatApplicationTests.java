package com.lxg.wschat;

import com.lxg.wschat.mahout.MahoutDataModel;
import com.lxg.wschat.service.UserService;
import com.lxg.wschat.utils.CreateGroupIdUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WsChatApplicationTests {
    @Autowired
    UserService userService;

    /**
     * dataModel 有两种结构：
     * GenericDataModel: 用户ID，物品ID，用户对物品的打分(UserID,ItemID,PreferenceValue)
     * GenericBooleanPrefDataModel: 用户ID，物品ID (UserID,ItemID)，这种方式表达用户是否浏览过该物品，但并未对物品进行打分。
     * 因为系统需要根据用户行为或评分进行推荐所以使用GenericDataModel
     * @param preferenceList 用户行为或评分集合
     * @return DataModel
     */
    private static DataModel buildJdbcDataModel(List<MahoutDataModel> preferenceList) {
        FastByIDMap<PreferenceArray> fastByIdMap = new FastByIDMap<>();
        Map<Long, List<MahoutDataModel>> map = preferenceList.stream().collect(Collectors.groupingBy(MahoutDataModel::getUserId));
        Collection<List<MahoutDataModel>> list = map.values();
        for (List<MahoutDataModel> preferences : list) {
            GenericPreference[] array = new GenericPreference[preferences.size()];
            for (int i = 0; i < preferences.size(); i++) {
                MahoutDataModel preference = preferences.get(i);
                GenericPreference item = new GenericPreference(preference.getUserId(), preference.getItemId(), preference.getScore());
                array[i] = item;
            }
            fastByIdMap.put(array[0].getUserID(), new GenericUserPreferenceArray(Arrays.asList(array)));
        }
        return new GenericDataModel(fastByIdMap);
    }

    @Test
    void contextLoads() {
        File file = new File("D:\\testMahout.txt");
        // 实例化DataModel并将数据传入其内
        DataModel dataModel = null;
        try {
            dataModel = new FileDataModel(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        List<MahoutDataModel> list = userService.getDataModel();
//        System.out.println(list);
//        dataModel = buildJdbcDataModel(list);

        try {
            //余弦相似度
            UserSimilarity userSimilarity = new UncenteredCosineSimilarity(dataModel);
            //欧几里得相似度
//            UserSimilarity userSimilarity = new EuclideanDistanceSimilarity(dataModel);
            //皮尔森相似度
//            UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
            //阈值相似度
            UserNeighborhood userNeighborhood = new ThresholdUserNeighborhood(0.5, userSimilarity, dataModel);
//            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(2,userSimilarity2,dataModel);
            // 构建推荐器，使用基于用户的协同过滤推荐
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
            //从数据模型中获取所有用户的ID迭代器
            LongPrimitiveIterator usersIterator = dataModel.getUserIDs();
            List<RecommendedItem> recommendedItemList = null;
            recommendedItemList = recommender.recommend(1, 10);
            System.out.println(recommendedItemList);




            //通过迭代器遍历所有用户ID
//            while (usersIterator.hasNext()) {
//                System.out.println("=======================================");
//                //用户ID
//                long userID = usersIterator.nextLong();
//                //用户ID迭代器
//                LongPrimitiveIterator otherusersIterator = dataModel.getUserIDs();
//                //相当于两个for循环，遍历用户ID，计算任何两个用户的相似度
//                while (otherusersIterator.hasNext()) {
//                    Long otherUserID = otherusersIterator.nextLong();
//                    System.out.println("用户 " + userID
//                            + " 与用户 " + otherUserID + " 的相似度为 "
//                            + userSimilarity.userSimilarity(userID, otherUserID));
//                }
//                //userID的N-最近邻
//                long[] userN = userNeighborhood.getUserNeighborhood(userID);
//                //用户userID的推荐物品，最多推荐两个
//                List<RecommendedItem> recommendedItems = recommender.recommend(userID, 10);
//                System.out.println("用户 " + userID + " 的2-最近邻是 " + Arrays.toString(userN));
//                if (recommendedItems.size() > 0) {
//                    for (RecommendedItem item : recommendedItems) {
//                        System.out.println("推荐的物品 " + item.getItemID()
//                                + "预测评分是 " + item.getValue());
//                    }
//                } else {
//                    System.out.println("无任何物品推荐");
//                }
//            }


        } catch (TasteException e) {
            throw new RuntimeException(e);
        }


    }





}

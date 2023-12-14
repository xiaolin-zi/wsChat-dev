/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.mahout;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MyRecommender
 *
 * @author linxugeng
 * @since 2023/12/12
 */
public class MyRecommender {
    /**
     * dataModel 有两种结构：
     * GenericDataModel: 用户ID，物品ID，用户对物品的打分(UserID,ItemID,PreferenceValue)
     * GenericBooleanPrefDataModel: 用户ID，物品ID (UserID,ItemID)，这种方式表达用户是否浏览过该物品，但并未对物品进行打分。
     * 因为系统需要根据用户行为或评分进行推荐所以使用GenericDataModel
     * @param preferenceList 用户行为或评分集合
     * @return DataModel
     */
    public static DataModel buildJdbcDataModel(List<MahoutDataModel> preferenceList) {
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

    /**
     * 获取相似度算法
     * @param type
     * @param m
     * @return
     * @throws TasteException
     */
    public static Refreshable getSimilarity(String type, DataModel m) throws TasteException {
        switch (type) {
            case RecommenderConstants.SIMILARITY_PEARSON:
                return new PearsonCorrelationSimilarity(m);
            case RecommenderConstants.SIMILARITY_COSINE:
                return new UncenteredCosineSimilarity(m);
            case RecommenderConstants.SIMILARITY_TANIMOTO:
                return new TanimotoCoefficientSimilarity(m);
            case RecommenderConstants.SIMILARITY_LOGLIKELIHOOD:
                return new LogLikelihoodSimilarity(m);
            case RecommenderConstants.SIMILARITY_CITY_BLOCK:
                return new CityBlockSimilarity(m);
            case RecommenderConstants.SIMILARITY_EUCLIDEAN:
            case RecommenderConstants.SIMILARITY_SPEARMAN:
            default:
                return new SpearmanCorrelationSimilarity(m);
        }
    }

}

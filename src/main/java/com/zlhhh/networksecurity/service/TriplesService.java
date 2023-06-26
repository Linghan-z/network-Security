package com.zlhhh.networksecurity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlhhh.networksecurity.entity.Triples;

import java.util.List;

public interface TriplesService extends IService<Triples> {
    /**
     * 获取organization的相关实体，不包含ip、domain、sha256，返回json
     * @param organizationNameList
     */
    String getRelevantEntitiesOfOrganizations(List<String> organizationNameList);

    Object createRelation(Integer tripleId);

    void deleteRelation(Integer tripleId);

    void deleteRelationBatch(List<Integer> entityIds);

    /**
     * 获取某实体的相关实体以及关系的json
     *
     * @param entityLabel
     * @param entityValue
     * @return json
     */
    String getRelevantEntitiesOfOtherEntities(String entityLabel, String entityValue);

    /**
     * 获取给定关系下的所有实体以及关系的json
     * @param triplesList
     * @return json
     */
    String getTriplesGivenRelations(List<Triples> triplesList);
}

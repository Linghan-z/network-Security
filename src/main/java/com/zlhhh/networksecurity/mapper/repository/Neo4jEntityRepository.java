package com.zlhhh.networksecurity.mapper.repository;

import com.zlhhh.networksecurity.entity.Neo4jBaseNodeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Map;

/**
 * 实现neo4j的增删改
 */
public interface Neo4jEntityRepository extends Neo4jRepository<Neo4jBaseNodeEntity, Long> {
    /**
     * 创建新的节点
     *
     * @param label      节点的label
     * @param properties HashMap，包含了节点的所有属性
     * @return
     */
    @Query("CREATE (n:`:#{literal(#label)}` $properties) RETURN n.value")
    String createNode(@Param("label") String label, @Param("properties") Map<String, Object> properties);


    /**
     * 修改现有的属性
     * @param label         类别
     * @param id            根据neo4j中的id找到节点
     * @param value         值
     * @param introduction  简介（organization、 attacktype）
     * @param occurtime     产生时间（organization）
     * @param motivation    动机（organization）
     * @param referlink     参考链接（organization）
     * @return              n.value: String
     */
    @Query("MATCH (n:`:#{literal(#label)}`) WHERE id(n) = $id SET n.value=$value, n.introduction=$introduction, n.occurtime=$occurtime, n.motivation=$motivation, n.referlink=$referlink RETURN n.value")
    String setOrganizationNode(@Param("label") String label, @Param("id") Long id, @Param("value") String value, @Param("introduction") String introduction, @Param("occurtime") String occurtime, @Param("motivation") String motivation, @Param("referlink") String referlink);

    @Query("MATCH (n:`:#{literal(#label)}`) WHERE id(n) = $id SET n.value=$value, n.format=$format RETURN n.value")
    String setSha256Node(@Param("label") String label, @Param("id") Long id, @Param("value") String value, @Param("format") String format);

    @Query("MATCH (n:`:#{literal(#label)}`) WHERE id(n) = $id SET n.value=$value, n.information=$information RETURN n.value")
    String setAttackTypeNode(@Param("label") String label, @Param("id") Long id, @Param("value") String value, @Param("information") String information);

    @Query("MATCH (n:`:#{literal(#label)}`) WHERE id(n) = $id SET n.value=$value RETURN n.value")
    String setOtherNode(@Param("label") String label, @Param("id") Long id, @Param("value") String value);

    /**
     * 使用neo4j查询对应的节点并得到该节点在neo4j中的id
     *
     * @param label 节点的label
     * @param value 节点的value
     * @return 节点的id(neo4j中 ， Java Long类型)
     */
    @Query("MATCH (n:`:#{literal(#label)}`) WHERE n.value=$value RETURN id(n)")
    Long searchNodeIdByNameAndLabel(@Param("label") String label, @Param("value") String value);


    /**
     * 通过id来删除节点
     *
     * @param neo4jId
     */
    @Query("MATCH (n:`:#{literal(#label)}`) WHERE ID(n)=$neo4jId DETACH DELETE n")
    void deleteNodeById(@Param("label") String label, @Param("neo4jId") Long neo4jId);
}

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


    // TODO：好像不能这样设置set，试一下delete再重新创建......用不了

    /**
     * 新增或修改现有节点的属性
     *
     * @param label     节点的label
     * @param value     节点值（从mysql中查出value然后通过value匹配neo4j中的节点
     * @param setCypher SET的cypher，在实现类中拼接
     * @return
     */
    @Query("MATCH (n:`:#{literal(#label)}`) WHERE n.value=$value `:#{literal(#setCypher)}` RETURN n.value")
    String setNode(@Param("label") String label, @Param("value") String value, @Param("setCypher") String setCypher);

    /**
     * 使用neo4j查询对应的节点并得到该节点在neo4j中的id
     * @param label 节点的label
     * @param value 节点的value
     * @return      节点的id(neo4j中，Java Long类型)
     */
    @Query("MATCH (n:`:#{literal(#label)}`) WHERE n.value=$value RETURN id(n)")
    Long searchNodeIdByNameAndLabel(@Param("label") String label, @Param("value") String value);

    /**
     * 通过id来删除节点
     *
     * @param neo4jId
     */
    void deleteById(Long neo4jId);
}

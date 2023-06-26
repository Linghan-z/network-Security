package com.zlhhh.networksecurity.mapper.repository;

import com.zlhhh.networksecurity.entity.Neo4jBaseNodeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface Neo4jTriplesRepository extends Neo4jRepository<Neo4jBaseNodeEntity, Long> {
    @Query("MATCH (p),(m) WHERE id(p)=$headId AND id(m)=$tailId MERGE (p)-[r:`:#{literal(#relation)}`]->(m) RETURN id(r)")
    Long createTriples(@Param("headId") Long headId, @Param("relation") String relation, @Param("tailId") Long tailId);

    @Query("MATCH (p),(m) WHERE id(p)=$headId AND id(m)=$tailId MERGE (p)-[r:`:#{literal(#relation)}`]->(m) delete r")
    Long deleteTriples(@Param("headId") Long headId, @Param("relation") String relation, @Param("tailId") Long tailId);

}

package com.zlhhh.networksecurity.mapper;

import com.zlhhh.networksecurity.entity.Neo4jNodeArea;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;


public interface AreaMapper extends Neo4jRepository<Neo4jNodeArea, Long> {

    @Query("match(n:Area) return n limit 5")
    List<Neo4jNodeArea> findAll();

    /**
     * @param organizationName
     * @return Area
     */
    @Query("MATCH (a:Organization WHERE a.value = $organizationName) -[r:organization_from]->(b:Area) RETURN b")
    Neo4jNodeArea findOrganizationOrigin(@Param("organizationName") String organizationName);

        @Query("MATCH (a:Organization WHERE a.value = $organizationName) -[r:organization_has_area]->(b:Area) RETURN b")
//    @Query("MATCH (a:Area) WHERE a.value = $organizationName RETURN a")
        Neo4jNodeArea organizationHasArea(@Param("organizationName") String organizationName);


}

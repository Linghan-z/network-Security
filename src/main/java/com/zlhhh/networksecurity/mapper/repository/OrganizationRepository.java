package com.zlhhh.networksecurity.mapper.repository;

import com.zlhhh.networksecurity.entity.Neo4jNodeOrganization;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;

public interface OrganizationRepository extends Neo4jRepository<Neo4jNodeOrganization,Long> {

    @Query("match(n:Organization) return n")
//    @Query("MATCH (a:Organization WHERE a.value = '海莲花') RETURN a")
    List<Neo4jNodeOrganization> findAll();

    @Query("MATCH (a:Organization) WHERE a.value = $organizationName RETURN a")
    Neo4jNodeOrganization searchOrganization(@Param("organizationName") String organizationName);

    @Query("CREATE (a:`:#{literal(#label)}` $properties) RETURN a.value")
    String createNode(@Param("label") String label, @Param("properties") Map<String, Object> properties);

}

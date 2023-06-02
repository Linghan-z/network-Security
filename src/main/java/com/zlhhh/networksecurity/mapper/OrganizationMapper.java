package com.zlhhh.networksecurity.mapper;

import com.zlhhh.networksecurity.entity.Area;
import com.zlhhh.networksecurity.entity.AttackType;
import com.zlhhh.networksecurity.entity.Organization;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface OrganizationMapper extends Neo4jRepository<Organization,Long> {

    @Query("match(n:Organization) return n")
//    @Query("MATCH (a:Organization WHERE a.value = '海莲花') RETURN a")
    List<Organization> findAll();

    @Query("MATCH (a:Organization) WHERE a.value = $organizationName RETURN a")
    Organization searchOrganization(@Param("organizationName") String organizationName);

}

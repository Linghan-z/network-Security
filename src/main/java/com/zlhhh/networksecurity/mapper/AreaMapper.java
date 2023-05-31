package com.zlhhh.networksecurity.mapper;

import com.zlhhh.networksecurity.entity.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface AreaMapper extends Neo4jRepository<Area,Long> {

    @Query("match(n:Area) return n limit 5")
    List<Area> findAll();

    /**
     * @param organizationName
     * @return Area
     */
    @Query("MATCH (a:Organization WHERE a.name = $organizationName) -[r:organization_from]->(b:Area) RETURN b")
    Area findOrganizationOrigin(@Param("organizationName") String organizationName);

    @Query("MATCH (a:Organization WHERE a.name = $organizationName) -[r:organization_has_area]->(b:Area) RETURN b")
    Area organizationHasArea(@Param("organizationName") String organizationName);
}

package com.zlhhh.networksecurity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlhhh.networksecurity.entity.Area;
import com.zlhhh.networksecurity.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrganizationMapper extends Neo4jRepository<Organization,Long> {

//    List<Organization> findOrganizationOrigin(@Param("areaName") String areaName);

}

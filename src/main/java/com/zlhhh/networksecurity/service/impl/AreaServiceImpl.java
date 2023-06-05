package com.zlhhh.networksecurity.service.impl;

import com.zlhhh.networksecurity.entity.Neo4jNodeArea;
import com.zlhhh.networksecurity.mapper.AreaMapper;
import com.zlhhh.networksecurity.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public List<Neo4jNodeArea> getAll() {
        List<Neo4jNodeArea> allNeo4jNodeArea = areaMapper.findAll();
        return allNeo4jNodeArea;
    }

    @Override
    public Neo4jNodeArea findOrganizationOrigin(String organizationName) {
        Neo4jNodeArea neo4jNodeArea = areaMapper.findOrganizationOrigin(organizationName);
        return neo4jNodeArea;
    }
//
//    @Override
//    public int saveArea(Area area) {
//        return 0;
//    }

}

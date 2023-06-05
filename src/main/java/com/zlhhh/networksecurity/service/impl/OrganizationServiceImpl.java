package com.zlhhh.networksecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlhhh.networksecurity.entity.EntityDetails;
import com.zlhhh.networksecurity.entity.Neo4jNodeOrganization;
import com.zlhhh.networksecurity.mapper.EntityDetailsMapper;
import com.zlhhh.networksecurity.mapper.OrganizationRepository;
import com.zlhhh.networksecurity.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Resource
    private EntityDetailsMapper entityDetailsMapper;

    @Override
    public List<Neo4jNodeOrganization> getAll() {
        List<Neo4jNodeOrganization> allNeo4jNodeOrganization = organizationRepository.findAll();
        return allNeo4jNodeOrganization;
    }

    @Override
    public Neo4jNodeOrganization searchOrganization(String organizationName) {
        Neo4jNodeOrganization neo4jNodeOrganization = organizationRepository.searchOrganization(organizationName);
        System.out.println(neo4jNodeOrganization);
        return neo4jNodeOrganization;
    }

    /**
     * 修 改
     *
     * @param neo4jNodeOrganization
     * @return
     */

    @Override
    public Neo4jNodeOrganization modifyOrganizationEntity(Neo4jNodeOrganization neo4jNodeOrganization) {
        Neo4jNodeOrganization neo4jNodeOrganization1 = organizationRepository.save(neo4jNodeOrganization);
        return neo4jNodeOrganization1;
    }

    @Override
    public String createNode(String label) {
        LambdaQueryWrapper<EntityDetails> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityDetails::getEntityValue,"海莲花1");
        List<EntityDetails> entityDetailsList= entityDetailsMapper.selectList(lambdaQueryWrapper);
        Map<String, Object> attributeMap = new HashMap<>();
        for (EntityDetails entityDetails: entityDetailsList) {
            attributeMap.put(entityDetails.getMapKey(), entityDetails.getMapValue());
        }
        System.out.println("-===================="+attributeMap);
//        String query = generateQuery(attributeMap);
//        System.out.println(query);
        return organizationRepository.createNode(label, attributeMap);
    }

    private String generateQuery(Map<String, Object> attributeMap) {
        if (!attributeMap.isEmpty()) {
            StringBuilder query = new StringBuilder("{");
            Set<String> attrSet = attributeMap.keySet();
            for (String attr : attrSet) {
                query.append(attr).append(": '").append(attributeMap.get(attr)).append("'").append(", ");
            }
            query.deleteCharAt(query.length() - 1);
            query.deleteCharAt(query.length() - 1);
            query.append("}");
            return query.toString();
        } else {
            return null;
        }
    }
}

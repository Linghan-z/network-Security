package com.zlhhh.networksecurity.service;

import com.zlhhh.networksecurity.entity.Neo4jNodeOrganization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrganizationService {
    List<Neo4jNodeOrganization> getAll();
    Neo4jNodeOrganization searchOrganization(String organizationName);

    /**
     *  修 改
     * @param neo4jNodeOrganization
     */
    Neo4jNodeOrganization modifyOrganizationEntity(Neo4jNodeOrganization neo4jNodeOrganization);

    String createNode(String label);
}

package com.zlhhh.networksecurity.service;

import com.zlhhh.networksecurity.entity.Neo4jNodeArea;

import java.util.List;

public interface AreaService {
    /**
     * 获取所有数据
     * @return
     */
//    @Transactional(transactionManager = "neo4jTransactionManager")
    List<Neo4jNodeArea> getAll();

    Neo4jNodeArea findOrganizationOrigin(String organizationName);

    /**
     * 添加节点
     * @param area
     */
//    @Transactional(transactionManager = "neo4jTransactionManager")
//    public int saveArea(Area area);


}

package com.zlhhh.networksecurity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlhhh.networksecurity.entity.EntityInfo;

import java.util.List;

public interface EntityInfoService extends IService<EntityInfo> {
    /**
     * 根据label查询该类型的节点
     *
     * @param label 节点类型
     * @param num   要查询的数量
     * @return 节点信息的list
     */
    List<EntityInfo> searchEntitiesByLabel(String label, Integer num);

    /**
     * 创建新的节点
     *
     * @param entityId 节点id(mysql)
     * @return String
     */
    String createNode(Integer entityId);

    /**
     * set更改或新建节点的属性
     * @param entityId 节点id(mysql)
     * @return String
     */
    String setNode(Integer entityId);


    // TODO：在mysql中存一下neo4j中的id。。。
    /**
     * 删除节点(比较麻烦，通过mysql中的id查询到实体的value，再通过cypher查询id，再删除）
     * @param entityId
     */
    void deleteNode(Integer entityId);

}

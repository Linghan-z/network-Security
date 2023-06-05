package com.zlhhh.networksecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

/**
 * neo4j
 * entity: Area
 * relationship:
 *      from: area_targeted_by_org
 *      to: organization_has_area  organization_from
 */
@NodeEntity(label = "Area")
public class Neo4jNodeArea extends BaseNodeEntity {

//    // neo4j数据库中的关系
//    @Relationship(type = "organization_from",direction = Relationship.INCOMING)
//    private List<>

}

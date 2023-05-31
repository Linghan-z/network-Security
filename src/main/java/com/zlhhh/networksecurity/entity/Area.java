package com.zlhhh.networksecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.List;

/**
 * neo4j
 * entity: Area
 * relationship:
 *      from: area_targeted_by_org
 *      to: organization_has_area  organization_from
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "Area")
public class Area {
    @Id
    @GeneratedValue
    private long id;

    private String name;

//    // neo4j数据库中的关系
//    @Relationship(type = "organization_from",direction = Relationship.INCOMING)
//    private List<>

}

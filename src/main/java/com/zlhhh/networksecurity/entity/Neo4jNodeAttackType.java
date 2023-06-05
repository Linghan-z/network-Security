package com.zlhhh.networksecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "Attacktype")
public class Neo4jNodeAttackType extends BaseNodeEntity {
    private String introduction;
}

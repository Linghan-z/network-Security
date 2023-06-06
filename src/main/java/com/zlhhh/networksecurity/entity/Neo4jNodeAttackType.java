package com.zlhhh.networksecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "Attacktype")
public class Neo4jNodeAttackType extends Neo4jBaseNodeEntity {
    private String introduction;
}

package com.zlhhh.networksecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "Organization")
public class Neo4jNodeOrganization extends Neo4jBaseNodeEntity {

    private String introduction;

    private String motivation;

//    @Property(name = "occurtime")
    private String occurtime;

//    @Property(name = "referlink")
    private String referlink;
}

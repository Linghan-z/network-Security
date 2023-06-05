package com.zlhhh.networksecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;


/**
 * neo4j 实体类的抽象类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity
public class BaseNodeEntity {
    @Id
    @GeneratedValue
    private long id;
    private String value;
}

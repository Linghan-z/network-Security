package com.zlhhh.networksecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NodeEntity(label = "Organization")
public class Organization {

    @Id
    @GeneratedValue
    private long id;
    private String value;

    private String introduction;

    private String motivation;

//    @Property(name = "occurtime")
    private String occurtime;

//    @Property(name = "referlink")
    private String referlink;
}

package com.zlhhh.networksecurity.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("neo4j_node_entity")
@ApiModel(value = "Entity及属性", description = "")
public class EntityInfo {
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Long neo4jId;
    private String label;
    private String value;
    private String introduction;
    private String occurtime;
    private String motivation;
    private String referlink;
    private String format;
    private Boolean updated;
    private String linkIdentifier;
    private Boolean isDeleted;
}

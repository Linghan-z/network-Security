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
@TableName("neo4j_triples")
@ApiModel(value = "三元组", description = "")
public class Triples {
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer headId;
    private String headLabel;
    private String headValue;
    private String relation;
    private Integer tailId;
    private String tailValue;
    private String tailLabel;
    private Boolean updated;
    private Boolean deleted;

}

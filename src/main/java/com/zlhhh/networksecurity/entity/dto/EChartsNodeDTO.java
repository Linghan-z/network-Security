package com.zlhhh.networksecurity.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EChartsNodeDTO {
    private String id;  // eChart id是string
//    private String label;
    @JsonProperty("name")
    private String value;
//    private String introduction;
//    private String occurtime;
//    private String motivation;
//    private String referlink;
//    private String format;
    private Integer category;  // e-charts中分类是数字
    private double symbolSize;  // 节点大小设置为连接的节点的数量的自然对数ln(n) + 1
}

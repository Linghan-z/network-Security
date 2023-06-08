package com.zlhhh.networksecurity.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.entity.dto.OrganizationEntityDTO;
import com.zlhhh.networksecurity.mapper.OrganizationEntityMapper;
import com.zlhhh.networksecurity.service.TriplesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/triples")
@Api(tags = "三元组操作")
public class TriplesController {
    @Resource
    private TriplesService triplesService;

    @ApiOperation("获取组织Organization相关的实体（发源地、目标地区、攻击方式、目标行业）")
    @ApiImplicitParam(name = "organizationNameList", value = "organizationNameList", allowMultiple = true, dataTypeClass = List.class, paramType = "query")
    @GetMapping("/echarts")
    public String getRelevantEntities(@RequestParam(value = "organizationNameList") List<String> organizationNameList) {
        return triplesService.getRelevantEntities(organizationNameList);
    }




}

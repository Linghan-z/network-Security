package com.zlhhh.networksecurity.controller;

import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.service.TriplesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Triples")
@Api(tags = "三元组操作")
public class TriplesController {
    @Resource
    private TriplesService triplesService;
    @ApiOperation("获取组织Organization相关的实体（发源地、目标地区、攻击方式、目标行业）")
    @GetMapping("/{organizationName}")
    public Result getRelevantEntities(@PathVariable String organizationName) {
        return Result.success(triplesService.getRelevantEntities(organizationName));
    }

}

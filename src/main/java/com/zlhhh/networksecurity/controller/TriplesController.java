package com.zlhhh.networksecurity.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.entity.Triples;
import com.zlhhh.networksecurity.service.TriplesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
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

    @ApiOperation("获取所有三元组信息")
    @GetMapping("/page")
    public Result findTriplesPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize) {
        LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Triples::getDeleted, false);
        return Result.success(triplesService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

    @ApiOperation("获取特定关系的所有三元组信息")
    @GetMapping("/page/{relation}")
    public Result findTriplesPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @PathVariable String relation) {
        LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Triples::getRelation, relation);
        lambdaQueryWrapper.eq(Triples::getDeleted, false);
        return Result.success(triplesService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

    /**
     * 模糊查询
     *
     * @param pageNum   页数
     * @param pageSize  页面大小
     * @param headValue 头实体值（Organization）
     * @param relation  关系
     * @param tailValue 尾实体值（其他）
     * @return result
     */
    @ApiOperation("模糊查询信息")
    @GetMapping("/fuzzySearch")
    public Result findTriples(@RequestParam Integer pageNum,
                              @RequestParam Integer pageSize,
                              @RequestParam(defaultValue = "") String headValue,
                              @RequestParam(defaultValue = "") String relation,
                              @RequestParam(defaultValue = "") String tailValue) {
        LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(headValue), Triples::getHeadValue, headValue);
        lambdaQueryWrapper.like(Strings.isNotEmpty(tailValue), Triples::getTailValue, tailValue);
        lambdaQueryWrapper.like(Strings.isNotEmpty(relation), Triples::getRelation, relation);
        return Result.success(triplesService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }


    /**
     * 查询没有更新到neo4j中的三元组
     * @param pageNum 1
     * @param pageSize 当前页数
     * @return Result
     */
    @ApiOperation("获取没有更新的实体")
    @GetMapping("/notUpdatedPage")
    public Result findTriplesNotUpdatedPage(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize) {
        LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Triples::getDeleted, false);
        lambdaQueryWrapper.eq(Triples::getUpdated, false);
        return Result.success(triplesService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

    /**
     * 保存数据到数据库
     *
     * @param triples
     * @return
     */
    @ApiOperation("保存信息到数据库")
    @PostMapping("/save")
    public Result save(@RequestBody Triples triples) {
        triples.setUpdated(false);
        return Result.success(triplesService.saveOrUpdate(triples));
    }



//    @ApiOperation("新增Triples")
//    @PostMapping("/upload")
//    public Result upload()


}

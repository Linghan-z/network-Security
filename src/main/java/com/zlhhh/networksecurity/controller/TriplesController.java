package com.zlhhh.networksecurity.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlhhh.networksecurity.common.Constants;
import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.entity.Triples;
import com.zlhhh.networksecurity.entity.dto.EntityValueDTO;
import com.zlhhh.networksecurity.service.EntityInfoService;
import com.zlhhh.networksecurity.service.TriplesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/triples")
@Api(tags = "三元组操作")
public class TriplesController {
    @Resource
    private TriplesService triplesService;
    @Resource
    private EntityInfoService entityInfoService;

    @ApiOperation("获取组织Organization相关的实体（发源地、目标地区、攻击方式、目标行业）")
    @ApiImplicitParam(name = "organizationNameList", value = "organizationNameList", allowMultiple = true, dataTypeClass = List.class, paramType = "query")
    @GetMapping("/echarts/organization")
    public String getRelevantEntitiesOfOrganizations(@RequestParam(value = "organizationNameList") List<String> organizationNameList) {
        return triplesService.getRelevantEntitiesOfOrganizations(organizationNameList);
    }

    @ApiOperation("获取某实体的直接相关实体")
    @GetMapping("/echarts/entity")
    public String getRelevantEntities(@RequestParam(value = "entityValue") String entityValue) {
        LambdaQueryWrapper<EntityInfo> entityInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entityInfoLambdaQueryWrapper.eq(EntityInfo::getValue, entityValue);
        List<EntityInfo> entities = entityInfoService.list(entityInfoLambdaQueryWrapper);
        EntityInfo entityInfo;
        if (!entities.isEmpty()) {
            entityInfo = entities.get(0);
        } else {
            entityInfo = null;
            return null;
        }
        String entityLabel = entityInfo.getLabel();
        if (Objects.equals(entityLabel, "Organization")) {
            ArrayList<String> entityValueList = new ArrayList<>();
            entityValueList.add(entityValue);
            return triplesService.getRelevantEntitiesOfOrganizations(entityValueList);
        } else {
            return triplesService.getRelevantEntitiesOfOtherEntities(entityLabel, entityValue);
        }
    }

    @ApiOperation("根据关系查询实体")
    @ApiImplicitParam(name = "relationsList", value = "relationsList", allowMultiple = true, dataTypeClass = List.class, paramType = "query")
    @GetMapping("/echarts/relations")
    public String getTriplesGivenRelations(@RequestParam(value = "relationsList") List<String> relationList) {
        List<Triples> triplesList = new ArrayList<>();
        for (String relation: relationList) {
            LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Strings.isNotEmpty(relation), Triples::getRelation, relationList);
            lambdaQueryWrapper.eq(Triples::getDeleted, false);
            List<Triples> tempTriplesList = triplesService.list(lambdaQueryWrapper);
            triplesList.addAll(tempTriplesList);
        }
        return triplesService.getTriplesGivenRelations(triplesList);
//        return triplesService.getTriplesGivenRelations(triplesList);
    }


    /**
     * 查询所有的关系, 用于传给关系的选择输入框
     */
    @GetMapping("/relations")
    public Result getRelations() {
        LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Triples::getRelation).groupBy(Triples::getRelation);
        lambdaQueryWrapper.eq(Triples::getDeleted, false);
        List<Triples> triplesList = triplesService.list(lambdaQueryWrapper);
        List<EntityValueDTO> entityValueDTOList = new ArrayList<>();
        for (Triples triples : triplesList) {
            EntityValueDTO entityValueDTO = new EntityValueDTO();
            entityValueDTO.setValue(triples.getRelation());
            entityValueDTOList.add(entityValueDTO);
        }
        return Result.success(entityValueDTOList);
    }

    /**
     * 查询所有
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("获取所有三元组信息")
    @GetMapping("/page")
    public Result findTriplesPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize) {
        LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Triples::getDeleted, false);
        return Result.success(triplesService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

    /**
     * 输入关系，查询该关系下的所有三元组
     *
     * @param pageNum
     * @param pageSize
     * @param relation
     * @return
     */
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
        lambdaQueryWrapper.eq(Triples::getDeleted, false);
        return Result.success(triplesService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }


    /**
     * 查询没有更新到neo4j中的三元组
     *
     * @param pageNum  1
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
        if (triples.getHeadId() != null && triples.getTailId() != null) {
            boolean saved = triplesService.saveOrUpdate(triples);
            if (saved) {
                LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Triples::getHeadId, triples.getHeadId());
                lambdaQueryWrapper.eq(Triples::getTailId, triples.getTailId());
                lambdaQueryWrapper.eq(Triples::getRelation, triples.getRelation());
                Triples triple = triplesService.getOne(lambdaQueryWrapper);
                Integer tripleId = triple.getId();
                return Result.success(triplesService.createRelation(tripleId));
            } else {
                Result.error(Constants.CODE_500, "内部错误");
            }
        } else {
            return Result.error(Constants.CODE_400, "参数错误");
        }

        return Result.success();
    }

    @ApiOperation("建立新关系")
    @PostMapping("/executeCypher/create")
    public Result createRelation(Integer tripleId) {
        return Result.success(triplesService.createRelation(tripleId));
    }

    @ApiOperation("删除关系")
    @PostMapping("/executeCypher/delete/{tripleId}")
    public Result deleteRelation(@PathVariable Integer tripleId) {
        triplesService.deleteRelation(tripleId);
        return Result.success();
    }

    @ApiOperation("批量删除节点")
    @PostMapping("/executeCypher/delete/batch")
    public Result deleteRelationBatch(@RequestBody List<Integer> entityIds) {
        triplesService.deleteRelationBatch(entityIds);
        return Result.success();
    }


//    @ApiOperation("新增Triples")
//    @PostMapping("/upload")
//    public Result upload()


}

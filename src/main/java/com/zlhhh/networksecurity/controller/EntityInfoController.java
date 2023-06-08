package com.zlhhh.networksecurity.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.entity.dto.OrganizationEntityDTO;
import com.zlhhh.networksecurity.service.EntityInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/entityInfo")
@Api(tags = "实体信息操作")
public class EntityInfoController {
    @Resource
    private EntityInfoService entityInfoService;

    /**
     * 获取某个label下的数据
     *
     * @param pageNum  页数
     * @param pageSize 页大小
     * @param label    label类别
     * @return Result
     */
    @ApiOperation("获取某个label下的信息")
    @GetMapping("/page")
    public Result findEntityInfoPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String label) {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getLabel, label);
        return Result.success(entityInfoService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

    @GetMapping("/organizations")
    public Result getOrganizations() {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getLabel, "Organization");
        List<EntityInfo> entityInfoList = entityInfoService.list(lambdaQueryWrapper);
        List<OrganizationEntityDTO> organizationEntityDTOList = new ArrayList<>();
        for (EntityInfo entityInfo: entityInfoList) {
            OrganizationEntityDTO organizationEntityDTO = new OrganizationEntityDTO();
            organizationEntityDTO.setValue(entityInfo.getValue());
            organizationEntityDTOList.add(organizationEntityDTO);
        }
        return Result.success(organizationEntityDTOList);
    }

    @ApiOperation("查询实体信息")
    @GetMapping("/{entityValue}")
    public Result findEntity(@PathVariable String entityValue) {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getValue, entityValue);
        return Result.success(entityInfoService.getOne(lambdaQueryWrapper));
    }


    /**
     * 保存数据到数据库
     *
     * @param entityInfo
     * @return
     */
    @ApiOperation("保存信息到数据库")
    @PostMapping("/save")
    public Result save(@RequestBody EntityInfo entityInfo) {
        entityInfo.setUpdated(false);
        return Result.success(entityInfoService.saveOrUpdate(entityInfo));
    }

    /**
     * 查询没有update的实体
     *
     * @return
     */
    @ApiOperation("从数据库中查询没有被更新（到neo4j中）的实体")
    @GetMapping("/waitingForUpdate")
    public Result findEntityNotUpdated() {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getUpdated, false);
        return Result.success(entityInfoService.list(lambdaQueryWrapper));
    }

    @ApiOperation("创建新节点")
    @PostMapping("/executeCypher/create")
    public Result createNode(Integer entityId) {
        return Result.success(entityInfoService.createNode(entityId));
    }

    @ApiOperation("更改节点信息")
    @PostMapping("/executeCypher/set")
    public Result setNode(Integer entityId) {
        return Result.success(entityInfoService.setNode(entityId));
    }

    @ApiOperation("删除节点")
    @PostMapping("/executeCypher/delete")
    public Result deleteNode(Integer entityId) {
        entityInfoService.deleteNode(entityId);
        return Result.success();
    }
}

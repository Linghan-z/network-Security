package com.zlhhh.networksecurity.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.entity.dto.EntityValueDTO;
import com.zlhhh.networksecurity.service.EntityInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
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
        lambdaQueryWrapper.eq(EntityInfo::getIsDeleted, false);
        return Result.success(entityInfoService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }
    @ApiOperation("查询实体的详细信息")
    @GetMapping("/searchDetail/{entityValue}")
    public Result searchDetail(@PathVariable String entityValue) {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getValue, entityValue);
        List<EntityInfo> entities = entityInfoService.list(lambdaQueryWrapper);
        EntityInfo entityInfo;
        if (!entities.isEmpty()) {
            entityInfo = entities.get(0);
        } else {
            entityInfo = null;
        }
        return Result.success(entityInfo);
    }

    /**
     * 查询所有的组织, 用于传给组织的选择输入框
     */
    @GetMapping("/organizations")
    public Result getOrganizations() {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getLabel, "Organization");
        List<EntityInfo> entityInfoList = entityInfoService.list(lambdaQueryWrapper);
        List<EntityValueDTO> entityValueDTOList = new ArrayList<>();
        for (EntityInfo entityInfo : entityInfoList) {
            EntityValueDTO entityValueDTO = new EntityValueDTO();
            entityValueDTO.setValue(entityInfo.getValue());
            entityValueDTOList.add(entityValueDTO);
        }
        return Result.success(entityValueDTOList);
    }

    /**
     * 查询实体的信息（模糊查询
     *
     * @param pageNum     1
     * @param pageSize    当前页数
     * @param entityValue 实体的value
     * @return Result
     */
    @ApiOperation("查询实体信息")
    @GetMapping("/search")
    public Result findEntity(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize,
                             @RequestParam(defaultValue = "") String entityValue) {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(entityValue), EntityInfo::getValue, entityValue);
        return Result.success(entityInfoService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

    /**
     * 查询没有更新到neo4j中的实体
     *
     * @param pageNum  1
     * @param pageSize 当前页数
     * @return Result
     */
    @ApiOperation("获取没有更新的实体")
    @GetMapping("/notUpdatedPage")
    public Result findEntityNotUpdatedPage(@RequestParam Integer pageNum,
                                           @RequestParam Integer pageSize) {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getIsDeleted, false);
        lambdaQueryWrapper.eq(EntityInfo::getUpdated, false);
        return Result.success(entityInfoService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
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

    @ApiOperation("根据实体的Value查询实体id")
    @GetMapping("/searchId/{entityValue}")
    public Result searchByValue(@PathVariable String entityValue) {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getValue, entityValue);
        List<EntityInfo> entities = entityInfoService.list(lambdaQueryWrapper);
        Integer entityId;
        if (!entities.isEmpty()) {
            EntityInfo entity = entities.get(0);
            entityId = entity.getId();
        } else {
            entityId = null;
        }
        return Result.success(entityId);
    }

//    /**
//     * 查询没有update的实体
//     *
//     * @return
//     */
//    @ApiOperation("从数据库中查询没有被更新（到neo4j中）的实体")
//    @GetMapping("/waitingForUpdate")
//    public Result findEntityNotUpdated() {
//        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(EntityInfo::getUpdated, false);
//        return Result.success(entityInfoService.list(lambdaQueryWrapper));
//    }

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

    @ApiOperation("更改节点信息")
    @PostMapping("/executeCypher/setNodeInfo")
    public Result setNodeInfo(@RequestBody List<Integer> entityIds) {
        return Result.success(entityInfoService.setNodeInfo(entityIds));
    }

    @ApiOperation("删除节点")
    @PostMapping("/executeCypher/delete/{entityId}")
    public Result deleteNode(@PathVariable Integer entityId) {
        entityInfoService.deleteNode(entityId);
        return Result.success();
    }

    @ApiOperation("批量删除节点")
    @PostMapping("/executeCypher/delete/batch")
    public Result deleteNodeBatch(@RequestBody List<Integer> entityIds) {
        entityInfoService.deleteNodeBatch(entityIds);
        return Result.success();
    }
}

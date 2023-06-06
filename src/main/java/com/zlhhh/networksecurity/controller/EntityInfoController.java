package com.zlhhh.networksecurity.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.service.EntityInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/EntityInfo")
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
    @GetMapping("/page")
    public Result findEntityInfoPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String label) {
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getLabel, label);
        return Result.success(entityInfoService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper));
    }

    /**
     * 保存数据到数据库
     *
     * @param entityInfo
     * @return
     */
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

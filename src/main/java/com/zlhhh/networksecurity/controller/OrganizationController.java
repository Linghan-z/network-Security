package com.zlhhh.networksecurity.controller;

import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.service.OrganizationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entity")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @ApiOperation("查询组织")
    @GetMapping("/organization")
    public Result getAll() {
        return Result.success(organizationService.getAll());
    }

    @ApiOperation("查询详情")
    @GetMapping("/organization/{organizationName}")
    public Result searchOrganization(@PathVariable String organizationName) {
        return Result.success(organizationService.searchOrganization(organizationName));
    }


    @ApiOperation("创建新节点")
    @PostMapping("/create_node")
    public String createNode(String label) {
        return organizationService.createNode(label);
    }

}

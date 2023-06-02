package com.zlhhh.networksecurity.controller;

import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.entity.Organization;
import com.zlhhh.networksecurity.service.OrganizationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}

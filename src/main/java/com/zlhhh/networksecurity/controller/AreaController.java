package com.zlhhh.networksecurity.controller;

import com.zlhhh.networksecurity.common.Result;
import com.zlhhh.networksecurity.service.AreaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/area")
public class AreaController {
    @Autowired
    AreaService areaService;

    @ApiOperation("查询地区")
    @GetMapping()
    public Result getAll() {
        return Result.success(areaService.getAll());
    }
//
    @ApiOperation("查询组织的来源地")
    @GetMapping("/{organizationName}")
    public Result findOrganizationOrigin(@PathVariable String organizationName) {
        return Result.success(areaService.findOrganizationOrigin(organizationName));
    }
}

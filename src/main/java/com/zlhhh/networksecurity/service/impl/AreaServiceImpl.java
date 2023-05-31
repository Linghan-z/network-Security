package com.zlhhh.networksecurity.service.impl;

import com.zlhhh.networksecurity.mapper.AreaMapper;
import com.zlhhh.networksecurity.entity.Area;
import com.zlhhh.networksecurity.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public List<Area> getAll() {
        List<Area> allArea = areaMapper.findAll();
        return allArea;
    }

    @Override
    public Area findOrganizationOrigin(String organizationName) {
        Area area = areaMapper.findOrganizationOrigin(organizationName);
        return area;
    }
//
//    @Override
//    public int saveArea(Area area) {
//        return 0;
//    }

}

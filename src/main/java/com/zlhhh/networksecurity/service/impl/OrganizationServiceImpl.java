package com.zlhhh.networksecurity.service.impl;

import com.zlhhh.networksecurity.entity.AttackType;
import com.zlhhh.networksecurity.entity.Organization;
import com.zlhhh.networksecurity.mapper.OrganizationMapper;
import com.zlhhh.networksecurity.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public List<Organization> getAll() {
        List<Organization> allOrganization = organizationMapper.findAll();
        return allOrganization;
    }

    @Override
    public Organization searchOrganization(String organizationName) {
        Organization organization = organizationMapper.searchOrganization(organizationName);
        return organization;
    }
}

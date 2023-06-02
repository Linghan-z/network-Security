package com.zlhhh.networksecurity.service;

import com.zlhhh.networksecurity.entity.AttackType;
import com.zlhhh.networksecurity.entity.Organization;

import java.util.List;

public interface OrganizationService {
    List<Organization> getAll();
    Organization searchOrganization(String organizationName);
}

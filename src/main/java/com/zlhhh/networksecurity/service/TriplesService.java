package com.zlhhh.networksecurity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlhhh.networksecurity.entity.Triples;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TriplesService extends IService<Triples> {
    /**
     * 获取organization的相关实体，不包含ip、domain、sha256
     * @param organizationNameList
     */
    String getRelevantEntities(List<String> organizationNameList);
}

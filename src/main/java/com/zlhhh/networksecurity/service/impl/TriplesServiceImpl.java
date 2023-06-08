package com.zlhhh.networksecurity.service.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlhhh.networksecurity.entity.Triples;
import com.zlhhh.networksecurity.entity.dto.EChartsCategoryDTO;
import com.zlhhh.networksecurity.entity.dto.EChartsNodeDTO;
import com.zlhhh.networksecurity.entity.dto.EChartsRelationDTO;
import com.zlhhh.networksecurity.mapper.TriplesMapper;
import com.zlhhh.networksecurity.service.TriplesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TriplesServiceImpl extends ServiceImpl<TriplesMapper, Triples> implements TriplesService {
    private static final Log LOG = Log.get();
    @Resource
    private TriplesMapper triplesMapper;


    /**
     * 获取organization的相关实体，不包含ip、domain、sha256
     *
     * @param organizationName
     */
    @Override
    public String getRelevantEntities(String organizationName) {
        List<String> entityLabelList = Arrays.asList("Area", "Attacktype", "Industry");
        // 根据Label标记分类（用于echart）
        Map<String, Integer> categoryMap = new HashMap<>();
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("Organization");
        categoryMap.put("Organization", categoryList.indexOf("Organization") + 1);
        LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Triples::getHeadValue, organizationName);
        lambdaQueryWrapper.in(Triples::getTailLabel, entityLabelList);
        List<Triples> triplesList = triplesMapper.selectList(lambdaQueryWrapper);
//        System.out.println(triplesList);
        List<EChartsNodeDTO> eChartsNodeDTOList = new ArrayList<>();  // echart节点的列表
        List<EChartsRelationDTO> eChartsRelationDTOList = new ArrayList<>();  // echart关系的列表
        EChartsNodeDTO eChartOrgNodeDTO = new EChartsNodeDTO();
        eChartOrgNodeDTO.setId(triplesList.get(0).getHeadId().toString());
        eChartOrgNodeDTO.setSymbolSize(Math.log(triplesList.size()) + 1);
        eChartOrgNodeDTO.setValue(organizationName);
        eChartOrgNodeDTO.setCategory(1);
        eChartsNodeDTOList.add(eChartOrgNodeDTO);
        for (Triples triplesItem : triplesList) {
            // 处理节点
            EChartsNodeDTO eChartsNodeDTO = new EChartsNodeDTO();
            eChartsNodeDTO.setId(triplesItem.getTailId().toString());
            eChartsNodeDTO.setValue(triplesItem.getTailValue());
            eChartsNodeDTO.setSymbolSize(1.0);

            // 判断label是否为新出现的，如果是新的就在categoryList中新增一个，并通过categoryMap映射 label和序数之间的关系
            // 如果不是新出现的则直接查询Map中的对应关系
            if (categoryMap.containsKey(triplesItem.getTailLabel())) {
                eChartsNodeDTO.setCategory(categoryMap.get(triplesItem.getTailLabel()));  // 按照Label来标记分类
            } else {
                categoryList.add(triplesItem.getTailLabel());
                categoryMap.put(triplesItem.getTailLabel(), categoryList.indexOf(triplesItem.getTailLabel()) + 1);
                eChartsNodeDTO.setCategory(categoryMap.get(triplesItem.getTailLabel()));  // 按照Label来标记分类
            }
            eChartsNodeDTOList.add(eChartsNodeDTO);

            // 处理关系
            EChartsRelationDTO eChartsRelationDTO = new EChartsRelationDTO();
            eChartsRelationDTO.setSource(triplesItem.getHeadId().toString());
            eChartsRelationDTO.setTarget(triplesItem.getTailId().toString());
            eChartsRelationDTOList.add(eChartsRelationDTO);
        }
        System.out.println("==========================================");
        Map<String, Object> result = new HashMap<>();
        result.put("nodes", eChartsNodeDTOList);
        result.put("links", eChartsRelationDTOList);
        List<EChartsCategoryDTO> eChartsCategoryDTOList = new ArrayList<>();
        for (String categories : categoryList) {
            EChartsCategoryDTO eChartsCategoryDTO = new EChartsCategoryDTO();
            eChartsCategoryDTO.setName(categories);
            eChartsCategoryDTOList.add(eChartsCategoryDTO);
        }
        result.put("cotegories", eChartsCategoryDTOList);
        String json = JSONUtil.toJsonStr(result);
        System.out.println(json);
        return json;
    }
}

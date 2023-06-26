package com.zlhhh.networksecurity.service.impl;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.entity.Triples;
import com.zlhhh.networksecurity.entity.dto.EChartsCategoryDTO;
import com.zlhhh.networksecurity.entity.dto.EChartsNodeDTO;
import com.zlhhh.networksecurity.entity.dto.EChartsRelationDTO;
import com.zlhhh.networksecurity.mapper.EntityInfoMapper;
import com.zlhhh.networksecurity.mapper.TriplesMapper;
import com.zlhhh.networksecurity.mapper.repository.Neo4jTriplesRepository;
import com.zlhhh.networksecurity.service.EntityInfoService;
import com.zlhhh.networksecurity.service.TriplesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TriplesServiceImpl extends ServiceImpl<TriplesMapper, Triples> implements TriplesService {
    private static final Log LOG = Log.get();
    @Resource
    private TriplesMapper triplesMapper;

    @Resource
    private EntityInfoMapper entityInfoMapper;

    @Autowired
    private Neo4jTriplesRepository neo4jTriplesRepository;


    /**
     * 获取organization的相关实体，不包含ip、domain、sha256， 返回JSON 用于eChart
     *
     * @param organizationNameList
     */
    @Override
    public String getRelevantEntitiesOfOrganizations(List<String> organizationNameList) {
        System.out.println("+++++++++++=============");
        System.out.println(organizationNameList);
        List<String> entityLabelList = Arrays.asList("Area", "Attacktype", "Industry");
        // 根据Label标记分类（用于echart）
        Map<String, Integer> categoryMap = new HashMap<>();
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("Organization");
        categoryMap.put("Organization", categoryList.indexOf("Organization"));

        List<EChartsNodeDTO> eChartsNodeDTOList = new ArrayList<>();  // echart节点的列表

        // 查询
        List<Triples> triplesList = new ArrayList<>();
        for (String organizationName : organizationNameList) {
            System.out.println(organizationName);
            LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Triples::getHeadValue, organizationName);
            lambdaQueryWrapper.in(Triples::getTailLabel, entityLabelList);
            List<Triples> tempTriplesList = triplesMapper.selectList(lambdaQueryWrapper);
            System.out.println(tempTriplesList);
            triplesList.addAll(tempTriplesList);
            // 处理Organization 节点
            EChartsNodeDTO eChartOrgNodeDTO = new EChartsNodeDTO();
            eChartOrgNodeDTO.setId(tempTriplesList.get(0).getHeadId().toString());
            eChartOrgNodeDTO.setSymbolSize(Math.max(10 * Math.log(tempTriplesList.size()) + 1, 10));
            eChartOrgNodeDTO.setName(organizationName);
            eChartOrgNodeDTO.setCategory(0);
            eChartsNodeDTOList.add(eChartOrgNodeDTO);
        }
        List<EChartsRelationDTO> eChartsRelationDTOList = new ArrayList<>();  // echart关系的列表
        for (Triples triplesItem : triplesList) {
            // 处理节点
            EChartsNodeDTO eChartsNodeDTO = new EChartsNodeDTO();
            eChartsNodeDTO.setId(triplesItem.getTailId().toString());
            eChartsNodeDTO.setName(triplesItem.getTailValue());
            eChartsNodeDTO.setSymbolSize(10.0);

            // 判断label是否为新出现的，如果是新的就在categoryList中新增一个，并通过categoryMap映射 label和序数之间的关系
            // 如果不是新出现的则直接查询Map中的对应关系
            if (categoryMap.containsKey(triplesItem.getTailLabel())) {
                eChartsNodeDTO.setCategory(categoryMap.get(triplesItem.getTailLabel()));  // 按照Label来标记分类
            } else {
                categoryList.add(triplesItem.getTailLabel());
                categoryMap.put(triplesItem.getTailLabel(), categoryList.indexOf(triplesItem.getTailLabel()));
                eChartsNodeDTO.setCategory(categoryMap.get(triplesItem.getTailLabel()));  // 按照Label来标记分类
            }
            // 如果不包含就新增一个
            if (!eChartsNodeDTOList.contains(eChartsNodeDTO)) {
                eChartsNodeDTOList.add(eChartsNodeDTO);
            }
            // 处理关系
            EChartsRelationDTO eChartsRelationDTO = new EChartsRelationDTO();
            eChartsRelationDTO.setSource(triplesItem.getHeadId().toString());
            eChartsRelationDTO.setTarget(triplesItem.getTailId().toString());
            eChartsRelationDTOList.add(eChartsRelationDTO);
        }
        System.out.println("==========================================");
        return getJson(categoryList, eChartsNodeDTOList, eChartsRelationDTOList);
    }

    /**
     * 获取某实体的相关实体以及关系，返回JSON，用于eChart
     *
     * @param entityLabel
     * @param entityValue
     * @return
     */
    @Override
    public String getRelevantEntitiesOfOtherEntities(String entityLabel, String entityValue) {
//        // HashMap: {"Organization" = 0, 实体类型entityLabel=1}
//        Map<String, Integer> categoryMap = new HashMap<>();
//        categoryMap.put("Organization", 0);
//        categoryMap.put(entityLabel, 1);
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add(entityLabel);
        categoryList.add("Organization");
        // echart节点的列表
        List<EChartsNodeDTO> eChartsNodeDTOList = new ArrayList<>();
        // 查询三元组
        LambdaQueryWrapper<Triples> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Triples::getTailValue, entityValue);
        List<Triples> triplesList = triplesMapper.selectList(lambdaQueryWrapper);
        EChartsNodeDTO eChartsEntityNodeDTO = new EChartsNodeDTO();
        eChartsEntityNodeDTO.setId(triplesList.get(0).getTailId().toString());
        eChartsEntityNodeDTO.setName(entityValue);
        eChartsEntityNodeDTO.setSymbolSize(10 * Math.log(triplesList.size()) + 1);
        eChartsEntityNodeDTO.setCategory(0);
        eChartsNodeDTOList.add(eChartsEntityNodeDTO);
        // echart关系的列表
        List<EChartsRelationDTO> eChartsRelationDTOList = new ArrayList<>();
        for (Triples triplesItem : triplesList) {
            // 处理节点
            EChartsNodeDTO eChartsNodeDTO = new EChartsNodeDTO();
            eChartsNodeDTO.setId(triplesItem.getHeadId().toString());
            eChartsNodeDTO.setName(triplesItem.getHeadValue());
            eChartsNodeDTO.setSymbolSize(10.0);
            eChartsNodeDTO.setCategory(1);
            if (!eChartsNodeDTOList.contains(eChartsNodeDTO)) {
                eChartsNodeDTOList.add(eChartsNodeDTO);
            }
            // 处理关系
            EChartsRelationDTO eChartsRelationDTO = new EChartsRelationDTO();
            eChartsRelationDTO.setSource(triplesItem.getHeadId().toString());
            eChartsRelationDTO.setTarget(triplesItem.getTailId().toString());
            eChartsRelationDTOList.add(eChartsRelationDTO);
        }
        return getJson(categoryList, eChartsNodeDTOList, eChartsRelationDTOList);
    }

    /**
     * 给定类别，获取该类别下的所有实体以及关系，返回JSON 用于eChart
     *
     * @param triplesList
     * @return
     */
    @Override
    public String getTriplesGivenRelations(List<Triples> triplesList) {
        // 储存类别
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add(triplesList.get(0).getHeadLabel());
        // 各组织的相关实体计数
        Map<String, Integer> OrganizationTriplesCounts = new HashMap<>();

        // echart节点的列表
        List<EChartsNodeDTO> eChartsNodeDTOList = new ArrayList<>();
        // echart关系的列表
        List<EChartsRelationDTO> eChartsRelationDTOList = new ArrayList<>();

        // 优先处理尾节点，并根据头结点的值计数
        for (Triples triplesItem : triplesList) {
            String orgValue = triplesItem.getHeadValue();
            if (OrganizationTriplesCounts.containsKey(orgValue)) {
                Integer count = OrganizationTriplesCounts.get(orgValue);
                OrganizationTriplesCounts.replace(orgValue, count, count + 1);
            } else {
                OrganizationTriplesCounts.put(orgValue, 1);
            }
            // 处理尾节点
            EChartsNodeDTO eChartsNodeDTO = new EChartsNodeDTO();
            eChartsNodeDTO.setId(triplesItem.getTailId().toString());
            eChartsNodeDTO.setName(triplesItem.getTailValue());
            eChartsNodeDTO.setSymbolSize(10.0);
            // 判断label是否为新出现的，如果是新的就在categoryList中新增一个
            // 如果不是新出现的则直接查询Map中的对应关系
            if (categoryList.contains(triplesItem.getTailLabel())) {
                eChartsNodeDTO.setCategory(categoryList.indexOf(triplesItem.getTailLabel()));  // 按照Label来标记分类
            } else {
                categoryList.add(triplesItem.getTailLabel());
                eChartsNodeDTO.setCategory(categoryList.indexOf(triplesItem.getTailLabel()));  // 按照Label来标记分类
            }
            // 如果不包含就新增一个
            if (!eChartsNodeDTOList.contains(eChartsNodeDTO)) {
                eChartsNodeDTOList.add(eChartsNodeDTO);
            }
            // 处理关系
            EChartsRelationDTO eChartsRelationDTO = new EChartsRelationDTO();
            eChartsRelationDTO.setSource(triplesItem.getHeadId().toString());
            eChartsRelationDTO.setTarget(triplesItem.getTailId().toString());
            eChartsRelationDTOList.add(eChartsRelationDTO);
        }
        int orgCount = 0;
        for (String organizationValue : OrganizationTriplesCounts.keySet()) {
            LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(EntityInfo::getValue, organizationValue);
            EntityInfo entityInfo = entityInfoMapper.selectOne(lambdaQueryWrapper);
            EChartsNodeDTO eChartOrgNodeDTO = new EChartsNodeDTO();
            eChartOrgNodeDTO.setId(entityInfo.getId().toString());
            eChartOrgNodeDTO.setSymbolSize(Math.max(10 * Math.log10(OrganizationTriplesCounts.get(organizationValue)) + 1, 10));
            eChartOrgNodeDTO.setName(organizationValue);
            eChartOrgNodeDTO.setCategory(0);
            eChartsNodeDTOList.add(orgCount, eChartOrgNodeDTO);
            orgCount++;
        }
        return getJson(categoryList, eChartsNodeDTOList, eChartsRelationDTOList);
    }


    /**
     * 在Neo4j中创建三元组
     *
     * @param tripleId
     * @return
     */
    @Override
    public Object createRelation(Integer tripleId) {
        Triples triples = triplesMapper.selectById(tripleId);
        Long headId = getNeo4jIdBySqlId(triples.getHeadId());
        Long tailId = getNeo4jIdBySqlId(triples.getTailId());
        String relation = triples.getRelation();
        Long relationId = neo4jTriplesRepository.createTriples(headId, relation, tailId);
        System.out.println(relationId);
        triples.setUpdated(true);
        triplesMapper.updateById(triples);
        return relationId;
    }

    /**
     * 删除三元组
     *
     * @param tripleId
     */
    @Override
    public void deleteRelation(Integer tripleId) {
        Triples triples = triplesMapper.selectById(tripleId);
        Long headId = getNeo4jIdBySqlId(triples.getHeadId());
        Long tailId = getNeo4jIdBySqlId(triples.getTailId());
        String relation = triples.getRelation();
        neo4jTriplesRepository.deleteTriples(headId, relation, tailId);
        triples.setUpdated(true);
        triples.setDeleted(true);
        triplesMapper.updateById(triples);
    }

    @Override
    public void deleteRelationBatch(List<Integer> entityIds) {
        for (Integer entityId : entityIds) {
            deleteRelation(entityId);
        }
    }


    private String getJson(ArrayList<String> categoryList, List<EChartsNodeDTO> eChartsNodeDTOList, List<EChartsRelationDTO> eChartsRelationDTOList) {
        Map<String, Object> result = new HashMap<>();
        result.put("nodes", eChartsNodeDTOList);
        result.put("links", eChartsRelationDTOList);
        List<EChartsCategoryDTO> eChartsCategoryDTOList = new ArrayList<>();
        for (String categories : categoryList) {
            EChartsCategoryDTO eChartsCategoryDTO = new EChartsCategoryDTO();
            eChartsCategoryDTO.setName(categories);
            eChartsCategoryDTOList.add(eChartsCategoryDTO);
        }
        result.put("categories", eChartsCategoryDTOList);
        String json = JSONUtil.toJsonStr(result);
        System.out.println(json);
        return json;
    }

    private Long getNeo4jIdBySqlId(Integer sqlId) {
        EntityInfo entityInfo = entityInfoMapper.selectById(sqlId);
        return entityInfo.getNeo4jId();
    }
}
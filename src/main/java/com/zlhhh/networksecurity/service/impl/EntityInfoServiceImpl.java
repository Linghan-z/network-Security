package com.zlhhh.networksecurity.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.mapper.EntityInfoMapper;
import com.zlhhh.networksecurity.mapper.repository.Neo4jEntityRepository;
import com.zlhhh.networksecurity.service.EntityInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EntityInfoServiceImpl extends ServiceImpl<EntityInfoMapper, EntityInfo> implements EntityInfoService {

    private static final Log LOG = Log.get();


    // 建议使用构造函数注入（Constructor Injection）或Setter方法注入（Setter Injection）的方式来注入依赖项。这样做可以使代码更清晰、更易于测试和更安全。
    // 而非@Autowired
    private Neo4jEntityRepository neo4jEntityRepository;
    @Resource
    private EntityInfoMapper entityInfoMapper;

    public EntityInfoServiceImpl(Neo4jEntityRepository neo4jEntityRepository) {
        this.neo4jEntityRepository = neo4jEntityRepository;
    }

    @Override
    public List<EntityInfo> searchEntitiesByLabel(String label, Integer num) {
        return null;
    }


    /**
     * 创建neo4j中的节点
     *
     * @param entityId 节点id(mysql)
     * @return
     */
    @Override
    public String createNode(Integer entityId) {
        EntityInfo entityInfo = queryEntity(entityId);
        String label = entityInfo.getLabel();
        Map<String, Object> entityInfoMap = generateHashMap(entityInfo);
        System.out.println(entityInfoMap);
        return neo4jEntityRepository.createNode(label, entityInfoMap);
    }

    /**
     * 更改节点的值
     * 这个方法不用了
     *
     * @param entityId 节点id(mysql)
     * @return
     */
    @Override
    public String setNode(Integer entityId) {
        EntityInfo entityInfo = queryEntity(entityId);
        String label = entityInfo.getLabel();
        String value = entityInfo.getValue();
        Map<String, Object> entityInfoMap = generateHashMap(entityInfo);
//        System.out.println(entityInfoMap);
        Long neo4jId = neo4jEntityRepository.searchNodeIdByNameAndLabel(label, value);
        neo4jEntityRepository.deleteNodeById(label, neo4jId);
        return neo4jEntityRepository.createNode(label, entityInfoMap);
    }

    /**
     * 从mysql中根据id取到实体数据
     * 进行判断：
     * 如果neo4j_id是空
     * 则新建节点
     * 否则更新节点数据doSetNodeInfo
     *
     * @param entityIds idList
     * @return String
     */
    @Override
    public String setNodeInfo(List<Integer> entityIds) {
        String entityValues = "";
        Integer idCount = entityIds.size();
        for (Integer entityId : entityIds) {
            EntityInfo entityInfo = queryEntity(entityId);
            String entityValue;
            if (entityInfo.getNeo4jId() == null) {
                entityValue = doCreateNode(entityInfo);
            } else {
                entityValue = doSetNodeInfo(entityInfo);
            }
            entityValues.concat(entityValue);
            entityValues.concat(", ");
        }
        return entityValues;
    }

    /**
     * 删除节点
     *
     * @param entityId 实体在mysql中的id
     */
    @Override
    public void deleteNode(Integer entityId) {
        EntityInfo entityInfo = queryEntity(entityId);
        String label = entityInfo.getLabel();
        if (entityInfo.getNeo4jId() != null) {  // 证明这个实体在neo4j中有对应的节点，不是新增的，需要更新neo4j中的信息
            Long neo4jId = entityInfo.getNeo4jId();
            neo4jEntityRepository.deleteNodeById(label, neo4jId);
            entityInfo.setNeo4jId(null);
        }
        entityInfo.setIsDeleted(true);
        entityInfoMapper.updateById(entityInfo);
//        Long neo4jId = neo4jEntityRepository.searchNodeIdByNameAndLabel(label, value);
//        System.out.println(neo4jId);
    }

    @Override
    public void deleteNodeBatch(List<Integer> entityIds) {
        for (Integer entityId : entityIds) {
            deleteNode(entityId);
        }
    }

    /***************/

    private String doCreateNode(EntityInfo entityInfo) {
        String label = entityInfo.getLabel();
        Map<String, Object> entityInfoMap = generateHashMap(entityInfo);
        String entityValue = neo4jEntityRepository.createNode(label, entityInfoMap);
        Long neo4jId = neo4jEntityRepository.searchNodeIdByNameAndLabel(label, entityValue);
        entityInfo.setNeo4jId(neo4jId);
        entityInfo.setUpdated(true);
        entityInfoMapper.updateById(entityInfo);
        return entityValue;
    }

    private String doSetNodeInfo(EntityInfo entityInfo) {
        String label = entityInfo.getLabel();
        Long id = entityInfo.getNeo4jId();
        String value = entityInfo.getValue();
        if (Objects.equals(label, "Organization")) {
            String introduction = entityInfo.getIntroduction();
            String occurtime = entityInfo.getOccurtime();
            String referlink = entityInfo.getReferlink();
            String motivation = entityInfo.getMotivation();
            entityInfo.setUpdated(true);
//            System.out.println(entityInfo);
            entityInfoMapper.updateById(entityInfo);
            return neo4jEntityRepository.setOrganizationNode(label, id, value, introduction, occurtime, motivation, referlink);
        } else if (Objects.equals(label, "Attacktype")) {
            String introduction = entityInfo.getIntroduction();
            return neo4jEntityRepository.setAttackTypeNode(label, id, value, introduction);
        } else if (Objects.equals(label, "Sha256")) {
            String format = entityInfo.getFormat();
            return neo4jEntityRepository.setSha256Node(label, id, value, format);
        } else {
            return neo4jEntityRepository.setOtherNode(label, id, value);
        }
    }

    /**
     * 根据数据库中查询到的数据生成Hashmap
     *
     * @param entityInfo
     * @return
     */

    private Map<String, Object> generateHashMap(EntityInfo entityInfo) {
        Map<String, Object> entityInfoMap = new HashMap<>();

        entityInfoMap.put("value", entityInfo.getValue());
        if (entityInfo.getOccurtime() != null) {
            entityInfoMap.put("occurtime", entityInfo.getOccurtime());
        }
        if (entityInfo.getMotivation() != null) {
            entityInfoMap.put("motivation", entityInfo.getMotivation());
        }
        if (entityInfo.getIntroduction() != null) {
            entityInfoMap.put("introduction", entityInfo.getIntroduction());
        }
        if (entityInfo.getReferlink() != null) {
            entityInfoMap.put("referlink", entityInfo.getReferlink());
        }
        if (entityInfo.getFormat() != null) {
            entityInfoMap.put("format", entityInfo.getFormat());
        }
        return entityInfoMap;
    }

    /**
     * 根据id(mysql)查询entity实体
     *
     * @param entityId
     * @return
     */
    private EntityInfo queryEntity(Integer entityId) {
        EntityInfo entityInfo = entityInfoMapper.selectById(entityId);
//        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(EntityInfo::getId, entityId);
//        EntityInfo entityInfo;
//        try {
//            entityInfo = getOne(lambdaQueryWrapper);  // 从数据库查询用户信息
//        } catch (Exception e) {
//            e.printStackTrace();  //  打印异常的堆栈
//            LOG.error(e);
//            // sql查询发生的错误（sql系统），返回错误信息
//            throw new ServiceException(Constants.CODE_500, "系统错误");
//        }
        entityInfo.setUpdated(true);
        entityInfoMapper.updateById(entityInfo);

        return entityInfo;
    }
}

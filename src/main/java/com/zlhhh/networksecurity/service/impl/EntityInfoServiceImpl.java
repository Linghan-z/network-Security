package com.zlhhh.networksecurity.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlhhh.networksecurity.common.Constants;
import com.zlhhh.networksecurity.entity.EntityInfo;
import com.zlhhh.networksecurity.exception.ServiceException;
import com.zlhhh.networksecurity.mapper.EntityInfoMapper;
import com.zlhhh.networksecurity.mapper.repository.Neo4jEntityRepository;
import com.zlhhh.networksecurity.service.EntityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EntityInfoServiceImpl extends ServiceImpl<EntityInfoMapper, EntityInfo> implements EntityInfoService {

    private static final Log LOG = Log.get();


    // TODO: 建议使用构造函数注入（Constructor Injection）或Setter方法注入（Setter Injection）的方式来注入依赖项。这样做可以使代码更清晰、更易于测试和更安全。

    private Neo4jEntityRepository neo4jEntityRepository;

    // TODO: 不用@Autowired 注解，而是使用下面注释掉的构造方法
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
        System.out.println(entityInfoMap);
        String setCypher;
        setCypher = "SET n.introduction = '这是周杰伦'";
        return neo4jEntityRepository.setNode(label, value, setCypher);
    }

    @Override
    public void deleteNode(Integer entityId) {
        EntityInfo entityInfo = queryEntity(entityId);
        String label = entityInfo.getLabel();
        String value = entityInfo.getValue();
        Long neo4jId = neo4jEntityRepository.searchNodeIdByNameAndLabel(label, value);
        System.out.println("++++++++==========");
        System.out.println(neo4jId);
        
        // TODO: 这个delete好像不灵呀
        neo4jEntityRepository.deleteById(neo4jId);
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
        LambdaQueryWrapper<EntityInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EntityInfo::getId, entityId);
        EntityInfo entityInfo;
        try {
            entityInfo = getOne(lambdaQueryWrapper);  // 从数据库查询用户信息
        } catch (Exception e) {
            e.printStackTrace();  //  打印异常的堆栈
            LOG.error(e);
            // sql查询发生的错误（sql系统），返回错误信息
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return entityInfo;
    }
}

package com.pwc.modules.sys.dao;

import com.pwc.modules.sys.entity.SysDistrictEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 行政区字典
 * 
 * @author zk
 * @email 
 * @date 2019-11-13 18:54:25
 */
@Mapper
public interface SysDistrictDao extends BaseMapper<SysDistrictEntity> {

    List<SysDistrictEntity> queryList(Map<String, Object> params);


    /**
     * 查询子列表
     * @param parentId  上级ID
     */
    List<SysDistrictEntity> queryDistrictList(Long parentId);

    /**
     * 查询
     * @param id  id
     */
    SysDistrictEntity queryById(Long id);
}

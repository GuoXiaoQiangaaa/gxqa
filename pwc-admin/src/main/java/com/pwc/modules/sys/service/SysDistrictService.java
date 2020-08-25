package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysDistrictEntity;

import java.util.List;
import java.util.Map;

/**
 * 行政区字典
 *
 * @author zk
 * @email 
 * @date 2019-11-13 18:54:25
 */
public interface SysDistrictService extends IService<SysDistrictEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SysDistrictEntity> queryList(Map<String, Object> map);

    /**
     * 查询子列表
     * @param parentId  上级地区ID
     */
    List<SysDistrictEntity> queryDistrictList(Long parentId);

    /**
     * 获取子
     */
    List<SysDistrictEntity> getSubDistrictList(Long districtId);

    SysDistrictEntity queryById(Long id);
}


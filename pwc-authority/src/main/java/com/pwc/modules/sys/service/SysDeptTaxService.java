package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysDeptTaxEntity;

import java.util.List;
import java.util.Map;

/**
 * 企业设置税种关联表
 *
 * @author zk
 * @email 
 * @date 2019-12-31 17:21:04
 */
public interface SysDeptTaxService extends IService<SysDeptTaxEntity> {

    /**
     * 保存或更新企业税种关系
     * @param deptId 企业id
     * @param taxIdList 税种id列表
     */
    void saveOrUpdate(Long deptId, List<Long> taxIdList);

    /**
     * 分页
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取企业设置的税种
     * @param deptId
     * @return
     */
    List<Long> getTaxIdByDeptId(Long deptId);
}


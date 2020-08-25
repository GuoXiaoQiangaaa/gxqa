package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysTaxEntity;

import java.util.List;
import java.util.Map;

/**
 * 税种表
 *
 * @author zk
 * @email 
 * @date 2019-12-31 17:21:04
 */
public interface SysTaxService extends IService<SysTaxEntity> {

    /**
     * 分页
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 设置税种
     * @param deptId
     * @param taxIds
     */
    void setup(long deptId, List<Long> taxIds);
}


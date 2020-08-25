package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.FilingThirdCityCodeEntity;

import java.util.Map;

/**
 * 与第三方City对应关系表
 *
 * @author zk
 * @email 
 * @date 2020-02-03 15:54:10
 */
public interface FilingThirdCityCodeService extends IService<FilingThirdCityCodeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取第三方 city code
     * @param deptCityCode
     * @return
     */
    String getByDeptCityCode(String deptCityCode);
}


package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.FilingThirdDistrictEntity;

import java.util.Map;

/**
 * 第三方地区code
 *
 * @author zk
 * @email 
 * @date 2020-02-03 15:54:10
 */
public interface FilingThirdDistrictService extends IService<FilingThirdDistrictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


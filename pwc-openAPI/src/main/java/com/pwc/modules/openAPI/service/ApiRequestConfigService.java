package com.pwc.modules.openAPI.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.openAPI.entity.ApiRequestConfig;

import java.util.Map;


public interface ApiRequestConfigService extends IService<ApiRequestConfig> {

    PageUtils queryPage(Map<String, Object> params);

}


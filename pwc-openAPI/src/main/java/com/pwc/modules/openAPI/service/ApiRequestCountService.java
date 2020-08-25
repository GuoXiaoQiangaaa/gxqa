package com.pwc.modules.openAPI.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.openAPI.entity.ApiRequestCount;

import java.util.Map;


public interface ApiRequestCountService extends IService<ApiRequestCount> {


    PageUtils queryPage(Map<String, Object> params);

    void saveRequest(Integer companyId, String invoiceCheckTrue);
}


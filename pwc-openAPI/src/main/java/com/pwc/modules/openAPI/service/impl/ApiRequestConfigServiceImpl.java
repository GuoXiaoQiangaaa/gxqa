package com.pwc.modules.openAPI.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.openAPI.dao.ApiRequestConfigDao;
import com.pwc.modules.openAPI.entity.ApiRequestConfig;
import com.pwc.modules.openAPI.service.ApiRequestConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("apiRequestConfigService")
public class ApiRequestConfigServiceImpl extends ServiceImpl<ApiRequestConfigDao, ApiRequestConfig> implements ApiRequestConfigService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String companyId = (String)params.get("companyId");
        IPage<ApiRequestConfig> page = this.page(
                new Query<ApiRequestConfig>().getPage(params),
                new QueryWrapper<ApiRequestConfig>()
                        .eq(StringUtils.isNotBlank(companyId),"company_id", companyId)
        );
        return new PageUtils(page);
    }

}

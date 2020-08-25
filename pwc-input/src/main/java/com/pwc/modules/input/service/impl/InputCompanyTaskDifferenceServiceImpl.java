package com.pwc.modules.input.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputCompanyTaskDifferenceDao;
import com.pwc.modules.input.entity.InputCompanyTaskDifference;
import com.pwc.modules.input.service.InputCompanyTaskDifferenceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("companyTaskDifferenceService")
public class InputCompanyTaskDifferenceServiceImpl extends ServiceImpl<InputCompanyTaskDifferenceDao, InputCompanyTaskDifference> implements InputCompanyTaskDifferenceService {


    @Override
    public PageUtils getDifferenceList(Map<String, Object> params) {
        String statisticsMonth=(String)params.get("statisticsMonth");
        String companyId=String.valueOf(params.get("companyId"));
        IPage<InputCompanyTaskDifference> page = this.page(
                new Query<InputCompanyTaskDifference>().getPage(params, null, true),
                new QueryWrapper<InputCompanyTaskDifference>()
                        .eq(StringUtils.isNotBlank(statisticsMonth),"statistics_month",statisticsMonth)
                        .eq(StringUtils.isNotBlank(companyId),"company_id",companyId)
        );
        return new PageUtils(page);
    }
}

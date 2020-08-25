package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputCompanyTaskDetailDao;
import com.pwc.modules.input.entity.InputCompanyTaskDetail;
import com.pwc.modules.input.service.InputCompanyTaskDetailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("companyTaskDetailService")
public class InputCompanyTaskDetailServiceImpl extends ServiceImpl<InputCompanyTaskDetailDao, InputCompanyTaskDetail> implements InputCompanyTaskDetailService {


    @Override
    public PageUtils getDetailList(Map<String, Object> params) {
        String statisticsMonth=(String)params.get("statisticsMonth");
        String companyId=String.valueOf(params.get("companyId"));
        IPage<InputCompanyTaskDetail> page = this.page(
                new Query<InputCompanyTaskDetail>().getPage(params, null, true),
                new QueryWrapper<InputCompanyTaskDetail>()
                        .eq(StringUtils.isNotBlank(statisticsMonth),"statistics_month",statisticsMonth)
                        .eq(StringUtils.isNotBlank(companyId),"company_id",companyId)
        );
        return new PageUtils(page);
    }
}

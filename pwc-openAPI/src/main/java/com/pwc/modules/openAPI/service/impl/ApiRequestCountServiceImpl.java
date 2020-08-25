package com.pwc.modules.openAPI.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.openAPI.dao.ApiRequestCountDao;
import com.pwc.modules.openAPI.entity.ApiRequestCount;
import com.pwc.modules.openAPI.service.ApiRequestCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pwc.modules.sys.entity.SysDeptEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("apiRequestCountService")
public class ApiRequestCountServiceImpl extends ServiceImpl<ApiRequestCountDao, ApiRequestCount> implements ApiRequestCountService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String companyId = (String)params.get("companyId");
        IPage<ApiRequestCount> page = this.page(
                new Query<ApiRequestCount>().getPage(params),
                new QueryWrapper<ApiRequestCount>()
                        .eq(StringUtils.isNotBlank(companyId),"company_id", companyId)
        );
        return new PageUtils(page);
    }

    @Override
    public void saveRequest(Integer companyId, String invoiceCheckTrue) {
        ApiRequestCount one = this.getOne(
                new QueryWrapper<ApiRequestCount>()
                        .eq("company_id", companyId)
                        .eq("type", invoiceCheckTrue));
        if (one!=null){
            one.setCount(one.getCount()+1);
            this.updateById(one);
        }else{
            ApiRequestCount count=new ApiRequestCount();
            count.setCompanyId(companyId);
            count.setType(invoiceCheckTrue);
            count.setCount(1);
            this.save(count);
        }
    }
}

package com.pwc.modules.output.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.output.dao.OutputCustomerRulesDao;
import com.pwc.modules.output.entity.OutputCustomerRulesEntity;
import com.pwc.modules.output.service.OutputCustomerRulesService;


@Service("outputCustomerRulesService")
public class OutputCustomerRulesServiceImpl extends ServiceImpl<OutputCustomerRulesDao, OutputCustomerRulesEntity> implements OutputCustomerRulesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputCustomerRulesEntity> page = this.page(
                new Query<OutputCustomerRulesEntity>().getPage(params),
                new QueryWrapper<OutputCustomerRulesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean delByCustomerIds(List<Long> customerIds) {
        return baseMapper.delete(new QueryWrapper<OutputCustomerRulesEntity>().in("customer_id", customerIds)) > 0;
    }
}

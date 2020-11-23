package com.pwc.modules.output.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.output.dao.OutputBillingDao;
import com.pwc.modules.output.entity.OutputBillingEntity;
import com.pwc.modules.output.service.OutputBillingService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 销项Billing服务实现
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Service("outputBillingService")
public class OutputBillingServiceImpl extends ServiceImpl<OutputBillingDao, OutputBillingEntity> implements OutputBillingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputBillingEntity> page = this.page(
                new Query<OutputBillingEntity>().getPage(params),
                new QueryWrapper<OutputBillingEntity>()
        );

        return new PageUtils(page);
    }

}

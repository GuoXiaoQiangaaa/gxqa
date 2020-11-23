package com.pwc.modules.output.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.output.dao.OutputInvoiceApplyDetailDao;
import com.pwc.modules.output.entity.OutputInvoiceApplyDetailEntity;
import com.pwc.modules.output.service.OutputInvoiceApplyDetailService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 开票申请明细服务实现
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Service("outputInvoiceApplyDetailService")
public class OutputInvoiceApplyDetailServiceImpl extends ServiceImpl<OutputInvoiceApplyDetailDao, OutputInvoiceApplyDetailEntity> implements OutputInvoiceApplyDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputInvoiceApplyDetailEntity> page = this.page(
                new Query<OutputInvoiceApplyDetailEntity>().getPage(params),
                new QueryWrapper<OutputInvoiceApplyDetailEntity>()
        );

        return new PageUtils(page);
    }

}

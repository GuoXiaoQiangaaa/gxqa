package com.pwc.modules.output.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.output.dao.OutputInvoiceApplyDao;
import com.pwc.modules.output.entity.OutputInvoiceApplyEntity;
import com.pwc.modules.output.service.OutputInvoiceApplyService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 开票申请服务实现
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Service("outputInvoiceApplyService")
public class OutputInvoiceApplyServiceImpl extends ServiceImpl<OutputInvoiceApplyDao, OutputInvoiceApplyEntity> implements OutputInvoiceApplyService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputInvoiceApplyEntity> page = this.page(
                new Query<OutputInvoiceApplyEntity>().getPage(params),
                new QueryWrapper<OutputInvoiceApplyEntity>()
        );

        return new PageUtils(page);
    }

}

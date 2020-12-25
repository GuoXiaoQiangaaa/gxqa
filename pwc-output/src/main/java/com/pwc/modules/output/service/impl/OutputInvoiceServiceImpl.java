package com.pwc.modules.output.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.output.dao.OutputInvoiceDao;
import com.pwc.modules.output.entity.OutputInvoiceEntity;
import com.pwc.modules.output.service.OutputInvoiceService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 销项发票服务实现
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Service("outputInvoiceService")
public class OutputInvoiceServiceImpl extends ServiceImpl<OutputInvoiceDao, OutputInvoiceEntity> implements OutputInvoiceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputInvoiceEntity> page = this.page(
                new Query<OutputInvoiceEntity>().getPage(params),
                new QueryWrapper<OutputInvoiceEntity>()
        );

        return new PageUtils(page);
    }


}

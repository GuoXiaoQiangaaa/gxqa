package com.pwc.modules.output.service.impl;

import com.pwc.modules.output.entity.OutputGoodsEntity;
import com.pwc.modules.output.entity.OutputInvoiceRulesGoodsEntity;
import com.pwc.modules.output.service.OutputGoodsService;
import com.pwc.modules.output.service.OutputInvoiceRulesGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.output.dao.OutputInvoiceRulesDao;
import com.pwc.modules.output.entity.OutputInvoiceRulesEntity;
import com.pwc.modules.output.service.OutputInvoiceRulesService;


@Service("outputInvoiceRulesService")
public class OutputInvoiceRulesServiceImpl extends ServiceImpl<OutputInvoiceRulesDao, OutputInvoiceRulesEntity> implements OutputInvoiceRulesService {

    @Autowired
    private OutputInvoiceRulesGoodsService outputInvoiceRulesGoodsService;
    @Autowired
    private OutputGoodsService outputGoodsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputInvoiceRulesEntity> page = this.page(
                new Query<OutputInvoiceRulesEntity>().getPage(params),
                new QueryWrapper<OutputInvoiceRulesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OutputInvoiceRulesEntity getByCustomerId(Long customerId) {
        OutputInvoiceRulesEntity  invoiceRulesEntity = this.getOne(new QueryWrapper<OutputInvoiceRulesEntity>().eq("customer_id", customerId));
        if (null != invoiceRulesEntity) {
            List<OutputInvoiceRulesGoodsEntity> specialRules = outputInvoiceRulesGoodsService.getByRulesId(invoiceRulesEntity.getRulesId());
            for (OutputInvoiceRulesGoodsEntity special : specialRules) {
                OutputGoodsEntity goodsEntity = outputGoodsService.getById(special.getGoodsId());
                if (null != goodsEntity) {
                    special.setGoodsName(goodsEntity.getGoodsName());
                }
            }
        }
        return invoiceRulesEntity;
    }
}

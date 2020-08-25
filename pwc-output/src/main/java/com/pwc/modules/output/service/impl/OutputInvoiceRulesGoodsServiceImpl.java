package com.pwc.modules.output.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.output.dao.OutputInvoiceRulesGoodsDao;
import com.pwc.modules.output.entity.OutputInvoiceRulesGoodsEntity;
import com.pwc.modules.output.service.OutputInvoiceRulesGoodsService;


@Service("outputInvoiceRulesGoodsService")
public class OutputInvoiceRulesGoodsServiceImpl extends ServiceImpl<OutputInvoiceRulesGoodsDao, OutputInvoiceRulesGoodsEntity> implements OutputInvoiceRulesGoodsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputInvoiceRulesGoodsEntity> page = this.page(
                new Query<OutputInvoiceRulesGoodsEntity>().getPage(params),
                new QueryWrapper<OutputInvoiceRulesGoodsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<OutputInvoiceRulesGoodsEntity> getByRulesId(Long rulesId) {
        return this.list(new QueryWrapper<OutputInvoiceRulesGoodsEntity>().eq("rules_id", rulesId));
    }
}

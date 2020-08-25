package com.pwc.modules.output.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.output.dao.OutputGoodsRulesDao;
import com.pwc.modules.output.entity.OutputGoodsRulesEntity;
import com.pwc.modules.output.service.OutputGoodsRulesService;


@Service("outputGoodsRulesService")
public class OutputGoodsRulesServiceImpl extends ServiceImpl<OutputGoodsRulesDao, OutputGoodsRulesEntity> implements OutputGoodsRulesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputGoodsRulesEntity> page = this.page(
                new Query<OutputGoodsRulesEntity>().getPage(params),
                new QueryWrapper<OutputGoodsRulesEntity>()
        );

        return new PageUtils(page);
    }

}

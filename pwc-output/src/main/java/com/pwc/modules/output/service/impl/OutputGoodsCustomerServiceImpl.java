package com.pwc.modules.output.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.output.dao.OutputGoodsCustomerDao;
import com.pwc.modules.output.entity.OutputGoodsCustomerEntity;
import com.pwc.modules.output.service.OutputGoodsCustomerService;


@Service("outputGoodsCustomerService")
public class OutputGoodsCustomerServiceImpl extends ServiceImpl<OutputGoodsCustomerDao, OutputGoodsCustomerEntity> implements OutputGoodsCustomerService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputGoodsCustomerEntity> page = this.page(
                new Query<OutputGoodsCustomerEntity>().getPage(params),
                new QueryWrapper<OutputGoodsCustomerEntity>()
        );

        return new PageUtils(page);
    }

}

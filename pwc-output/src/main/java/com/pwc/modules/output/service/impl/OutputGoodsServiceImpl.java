package com.pwc.modules.output.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.output.dao.OutputGoodsDao;
import com.pwc.modules.output.entity.OutputGoodsEntity;
import com.pwc.modules.output.service.OutputGoodsService;


@Service("outputGoodsService")
public class OutputGoodsServiceImpl extends ServiceImpl<OutputGoodsDao, OutputGoodsEntity> implements OutputGoodsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputGoodsEntity> page = this.page(
                new Query<OutputGoodsEntity>().getPage(params),
                new QueryWrapper<OutputGoodsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<OutputGoodsEntity> queryAllGoods(String keywords) {
        return this.list(new QueryWrapper<OutputGoodsEntity>().like("goods_no", keywords).or().like("goods_name", keywords));
    }
}

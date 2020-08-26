package com.pwc.modules.data.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.OutputGoodsDao;
import com.pwc.modules.data.entity.OutputGoodsEntity;
import com.pwc.modules.data.service.OutputGoodsService;

/**
 * 商品信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
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

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputGoodsEntity reqVo) {
        super.updateById(reqVo);
    }
}

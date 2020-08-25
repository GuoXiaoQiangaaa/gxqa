package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputGoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:05
 */
public interface OutputGoodsService extends IService<OutputGoodsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询所有商品
     * @return
     */
    List<OutputGoodsEntity> queryAllGoods(String keywords);
}


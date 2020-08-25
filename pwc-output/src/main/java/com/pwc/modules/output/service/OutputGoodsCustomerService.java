package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputGoodsCustomerEntity;

import java.util.Map;

/**
 * 商品客户关联价格
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
public interface OutputGoodsCustomerService extends IService<OutputGoodsCustomerEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


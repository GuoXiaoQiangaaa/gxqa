package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputGoodsRulesEntity;

import java.util.Map;

/**
 * 商品规则
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
public interface OutputGoodsRulesService extends IService<OutputGoodsRulesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


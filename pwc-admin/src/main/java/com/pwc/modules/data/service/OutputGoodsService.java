package com.pwc.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.data.entity.OutputGoodsEntity;

import java.util.Map;

/**
 * 商品信息服务
 *
 * @author fanpf
 * @date 2020/8/24
 */
public interface OutputGoodsService extends IService<OutputGoodsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


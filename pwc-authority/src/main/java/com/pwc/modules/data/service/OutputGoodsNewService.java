package com.pwc.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.data.entity.OutputGoodsNewEntity;

import java.util.Map;

/**
 * 商品信息服务
 *
 * @author fanpf
 * @date 2020/8/24
 */
public interface OutputGoodsNewService extends IService<OutputGoodsNewEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 新增
     */
    boolean save(OutputGoodsNewEntity outputGoods);

    /**
     * 编辑
     */
    boolean updateById(OutputGoodsNewEntity outputGoods);

    /**
     * 禁用/启用
     */
    void disableOrEnable(OutputGoodsNewEntity reqVo);

    /**
     * 关键字查询
     */
    PageUtils search(Map<String, Object> params);
}


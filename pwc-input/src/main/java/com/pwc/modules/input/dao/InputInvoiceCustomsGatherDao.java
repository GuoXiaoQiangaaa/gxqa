package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceCustomsGatherEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 海关缴款书（同步）
 * 
 * @author zk
 * @email 
 * @date 2020-12-16 13:26:51
 */
@Mapper
public interface InputInvoiceCustomsGatherDao extends BaseMapper<InputInvoiceCustomsGatherEntity> {

    int getListByShow();

}

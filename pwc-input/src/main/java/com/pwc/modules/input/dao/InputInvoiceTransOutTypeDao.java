package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceTransOutTypeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 
 * 
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2020-01-12 13:17:28
 */
@Mapper
public interface InputInvoiceTransOutTypeDao extends BaseMapper<InputInvoiceTransOutTypeEntity> {
    PageUtils findList(Map<String, Object> params);

    void save(InputInvoiceTransOutTypeEntity invoiceTransOutTypeEntity);

    InputInvoiceTransOutTypeEntity get(InputInvoiceTransOutTypeEntity invoiceTransOutTypeEntity);



}

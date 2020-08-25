package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputSapInvoiceMappingIdEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * DAO接口
 * @author cj
 * @version 2018-04-01
 */
@Mapper
public interface InputSapInvoiceMappingIdDao extends BaseMapper<InputSapInvoiceMappingIdEntity> {
    InputSapInvoiceMappingIdEntity getOneBySapId(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity);
    void save(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity);
    void update(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity);
    List<InputSapInvoiceMappingIdEntity> getListByTaxOrName(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity);

}

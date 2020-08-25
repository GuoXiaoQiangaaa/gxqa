package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceConditionMap;
import com.pwc.modules.input.entity.InputInvoiceMaterialSapEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InputInvoiceConditionMapDao extends BaseMapper<InputInvoiceConditionMap> {

    InputInvoiceConditionMap getMaxConditionCode(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);

    List<InputInvoiceConditionMap> getAllList();

    PageUtils getList(Map<String, Object> params);

    void update(InputInvoiceConditionMap invoiceConditionMap);

    void save(InputInvoiceConditionMap invoiceConditionMap);

    InputInvoiceConditionMap get(InputInvoiceConditionMap invoiceConditionMap);

    InputInvoiceConditionMap getByCondition(InputInvoiceConditionMap invoiceConditionMap);

    void updateByDelete(InputInvoiceConditionMap invoiceConditionMap);

    InputInvoiceConditionMap getMaxByList(@Param("list") List<String> condition);
}

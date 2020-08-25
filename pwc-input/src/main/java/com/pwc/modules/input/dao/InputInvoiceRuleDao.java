package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceRuleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 *
 * @author qiuyin
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputInvoiceRuleDao extends BaseMapper<InputInvoiceRuleEntity> {
//    List<InputInvoiceRuleEntity> findList(Pagination page, @Param("invoiceRule") InputInvoiceRuleEntity invoiceRule, @Param("key") String key);
    InputInvoiceRuleEntity get(InputInvoiceRuleEntity invoiceRuleEntity);
    List<InputInvoiceRuleEntity> getList();
    void save(InputInvoiceRuleEntity invoiceRuleEntity);
    void ruleDelete(Integer[] ids);
    InputInvoiceRuleEntity getOneByName(InputInvoiceRuleEntity invoiceRuleEntity);
}

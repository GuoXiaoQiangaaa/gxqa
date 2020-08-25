package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputExpenseInvoiceMappingBean;
import com.pwc.modules.input.entity.InputOaExpenseInvoiceMappingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-23 15:15:03
 */
@Mapper
public interface InputOaExpenseInvoiceMappingDao extends
        BaseMapper<InputOaExpenseInvoiceMappingEntity> {

        int save(InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMappingEntity);

        InputOaExpenseInvoiceMappingEntity getById(Integer id);
    
        void removeByIds(Integer[] ids);
    
        List<Integer> getInvoiceEntitiesByExpenseNoAndType(Integer expenseNo, String invoiceType);

        List<Integer> getInformalInvoiceEntitiesByExpenseNoAndType(Integer expenseNo, String invoiceType);
    
        List<InputExpenseInvoiceMappingBean> getInvoiceExpenseMappingRelationShip(Integer expenseNo, String sqlFilter);
    
        String getSumAmountOfInvoiceByExpenseNo(List<Integer> invoiceIds);
    
        InputExpenseInvoiceMappingBean getInvoicesAmountsAndCountsByIds(List<Integer> invoiceIds, @Param("sqlFilter") String sqlFilter);
    
        InputExpenseInvoiceMappingBean getUnformatInvoiceIdsAmountsByIds(List<Integer> unformatIds, @Param("sqlFilter") String sqlFilter);
    
        List<String> getInformalInvoiceIdsByExpenseNoAndType(String expenseNo, String invoiceType);

        List<Integer> getInvoiceEntitiesByExpenseNoAndTypeAndStatus(@Param("expenseNo")Integer expenseNo, @Param("invoiceType")List<String> invoiceType, @Param("list")List<Integer> invoiceStatus);

}

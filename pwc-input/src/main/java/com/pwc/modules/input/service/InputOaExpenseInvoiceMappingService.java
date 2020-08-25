package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputExpenseInvoiceMappingBean;
import com.pwc.modules.input.entity.InputOaExpenseInvoiceMappingEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-23 15:15:03
 */
public interface InputOaExpenseInvoiceMappingService extends IService<InputOaExpenseInvoiceMappingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    InputOaExpenseInvoiceMappingEntity getById(Integer id);

    void removeByIds(Integer[] ids);

    List<InputExpenseInvoiceMappingBean> getInvoiceExpenseMappingRelationShip(Integer expenseNo, String sqlFilter);

    List<Integer> getInvoiceEntitiesByExpenseNoAndType(Integer ExpenseNo, String invoiceType);

    List<String> getInformalInvoiceIdsByExpenseNoAndType(String expenseNo, String invoiceType);

    String getSumAmountOfInvoiceByExpenseNo(List<Integer> invoiceIds);

    List<Integer> getInformalInvoiceEntitiesByExpenseNoAndType(Integer expenseNo, String invoiceType);

    InputExpenseInvoiceMappingBean getInvoicesAmountsAndCountsByIds(List<Integer> invoiceIds,String sqlFilter);

    InputExpenseInvoiceMappingBean getUnformatInvoiceIdsAmountsByIds(List<Integer> unformatIds,String sqlFilter);

    List<InputExpenseInvoiceMappingBean> getByExpenseNo(Map<String,Object> param);

    List<Integer> getInvoiceEntitiesByExpenseNoAndTypeAndStatus(Integer id, List<String> type, List<Integer> s);
}


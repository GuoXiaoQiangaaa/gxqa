package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputOaExpenseInvoicetypeMappingEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-29 02:09:58
 */
public interface InputOaExpenseInvoicetypeMappingService extends
        IService<InputOaExpenseInvoicetypeMappingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    InputOaExpenseInvoicetypeMappingEntity getById(Integer id);

    void removeByIds(Integer[] ids);

    List<InputOaExpenseInvoicetypeMappingEntity> getOaExpenseInvoicetypeMappingEntitiesByExpenseNo(Integer expenseNo);

}


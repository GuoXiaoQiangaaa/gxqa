package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputOaExpenseInvoicetypeMappingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-29 02:09:58
 */
@Mapper
public interface InputOaExpenseInvoicetypeMappingDao extends
        BaseMapper<InputOaExpenseInvoicetypeMappingEntity> {

        int save(InputOaExpenseInvoicetypeMappingEntity oaExpenseInvoicetypeMappingEntity);

        InputOaExpenseInvoicetypeMappingEntity getById(Integer id);
        void removeByIds(Integer[] ids);

        List<InputOaExpenseInvoicetypeMappingEntity> getOaExpenseInvoicetypeMappingEntitiesByExpenseNo(Integer expenseNo);


}

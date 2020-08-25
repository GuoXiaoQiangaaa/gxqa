package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputOaExpenseInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-23 15:15:03
 */
@Mapper
public interface InputOaExpenseInfoDao extends BaseMapper<InputOaExpenseInfoEntity> {
    int save(InputOaExpenseInfoEntity oaExpenseInfoEntity);
    InputOaExpenseInfoEntity getById(Integer id);
    void removeByIds(List<Integer> ids);
  int update(InputOaExpenseInfoEntity oaExpenseInfoEntity);


    List<InputInvoiceEntity> getInvoiceEntitiesByExpenseNoAndType(Integer ExpenseNo, String invoiceType);


}

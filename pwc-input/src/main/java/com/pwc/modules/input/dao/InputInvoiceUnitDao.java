package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceUnitEntity;
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
public interface InputInvoiceUnitDao extends BaseMapper<InputInvoiceUnitEntity> {
//    List<InputInvoiceUnitEntity> findList(Pagination page, @Param("invoiceUnit") InputInvoiceUnitEntity invoiceUnit, @Param("key") String key);
    InputInvoiceUnitEntity get(InputInvoiceUnitEntity invoiceUnitEntity);
    List<InputInvoiceUnitEntity> getList();
    List<InputInvoiceUnitEntity> getUnit(InputInvoiceUnitEntity invoiceUnitEntity);
    InputInvoiceUnitEntity save(InputInvoiceUnitEntity invoiceUnitEntity);
    void unitDelete(Integer[] ids);
}

package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceResponsibleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author qiuyin
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputInvoiceResponsibleDao extends BaseMapper<InputInvoiceResponsibleEntity> {
    InputInvoiceResponsibleEntity getByResponsibleAndCategory(InputInvoiceResponsibleEntity invoiceResponsibleEntity);
//
    void updateByResponsibleAndCategory(InputInvoiceResponsibleEntity invoiceResponsibleEntity);
//
//    List<InputInvoiceResponsibleEntity> findList(Pagination page, @Param("invoiceResponsible") InputInvoiceResponsibleEntity invoiceResponsible, @Param("key") String key);
//
    InputInvoiceResponsibleEntity get(InputInvoiceResponsibleEntity invoiceResponsibleEntity);
//
    List<InputInvoiceResponsibleEntity> getList();
//
//    void save(InputInvoiceResponsibleEntity invoiceResponsibleEntity);
//
    void responsibleDelete(Integer[] ids);
//
    InputInvoiceResponsibleEntity getOneByCondition(InputInvoiceResponsibleEntity invoiceResponsibleEntity);
//
    InputInvoiceResponsibleEntity getOneById(InputInvoiceResponsibleEntity invoiceResponsibleEntity);
//
    void updateResponsibleDelete(InputInvoiceResponsibleEntity invoiceResponsibleEntity);
//
    void deleteAll();
//
    void insertAll(List<InputInvoiceResponsibleEntity> list);
}

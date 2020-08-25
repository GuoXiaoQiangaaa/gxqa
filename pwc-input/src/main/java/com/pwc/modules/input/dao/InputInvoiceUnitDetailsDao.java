package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceUnitDetailsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author qiuyin
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputInvoiceUnitDetailsDao extends BaseMapper<InputInvoiceUnitDetailsEntity> {
    InputInvoiceUnitDetailsEntity getByUnitName(@Param("unitName") String unitName);
    InputInvoiceUnitDetailsEntity getOneByNameOrCode(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    InputInvoiceUnitDetailsEntity getOneByUnitAndNameOrCode(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
//    List<InputInvoiceUnitDetailsEntity> findList(Pagination page, @Param("invoiceUnitDetails") InputInvoiceUnitDetailsEntity invoiceRule, @Param("key") String key);
    InputInvoiceUnitDetailsEntity get(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    List<InputInvoiceUnitDetailsEntity> getUnitList(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    List<InputInvoiceUnitDetailsEntity> getUnit(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    InputInvoiceUnitDetailsEntity getFind(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    void detailsDelete(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    List<InputInvoiceUnitDetailsEntity> getByDetailsNames(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
//    void deleteList(Integer[] invoiceUnit);
//    List<Integer> findListByInvoiceUnit(Integer invoiceUnit);
}

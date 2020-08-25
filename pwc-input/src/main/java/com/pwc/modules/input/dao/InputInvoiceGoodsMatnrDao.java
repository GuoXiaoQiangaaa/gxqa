package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceGoodsMatnr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InputInvoiceGoodsMatnrDao extends BaseMapper<InputInvoiceGoodsMatnr> {
    void updateByThreebasic(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    InputInvoiceGoodsMatnr getByThreeBasic(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    InputInvoiceGoodsMatnr getById(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    void update(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    void save(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    void delete(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    void deleteByIds(@Param("array") Integer[] ids);

    List<InputInvoiceGoodsMatnr> getListByCondition(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    InputInvoiceGoodsMatnr getOneByCondition(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    List<InputInvoiceGoodsMatnr> getListAll();

    void deleteAll();

    void insertAll(List<InputInvoiceGoodsMatnr> list);
}

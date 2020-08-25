package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发票物料DAO接口
 * @author cj
 * @version 2018-12-29
 */
@Mapper
public interface InputInvoiceMaterialDao extends BaseMapper<InputInvoiceMaterialEntity> {
    void updateByEnter(@Param("list") List<InputInvoiceMaterialEntity> invoiceMaterialEntityList);
    List<InputInvoiceMaterialEntity> getByInvoiceIds(@Param("list") List<Integer> invoiceIds);
//    InputInvoiceMaterialEntity getListByMaterialId(InputInvoiceMaterialEntity invoiceMaterial);
//    void save(InputInvoiceMaterialEntity invoiceMaterialEntity);
//    List<InputInvoiceMaterialEntity> findList(Pagination page, @Param("invoiceMaterial") InputInvoiceMaterialEntity invoiceMaterial, @Param("key") String key);
//    List<InputInvoiceMaterialEntity> list(InputInvoiceMaterialEntity invoiceMaterialEntity);
    List<InputInvoiceMaterialEntity> getListByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity);
    List<InputInvoiceMaterialEntity>getByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity);
//    List<InputInvoiceMaterialEntity> get(InputInvoiceMaterialEntity invoiceMaterialEntity);
    void update(InputInvoiceMaterialEntity invoiceMaterialEntity);
//    void updateStatus(InputInvoiceMaterialEntity invoiceMaterialEntity);
//    InputInvoiceMaterialEntity getById(InputInvoiceMaterialEntity invoiceMaterialEntity);
    List<InputInvoiceMaterialEntity> getByIds(@Param("array") Integer[] ids);
//    List<InputInvoiceMaterialEntity> getByIdsGroupByInvoiceId(@Param("array") Integer[] ids);
    void deleteInvoiceMaterialByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity);
    List<InputInvoiceMaterialEntity> getListByIds(InputInvoiceMaterialEntity invoiceMaterialEntity);
//    void mateLose(@Param("ids") String[] ids);
//    /**
//     * 获取发票下的所有物料信息
//     * @param invoiceEntity
//     * @return
//     */
//    List<InputInvoiceMaterialEntity> getMaterialListByInvoiceId(InputInvoiceEntity invoiceEntity);
//    void updateMatchStatus3(InputInvoiceMaterialEntity invoiceMaterialEntity);
//    void updateDeleteTax(InputInvoiceMaterialEntity invoiceMaterialEntity);
}

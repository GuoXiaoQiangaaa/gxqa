package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceMaterialSapEntity;
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
public interface InputInvoiceMaterialSapDao extends BaseMapper<InputInvoiceMaterialSapEntity> {
    String getMaxQmdateByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
    String getMaxBudatMkpfByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
    List<InputInvoiceMaterialSapEntity> getListBySapId(@Param("list") List<InputInvoiceMaterialSapEntity> list);
    void updateByPostQm(@Param("list") List<InputInvoiceMaterialSapEntity> list);
//    void updateByPostUnitprice(@Param("list") List<InputInvoiceMaterialSapEntity> list);
//    List<InputInvoiceMaterialSapEntity> getListByMblnrAndZeile(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
    void updateByEnter(@Param("list") List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList);
//    void updateByIsAccount(@Param("list") List<Integer> sapIds);
//    List<InputInvoiceMaterialSapEntity> getListByBatchIds(@Param("list") List<Integer> batchIds);
    InputInvoiceMaterialSapEntity get(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    void mateLose(@Param("ids") String[] ids);
//    List<InputInvoiceMaterialSapEntity> getById(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByMated(InputInvoiceMaterialEntity invoiceMaterialEntity);
    List<InputInvoiceMaterialSapEntity> getListByLineIdAndMate(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> findList(Pagination page, @Param("invoiceRule") InputInvoiceMaterialSapEntity InvoiceMaterialSap, @Param("key") String key);
//    List<InputInvoiceMaterialSapEntity> getListByMBLNR(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
    List<InputInvoiceMaterialSapEntity> getListByMBLNRids(@Param("mblnrs") List<String> MBLNRIds);
//
//    void save(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
    void update(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByMBLNRAndBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    void updateMate(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByMATNR(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByIds(@Param("array") String[] sapIds);
    void deleteSapByInvoiceBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> findListForSap(@Param("offset") int offset, @Param("limit") int limit, @Param("mblnr") String mblnr);
//    int selectCount(@Param("mblnr") String mblnr);
    List<InputInvoiceMaterialSapEntity> getListByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByBatchId2(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByMatkl(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByMaktx(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByMatnr(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListByMate();
    void deleteByBatchIdAndMblnr(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    void updateByBatchIdAndMaterialLineId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    void updateMatchStatusByMatnrAndBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    void deleteById(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    void updateDeleteTax(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    List<InputInvoiceMaterialSapEntity> getListForUpdateData();
//    void updateMatchStatusByMatnrAndBatchId2(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    void insertList(@Param("list") List<InputInvoiceMaterialSapEntity> list);
    void updateLineId(@Param("list") List<InputInvoiceMaterialSapEntity> list);
}

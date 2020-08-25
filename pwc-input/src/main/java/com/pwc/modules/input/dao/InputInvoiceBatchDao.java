package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceBatchEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据批次DAO接口
 * @author cj
 * @version 2018-12-05
 */
@Mapper
public interface InputInvoiceBatchDao extends BaseMapper<InputInvoiceBatchEntity> {
//    List<InputInvoiceBatchEntity> getMigrationList();
//    int getListByShow(@Param("companyIds") String[] companyIds);
//    List<InputInvoiceBatchEntity> getListById(@Param("array") int[] array);
//    List<InputInvoiceBatchEntity> findList(Pagination page, @Param("invoiceBatch") InputInvoiceBatchEntity invoiceBatch, @Param("key") String key);
    List<InputInvoiceBatchEntity> getList();
//    List<InputInvoiceBatchEntity> getListBatch();
    InputInvoiceBatchEntity getLastOne();
    InputInvoiceBatchEntity get(InputInvoiceBatchEntity invoiceBatchEntity);
//    int save(InputInvoiceBatchEntity invoiceBatchEntity);
    void update(InputInvoiceBatchEntity invoiceBatchEntity);
//
    List<InputInvoiceBatchEntity> findListForThree(@Param("offset") int offset, @Param("limit") int limit, @Param("invoicePurchaserCompany") String invoicePurchaserCompany,
                                              @Param("invoiceSellCompany") String invoiceSellCompany, @Param("array") String[] array, @Param("statusSql") String statusSql,
                                              @Param("createTime") String createTime, @Param("createTime2") String createTime2, @Param("createBy") String createBy, @Param("updateBy") String updateBy,
                                              @Param("invoiceCode") String invoiceCode, @Param("invoiceNumber") String invoiceNumber, @Param("invoiceEntity") String invoiceEntity,
                                              @Param("invoiceType") String invoiceType, @Param("invoiceCreateDateBegin") String invoiceCreateDateBegin, @Param("invoiceCreateDateEnd") String invoiceCreateDateEnd,
                                              @Param("entryDateBegin") String entryDateBegin, @Param("entryDateEnd") String entryDateEnd,
                                              @Param("minAmount") String minAmount, @Param("maxAmount") String maxAmount, @Param("sqlFilter") String sqlFilter);
//
    int findListForThree2(@Param("offset") int offset, @Param("limit") int limit, @Param("invoicePurchaserCompany") String invoicePurchaserCompany, @Param("invoiceSellCompany") String invoiceSellCompany,
                          @Param("array") String[] array, @Param("companyIds") String[] companyIds, @Param("statusSql") String statusSql, @Param("createTime") String createTime,
                          @Param("createTime2") String createTime2, @Param("createBy") String createBy, @Param("updateBy") String updateBy,
                          @Param("invoiceCode") String invoiceCode, @Param("invoiceNumber") String invoiceNumber, @Param("invoiceEntity") String invoiceEntity,
                          @Param("invoiceType") String invoiceType, @Param("invoiceCreateDateBegin") String invoiceCreateDateBegin, @Param("invoiceCreateDateEnd") String invoiceCreateDateEnd,
                          @Param("entryDateBegin") String entryDateBegin, @Param("entryDateEnd") String entryDateEnd,
                          @Param("minAmount") String minAmount, @Param("maxAmount") String maxAmount,  @Param("sqlFilter") String sqlFilter);

//    int selectCount(@Param("invoicePurchaserCompany") String invoicePurchaserCompany, @Param("invoiceSellCompany") String invoiceSellCompany);
//    List<InputInvoiceBatchEntity>getByInvoiceBatchId(InputInvoiceBatchEntity invoiceBatchEntity);
//    InputInvoiceBatchEntity info(InputInvoiceBatchEntity invoiceBatchEntity);
//    List<InputInvoiceMaterialSapEntity> getListByMATNR(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
//    void deleteByInvoiceBatchNumber(InputInvoiceBatchEntity invoiceBatchEntity);
    InputInvoiceBatchEntity getBatchNumber(InputInvoiceEntity invoiceEntity);
    void deleteBatch(InputInvoiceBatchEntity invoiceBatchEntity);
//
//    /**
//     * 根据Id获取批次对象
//     * @param batchIds
//     * @return
//     */
//    List<InputInvoiceBatchEntity> getBatchNumbersById(@Param("array") String[] batchIds);
}

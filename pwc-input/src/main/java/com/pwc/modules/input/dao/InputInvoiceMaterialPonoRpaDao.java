package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceDetailsDocumentRpa;
import com.pwc.modules.input.entity.InputInvoiceMaterialDocumentRpa;
import com.pwc.modules.input.entity.InputInvoiceMaterialPonoRpa;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InputInvoiceMaterialPonoRpaDao extends BaseMapper<InputInvoiceMaterialPonoRpa> {
    /**
     * 获取订单编号集合
     * @param invoicegroupRpaId
     * @return
     */
    List<InputInvoiceMaterialPonoRpa> getListByInvoicegroupRpaId(int invoicegroupRpaId);

    /**
     * 根据订单编号表查询物料凭证编码
     * @param ponoDocumentId
     * @return
     */
    List<InputInvoiceMaterialDocumentRpa> getListByPonoDocumentId(int ponoDocumentId);

    /**
     * 根据订单编号表查询费用凭证编码
     * @param ponoCostId
     * @return
     */
    List<InputInvoiceMaterialDocumentRpa> getListByPonoCostId(int ponoCostId);

    /**
     * 获取材料类物料表详情
     * @param documentRpaId
     * @return
     */
    List<InputInvoiceDetailsDocumentRpa> getListByDocumentRpaId(int documentRpaId);

    /**
     * 获取费用类物料表详情
     * @param costRpaId
     * @return
     */
    List<InputInvoiceDetailsDocumentRpa> getListByCostRpaId(int costRpaId);

    int saveInvoiceMaterialPonoRpa(InputInvoiceMaterialPonoRpa invoiceMaterialPonoRpa);
    int saveInvoiceMaterialDocumentRpa(InputInvoiceMaterialDocumentRpa invoiceMaterialDocumentRpa);
    int saveInvoiceMaterialCostRpa(InputInvoiceMaterialDocumentRpa invoiceMaterialDocumentRpa);

    int saveInvoiceDetailsDocument(InputInvoiceDetailsDocumentRpa invoiceDetailsDocumentRpa);
    int saveInvoiceDetailsCost(InputInvoiceDetailsDocumentRpa invoiceDetailsDocumentRpa);
}

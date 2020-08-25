package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputOAInvoiceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InputOAInvoiceInfoDao extends BaseMapper<InputOAInvoiceInfo> {
    List<InputOAInvoiceInfo> getOaInvoiceInfoByCodeAndNumber(InputOAInvoiceInfo oaInvoiceInfo);
    void updateByRequestId(InputOAInvoiceInfo oaInvoiceInfo);
    List<InputOAInvoiceInfo> getMigrationList();
    void save(InputOAInvoiceInfo invoiceInfo);

    void update(InputOAInvoiceInfo invoiceInfo);

    InputOAInvoiceInfo get(InputOAInvoiceInfo oaInvoiceInfo);

    void updateByIds(@Param("array") Integer[] ids);
    void updateByIds2(@Param("array") Integer[] ids);
    void deleteRefunds(@Param("array") Integer[] ids);

    void setDescription(@Param("invoiceRemark") String description, @Param("array") String[] ids);

    List<InputOAInvoiceInfo> getListByIds(@Param("array") String[] ids);

    void insertList(@Param("list") List<InputOAInvoiceInfo> list);

    /**
     * 根据指定id获取发票集合
     * @param oaInvoiceInfo
     * @return
     */
    List<InputOAInvoiceInfo> getListById(InputOAInvoiceInfo oaInvoiceInfo);

    int getListByShow();

    InputOAInvoiceInfo getByDocId(InputOAInvoiceInfo oaInvoiceInfo);

    List<InputOAInvoiceInfo> getListByRequest(@Param("requestId") String requestId);

    List<InputOAInvoiceInfo> getListByNumberAndCode(InputOAInvoiceInfo oaInvoiceInfo);

    void deleteByRequestId(String requestId);

    void updateForReFund(InputOAInvoiceInfo oaInvoiceInfo);

    void updatePassEntry(InputOAInvoiceInfo oaInvoiceInfo);
    List<InputOAInvoiceInfo> getListByRequestIdAndBarcode(InputOAInvoiceInfo oaInvoiceInfo);

}

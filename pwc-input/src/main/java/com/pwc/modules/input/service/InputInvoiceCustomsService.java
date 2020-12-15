package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.ApplyDeductResultInfo;
import com.fapiao.neon.model.in.CustomsInvoice;
import com.fapiao.neon.model.in.CustomsInvoiceInfo;
import com.fapiao.neon.model.in.CustomsInvoiceResult;
import com.fapiao.neon.param.PaymentCertificateParamBody;
import com.fapiao.neon.param.in.SyncInvoiceParamBody;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceCustomsEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceSapEntity;

import java.util.List;
import java.util.Map;

/**
 * 海关缴款书（同步）
 *
 * @author zlb
 * @email 
 * @date 2020-08-10 18:53:50
 */
public interface InputInvoiceCustomsService extends IService<InputInvoiceCustomsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 海关底账库同步
     * @param syncInvoiceParamBody
     * @return
     */
    CallResult<CustomsInvoiceInfo> invoices(SyncInvoiceParamBody syncInvoiceParamBody);

    /**
     * 同步海关缴款书
     */
    void sync(String taxNo, String startBillingDate, String endBillingDate, int page);

    String syncApply(String taxNo, String payNo, String billingDate, String totalTax);

    List<CustomsInvoiceResult> syncApplyResult(String requestId);

    /**
     * 海关缴款书入库
     */
    void saveInvoice(List<? extends CustomsInvoice> invoices);

    /**
     * 勾选/撤销
     */
    void deductOrCancel(Map<String, Object> params);

    /**
     * 获取勾选/撤销结果(定时任务用)
     */
    void getDeductOrCancelResult();


    /**
     * 手动勾选
     * @param invoiceIds
     * @param type
     * @param period
     */
    void deductInvoices(String invoiceIds, String type, String period);
    /**
     * 勾选
     * @param invoiceList
     * @return
     */
    void deductInvoices(List<InputInvoiceCustomsEntity> invoiceList, String deductType, String period);

    /**
     * 勾选
     * @param invoice
     * @return
     */
    String deductInvoices(InputInvoiceCustomsEntity invoice, String deductType,String period);
    /**
     * 海关缴款书勾选接口
     * @param paymentCertificateParamBody
     * @return
     */
    CallResult<ApplyDeductResultInfo> deductPaymentCertificates(PaymentCertificateParamBody paymentCertificateParamBody);
    /**
     * 勾选结果
     */
    void deductResult(InputInvoiceCustomsEntity invoice);

    /**
     * 入账或冲销
     * @param payNos
     * @param type
     */
    void entryInvoices(String payNos, String type);

    int getListByShow();
    /**
     * 海关缴款书SAP入账或冲销凭证信息推送
     * @param params
     * @return
     */
    Map saveVoucherPush(List<Map<String,Object>> params);

    PageUtils getMonthCredBeforeResult(Map<String, Object> params);
    /**
     * 作废(退票)
     */
    Map<String, Object> updateByIdReturn(Map<String, Object> params);
    /**
     * 人工入账
     *  @param entryState
     * @param invoiceId
     * @param voucherNumber
     */
    void entryByManual(String entryState, String invoiceId, String voucherNumber);

    /**
     * 凭证抽取
     */
    void voucherExtract();

    /**
     * 根据缴款书id获取所属期
     * @param id
     * @return
     */
    InputInvoiceCustomsEntity getPeriodById(String id);

    /**
     * 查询账票匹配成功数据
     * @param params
     * @return
     */
    PageUtils getListBySuccess(Map<String, Object> params);

    /**
     * 查询有票无帐数据
     * @param params
     * @return
     */
    PageUtils getListByNo(Map<String, Object> params);

    /**
     * 匹配差异数据
     * @param params
     * @return
     */
    PageUtils getListByError(Map<String, Object> params);

    /**
     * 手工入账
     * @param params
     */
    String manualEntry(Map<String, Object> params);

    /**
     * 自动入账
     * @param sapEntity
     */
    InputInvoiceSapEntity updateByEntry(InputInvoiceSapEntity sapEntity);

    /**
     * 获取查询月份的认证数据
     * @param params
     * @return
     */
    List<InputInvoiceCustomsEntity> getCertification(Map<String, Object> params);

}


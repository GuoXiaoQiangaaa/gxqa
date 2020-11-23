package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.*;
import com.fapiao.neon.model.in.inspect.BaseInvoice;
import com.fapiao.neon.param.in.*;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceSyncEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zk
 * @email 
 * @date 2020-01-19 18:27:48
 */
public interface InputInvoiceSyncService extends IService<InputInvoiceSyncEntity> {

    PageUtils queryPage(Map<String, Object> params, InputInvoiceSyncEntity entity);

    void sync();

    CallResult<SyncInvoiceInfo> invoiceSync(SyncInvoiceParamBody syncInvoiceParamBody);

    CallResult<BaseInvoice> invoiceCheck(InvoiceInspectionParamBody invoiceInspectionParamBody);

    CallResult<ApplyDeductResultInfo> deductInvoices(DeductParamBody deductParamBody);

    CallResult<DeductResultListInfo> deductResult(CommonParamBody commonParamBody);

    InputInvoiceSyncEntity queryInvoiceSync(Map<String, Object> params);

    InputInvoiceSyncEntity findInvoiceSync(InputInvoiceEntity invoiceEntity);

    void saveInvoice(List<? extends SyncInvoice> invoiceSyncList);


    /**
     * 申请/撤销统计
     */
    void statistics(Map<String, Object> params);

    /**
     * 获取统计结果
     */
    void statisticsResult();

    /**
     * 确认统计
     */
    void applyConfirm(Map<String, Object> params);

    /**
     * 获取确认结果
     */
    void confirmResult();

    /**
     * 勾选
     * @param invoiceIds 多个以逗号隔开
     * @return
     */
    void deductInvoices(String invoiceIds, String deductType);

    /**
     * 勾选
     * @param invoiceList
     * @return
     */
    void deductInvoices(List<InputInvoiceEntity> invoiceList, String deductType);

    /**
     * 勾选
     * @param invoiceList
     * @return
     */
    String deductInvoices(InputInvoiceEntity invoice, String deductType);

    /**
     * 勾选结果
     */
    void deductResult();

    /**
     * 查验发票，作废红冲修改
     */
    void checkInvoice();

    CallResult<Page> invoiceCollection(InvoiceCollectionParamBody invoiceCollectionParamBody);
    void syncInvoice(int page, String startDate, String endDate, String taxNo);

    /**
     * 根据开票日期查询数据
     * @param date
     * @return
     */
    List<InputInvoiceSyncEntity>  findByBillingDate(String date);
}


package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.*;

import java.util.List;
import java.util.Map;

/**
 * 票据上传Service
 * @author cj
 * @version 2018-12-05
 */
public interface InputInvoiceService extends IService<InputInvoiceEntity> {

    List<InputInvoiceEntity> getListAndCreateName(List<InputInvoiceEntity> list, String createUserName);

    PageUtils queryPage(Map<String, Object> params, InputInvoiceEntity invoiceEntity);

    void setBatchNumber(List<InputInvoiceEntity> invoiceEntities);

    PageUtils findBillList(Map<String, Object> params,InputInvoiceEntity invoiceEntity);

    List<InputInvoiceEntity> getListByBatchId(InputInvoiceEntity invoiceEntity);

    List<InputInvoiceEntity> getListByBatchIds(InputInvoiceEntity invoiceEntity);

    //传入发票识别
    String functionSaveInvoice(InputInvoiceEntity invoiceEntity);

    /**
     * 根据发票代码和发票号码查询发票是否重复
     * @param invoiceCode
     * @param invoiceNumber
     * @return
     */
    Integer getOAInvoiceList(String invoiceCode, String invoiceNumber);

    /**
     * 根据发票代码和发票号码更新重复发票状态
     * @param invoiceCode
     * @param invoiceNumber
     */
    void updateRepeat(String invoiceCode, String invoiceNumber, String repeatBill);

    void update(InputInvoiceEntity invoiceEntity);


    void deleteInvoiceByInvoiceBatchNumber(InputInvoiceEntity invoiceEntity);

    /**
     * 验真
     * @param invoiceEntity
     * @return
     */
    String functionCheckTrue(InputInvoiceEntity invoiceEntity);

    /**
     * 强制验真
     * @param invoiceEntity
     * @return
     */
    String forceCheckTrue(InputInvoiceEntity invoiceEntity);


    List<InputInvoiceEntity> findInvoiceEntityById(InputInvoiceEntity invoiceEntity);

    List<InputInvoiceSyncEntity> getSkssqEntity(String nsrsbh, String createDate);

    /**
     * 传入识别成功的发票与当前批次号进行分组
     * @param invoiceEntityList
     * @param invoiceVoucherEntity
     * @return
     */
    Boolean functionGroupByInvoice(List<InputInvoiceEntity> invoiceEntityList, InputInvoiceVoucherEntity invoiceVoucherEntity);

    void updateInvoiceGroup(InputInvoiceEntity invoiceEntity);

    List<InputInvoiceEntity> getListByBatchIdAndInvoiceGroup(InputInvoiceEntity invoiceEntity);

    List<InputInvoiceEntity> getListByInvoiceNumberAndBatchId(InputInvoiceEntity invoiceEntity);

    void updateGroupByBatchId(InputInvoiceEntity invoiceEntity);

    void updateGroup(InputInvoiceEntity invoiceEntity);

    int functionCheckTrue(InputInvoiceEntity invoiceEntity, String flag);

    String checkWhiteBlackEntity(List<String> bsartList);

    String checkWhiteBlackByMatnrList(List<String> matnrList);

    void updateByEnter(InputInvoiceEntity invoiceEntity);

    Boolean any(String code, String number);

    List<InputInvoiceEntity> getListByIds(List<Integer> invoiceIds);

    String checkWhiteBlackByEntry(List<String> lifnrList);

    void updateList(List<InputInvoiceEntity> invoiceEntityList);

    String mate(InputInvoiceEntity invoiceEntity);

    InputInvoiceEntity get(InputInvoiceEntity invoiceEntity);

    void updateByIdPendingRefund(List<String> ids);

    PageUtils invoiceException(Map<String, Object> params);

    Integer getGroupSizeByBatchId(InputInvoiceEntity invoiceEntity);

    List<InputInvoiceVo> getVoListById(InputInvoiceVo invoiceVo);

    PageUtils findListByInvoiceIds(Map<String, Object> params,List<Integer> ids);


    R getApplyResule(String nsrsbh, String businessType, String taskNo);

    void updateApply(InputInvoiceEntity invoiceEntity);

    R getApply(String nsrsbh,String businessType);

    R getCensusResult(String nsrsbh,String statisticsTime);

    R getAffirmCensus(String nsrsbh,String statisticsTime);

    List<InputInvoiceEntity> getByInvoiceIds(List<Integer> ids);

    /**
     * 根据指定id获取发票集合
     * @param invoiceEntity
     * @return
     */
    List<InputInvoiceEntity> getListById(InputInvoiceEntity invoiceEntity);

    void checkStstus(List<InputInvoiceEntity> list);

    int getListByShow();


    List<InputInvoiceEntity> getListByItems(List<InputInvoiceEntity> list);

    void updateByIdArray(Integer[] ids);

    /**
     * 发票关联采购单并修改采购单状态
     * @param invoiceIds
     * @param batchId
     */
    void relatedBatch(String invoiceIds, String batchId);

    /**
     * 人工入账
     * @param invoiceIds
     */
    void manualEntry(String invoiceIds);

    /**
     * 手动三单匹配
     * @param invoiceIds
     */
    void manualMatch(String invoiceIds);

    /**
     * 勾选只改了状态
     * @param invoiceEntity
     * @return
     */
    boolean functionVerfy(InputInvoiceEntity invoiceEntity);
    /**
     * 根据发票状态
     * @param status
     * @return
     */
    List<InputInvoiceEntity> getByStatus(String status);

    List<InputInvoiceEntity> getListByBatchIdAndStatus(String batchId, List<String> status);


    /**
     * 获取上一个seq
     * @param invoiceSeq
     * @return
     */
    String getLastSeq(String invoiceSeq);

}
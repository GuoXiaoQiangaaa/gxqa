package com.pwc.modules.input.dao;

import com.pwc.modules.input.entity.InputInvoiceBatchEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceVo;
import com.pwc.modules.input.entity.InputOAInvoiceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据上传DAO接口
 * @author cj
 * @version 2018-12-05
 */
@Mapper
public interface InputInvoiceDao extends BaseMapper<InputInvoiceEntity> {
//
    List<InputInvoiceEntity> getListByGroup(InputInvoiceBatchEntity invoiceBatchEntity);
    void updateByIdPendingRefund(List<String> ids);
    void updateGroup(InputInvoiceEntity invoiceEntity);
//    void setDescription(@Param("invoiceRemarks") String invoiceRemark, @Param("list") String[] ids);
    void updateByEnter(InputInvoiceEntity invoiceEntity);
    List<InputInvoiceEntity> getListAndCreateName(@Param("list") List<InputInvoiceEntity> list, @Param("createUserName") String createUserName);
    void setCreateBy(@Param("list") List<InputInvoiceEntity> list, @Param("userId") Long userId);
    void deleteByRepeat(@Param("list") List<Integer> list);
    Integer getGroupSizeByBatchId(InputInvoiceEntity invoiceEntity);
//    void updateByBatchNumber(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getListByVoucher(InputInvoiceVoucherEntity invoiceVoucherEntity);
//    List<InputInvoiceEntity> getInvoiceByBatchNumber(int invoiceBatchNumber);
    int getListByShow();
//    List<InputInvoiceEntity> findList(Pagination page, @Param("invoice") InputInvoiceEntity invoice, @Param("key") String key);
//    List<InputInvoiceEntity> invoiceList(Pagination page, @Param("invoice") InputInvoiceEntity invoice, @Param("key") String key, InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> invoiceException(Pagination page, @Param("invoice") InputInvoiceEntity invoice, @Param("key") String key, InputInvoiceEntity invoiceEntity);
    InputInvoiceEntity get(InputInvoiceEntity invoiceEntity);
//    int save(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getMigrationList();
    void updateByEntity(InputInvoiceEntity invoiceEntity);
//    void updateByIdRefund(Integer[] ids);
    void updateByIdArray(Integer[] ids);
//    void updateByIdPass(Integer[] ids);
//    List<InputInvoiceEntity> getList();
//    List<InputInvoiceEntity> getGroupListByInvoiceId(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getListForThree(InputInvoiceEntity invoiceEntity);
//    InputInvoiceEntity getInvoice(InputInvoiceEntity invoiceEntity);
//    void updateFromtoByBatchNumber(InputInvoiceEntity invoiceEntity);
    void updateInvoiceGroup(InputInvoiceEntity invoiceEntity);
    List<InputInvoiceEntity> getListByBatchId(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getListId(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity>getByInvoiceId(InputInvoiceEntity invoiceEntity);
//    InputInvoiceEntity getInvoiceInfoGroupByStatus(InputInvoiceEntity invoiceEntity);
//    InputInvoiceEntity getTheMaxGroup(InputInvoiceEntity invoiceEntity);
    List<InputInvoiceEntity> getListByBatchIdAndInvoiceGroup(InputInvoiceEntity invoiceEntity);
    List<InputInvoiceEntity> getListByIds(@Param("list") List<Integer> invoiceIds);
//    List<InputInvoiceEntity> getExport(InputInvoiceEntity invoiceEntity);
    List<InputInvoiceEntity> getListByInvoiceNumberAndBatchId(InputInvoiceEntity invoiceEntity);
    List<InputInvoiceEntity> getListByBatchIds(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getListLikeInvoiceNumber(InputInvoiceEntity invoiceEntity);
//    void updateInvoiceBatch(InputInvoiceEntity invoiceEntity);
    void deleteInvoiceByInvoiceBatchNumber(InputInvoiceEntity invoiceEntity);
    List<InputInvoiceEntity> getListByStatus(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getList2(@Param("list") List<InputInvoiceEntity> list);
//    void authPass(Integer id);
    void updateApply(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getListByInvoicePurchaserParagraph(@Param("invoicePurchaserParagraph") String invoicePurchaserParagraph);
//    void updateByIdCheckPending(Integer[] ids);
//    List<InputInvoiceEntity> getListByBarCode(InputInvoiceEntity invoiceEntity);
//
//    List<InputInvoiceEntity> selectByFourItems(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> selectByFour(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getByEcho(InputInvoiceEntity invoiceEntity);
//
//    List<InputInvoiceEntity> getListBeforeTwenty(String date);
//    List<InputInvoiceEntity> getListBeforeThirty(String beginDate, String endDate);
//    List<InputInvoiceEntity> selectFromAuth(@Param("list") List<InputInvoiceEntity> list);
//    void updateListByCodeAndNumber(List<InputInvoiceEntity> list);
//
//    void updateInvoiceTransOutType(String invoiceTransOut, @Param("array") List<Integer> invoiceId);
//
//
    /**
     * 根据指定id获取发票集合
     * @param array
     * @return
     */
    List<InputInvoiceEntity> getListById(@Param("array") int[] array);
//    List<InputInvoiceEntity>getByIds(InputInvoiceEntity invoiceEntity);
//    List<InputInvoiceEntity> getListByBatchIdAndStatus(InputInvoiceEntity invoiceEntity);
//    /**
//     * 根据发票号码获取所在批次和分组的所有发票
//     * @param invoiceNumber
//     * @return
//     */
//    List<InputInvoiceEntity> getListByinvoiceNumber(String invoiceNumber);
//
    List<InputInvoiceEntity> getListByItems(@Param("list") List<InputInvoiceEntity> list);
//
//    /**
//     * 根据入账结果更新数据库
//     * @param invoiceEntities
//     */
//    void updateByResult(@Param("list") List<InputInvoiceEntity> invoiceEntities);
//
//    List<String> getBatchNumber(@Param("list") List<Integer> batChNumberIds);
//
//    void updateByCompany(@Param("list") List<InputInvoiceEntity> list);
//
//    List<InputInvoiceEntity> getListByBatchAndStatus(InputInvoiceEntity invoiceEntity);
//
//    void setNull(InputInvoiceVoucherEntity invoiceVoucherEntity);
//
    void updateList(List<InputInvoiceEntity> invoiceEntityList);
//
//    List<InputInvoiceEntity> getListForMatch(InputInvoiceEntity invoiceEntity);
    /**
     * 根据发票代码和发票号码查询发票是否重复
     * @param invoiceCode
     * @param invoiceNumber
     * @return
     */
    Integer getOAInvoiceList(@Param("invoiceCode") String invoiceCode, @Param("invoiceNumber") String invoiceNumber);
//
    /**
     * 根据发票代码和发票号码更新重复发票状态
     * @param invoiceCode
     * @param invoiceNumber
     */
    void updateRepeat(@Param("invoiceCode") String invoiceCode, @Param("invoiceNumber") String invoiceNumber, @Param("repeatBill") String repeatBill);
//
    /**
     * 更新worning
     */
    void setWorn(InputOAInvoiceInfo oaInvoiceInfo);
    /**
     * 若购方税号为空则更新worning
     */
    void setWorn2(InputOAInvoiceInfo oaInvoiceInfo);
//
    void updateGroupByBatchId(InputInvoiceEntity invoiceEntity);

    List<InputInvoiceVo> getVoListById(InputInvoiceVo invoiceVo);

    /**
     * 获取上一个seq
     * @param invoiceSeq
     * @return
     */
    String getLastSeq(String invoiceSeq);
    String getCountByVoucherCode(String voucherCode);
}

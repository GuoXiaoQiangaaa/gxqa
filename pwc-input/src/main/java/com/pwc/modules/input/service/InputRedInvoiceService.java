package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceCustomsEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceSapEntity;
import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 红字发票服务
 *
 * @author fanpf
 * @date 2020/8/25
 */
public interface InputRedInvoiceService extends IService<InputRedInvoiceEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 红字通知单条件查询
     */
    PageUtils conditionList(Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity);

    /**
     * 导入红字通知单
     */
    Map<String, Object> importRedNotice(MultipartFile[] files);

    /**
     * 接收红字发票并更新红字通知单状态
     */
    void receiveRedInvoice(MultipartFile file);

    /**
     * 红字发票监控条件查询
     */
    PageUtils redList(Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity);

    /**
     * 关联红字发票
     */
    boolean link(Long redId, Map<String, Object> params);

    InputRedInvoiceEntity findRedNoticeNumber(String redNoticeNumber);

    /**
     * 手工入账
     * @param params
     */
    String manualEntryByRed(Map<String, Object> params);

    /**
     * 作废红字通知单
     * @param inputRedInvoice
     */
    void obsoleteEntryByRed(InputRedInvoiceEntity inputRedInvoice);

    int getListByShow();

    /**
     * 自动入账
     * @param
     */
    InputInvoiceSapEntity voluntaryEntry(InputInvoiceSapEntity sapEntity);

    /**
     * 查询账票匹配成功数据
     * @param params
     * @return
     */
    PageUtils getListByMatching(Map<String, Object> params);

    /**
     * 获取查询月份的认证数据
     * @param params
     * @return
     */
    List<InputRedInvoiceEntity> getCertification(Map<String, Object> params);

    PageUtils getMonthCredBeforeResult(Map<String, Object> params);
}


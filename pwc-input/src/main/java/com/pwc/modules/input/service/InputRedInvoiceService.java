package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceSapEntity;
import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import org.springframework.web.multipart.MultipartFile;

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
    void manualEntryByRed(Map<String, Object> params);

    /**
     * 自动入账
     * @param
     */
    int voluntaryEntry(InputInvoiceSapEntity sapEntity);
}


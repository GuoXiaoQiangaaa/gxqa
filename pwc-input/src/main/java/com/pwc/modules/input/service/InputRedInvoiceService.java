package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
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
     * 条件查询
     */
    PageUtils conditionList(Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity);

    /**
     * 导入红字通知单
     */
    void importRedNotice(MultipartFile file);

    /**
     * 导入红字发票并更新红字通知单状态
     */
    void importRedInvoice(MultipartFile file);
}

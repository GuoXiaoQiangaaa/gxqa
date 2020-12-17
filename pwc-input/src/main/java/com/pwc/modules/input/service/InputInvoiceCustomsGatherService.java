package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceCustomsGatherEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 海关缴款书（同步）
 *
 * @author zk
 * @email 
 * @date 2020-12-16 13:26:51
 */
public interface InputInvoiceCustomsGatherService extends IService<InputInvoiceCustomsGatherEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Map<String, Object> getImportBySap(MultipartFile file) throws Exception;

    int getListByShow();

    /**
     * 获取采集结果
     */
    void customsGatherResult(String taxNo);


    /**
     * 定时获取采集结果
     */
    void customsGatherResultByTime();
}


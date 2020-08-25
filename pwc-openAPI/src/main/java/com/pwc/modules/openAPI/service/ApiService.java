package com.pwc.modules.openAPI.service;


import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


public interface ApiService {
    /**
     * 验真接口
     */
    R invoiceCheck(Map<String, Object> params);
    /**
     * 调用增值税发票OCR接口
     */
    R vatInvoice(Map<String, Object> params, MultipartFile file);
}

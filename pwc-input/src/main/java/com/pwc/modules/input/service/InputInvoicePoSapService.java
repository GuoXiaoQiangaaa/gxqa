package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoicePoSapEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 
 *
 * @author zk
 * @email 
 * @date 2020-12-03 16:28:38
 */
public interface InputInvoicePoSapService extends IService<InputInvoicePoSapEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * SapPo明细导入
     */
    Map<String, Object> importSapPoData(MultipartFile[] files);
}


package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.filing.entity.FilingBelleVatEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 
 *
 * @author zk
 * @email 
 * @date 2020-01-13 18:32:25
 */
public interface FilingBelleVatService extends IService<FilingBelleVatEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 初始化
     * @param multipartFile
     * @return
     */
    boolean initUpload( MultipartFile multipartFile);
}


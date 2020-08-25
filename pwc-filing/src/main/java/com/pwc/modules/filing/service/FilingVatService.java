package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.filing.entity.FilingVatEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 
 *
 * @author zk
 * @email 
 * @date 2019-11-18 18:34:05
 */
public interface FilingVatService extends IService<FilingVatEntity> {

    /**
     * 分页
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 初始化
     * @param multipartFile
     * @return
     */
    boolean initUpload( MultipartFile multipartFile, Long deptId);

    /**
     *  查询vat信息
     * @param taxCode
     * @param createTime yyyy-MM
     * @return
     */
    FilingVatEntity getVatByTaxCodeAndCreateTime(String taxCode, String createTime);

}


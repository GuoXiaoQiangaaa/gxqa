package com.pwc.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.data.entity.OutputSapTaxListEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * SAP税码清单服务
 *
 * @author fanpf
 * @date 2020/8/27
 */
public interface OutputSapTaxListService extends IService<OutputSapTaxListEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 禁用/启用
     */
    void disableOrEnable(OutputSapTaxListEntity reqVo);

    /**
     * 关键字查询
     */
    PageUtils search(Map<String, Object> params);

    /**
     * 数据导入
     */
    Map<String, Object> importSapTax(MultipartFile file);
}


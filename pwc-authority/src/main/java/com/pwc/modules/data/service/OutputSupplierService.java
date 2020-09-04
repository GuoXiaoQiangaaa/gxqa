package com.pwc.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.excel.ExportExcel;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.data.entity.OutputSupplierEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 供应商信息服务
 *
 * @author fanpf
 * @date 2020/8/24
 */
public interface OutputSupplierService extends IService<OutputSupplierEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 新增
     */
    boolean save(OutputSupplierEntity outputSupplier);

    /**
     * 编辑
     */
    boolean updateById(OutputSupplierEntity outputSupplier);

    /**
     * 禁用/启用
     */
    void disableOrEnable(OutputSupplierEntity reqVo);

    /**
     * 关键字查询
     */
    PageUtils search(Map<String, Object> params);

    /**
     * 数据导入
     */
    Map<String, Object> importSupplier(MultipartFile file);

}


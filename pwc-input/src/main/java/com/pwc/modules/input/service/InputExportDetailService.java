package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputExportDetailEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 进项转出明细服务
 *
 * @author fanpf
 * @date 2020/9/17
 */
public interface InputExportDetailService extends IService<InputExportDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 进项转出明细导入
     */
    Map<String, Object> importData(MultipartFile[] files);
}


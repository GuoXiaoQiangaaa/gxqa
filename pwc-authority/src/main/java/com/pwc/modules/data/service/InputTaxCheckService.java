package com.pwc.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.data.entity.InputTaxCheckEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 进项税率校验服务
 *
 * @author fanpf
 * @date 2020/8/29
 */
public interface InputTaxCheckService extends IService<InputTaxCheckEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 禁用/启用
     */
    void disableOrEnable(InputTaxCheckEntity reqVo);

    /**
     * 关键字查询
     */
    PageUtils search(Map<String, Object> params);

    /**
     * 数据导入
     */
    Map<String, Object> importTaxCheck(MultipartFile file);
}


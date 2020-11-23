package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputCollectFormEntity;

import java.util.List;
import java.util.Map;

/**
 * 进项汇总报表服务
 *
 * @author fanpf
 * @date 2020/9/16
 */
public interface InputCollectFormService extends IService<InputCollectFormEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 生成报表数据
     */
    List<InputCollectFormEntity> createData(Map<String, Object> params);

    /**
     * 报表明细
     */
    PageUtils formDetail(Map<String, Object> params);

    /**
     * 区分数据(认证/转出)
     */
    Map<String, List<InputCollectFormEntity>> distinguish(List<InputCollectFormEntity> formData);

}

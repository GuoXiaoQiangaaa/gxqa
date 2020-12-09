package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputSqpMatchResultEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author myz
 * @email 
 * @date 2020-12-09 11:35:43
 */
public interface InputSqpMatchResultService extends IService<InputSqpMatchResultEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //实时查询账票匹配接口
    List<InputSqpMatchResultEntity> queryMatchCurTime(Map<String, Object> params);
}


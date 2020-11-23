package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.output.entity.OutputAlarmRulesEntity;

import java.util.List;
import java.util.Map;

/**
 * 报警规则服务
 *
 * @author fanpf
 * @date 2020/9/25
 */
public interface OutputAlarmRulesService extends IService<OutputAlarmRulesEntity> {

    /**
     * 列表
     */
    List<OutputAlarmRulesEntity> list(Map<String, Object> params);

    void saveOrUpdate(List<OutputAlarmRulesEntity> rulesEntityList, Map<String, Object> params);
}


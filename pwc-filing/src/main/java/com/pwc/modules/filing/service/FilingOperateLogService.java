package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.filing.entity.FilingOperateLogEntity;
import com.pwc.modules.filing.entity.FilingRecordEntity;

import java.util.Map;

/**
 * 申报过程操作记录
 *
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
public interface FilingOperateLogService extends IService<FilingOperateLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加申报操作记录
     * @param filingRecordEntity
     * @return
     */
    boolean save(FilingRecordEntity filingRecordEntity, Integer logType, Long userId, Integer fileType);
}


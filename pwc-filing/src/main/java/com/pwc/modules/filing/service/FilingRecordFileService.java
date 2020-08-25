package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.filing.entity.FilingRecordFileEntity;

import java.util.Map;

/**
 * 申报附件表
 *
 * @author zk
 * @email 
 * @date 2020-01-07 16:27:18
 */
public interface FilingRecordFileService extends IService<FilingRecordFileEntity> {

    PageUtils queryPage(Map<String, Object> params);

}


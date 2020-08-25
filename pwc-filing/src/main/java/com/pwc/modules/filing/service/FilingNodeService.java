package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.filing.entity.FilingNodeEntity;

import java.util.Date;
import java.util.Map;

/**
 * 申报节点设置
 *
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
public interface FilingNodeService extends IService<FilingNodeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    FilingNodeEntity queryByDeptAndCreateTime(Long deptId, String createTime);

    /**
     * 设置节点
     * @param entity
     * @return
     */
    boolean saveFilingNote(FilingNodeEntity entity);
}


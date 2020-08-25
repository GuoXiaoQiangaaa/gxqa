package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.filing.entity.FilingProcessEntity;

import java.util.Map;

/**
 * 申报流程设置
 *
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
public interface FilingProcessService extends IService<FilingProcessEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 设置流程
     * @param process
     * @return
     */
    boolean saveProcess(FilingProcessEntity process);

    /**
     * 查询时间内部门是否设置过流程
     * @param deptId
     * @param createTime
     * @return
     */
    FilingProcessEntity queryByDeptAndCreateTime(Long deptId, String createTime);
}


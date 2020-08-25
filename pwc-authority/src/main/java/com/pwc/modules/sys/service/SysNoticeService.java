package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysNoticeEntity;

import java.util.List;
import java.util.Map;

/**
 * 通知代办表
 *
 * @author zk
 * @email 
 * @date 2020-02-04 17:52:31
 */
public interface SysNoticeService extends IService<SysNoticeEntity> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    void save(Long filingId, Long deptId, List<Long> roleIds, String descr);
}


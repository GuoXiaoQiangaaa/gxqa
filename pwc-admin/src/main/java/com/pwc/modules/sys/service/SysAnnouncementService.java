package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysAnnouncementEntity;

import java.util.Map;

/**
 * 公告表
 *
 * @author zk
 * @email 
 * @date 2019-12-09 19:03:00
 */
public interface SysAnnouncementService extends IService<SysAnnouncementEntity> {

    /**
     * 分页查询
     * @param params
     * @return PageUtils
     */
    PageUtils queryPage(Map<String, Object> params);
}


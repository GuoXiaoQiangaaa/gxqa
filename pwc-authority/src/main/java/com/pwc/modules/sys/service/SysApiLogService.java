package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysApiLogEntity;

import java.util.Map;

/**
 * 请求接口日志
 *
 * @author zlb
 * @email 
 * @date 2020-08-28 18:07:21
 */
public interface SysApiLogService extends IService<SysApiLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


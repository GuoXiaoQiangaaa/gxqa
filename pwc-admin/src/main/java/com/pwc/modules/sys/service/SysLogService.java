

package com.pwc.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 *
 * @author
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

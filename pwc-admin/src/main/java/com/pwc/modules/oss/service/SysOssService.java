

package com.pwc.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.oss.entity.SysOssEntity;
import com.pwc.common.utils.PageUtils;

import java.util.Map;

/**
 * 文件上传
 *
 * @author
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}

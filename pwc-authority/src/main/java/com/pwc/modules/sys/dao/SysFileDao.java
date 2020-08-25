package com.pwc.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.sys.entity.SysFileEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 *
 * @author zk
 */
@Mapper
public interface SysFileDao extends BaseMapper<SysFileEntity> {
}

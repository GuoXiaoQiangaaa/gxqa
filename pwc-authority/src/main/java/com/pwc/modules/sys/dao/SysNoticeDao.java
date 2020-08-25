package com.pwc.modules.sys.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pwc.modules.sys.entity.SysNoticeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知代办表
 * 
 * @author zk
 * @email 
 * @date 2020-02-04 17:52:31
 */
@Mapper
public interface SysNoticeDao extends BaseMapper<SysNoticeEntity> {

    List<SysNoticeEntity> queryPage(IPage<SysNoticeEntity> page, @Param(Constants.WRAPPER) Wrapper<SysNoticeEntity> wrapper);
	
}

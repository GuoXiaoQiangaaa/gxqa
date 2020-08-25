package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputWhiteBlackListEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 *
 *
 * @author qiuyin
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputWhiteBlackListDao extends BaseMapper<InputWhiteBlackListEntity> {
//    List<InputWhiteBlackListEntity> findList(Pagination page, @Param("whiteBlackList") InputWhiteBlackListEntity whiteBlackListEntity, @Param("key") String key);
//    InputWhiteBlackListEntity get(InputWhiteBlackListEntity whiteBlackListEntity);
    List<InputWhiteBlackListEntity> getListByParentId(InputWhiteBlackListEntity whiteBlackListEntity);
//    List<InputWhiteBlackListEntity> getList();
}

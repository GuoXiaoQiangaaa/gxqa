package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputThreeSingleMatchEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 三单匹配DAO接口
 * @author cj
 * @version 2018-12-05
 */
@Mapper
public interface InputThreeSingleMatchDao extends BaseMapper<InputThreeSingleMatchEntity> {
//    List<InputThreeSingleMatchEntity> findList(Pagination page, @Param("threeSingleMatch") InputThreeSingleMatchEntity threeSingleMatch, @Param("key") String key);
    InputThreeSingleMatchEntity get(InputThreeSingleMatchEntity threeSingleMatch);
    void save(InputThreeSingleMatchEntity threeSingleMatch);
    void updateByIdArray(Integer[] ids);

}

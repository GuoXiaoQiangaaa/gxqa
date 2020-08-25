package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Administrator
 * 日志Dao层接口
 */
@Mapper
public interface LogDao extends BaseMapper<Log> {
    /**
     * @return
     * 获取前三个月的数据
     */
    List<Log> getList();
    /**
     * @return
     * 将前三个月的数据插入日志历史表中
     */
    void insertLogHistory(List<Log> list);
    /**
     * @return
     * 删除日志表中前三个月的数据
     */
    void deleteLog();
}

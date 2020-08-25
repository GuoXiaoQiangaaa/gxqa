package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputOASendMessage;
import com.pwc.modules.input.entity.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InputOASendMessageDao extends BaseMapper<InputOASendMessage> {
    InputOASendMessage getOASendMessageByRequestId(InputOASendMessage oaSendMessage);
    List<InputOASendMessage> getOASendMessageByBarCode(InputOASendMessage oaSendMessage);
    Integer save(InputOASendMessage oaSendMessage);

    InputOASendMessage getByRequestId(String requestId);

    int getCountByRequestId(String requestId);

    int getCountByBarCode(String requestId);

    void update(InputOASendMessage oaSendMessage);

    void insertLog(Log log);

    List<InputOASendMessage> getOASendMessageList();

    List<InputOASendMessage> getByCondition();


}

package com.pwc.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.sys.dao.SysApiLogDao;
import com.pwc.modules.sys.entity.SysApiLogEntity;
import com.pwc.modules.sys.service.SysApiLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysApiLogService")
public class SysApiLogServiceImpl extends ServiceImpl<SysApiLogDao, SysApiLogEntity> implements SysApiLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String createBy=(String)params.get("createBy");
        String type=(String)params.get("type");
        String requestTimeArray=(String)params.get("requestTimeArray");
        String responseTimeArray=(String)params.get("responseTimeArray");
        IPage<SysApiLogEntity> page = this.page(
                new Query<SysApiLogEntity>().getPage(params),
                new QueryWrapper<SysApiLogEntity>()
                .eq(StringUtils.isNotBlank(createBy),"create_by",createBy)
                .eq(StringUtils.isNotBlank(type),"type",type)
                .ge(StringUtils.isNotBlank(requestTimeArray), "request_time", !StringUtils.isNotBlank(requestTimeArray) ? "" : requestTimeArray.split(",")[0])
                .le(StringUtils.isNotBlank(requestTimeArray), "request_time", !StringUtils.isNotBlank(requestTimeArray) ? "" : requestTimeArray.split(",")[1])

                .ge(StringUtils.isNotBlank(responseTimeArray), "response_time", !StringUtils.isNotBlank(responseTimeArray) ? "" : responseTimeArray.split(",")[0])
                .le(StringUtils.isNotBlank(responseTimeArray), "response_time", !StringUtils.isNotBlank(responseTimeArray) ? "" : responseTimeArray.split(",")[1])

        );

        return new PageUtils(page);
    }

}

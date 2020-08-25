package com.pwc.modules.sys.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.pwc.modules.sys.dao.SysAnnouncementDao;
import com.pwc.modules.sys.entity.SysAnnouncementEntity;
import com.pwc.modules.sys.service.SysAnnouncementService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;



/**
 * @author zk
 */
@Service("sysAnnouncementService")
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementDao, SysAnnouncementEntity> implements SysAnnouncementService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String keywords = (String)params.get("keywords");
        String type = (String)params.get("type");
        String globalDate = (String)params.get("globalDate");
        if (StringUtils.isBlank(globalDate)) {
            globalDate = DateUtil.format(DateUtil.date(), "yyyy-MM");
        }
        String beginDate =(String) params.get("beginDate");
        String endDate =(String) params.get("endDate");
        IPage<SysAnnouncementEntity> page = this.page(
                new Query<SysAnnouncementEntity>().getPage(params),
                new QueryWrapper<SysAnnouncementEntity>()
                        .between(StrUtil.isNotBlank(beginDate) && StrUtil.isNotBlank(endDate),"create_time", beginDate, endDate)
                        .like(StrUtil.isNotBlank(globalDate),"create_time", globalDate)
                        .and(StrUtil.isNotBlank(keywords),wrapper -> wrapper.like(StrUtil.isNotBlank(keywords),"title", keywords).or().like(StrUtil.isNotBlank(keywords),"content", keywords))
                        .eq(StrUtil.isNotBlank(type),"type", type)
        );

        return new PageUtils(page);
    }

}

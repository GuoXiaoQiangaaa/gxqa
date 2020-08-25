package com.pwc.modules.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.sys.dao.FilingThirdDistrictDao;
import com.pwc.modules.sys.entity.FilingThirdDistrictEntity;
import com.pwc.modules.sys.service.FilingThirdDistrictService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author zk
 */
@Service("filingThirdDistrictService")
public class FilingThirdDistrictServiceImpl extends ServiceImpl<FilingThirdDistrictDao, FilingThirdDistrictEntity> implements FilingThirdDistrictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String cityName = (String) params.get("cityName");
        IPage<FilingThirdDistrictEntity> page = this.page(
                new Query<FilingThirdDistrictEntity>().getPage(params),
                new QueryWrapper<FilingThirdDistrictEntity>().like(StrUtil.isNotBlank(cityName),"city_name", cityName)
        );

        return new PageUtils(page);
    }

}

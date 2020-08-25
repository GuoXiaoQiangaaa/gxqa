package com.pwc.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.sys.dao.FilingThirdCityCodeDao;
import com.pwc.modules.sys.entity.FilingThirdCityCodeEntity;
import com.pwc.modules.sys.service.FilingThirdCityCodeService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author zk
 */
@Service("filingThirdCityCodeService")
public class FilingThirdCityCodeServiceImpl extends ServiceImpl<FilingThirdCityCodeDao, FilingThirdCityCodeEntity> implements FilingThirdCityCodeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FilingThirdCityCodeEntity> page = this.page(
                new Query<FilingThirdCityCodeEntity>().getPage(params),
                new QueryWrapper<FilingThirdCityCodeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public String getByDeptCityCode(String deptCityCode) {
        FilingThirdCityCodeEntity filingThirdCityCodeEntity = baseMapper.selectOne(new QueryWrapper<FilingThirdCityCodeEntity>().eq("city_code", deptCityCode));
        if(null != filingThirdCityCodeEntity) {
            return filingThirdCityCodeEntity.getThirdCityCode();
        }
        return null;
    }
}

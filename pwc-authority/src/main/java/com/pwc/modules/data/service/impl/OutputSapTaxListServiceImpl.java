package com.pwc.modules.data.service.impl;

import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.OutputSapTaxListDao;
import com.pwc.modules.data.entity.OutputSapTaxListEntity;
import com.pwc.modules.data.service.OutputSapTaxListService;

/**
 * SAP税码清单服务实现
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Service("outputSapTaxListService")
public class OutputSapTaxListServiceImpl extends ServiceImpl<OutputSapTaxListDao, OutputSapTaxListEntity> implements OutputSapTaxListService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputSapTaxListEntity> page = this.page(
                new Query<OutputSapTaxListEntity>().getPage(params),
                new QueryWrapper<OutputSapTaxListEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputSapTaxListEntity reqVo) {
        reqVo.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        reqVo.setUpdateTime(new Date());
        super.updateById(reqVo);
    }

    /**
     * 关键字查询
     */
    @Override
    public PageUtils search(Map<String, Object> params) {
        String keyWords = (String) params.get("keyWords");

        IPage<OutputSapTaxListEntity> page;
        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            page = this.page(
                    new Query<OutputSapTaxListEntity>().getPage(params),
                    new QueryWrapper<OutputSapTaxListEntity>()
                            .like("tax_code", keyWords).or()
                            .like("tax_type", keyWords).or()
                            .like("description", keyWords).or()
                            .like("tax_rate", keyWords)
            );
        }else {
            page = this.page(
                    new Query<OutputSapTaxListEntity>().getPage(params),
                    new QueryWrapper<OutputSapTaxListEntity>()
            );
        }

        return new PageUtils(page);
    }

}

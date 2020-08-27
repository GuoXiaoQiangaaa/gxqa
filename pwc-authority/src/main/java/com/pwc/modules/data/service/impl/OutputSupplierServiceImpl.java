package com.pwc.modules.data.service.impl;

import com.pwc.common.exception.RRException;
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

import com.pwc.modules.data.dao.OutputSupplierDao;
import com.pwc.modules.data.entity.OutputSupplierEntity;
import com.pwc.modules.data.service.OutputSupplierService;

/**
 * 供应商信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Service("outputSupplierService")
public class OutputSupplierServiceImpl extends ServiceImpl<OutputSupplierDao, OutputSupplierEntity> implements OutputSupplierService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputSupplierEntity> page = this.page(
                new Query<OutputSupplierEntity>().getPage(params),
                new QueryWrapper<OutputSupplierEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 新增
     */
    @Override
    public boolean save(OutputSupplierEntity outputSupplier) {
        // 校验参数
        this.checkParams(outputSupplier);

        outputSupplier.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        outputSupplier.setCreateTime(new Date());
        return super.save(outputSupplier);
    }

    /**
     * 编辑
     */
    @Override
    public boolean updateById(OutputSupplierEntity outputSupplier) {
        // 校验参数
        this.checkParams(outputSupplier);

        outputSupplier.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        outputSupplier.setUpdateTime(new Date());
        return super.updateById(outputSupplier);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputSupplierEntity reqVo) {
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

        IPage<OutputSupplierEntity> page;
        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            page = this.page(
                    new Query<OutputSupplierEntity>().getPage(params),
                    new QueryWrapper<OutputSupplierEntity>()
                            .like("sap_code", keyWords).or()
                            .like("dept_code", Long.valueOf(keyWords)).or()
                            .like("name", keyWords).or()
                            .like("tax_code", keyWords).or()
                            .like("address", keyWords).or()
                            .like("contact", keyWords).or()
                            .like("bank", keyWords).or()
                            .like("bank_account", keyWords).or()
                            .like("email", keyWords)
            );
        }else {
            page = this.page(
                    new Query<OutputSupplierEntity>().getPage(params),
                    new QueryWrapper<OutputSupplierEntity>()
            );
        }

        return new PageUtils(page);
    }

    /**
     * 参数校验
     */
    private void checkParams(OutputSupplierEntity outputSupplier){
        if(StringUtils.isBlank(outputSupplier.getSapCode())){
            throw new RRException("供应商SAP代码不能为空");
        }
        if(StringUtils.isBlank(outputSupplier.getName())){
            throw new RRException("供应商名称不能为空");
        }
        if(StringUtils.isBlank(outputSupplier.getTaxCode())){
            throw new RRException("纳税人识别号不能为空");
        }
    }

}

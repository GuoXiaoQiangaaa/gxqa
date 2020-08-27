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

import com.pwc.modules.data.dao.OutputCustomerNewDao;
import com.pwc.modules.data.entity.OutputCustomerNewEntity;
import com.pwc.modules.data.service.OutputCustomerNewService;

/**
 * 客户信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Service("outputCustomerNewService")
public class OutputCustomerNewServiceImpl extends ServiceImpl<OutputCustomerNewDao, OutputCustomerNewEntity> implements OutputCustomerNewService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputCustomerNewEntity> page = this.page(
                new Query<OutputCustomerNewEntity>().getPage(params),
                new QueryWrapper<OutputCustomerNewEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 新增
     */
    @Override
    public boolean save(OutputCustomerNewEntity outputCustomerNew) {
        // 参数校验
        this.checkParams(outputCustomerNew);

        outputCustomerNew.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        outputCustomerNew.setCreateTime(new Date());
        return super.save(outputCustomerNew);
    }

    /**
     * 编辑
     */
    @Override
    public boolean updateById(OutputCustomerNewEntity outputCustomerNew) {
        // 参数校验
        this.checkParams(outputCustomerNew);

        outputCustomerNew.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        outputCustomerNew.setUpdateTime(new Date());
        return super.updateById(outputCustomerNew);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputCustomerNewEntity reqVo) {
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

        IPage<OutputCustomerNewEntity> page;
        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            page = this.page(
                    new Query<OutputCustomerNewEntity>().getPage(params),
                    new QueryWrapper<OutputCustomerNewEntity>()
                            .like("sap_code", keyWords).or()
                            .like("dept_code", keyWords).or()
                            .like("name", keyWords).or()
                            .like("name_cn", keyWords).or()
                            .like("tax_code", keyWords).or()
                            .like("address", keyWords).or()
                            .like("contact", keyWords).or()
                            .like("bank", keyWords).or()
                            .like("bank_account", keyWords).or()
                            .like("email", keyWords)
            );
        }else {
            page = this.page(
                    new Query<OutputCustomerNewEntity>().getPage(params),
                    new QueryWrapper<OutputCustomerNewEntity>()
            );
        }

        return new PageUtils(page);
    }

    /**
     * 参数校验
     */
    private void checkParams(OutputCustomerNewEntity outputCustomerNew){
        if(StringUtils.isBlank(outputCustomerNew.getSapCode())){
            throw new RRException("客户SAP代码不能为空");
        }
        if(StringUtils.isBlank(outputCustomerNew.getNameCn())){
            throw new RRException("中文客户名称不能为空");
        }
        if(StringUtils.isBlank(outputCustomerNew.getTaxCode())){
            throw new RRException("纳税人识别号不能为空");
        }
        if(StringUtils.isBlank(outputCustomerNew.getAddress())){
            throw new RRException("客户地址不能为空");
        }
        if(StringUtils.isBlank(outputCustomerNew.getContact())){
            throw new RRException("客户电话不能为空");
        }
    }
}

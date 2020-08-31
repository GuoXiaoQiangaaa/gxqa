package com.pwc.modules.data.service.impl;

import com.pwc.common.excel.ImportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.OutputCustomerNewDao;
import com.pwc.modules.data.entity.OutputCustomerNewEntity;
import com.pwc.modules.data.service.OutputCustomerNewService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

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
     * 数据导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importCustomer(MultipartFile file) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<OutputCustomerNewEntity> entityList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        try {
            ImportExcel excel = new ImportExcel(file, 1, 0);
            List<OutputCustomerNewEntity> dataList = excel.getDataList(OutputCustomerNewEntity.class);
            if(CollectionUtils.isEmpty(dataList)){
                log.error("上传的Excel为空,请重新上传");
                throw new RRException("上传的Excel为空,请重新上传");
            }
            total = dataList.size();
            for (OutputCustomerNewEntity customerEntity : dataList) {
                // 参数校验
                if(1 == checkExcel(customerEntity)){
                    // 参数有误
                    fail += 1;
                }else {
                    // 添加校验正确的实体
                    entityList.add(customerEntity);
                }
            }
            resMap.put("total", total);
            resMap.put("success", entityList.size());
            resMap.put("fail", fail);
            super.saveBatch(entityList);

            return resMap;
        } catch (Exception e) {
            log.error("客户信息导入出错: {}", e);
            throw new RRException("客户信息导入出现异常");
        }
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

    /**
     * 校验Excel中参数
     */
    private int checkExcel(OutputCustomerNewEntity entity){
        if(StringUtils.isBlank(entity.getSapCode()) || StringUtils.isBlank(entity.getNameCn()) ||
                StringUtils.isBlank(entity.getTaxCode()) || StringUtils.isBlank(entity.getAddress()) ||
                StringUtils.isBlank(entity.getContact())){
            return 1;
        }
        return 0;
    }
}

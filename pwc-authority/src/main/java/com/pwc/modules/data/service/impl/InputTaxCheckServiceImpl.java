package com.pwc.modules.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.google.common.collect.Lists;
import com.pwc.common.excel.ImportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.InputTaxCheckDao;
import com.pwc.modules.data.entity.InputTaxCheckEntity;
import com.pwc.modules.data.service.InputTaxCheckService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * 进项税率校验服务实现
 *
 * @author fanpf
 * @date 2020/8/29
 */
@Service("inputTaxCheckService")
@Slf4j
public class InputTaxCheckServiceImpl extends ServiceImpl<InputTaxCheckDao, InputTaxCheckEntity> implements InputTaxCheckService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputTaxCheckEntity> page = this.page(
                new Query<InputTaxCheckEntity>().getPage(params),
                new QueryWrapper<InputTaxCheckEntity>()
                        .orderByDesc("create_time")
        );

        return new PageUtils(page);
    }

    /**
     * 新增
     */
    @Override
    public boolean save(InputTaxCheckEntity entity) {
        int count = super.count(
                new QueryWrapper<InputTaxCheckEntity>()
                        .eq("goods_name", entity.getGoodsName())
                        .eq("tax_type_code", entity.getTaxTypeCode())
                        .eq("tax_rate", entity.getTaxRate())
        );
        if(count > 0){
            throw new RRException("该数据已存在,请核对后再添加");
        }
        // 校验税率
        String taxRate = entity.getTaxRate();
        String [] taxRateArr = {"0%", "1%", "2%", "3%", "5%", "6%", "7%", "9%", "10%", "11%", "13%", "16%", "17%"};
        List<String> taxRateList = Lists.newArrayList(taxRateArr);
        if(StringUtils.isNotBlank(taxRate) && !CollectionUtil.contains(taxRateList, taxRate)){
            throw new RRException("税率选择有误,请核对后再添加");
        }

        entity.setDelFlag("1");
        entity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        entity.setCreateTime(new Date());
        return super.save(entity);
    }

    /**
     * 更新
     */
    @Override
    public boolean updateById(InputTaxCheckEntity entity) {
        // 验重
        InputTaxCheckEntity checkEntity = super.getOne(
                new QueryWrapper<InputTaxCheckEntity>()
                        .eq("goods_name", entity.getGoodsName())
                        .eq("tax_type_code", entity.getTaxTypeCode())
                        .eq("tax_rate", entity.getTaxRate())
        );
        if(null != checkEntity && !entity.getCheckId().equals(checkEntity.getCheckId())){
            throw new RRException("该数据已存在,请核对后再修改");
        }

        // 校验税率
        String taxRate = entity.getTaxRate();
        String [] taxRateArr = {"0%", "1%", "2%", "3%", "5%", "6%", "7%", "9%", "10%", "11%", "13%", "16%", "17%"};
        List<String> taxRateList = Lists.newArrayList(taxRateArr);
        if(StringUtils.isNotBlank(taxRate) && !CollectionUtil.contains(taxRateList, taxRate)){
            throw new RRException("税率选择有误,请核对后再添加");
        }
        entity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        entity.setUpdateTime(new Date());
        return super.updateById(entity);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(InputTaxCheckEntity reqVo) {
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

        IPage<InputTaxCheckEntity> page;
        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            page = this.page(
                    new Query<InputTaxCheckEntity>().getPage(params),
                    new QueryWrapper<InputTaxCheckEntity>()
                            .like("goods_name", keyWords).or()
                            .like("tax_type_code", keyWords).or()
                            .like("tax_rate", keyWords)
                            .orderByDesc("create_time")
            );
        }else {
            page = this.page(
                    new Query<InputTaxCheckEntity>().getPage(params),
                    new QueryWrapper<InputTaxCheckEntity>()
                            .orderByDesc("create_time")
            );
        }

        return new PageUtils(page);
    }

    /**
     * 数据导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importTaxCheck(MultipartFile file) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<InputTaxCheckEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<InputTaxCheckEntity> duplicateList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            String[] excelHead = {"货品名称", "税收分类编码", "非法税率"};
            String [] excelHeadAlias = {"goodsName", "taxTypeCode", "taxRate"};
            for (int i = 0; i < excelHead.length; i++) {
                reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
            }
            List<InputTaxCheckEntity> dataList = reader.read(0, 1, InputTaxCheckEntity.class);

            if(CollectionUtils.isEmpty(dataList)){
                log.error("上传的Excel为空,请重新上传");
                throw new RRException("上传的Excel为空,请重新上传");
            }
            total = dataList.size();
            List<String> repeatDataList = new ArrayList<>();
            for (InputTaxCheckEntity taxCheckEntity : dataList) {
                // 参数校验
                if(1 == checkExcel(taxCheckEntity)){
                    // 参数有误
                    fail += 1;
                }else {
                    // 税率获取到的为小数类型,转为百分数
                    String taxRate = taxCheckEntity.getTaxRate();
                    NumberFormat nf = NumberFormat.getPercentInstance();
                    taxCheckEntity.setTaxRate(nf.format(Double.valueOf(taxRate)));
                    // 去除Excel中重复数据
                    String repeatData = taxCheckEntity.getGoodsName() + taxCheckEntity.getTaxTypeCode() + taxCheckEntity.getTaxRate();
                    if(CollectionUtil.contains(repeatDataList, repeatData)){
                        fail += 1;
                        continue;
                    }
                    repeatDataList.add(repeatData);

                    // 验重
                    InputTaxCheckEntity duplicate = super.getOne(
                            new QueryWrapper<InputTaxCheckEntity>()
                                    .eq("goods_name", taxCheckEntity.getGoodsName())
                                    .eq("tax_type_code", taxCheckEntity.getTaxTypeCode())
                                    .eq("tax_rate", taxCheckEntity.getTaxRate())
                    );
                    if(null != duplicate){
                        duplicate.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                        duplicate.setUpdateTime(new Date());
                        duplicateList.add(duplicate);
                    }else {
                        // 添加校验正确的实体
                        taxCheckEntity.setDelFlag("1");
                        taxCheckEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                        taxCheckEntity.setCreateTime(new Date());
                        entityList.add(taxCheckEntity);
                    }
                }
            }
            resMap.put("total", total);
            resMap.put("success", duplicateList.size() + entityList.size());
            resMap.put("fail", fail);

            if(CollectionUtil.isNotEmpty(duplicateList)){
                super.updateBatchById(duplicateList);
            }
            if(CollectionUtil.isNotEmpty(entityList)){
                super.saveBatch(entityList);
            }

            return resMap;
        } catch (RRException e){
            throw e;
        } catch (Exception e) {
            log.error("进项税率校验数据导入出错: {}", e);
            throw new RRException("进项税率校验数据导入出现异常");
        }
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(InputTaxCheckEntity entity){
        if(StringUtils.isBlank(entity.getGoodsName()) || StringUtils.isBlank(entity.getTaxTypeCode()) ||
                StringUtils.isBlank(entity.getTaxRate())){
            return 1;
        }
        // 税率枚举校验 0%,1%,2%,3%,5%,6%,7%,9%,10%,11%,13%,16%,17%
        String[] taxRateArr = {"0", "0.01", "0.02", "0.03", "0.05", "0.06", "0.07", "0.09", "0.1", "0.11", "0.13", "0.16", "0.17"};
        List<String> taxRateList = Lists.newArrayList(taxRateArr);
        String taxRate = entity.getTaxRate();
        if(!CollectionUtil.contains(taxRateList, taxRate)){
            return 1;
        }
        return 0;
    }

}

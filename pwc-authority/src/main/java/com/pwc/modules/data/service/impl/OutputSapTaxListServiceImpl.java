package com.pwc.modules.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.data.dao.OutputSapTaxListDao;
import com.pwc.modules.data.entity.OutputSapTaxListEntity;
import com.pwc.modules.data.service.OutputSapTaxListService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.util.*;

/**
 * SAP税码清单服务实现
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Service("outputSapTaxListService")
@Slf4j
public class OutputSapTaxListServiceImpl extends ServiceImpl<OutputSapTaxListDao, OutputSapTaxListEntity> implements OutputSapTaxListService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputSapTaxListEntity> page = this.page(
                new Query<OutputSapTaxListEntity>().getPage(params),
                new QueryWrapper<OutputSapTaxListEntity>()
                        .orderByDesc("create_time")
        );

        return new PageUtils(page);
    }
    /**
     * 新增
     */
    @Override
    public boolean save(OutputSapTaxListEntity entity) {

        int count = super.count(
                new QueryWrapper<OutputSapTaxListEntity>()
                        .eq("tax_code", entity.getTaxCode())
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
    public boolean updateById(OutputSapTaxListEntity entity) {
        OutputSapTaxListEntity sapTaxListEntity = super.getOne(
                new QueryWrapper<OutputSapTaxListEntity>()
                        .eq("tax_code", entity.getTaxCode())
        );
        if(null != sapTaxListEntity && !entity.getTaxId().equals(sapTaxListEntity.getTaxId())){
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
                            .orderByDesc("create_time")
            );
        }else {
            page = this.page(
                    new Query<OutputSapTaxListEntity>().getPage(params),
                    new QueryWrapper<OutputSapTaxListEntity>()
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
    public Map<String, Object> importSapTax(MultipartFile[] files) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<OutputSapTaxListEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<OutputSapTaxListEntity> duplicateList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        // 记录excel中的重复数据
        List<String> repeatDataList = new ArrayList<>();
        // 记录数据有误的文件
        StringBuffer sb = new StringBuffer();
        try {
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename();
                ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
                String[] excelHead = {"税码", "税种", "描述", "税率"};
                String [] excelHeadAlias = {"taxCode", "taxType", "description", "taxRate"};
                for (int i = 0; i < excelHead.length; i++) {
                    reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
                }
                List<OutputSapTaxListEntity> dataList = reader.read(0, 1, OutputSapTaxListEntity.class);

                if(CollectionUtils.isEmpty(dataList)){
                    log.error("上传的{}Excel为空,请重新上传", filename);
//                    throw new RRException("上传的Excel为空,请重新上传");
                    continue;
                }
                total += dataList.size();
                int count = 1;
                for (OutputSapTaxListEntity sapTaxEntity : dataList) {
                    count++;
                    // 参数校验
                    if(1 == checkExcel(sapTaxEntity)){
                        // 参数有误
                        fail += 1;
                        if(!StringUtils.contains(sb.toString(), filename)){
                            sb.append("文件" + filename + "的错误行号为:");
                        }
                        sb.append(count + ",");
                    }else {
                        // 税率获取到的为小数类型,转为百分数
                        String taxRate = sapTaxEntity.getTaxRate();
                        NumberFormat nf = NumberFormat.getPercentInstance();
                        sapTaxEntity.setTaxRate(nf.format(Double.valueOf(taxRate)));
                        // 去除Excel中重复数据
                        String repeatData = sapTaxEntity.getTaxCode();
                        if(CollectionUtil.contains(repeatDataList, repeatData)){
                            fail += 1;
                            if(!StringUtils.contains(sb.toString(), filename)){
                                sb.append("文件" + filename + "的错误行号为:");
                            }
                            sb.append(count + ",");
                            continue;
                        }
                        repeatDataList.add(repeatData);

                        // 验重
                        OutputSapTaxListEntity duplicate = super.getOne(
                                new QueryWrapper<OutputSapTaxListEntity>()
                                        .eq("tax_code", sapTaxEntity.getTaxCode())
                        );
                        if(null != duplicate){
                            duplicate.setTaxType(sapTaxEntity.getTaxType());
                            duplicate.setDescription(sapTaxEntity.getDescription());
                            duplicate.setTaxRate(sapTaxEntity.getTaxRate());
                            duplicate.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                            duplicate.setUpdateTime(new Date());
                            duplicateList.add(duplicate);
                        }else {
                            // 添加校验正确的实体
                            sapTaxEntity.setDelFlag("1");
                            sapTaxEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                            sapTaxEntity.setCreateTime(new Date());
                            entityList.add(sapTaxEntity);
                        }
                    }
                }
                if(sb.toString().endsWith(",")){
                    sb.deleteCharAt(sb.lastIndexOf(",")).append(";");
                }
            }
            if(sb.toString().endsWith(";")){
                sb.deleteCharAt(sb.lastIndexOf(";")).append("。");
            }

            resMap.put("total", total);
            resMap.put("success", duplicateList.size() + entityList.size());
            resMap.put("fail", fail);
            resMap.put("failDetail", sb.toString());

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
            log.error("SAP税码清单导入出错: {}", e);
            throw new RRException("SAP税码清单导入出现异常");
        }
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(OutputSapTaxListEntity entity){
        if(StringUtils.isBlank(entity.getTaxCode()) || StringUtils.isBlank(entity.getTaxRate())){
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

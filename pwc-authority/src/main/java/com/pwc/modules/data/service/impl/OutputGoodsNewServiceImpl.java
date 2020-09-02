package com.pwc.modules.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.pwc.common.excel.ImportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.Constant;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.OutputGoodsNewDao;
import com.pwc.modules.data.entity.OutputGoodsNewEntity;
import com.pwc.modules.data.service.OutputGoodsNewService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 商品信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Service("outputGoodsNewService")
public class OutputGoodsNewServiceImpl extends ServiceImpl<OutputGoodsNewDao, OutputGoodsNewEntity> implements OutputGoodsNewService {

    @Resource
    private OutputGoodsNewDao goodsNewDao;

    /**
     * 列表
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputGoodsNewEntity> page = new Query<OutputGoodsNewEntity>().getPage(params);
        List<OutputGoodsNewEntity> list = goodsNewDao.list(page);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(list);
        return pageUtils;
    }

    /**
     * 新增
     */
    @Override
    public boolean save(OutputGoodsNewEntity outputGoods) {
        // 参数校验
//        this.checkParams(outputGoods);
        int count = super.count(
                new QueryWrapper<OutputGoodsNewEntity>()
                        .eq("goods_number", outputGoods.getGoodsNumber())
        );
        if(count > 0){
            throw new RRException("该数据已存在,请核对后再添加");
        }
        // 校验税率
        String taxRate = outputGoods.getTaxRate();
        String [] taxRateArr = {"0%", "1%", "2%", "3%", "5%", "6%", "7%", "9%", "10%", "11%", "13%", "16%", "17%"};
        List<String> taxRateList = Lists.newArrayList(taxRateArr);
        if(StringUtils.isNotBlank(taxRate) && !CollectionUtil.contains(taxRateList, taxRate)){
            throw new RRException("税率选择有误,请核对后再添加");
        }

        outputGoods.setDelFlag("1");
        outputGoods.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        outputGoods.setCreateTime(new Date());
        return super.save(outputGoods);
    }

    /**
     * 编辑
     */
    @Override
    public boolean updateById(OutputGoodsNewEntity outputGoods) {
        // 参数校验
//        this.checkParams(outputGoods);

        outputGoods.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        outputGoods.setUpdateTime(new Date());
        return super.updateById(outputGoods);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputGoodsNewEntity reqVo) {
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

        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            keyWords = "%" + keyWords + "%";
            IPage<OutputGoodsNewEntity> page = new Query<OutputGoodsNewEntity>().getPage(params);
            List<OutputGoodsNewEntity> list = goodsNewDao.keyWordsList(page, keyWords);
            PageUtils pageUtils = new PageUtils(page);
            pageUtils.setList(list);
            return pageUtils;
        }else {
            return this.queryPage(params);
        }
    }

    /**
     * 数据导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importGoods(MultipartFile file) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<OutputGoodsNewEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<OutputGoodsNewEntity> duplicateList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            String[] excelHead = {"商品编码（必填）", "商品名称（必填）", "商品规格型号", "计量单位", "商品价格",
                    "所属机构", "税收分类名称（必填）", "税收分类编码（必填）", "税率（必填）", "是否享受优惠政策", "优惠政策类型"};
            String [] excelHeadAlias = {"goodsNumber", "goodsName", "specifications", "unit", "price", "deptName",
                    "taxCategoryName", "taxCategoryCode", "taxRate", "preferentialStr", "preferentialType"};
            for (int i = 0; i < excelHead.length; i++) {
                reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
            }
            List<OutputGoodsNewEntity> dataList = reader.read(0, 1, OutputGoodsNewEntity.class);

            if(CollectionUtils.isEmpty(dataList)){
                log.error("上传的Excel为空,请重新上传");
                throw new RRException("上传的Excel为空,请重新上传");
            }
            total = dataList.size();
            for (OutputGoodsNewEntity goodsEntity : dataList) {
                // 参数校验
                if(1 == checkExcel(goodsEntity)){
                    // 参数有误
                    fail += 1;
                }else {
                    // 获取到的为小数类型,转为百分数
                    String taxRate = goodsEntity.getTaxRate();
                    NumberFormat nf = NumberFormat.getPercentInstance();
                    goodsEntity.setTaxRate(nf.format(Double.valueOf(taxRate)));
                    // 验重
                    OutputGoodsNewEntity duplicate = super.getOne(
                            new QueryWrapper<OutputGoodsNewEntity>()
                                    .eq("goods_number", goodsEntity.getGoodsNumber())
                    );

                    if(null != duplicate){
                        duplicate.setGoodsName(goodsEntity.getGoodsName());
                        duplicate.setDeptName(goodsEntity.getDeptName());
                        duplicate.setTaxRate(goodsEntity.getTaxRate());
                        duplicate.setPrice(goodsEntity.getPrice());
                        duplicate.setPreferentialStr(goodsEntity.getPreferentialStr());
                        duplicate.setTaxCategoryCode(goodsEntity.getTaxCategoryCode());
                        duplicate.setTaxCategoryName(goodsEntity.getTaxCategoryName());
                        duplicate.setSpecifications(goodsEntity.getSpecifications());
                        duplicate.setUnit(goodsEntity.getUnit());
                        duplicate.setPreferentialType(goodsEntity.getPreferentialType());
                        duplicate.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                        duplicate.setUpdateTime(new Date());
                        super.updateById(this.paraphraseParams(duplicate));
                        duplicateList.add(duplicate);
                    }else {
                        // 添加转义后的实体
                        goodsEntity.setDelFlag("1");
                        goodsEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                        goodsEntity.setCreateTime(new Date());
                        super.save(this.paraphraseParams(goodsEntity));
                        entityList.add(paraphraseParams(goodsEntity));
                    }
                }
            }
            resMap.put("total", total);
            resMap.put("success", duplicateList.size() + entityList.size());
            resMap.put("fail", fail);

            return resMap;
        } catch (RRException e){
            throw e;
        } catch (Exception e) {
            log.error("商品信息导入出错: {}", e);
            throw new RRException("商品信息导入出现异常");
        }
    }

    /**
     * 参数校验
     */
    private void checkParams(OutputGoodsNewEntity outputGoods){
        if(StringUtils.isBlank(outputGoods.getGoodsNumber())){
            throw new RRException("商品编码不能为空");
        }
        if(StringUtils.isBlank(outputGoods.getGoodsName())){
            throw new RRException("商品名称不能为空");
        }
        if(StringUtils.isBlank(outputGoods.getTaxCategoryCode())){
            throw new RRException("税收分类编码不能为空");
        }
        if(StringUtils.isBlank(outputGoods.getTaxCategoryName())){
            throw new RRException("税收分类名称不能为空");
        }
        if(StringUtils.isBlank(outputGoods.getTaxRate())){
            throw new RRException("商品税率不能为空");
        }
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(OutputGoodsNewEntity entity){
        // 非空校验
        if(StringUtils.isBlank(entity.getGoodsNumber()) || StringUtils.isBlank(entity.getGoodsName()) ||
                StringUtils.isBlank(entity.getTaxCategoryName()) || StringUtils.isBlank(entity.getTaxCategoryCode()) ||
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

    /**
     * 枚举值转义
     * preferential 是否享受优惠政策0：否 1:是
     * preferential_type 优惠政策类型(0:免税; 1:部分免税; 2:应税)
     */
    private OutputGoodsNewEntity paraphraseParams(OutputGoodsNewEntity entity){
        // 对preferential转义
        if(StringUtils.isNotBlank(entity.getPreferentialStr())){
            if("否".equals(entity.getPreferentialStr().trim())){
                entity.setPreferential(0);
            }else if("是".equals(entity.getPreferentialStr().trim())){
                entity.setPreferential(1);
            }
        }
        // 对preferential_type转义
        if(StringUtils.isNotBlank(entity.getPreferentialType())){
            if("免税".equals(entity.getPreferentialType().trim())){
                entity.setPreferentialType("0");
            }else if("部分免税".equals(entity.getPreferentialType().trim())){
                entity.setPreferentialType("1");
            }else if("应税".equals(entity.getPreferentialType().trim())){
                entity.setPreferentialType("2");
            }
        }
        return entity;
    }
}

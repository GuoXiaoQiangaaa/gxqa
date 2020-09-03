package com.pwc.modules.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
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

import com.pwc.modules.data.dao.OutputItemListDao;
import com.pwc.modules.data.entity.OutputItemListEntity;
import com.pwc.modules.data.service.OutputItemListService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 科目清单服务实现
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Service("outputItemListService")
public class OutputItemListServiceImpl extends ServiceImpl<OutputItemListDao, OutputItemListEntity> implements OutputItemListService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputItemListEntity> page = this.page(
                new Query<OutputItemListEntity>().getPage(params),
                new QueryWrapper<OutputItemListEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 添加
     */
    @Override
    public boolean save(OutputItemListEntity entity) {
        int count = super.count(
                new QueryWrapper<OutputItemListEntity>()
                        .eq("item_code", entity.getItemCode())
        );
        if(count > 0){
            throw new RRException("该数据已存在,请核对后再添加");
        }

        entity.setDelFlag("1");
        entity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        entity.setCreateTime(new Date());
        return super.save(entity);
    }

    /**
     * 修改
     */
    @Override
    public boolean updateById(OutputItemListEntity entity) {
        OutputItemListEntity itemListEntity = super.getOne(
                new QueryWrapper<OutputItemListEntity>()
                        .eq("item_code", entity.getItemCode())
        );
        if(null != itemListEntity && entity.getItemId().equals(itemListEntity.getItemId())){
            throw new RRException("该数据已存在,请核对后再修改");
        }
        entity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        entity.setUpdateTime(new Date());
        return super.updateById(entity);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputItemListEntity reqVo) {
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

        IPage<OutputItemListEntity> page;
        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            page = this.page(
                    new Query<OutputItemListEntity>().getPage(params),
                    new QueryWrapper<OutputItemListEntity>()
                            .like("item_code", keyWords).or()
                            .like("item_type", keyWords).or()
                            .like("description", keyWords)
            );
        }else {
            page = this.page(
                    new Query<OutputItemListEntity>().getPage(params),
                    new QueryWrapper<OutputItemListEntity>()
            );
        }

        return new PageUtils(page);
    }

    /**
     * 数据导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importItem(MultipartFile file) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<OutputItemListEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<OutputItemListEntity> duplicateList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            String[] excelHead = {"科目编码", "科目类型", "科目描述"};
            String [] excelHeadAlias = {"itemCode", "itemType", "description"};
            for (int i = 0; i < excelHead.length; i++) {
                reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
            }
            List<OutputItemListEntity> dataList = reader.read(0, 1, OutputItemListEntity.class);

            if(CollectionUtils.isEmpty(dataList)){
                log.error("上传的Excel为空,请重新上传");
                throw new RRException("上传的Excel为空,请重新上传");
            }
            total = dataList.size();
            List<String> repeatDataList = new ArrayList<>();
            for (OutputItemListEntity itemEntity : dataList) {
                // 参数校验
                if(1 == checkExcel(itemEntity)){
                    // 参数有误
                    fail += 1;
                }else {
                    // 去除Excel中重复数据
                    String repeatData = itemEntity.getItemCode();
                    if(CollectionUtil.contains(repeatDataList, repeatData)){
                        fail += 1;
                        continue;
                    }
                    repeatDataList.add(repeatData);
                    // 验重
                    OutputItemListEntity duplicate = super.getOne(
                            new QueryWrapper<OutputItemListEntity>()
                                    .eq("item_code", itemEntity.getItemCode())
                    );
                    if(null != duplicate){
                        duplicate.setItemType(itemEntity.getItemType());
                        duplicate.setDescription(itemEntity.getDescription());
                        duplicate.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                        duplicate.setUpdateTime(new Date());
                        duplicateList.add(duplicate);
                    }else {
                        // 添加校验正确的实体
                        itemEntity.setDelFlag("1");
                        itemEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                        itemEntity.setCreateTime(new Date());
                        entityList.add(itemEntity);
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
            log.error("科目清单导入出错: {}", e);
            throw new RRException("科目清单导入出现异常");
        }
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(OutputItemListEntity entity){
        if(StringUtils.isBlank(entity.getItemCode()) || StringUtils.isBlank(entity.getItemType())){
            return 1;
        }
        return 0;
    }
}

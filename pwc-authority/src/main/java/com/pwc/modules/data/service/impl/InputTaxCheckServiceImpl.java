package com.pwc.modules.data.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.pwc.common.excel.ImportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
        );

        return new PageUtils(page);
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
            );
        }else {
            page = this.page(
                    new Query<InputTaxCheckEntity>().getPage(params),
                    new QueryWrapper<InputTaxCheckEntity>()
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
            for (InputTaxCheckEntity taxCheckEntity : dataList) {
                // 参数校验
                if(1 == checkExcel(taxCheckEntity)){
                    // 参数有误
                    fail += 1;
                }else {
                    // 添加校验正确的实体
                    taxCheckEntity.setDelFlag("1");
                    taxCheckEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                    taxCheckEntity.setCreateTime(new Date());
                    entityList.add(taxCheckEntity);
                }
            }
            resMap.put("total", total);
            resMap.put("success", entityList.size());
            resMap.put("fail", fail);
            super.saveBatch(entityList);

            return resMap;
        } catch (Exception e) {
            log.error("进项税率校验数据导入出错: {}", e);
            throw new RRException("进项税率校验数据导入出现异常");
        }
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(InputTaxCheckEntity entity){
        if(StringUtils.isBlank(entity.getGoodsName()) || StringUtils.isBlank(entity.getTaxRate())){
            return 1;
        }
        return 0;
    }

}

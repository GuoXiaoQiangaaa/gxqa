package com.pwc.modules.data.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.pwc.common.excel.ExportExcel;
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

import com.pwc.modules.data.dao.OutputSupplierDao;
import com.pwc.modules.data.entity.OutputSupplierEntity;
import com.pwc.modules.data.service.OutputSupplierService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 供应商信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Service("outputSupplierService")
@Slf4j
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
//        this.checkParams(outputSupplier);

        int count = super.count(
                new QueryWrapper<OutputSupplierEntity>()
                        .eq("sap_code", outputSupplier.getSapCode())
                        .eq("tax_code", outputSupplier.getTaxCode())
        );
        if(count > 0){
            throw new RRException("该数据已存在,请核对后再添加");
        }
        outputSupplier.setDelFlag("1");
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
//        this.checkParams(outputSupplier);

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
                            .like("dept_code", keyWords).or()
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
     * 数据导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importSupplier(MultipartFile file) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<OutputSupplierEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<OutputSupplierEntity> duplicateList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            String[] excelHead = {"SAP代码（必填）", "公司代码", "供应商名称（必填）", "纳税人识别号（必填）",
                    "地址", "电话号码", "开户行", "银行账号", "供应商邮箱", "分类"};
            String [] excelHeadAlias = {"sapCode", "deptCode", "name", "taxCode", "address", "contact",
                    "bank", "bankAccount", "email", "invoiceType"};
            for (int i = 0; i < excelHead.length; i++) {
                reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
            }
            List<OutputSupplierEntity> dataList = reader.read(0, 1, OutputSupplierEntity.class);

            if(CollectionUtils.isEmpty(dataList)){
                log.error("上传的Excel为空,请重新上传");
                throw new RRException("上传的Excel为空,请重新上传");
            }
            total = dataList.size();
            for (OutputSupplierEntity supplierEntity : dataList) {
                // 参数校验
                if(1 == checkExcel(supplierEntity)){
                    // 参数有误
                    fail += 1;
                }else {
                    OutputSupplierEntity duplicate = super.getOne(
                            new QueryWrapper<OutputSupplierEntity>()
                                    .eq("sap_code", supplierEntity.getSapCode())
                                    .eq("tax_code", supplierEntity.getTaxCode())
                    );
                    if(null != duplicate){
                        duplicate.setDeptCode(supplierEntity.getDeptCode());
                        duplicate.setName(supplierEntity.getName());
                        duplicate.setAddress(supplierEntity.getAddress());
                        duplicate.setContact(supplierEntity.getContact());
                        duplicate.setBank(supplierEntity.getBank());
                        duplicate.setBankAccount(supplierEntity.getBankAccount());
                        duplicate.setEmail(supplierEntity.getEmail());
                        duplicate.setInvoiceType(supplierEntity.getInvoiceType());
                        duplicate.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                        duplicate.setUpdateTime(new Date());
                        super.updateById(this.paraphraseParams(duplicate));
                        duplicateList.add(duplicate);
                    }else {
                        // 添加转义后的实体
                        supplierEntity.setDelFlag("1");
                        supplierEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                        supplierEntity.setCreateTime(new Date());
                        super.save(this.paraphraseParams(supplierEntity));
                        entityList.add(this.paraphraseParams(supplierEntity));
                    }

                }
            }
            resMap.put("total", total);
            resMap.put("success", duplicateList.size() + entityList.size());
            resMap.put("fail", fail);

            return resMap;
        } catch (RRException e){
            throw e;
        }  catch (Exception e) {
            log.error("供应商信息导入出错: {}", e);
            throw new RRException("供应商信息导入出现异常");
        }
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

    /**
     * 校验Excel中参数
     */
    private int checkExcel(OutputSupplierEntity entity){
        // 非空校验
        if(StringUtils.isBlank(entity.getSapCode()) || StringUtils.isBlank(entity.getName()) ||
                StringUtils.isBlank(entity.getTaxCode())){
            return 1;
        }

        // 枚举校验
        /** 0:NonPo Related; 1:MKRO; 2:DFU; 3:EDI; 4:R&D_外部; 5:IC_R&D; 6:IC_RRB; 7:IC_非R&D; 8:Red-letter VAT; 9:General */
        if(StringUtils.isNotBlank(entity.getInvoiceType()) && !("NonPo Related".trim().equalsIgnoreCase(entity.getInvoiceType().trim()) ||
               "MKRO".equalsIgnoreCase(entity.getInvoiceType().trim()) ||
                "DFU".equalsIgnoreCase(entity.getInvoiceType().trim()) ||
                "EDI".equalsIgnoreCase(entity.getInvoiceType().trim()) ||
                "R&D_外部".equalsIgnoreCase(entity.getInvoiceType().trim()) ||
                "IC_R&D".equalsIgnoreCase(entity.getInvoiceType().trim()) ||
                "IC_RRB".equalsIgnoreCase(entity.getInvoiceType().trim()) ||
                "IC_非R&D".equalsIgnoreCase(entity.getInvoiceType().trim()) ||
                "Red-letter VAT".trim().equalsIgnoreCase(entity.getInvoiceType().trim()) ||
                "General".equalsIgnoreCase(entity.getInvoiceType().trim()))){
            return 1;
        }
        return 0;
    }

    /**
     * 枚举值转义
     */
    private OutputSupplierEntity paraphraseParams(OutputSupplierEntity entity){
        // 发票类型
        String type = entity.getInvoiceType();
        if(StringUtils.isNotBlank(type)){
            if("NonPo Related".trim().equalsIgnoreCase(type.trim())){
                entity.setInvoiceType("0");
            }else if("MKRO".equalsIgnoreCase(type)){
                entity.setInvoiceType("1");
            }else if("DFU".equalsIgnoreCase(type)){
                entity.setInvoiceType("2");
            }else if("EDI".equalsIgnoreCase(type)){
                entity.setInvoiceType("3");
            }else if("R&D_外部".equalsIgnoreCase(type)){
                entity.setInvoiceType("4");
            }else if("IC_R&D".equalsIgnoreCase(type)){
                entity.setInvoiceType("5");
            }else if("IC_RRB".equalsIgnoreCase(type)){
                entity.setInvoiceType("6");
            }else if("IC_非R&D".equalsIgnoreCase(type)){
                entity.setInvoiceType("7");
            }else if("Red-letter VAT".trim().equalsIgnoreCase(type)){
                entity.setInvoiceType("8");
            }else if("General".equalsIgnoreCase(type)){
                entity.setInvoiceType("9");
            }
        }

        return entity;
    }

}

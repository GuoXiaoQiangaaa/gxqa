package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputCollectFormDao;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 进项汇总报表服务实现
 *
 * @author fanpf
 * @date 2020/9/16
 */
@Service("collectFormService")
@Slf4j
public class InputCollectFormServiceImpl extends ServiceImpl<InputCollectFormDao, InputCollectFormEntity> implements InputCollectFormService {

    @Autowired
    private SysDeptService deptService;
    @Autowired
    private InputInvoiceService invoiceService;
    @Autowired
    private InputInvoiceCustomsService customsService;
    @Autowired
    private InputInvoiceWhtService whtService;
    @Autowired
    private InputExportDetailService exportDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputCollectFormEntity> page = this.page(
                new Query<InputCollectFormEntity>().getPage(params),
                new QueryWrapper<InputCollectFormEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 生成报表数据
     */
    @Override
    public List<InputCollectFormEntity> createData(Map<String, Object> params) {
        String deptId = (String) params.get("deptId");
        String authDate = (String) params.get("authDate");
        if(StringUtils.isBlank(deptId) || StringUtils.isBlank(authDate)){
            throw new RRException("参数不能为空");
        }
        List<InputCollectFormEntity> collectFormList = new ArrayList<>();
        try {
            SysDeptEntity deptEntity = deptService.queryInfo(Long.valueOf(deptId));
            if(null == deptEntity){
                throw new RRException("未查询到公司信息");
            }
            /** 认证统计 */
            // 统计AP发票
            InputCollectFormEntity apInfo = this.collectAPOrTE(deptEntity, authDate, 5);
            // 统计TE发票
            InputCollectFormEntity teInfo = this.collectAPOrTE(deptEntity, authDate, 4);
            // 统计海关缴款书
            InputCollectFormEntity customsInfo = this.collectCustoms(deptEntity, authDate);
            // 统计WHT
            InputCollectFormEntity whtInfo = this.collectWHT(deptEntity, authDate);
            // 认证总计
            InputCollectFormEntity authTotalInfo = this.collectAuthTotal(deptEntity, authDate, apInfo, teInfo, 
                    customsInfo, whtInfo);

            /** 转出统计 转出类型: 0:红字转出; 1:海关免税转出; 2:福利转出; 3:其他转出 */
            // 统计红字转出
            InputCollectFormEntity redInfo = this.collectExport(deptEntity, authDate, "0");
            // 统计海关免税转出
            InputCollectFormEntity exemptionInfo = this.collectExport(deptEntity, authDate, "1");
            // 统计福利转出
            InputCollectFormEntity welfareInfo = this.collectExport(deptEntity, authDate, "2");
            // 统计其他转出
            InputCollectFormEntity othersInfo = this.collectExport(deptEntity, authDate, "3");
            // 转出总计
            InputCollectFormEntity exportTotalInfo = this.collectExportTotal(deptEntity, authDate, redInfo,
                    exemptionInfo, welfareInfo, othersInfo);

            collectFormList.add(apInfo);
            collectFormList.add(teInfo);
            collectFormList.add(customsInfo);
            collectFormList.add(whtInfo);
            collectFormList.add(authTotalInfo);
            collectFormList.add(redInfo);
            collectFormList.add(exemptionInfo);
            collectFormList.add(welfareInfo);
            collectFormList.add(othersInfo);
            collectFormList.add(exportTotalInfo);

            super.saveBatch(collectFormList);
            return collectFormList;
        }catch (RRException e){
            log.error("生成数据出错: {}", e);
            throw e;
        }catch (Exception e){
            log.error("生成数据异常: {}", e);
            throw new RRException("生成数据出现异常");
        }
    }

    /**
     * 报表明细
     * 汇总类型itemType:0:AP发票; 1:TE发票; 2:海关缴款书; 3:WHT; 5:红字转出; 6:海关免税转出; 7:福利转出; 8:其他转出;
     */
    @Override
    public PageUtils formDetail(Map<String, Object> params) {
        String deptId = (String) params.get("deptId");
        String authDate = (String) params.get("authDate");
        String itemType = (String) params.get("itemType");

        if(StringUtils.isBlank(deptId) || StringUtils.isBlank(authDate) || StringUtils.isBlank(itemType)){
            throw new RRException("参数不能为空");
        }
        try {
            SysDeptEntity deptEntity = deptService.queryInfo(Long.valueOf(deptId));
            if(null == deptEntity){
                throw new RRException("未查询到公司信息");
            }
            switch (itemType){
                case "0":
                    return this.queryAPOrTE(deptEntity, authDate, itemType, params);
                case "1":
                    return this.queryAPOrTE(deptEntity, authDate, itemType, params);
                case "2":
                    return this.queryCustoms(deptEntity, authDate, params);
                case "3":
                    return this.queryWHT(deptEntity, authDate, params);
                case "5":
                    return this.queryExport(deptEntity, authDate, itemType, params);
                case "6":
                    return this.queryExport(deptEntity, authDate, itemType, params);
                case "7":
                    return this.queryExport(deptEntity, authDate, itemType, params);
                case "8":
                    return this.queryExport(deptEntity, authDate, itemType, params);
                default:
                    log.error("未匹配到汇总类型");
                    throw new RRException("参数有误");
            }
        } catch (RRException e) {
            log.error("查询明细出错: {}", e);
            throw e;
        } catch (Exception e){
            log.error("查询明细异常: {}", e);
            throw new RRException("查询明细出现异常");
        }
    }

    /**
     * 区分数据(认证/转出)
     */
    @Override
    public Map<String, List<InputCollectFormEntity>> distinguish(List<InputCollectFormEntity> formData) {
        Map<String, List<InputCollectFormEntity>> distinguishMap = new HashMap<>();
        List<InputCollectFormEntity> authData = new ArrayList<>();
        List<InputCollectFormEntity> exportData = new ArrayList<>();
        for (InputCollectFormEntity formEntity : formData) {
            switch (formEntity.getItemType()){
                case "0":
                    formEntity.setItemType("AP发票");
                    authData.add(formEntity);
                    break;
                case "1":
                    formEntity.setItemType("TE发票");
                    authData.add(formEntity);
                    break;
                case "2":
                    formEntity.setItemType("海关缴款书");
                    authData.add(formEntity);
                    break;
                case "3":
                    formEntity.setItemType("WHT");
                    authData.add(formEntity);
                    break;
                case "4":
                    formEntity.setItemType("Total");
                    authData.add(formEntity);
                    break;
                case "5":
                    formEntity.setItemType("红字转出");
                    exportData.add(formEntity);
                    break;
                case "6":
                    formEntity.setItemType("海关免税转出");
                    exportData.add(formEntity);
                    break;
                case "7":
                    formEntity.setItemType("福利转出");
                    exportData.add(formEntity);
                    break;
                case "8":
                    formEntity.setItemType("其他转出");
                    exportData.add(formEntity);
                    break;
                case "9":
                    formEntity.setItemType("Total");
                    exportData.add(formEntity);
                    break;
                default:
                    break;
            }
        }
        distinguishMap.put("authData", authData);
        distinguishMap.put("exportData", exportData);

        return distinguishMap;
    }

    /**
     * 统计AP/TE发票
     */
    private InputCollectFormEntity collectAPOrTE(SysDeptEntity deptEntity, String authDate, int invoiceType){
        List<InputInvoiceEntity> invoiceList = invoiceService.list(
                new QueryWrapper<InputInvoiceEntity>()
                        .eq(StringUtils.isNotBlank(deptEntity.getTaxCode()), "invoice_purchaser_paragraph", deptEntity.getTaxCode())
                        .eq("invoice_deductible_period", authDate)
                        // 是否退票 0:未退票; 1:已退票
                        .eq("invoice_return", "0")
                        // 是否失效 0:否; 1:是
                        .eq("invoice_delete", "0")
                        // 认证结果 true:成功; false:失败
                        .eq("verfy", true)
                        // 发票类型  5:AP; 4:TE
                        .eq("invoice_style", invoiceType)
        );

        InputCollectFormEntity invoiceInfo = new InputCollectFormEntity();
        // 统计类型:0:认证; 1:转出
        invoiceInfo.setCollectType("0");
        invoiceInfo.setDeptId(deptEntity.getDeptId());
        invoiceInfo.setDeptName(deptEntity.getName());
        invoiceInfo.setAuthDate(authDate);
        // 汇总类型:0:AP发票; 1:TE发票;
        if(5 == invoiceType){// AP发票
            invoiceInfo.setItemType("0");
        }else {// TE发票
            invoiceInfo.setItemType("1");
        }
        // 税额
        BigDecimal taxPrice = BigDecimal.ZERO;
        // 金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        invoiceInfo.setItemCount(0);

        if(CollectionUtil.isNotEmpty(invoiceList)){
            invoiceInfo.setItemCount(invoiceList.size());

            for (InputInvoiceEntity inputInvoiceEntity : invoiceList) {
                if(null != inputInvoiceEntity.getInvoiceTaxPrice()){
                    taxPrice = taxPrice.add(inputInvoiceEntity.getInvoiceTaxPrice());
                }
                if(null != inputInvoiceEntity.getInvoiceFreePrice()){
                    totalPrice = totalPrice.add(inputInvoiceEntity.getInvoiceFreePrice());
                }
            }
        }
        invoiceInfo.setTaxPrice(taxPrice);
        invoiceInfo.setTotalPrice(totalPrice);
        invoiceInfo.setCreateBy(ShiroUtils.getUserId().intValue());
        invoiceInfo.setCreateTime(new Date());
        return invoiceInfo;
    }

    /**
     * 统计海关缴款书
     */
    private InputCollectFormEntity collectCustoms(SysDeptEntity deptEntity, String authDate){
        List<InputInvoiceCustomsEntity> customsList = customsService.list(
                new QueryWrapper<InputInvoiceCustomsEntity>()
                        .eq(StringUtils.isNotBlank(deptEntity.getTaxCode()), "purchaser_tax_no", deptEntity.getTaxCode())
                        .eq("deductible_period", authDate)
                        // 勾选状态 0:未勾选; 1:已勾选
                        .eq("deductible", "1")
                        // 是否退单 0:未退; 1:已退
                        .eq("invoice_return", "0")
        );
        InputCollectFormEntity customsInfo = new InputCollectFormEntity();
        // 统计类型:0:认证; 1:转出
        customsInfo.setCollectType("0");
        customsInfo.setDeptId(deptEntity.getDeptId());
        customsInfo.setDeptName(deptEntity.getName());
        customsInfo.setAuthDate(authDate);
        // 汇总类型:2:海关缴款书;
        customsInfo.setItemType("2");
        // 税额
        BigDecimal taxPrice = BigDecimal.ZERO;
        // 金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        customsInfo.setItemCount(0);

        if(CollectionUtil.isNotEmpty(customsList)){
            customsInfo.setItemCount(customsList.size());

            for (InputInvoiceCustomsEntity customsEntity : customsList) {
                if(null != customsEntity.getTotalTax()){
                    taxPrice = taxPrice.add(new BigDecimal(customsEntity.getTotalTax()));
                }
            }
        }
        customsInfo.setTaxPrice(taxPrice);
        customsInfo.setTotalPrice(totalPrice);
        customsInfo.setCreateBy(ShiroUtils.getUserId().intValue());
        customsInfo.setCreateTime(new Date());
        return customsInfo;
    }

    /**
     * 统计WHT
     */
    private InputCollectFormEntity collectWHT(SysDeptEntity deptEntity, String authDate){
        List<InputInvoiceWhtEntity> whtList = whtService.list(
                new QueryWrapper<InputInvoiceWhtEntity>()
                        .eq("dept_code", deptEntity.getDeptCode())
                        .eq("dept_id", deptEntity.getDeptId())
                        .eq("auth_date", authDate)
                        // 删除标识 0:删除; 1:正常
                        .eq("del_flag", "1")
        );
        InputCollectFormEntity whtInfo = new InputCollectFormEntity();
        // 统计类型:0:认证; 1:转出
        whtInfo.setCollectType("0");
        whtInfo.setDeptId(deptEntity.getDeptId());
        whtInfo.setDeptName(deptEntity.getName());
        whtInfo.setAuthDate(authDate);
        // 汇总类型: 3:WHT;
        whtInfo.setItemType("3");
        // 税额
        BigDecimal taxPrice = BigDecimal.ZERO;
        // 金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        whtInfo.setItemCount(0);

        if(CollectionUtil.isNotEmpty(whtList)){
            whtInfo.setItemCount(whtList.size());

            for (InputInvoiceWhtEntity whtEntity : whtList) {
                if(null != whtEntity.getTaxPrice()){
                    taxPrice = taxPrice.add(whtEntity.getTaxPrice());
                }
                if(null != whtEntity.getTotalPrice()){
                    totalPrice = totalPrice.add(whtEntity.getTotalPrice());
                }
            }
        }
        whtInfo.setTaxPrice(taxPrice);
        whtInfo.setTotalPrice(totalPrice);
        whtInfo.setCreateBy(ShiroUtils.getUserId().intValue());
        whtInfo.setCreateTime(new Date());
        return whtInfo;
    }

    /**
     * 认证总计
     */
    private InputCollectFormEntity collectAuthTotal(SysDeptEntity deptEntity,
                                                    String authDate,
                                                    InputCollectFormEntity apInfo,
                                                    InputCollectFormEntity teInfo,
                                                    InputCollectFormEntity customsInfo,
                                                    InputCollectFormEntity whtInfo){
        InputCollectFormEntity authTotalInfo = new InputCollectFormEntity();
        // 统计类型:0:认证; 1:转出
        authTotalInfo.setCollectType("0");
        authTotalInfo.setDeptId(deptEntity.getDeptId());
        authTotalInfo.setDeptName(deptEntity.getName());
        authTotalInfo.setAuthDate(authDate);
        // 汇总类型: 4:认证总计;
        authTotalInfo.setItemType("4");
        // 税额
        BigDecimal taxPrice = BigDecimal.ZERO;
        taxPrice = taxPrice.add(apInfo.getTaxPrice()).add(teInfo.getTaxPrice()).add(customsInfo.getTaxPrice())
                .add(whtInfo.getTaxPrice());
        authTotalInfo.setTaxPrice(taxPrice);
        // 金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        totalPrice = totalPrice.add(apInfo.getTotalPrice()).add(teInfo.getTotalPrice())
                .add(customsInfo.getTotalPrice()).add(whtInfo.getTotalPrice());
        authTotalInfo.setTotalPrice(totalPrice);
        // 总计份数
        authTotalInfo.setItemCount(apInfo.getItemCount() + teInfo.getItemCount() + customsInfo.getItemCount()
                + whtInfo.getItemCount());
        authTotalInfo.setCreateBy(ShiroUtils.getUserId().intValue());
        authTotalInfo.setCreateTime(new Date());
        return authTotalInfo;
    }

    /**
     * 统计转出
     * 转出类型: 0:红字转出; 1:海关免税转出; 2:福利转出; 3:其他转出
     */
    private InputCollectFormEntity collectExport(SysDeptEntity deptEntity, String authDate, String exportType) throws ParseException {
        List<InputExportDetailEntity> exportDetailList = exportDetailService.list(
                new QueryWrapper<InputExportDetailEntity>()
                        .eq("company_code", deptEntity.getDeptCode())
                        .eq("pstng_date", authDate)
                        .eq("export_type", exportType)
        );

        InputCollectFormEntity exportInfo = new InputCollectFormEntity();
        // 统计类型:0:认证; 1:转出
        exportInfo.setCollectType("1");
        exportInfo.setDeptId(deptEntity.getDeptId());
        exportInfo.setDeptName(deptEntity.getName());
        exportInfo.setAuthDate(authDate);
        // 汇总类型:5:红字转出; 6:海关免税转出; 7:福利转出; 8:其他转出;
        if("0".equals(exportType)){// 红字转出
            exportInfo.setItemType("5");
        }else if("1".equals(exportType)){// 海关免税转出
            exportInfo.setItemType("6");
        }else if("2".equals(exportType)){// 福利转出
            exportInfo.setItemType("7");
        }else {// 其他转出
            exportInfo.setItemType("8");
        }
        // 税额
        BigDecimal taxPrice = BigDecimal.ZERO;
        // 金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        exportInfo.setItemCount(0);

        if(CollectionUtil.isNotEmpty(exportDetailList)){
            exportInfo.setItemCount(exportDetailList.size());

            for (InputExportDetailEntity exportDetailEntity : exportDetailList) {
                if(null != exportDetailEntity.getAmount() && StringUtils.isNotBlank(exportDetailEntity.getTaxRate())){
                    // 转换税率
                    NumberFormat nf = NumberFormat.getPercentInstance();
                    Double taxRate = nf.parse(exportDetailEntity.getTaxRate()).doubleValue();
                    // 计算税额
                    BigDecimal amount = exportDetailEntity.getAmount();
                    BigDecimal taxAmount = amount.multiply(BigDecimal.valueOf(taxRate)).setScale(2, BigDecimal.ROUND_HALF_UP);

                    taxPrice = taxPrice.add(taxAmount);
                }
                if(null != exportDetailEntity.getAmount()){
                    totalPrice = totalPrice.add(exportDetailEntity.getAmount());
                }
            }
        }
        exportInfo.setTaxPrice(taxPrice);
        exportInfo.setTotalPrice(totalPrice);
        exportInfo.setCreateBy(ShiroUtils.getUserId().intValue());
        exportInfo.setCreateTime(new Date());
        return exportInfo;
    }

    /**
     * 转出总计
     */
    private InputCollectFormEntity collectExportTotal(SysDeptEntity deptEntity,
                                                    String authDate,
                                                    InputCollectFormEntity redInfo,
                                                    InputCollectFormEntity exemptionInfo,
                                                    InputCollectFormEntity welfareInfo,
                                                    InputCollectFormEntity othersInfo){
        InputCollectFormEntity exportTotalInfo = new InputCollectFormEntity();
        // 统计类型:0:认证; 1:转出
        exportTotalInfo.setCollectType("1");
        exportTotalInfo.setDeptId(deptEntity.getDeptId());
        exportTotalInfo.setDeptName(deptEntity.getName());
        exportTotalInfo.setAuthDate(authDate);
        // 汇总类型: 9:转出总计;
        exportTotalInfo.setItemType("9");
        // 税额
        BigDecimal taxPrice = BigDecimal.ZERO;
        taxPrice = taxPrice.add(redInfo.getTaxPrice()).add(exemptionInfo.getTaxPrice()).add(welfareInfo.getTaxPrice())
                .add(othersInfo.getTaxPrice());
        exportTotalInfo.setTaxPrice(taxPrice);
        // 金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        totalPrice = totalPrice.add(redInfo.getTotalPrice()).add(exemptionInfo.getTotalPrice())
                .add(welfareInfo.getTotalPrice()).add(othersInfo.getTotalPrice());
        exportTotalInfo.setTotalPrice(totalPrice);
        // 总计份数
        exportTotalInfo.setItemCount(redInfo.getItemCount() + exemptionInfo.getItemCount() + welfareInfo.getItemCount()
                + othersInfo.getItemCount());
        exportTotalInfo.setCreateBy(ShiroUtils.getUserId().intValue());
        exportTotalInfo.setCreateTime(new Date());
        return exportTotalInfo;
    }

    /**
     * AP/TE发票明细
     */
    private PageUtils queryAPOrTE(SysDeptEntity deptEntity, String authDate, String itemType, Map<String, Object> params){
        String invoiceNumber = (String) params.get("invoiceNumber");
        String invoiceCode = (String) params.get("invoiceCode");
        String purchaserCompany = (String) params.get("invoicePurchaserCompany");
        String sellCompany = (String) params.get("invoiceSellCompany");
        // 发票类型  5:AP; 4:TE
        int invoiceType = 5;
        if("1".equals(itemType)){
            invoiceType = 4;
        }

        IPage<InputInvoiceEntity> page = invoiceService.page(
                new Query<InputInvoiceEntity>().getPage(params),
                new QueryWrapper<InputInvoiceEntity>()
                        .eq(StringUtils.isNotBlank(deptEntity.getTaxCode()), "invoice_purchaser_paragraph", deptEntity.getTaxCode())
                        .eq("invoice_deductible_period", authDate)
                        // 是否退票 0:未退票; 1:已退票
                        .eq("invoice_return", "0")
                        // 是否失效 0:否; 1:是
                        .eq("invoice_delete", "0")
                        // 认证结果 true:成功; false:失败
                        .eq("verfy", true)
                        // 发票类型  5:AP; 4:TE
                        .eq("invoice_style", invoiceType)
                        .like(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                        .like(StringUtils.isNotBlank(invoiceCode), "invoice_code", invoiceCode)
                        .like(StringUtils.isNotBlank(purchaserCompany), "invoice_purchaser_company", purchaserCompany)
                        .like(StringUtils.isNotBlank(sellCompany), "invoice_sell_company", sellCompany)
        );

        return new PageUtils(page);
    }

    /**
     * 海关缴款书明细
     */
    private PageUtils queryCustoms(SysDeptEntity deptEntity, String authDate, Map<String, Object> params){
        String payNo = (String) params.get("payNo");
        String billingDate = (String) params.get("billingDate");
        String purchaserName = (String) params.get("purchaserName");

        IPage<InputInvoiceCustomsEntity> page = customsService.page(
                new Query<InputInvoiceCustomsEntity>().getPage(params),
                new QueryWrapper<InputInvoiceCustomsEntity>()
                        .eq(StringUtils.isNotBlank(deptEntity.getTaxCode()), "purchaser_tax_no", deptEntity.getTaxCode())
                        .eq("deductible_period", authDate)
                        // 勾选状态 0:未勾选; 1:已勾选
                        .eq("deductible", "1")
                        // 是否退单 0:未退; 1:已退
                        .eq("invoice_return", "0")
                        .like(StringUtils.isNotBlank(payNo), "pay_no", payNo)
                        .eq(StringUtils.isNotBlank(billingDate), "billing_date", billingDate)
                        .like(StringUtils.isNotBlank(purchaserName), "purchaser_name", purchaserName)
        );

        return new PageUtils(page);
    }

    /**
     * WHT明细
     */
    private PageUtils queryWHT(SysDeptEntity deptEntity, String authDate, Map<String, Object> params){
        String whtCode = (String) params.get("whtCode");
        String deptCode = (String) params.get("deptCode");
        String deptName = (String) params.get("deptName");

        IPage<InputInvoiceWhtEntity> page = whtService.page(
                new Query<InputInvoiceWhtEntity>().getPage(params),
                new QueryWrapper<InputInvoiceWhtEntity>()
                        .eq("dept_code", deptEntity.getDeptCode())
                        .eq("dept_id", deptEntity.getDeptId())
                        .eq("auth_date", authDate)
                        // 删除标识 0:删除; 1:正常
                        .eq("del_flag", "1")
                        .like(StringUtils.isNotBlank(deptCode), "dept_code", deptCode)
                        .like(StringUtils.isNotBlank(whtCode), "wht_code", whtCode)
                        .like(StringUtils.isNotBlank(deptName), "dept_name", deptName)
        );

        return new PageUtils(page);
    }

    /**
     * 转出明细
     * 转出类型: 0:红字转出; 1:海关免税转出; 2:福利转出; 3:其他转出
     */
    private PageUtils queryExport(SysDeptEntity deptEntity, String authDate, String itemType, Map<String, Object> params){
        String companyCode = (String) params.get("companyCode");
        String account = (String) params.get("account");
        String documentNo = (String) params.get("documentNo");
        String pstngDate = (String) params.get("pstngDate");

        String exportType = "";
        if("5".equals(itemType)){
            exportType = "0";
        }else if("6".equals(itemType)){
            exportType = "1";
        }else if("7".equals(itemType)){
            exportType = "2";
        }else if("8".equals(itemType)){
            exportType = "3";
        }

        IPage<InputExportDetailEntity> page = exportDetailService.page(
                new Query<InputExportDetailEntity>().getPage(params),
                new QueryWrapper<InputExportDetailEntity>()
                        .eq("company_code", deptEntity.getDeptCode())
                        .eq("pstng_date", authDate)
                        .eq("export_type", exportType)
                        .like(StringUtils.isNotBlank(companyCode), "company_code", companyCode)
                        .like(StringUtils.isNotBlank(account), "account", account)
                        .like(StringUtils.isNotBlank(documentNo), "document_no", documentNo)
                        .like(StringUtils.isNotBlank(pstngDate), "pstng_date", pstngDate)
        );

        return new PageUtils(page);
    }
}

package com.pwc.modules.input.service.impl;

import com.pwc.common.utils.*;
import com.pwc.modules.input.dao.InputSqpMatchResultDao;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.entity.vo.MatchResultVo;
import com.pwc.modules.input.service.*;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("inputSqpMatchResultService")
public class InputSqpMatchResultServiceImpl extends ServiceImpl<InputSqpMatchResultDao, InputSqpMatchResultEntity> implements InputSqpMatchResultService {

    @Autowired
    private InputInvoiceSapService inputInvoiceSapService;
    @Autowired
    private InputInvoiceCustomsService inputInvoiceCustomsService;
    @Autowired
    private InputRedInvoiceService inputRedInvoiceService;
    @Autowired
    private InputInvoiceService inputInvoiceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        String sort = ParamsMap.findMap(params, "sort");
        String deptId = ParamsMap.findMap(params, "deptId");
        String status = ParamsMap.findMap(params, "status");
        String yearAndMonth = ParamsMap.findMap(params, "yearAndMonth");

        IPage<InputSqpMatchResultEntity> page = this.page(
                new Query<InputSqpMatchResultEntity>().getPage(params),
                new QueryWrapper<InputSqpMatchResultEntity>()
                        .eq(StringUtils.isNotBlank(sort), "sort", sort)
                        .eq(StringUtils.isNotBlank(deptId), "dept_id", deptId)
                        .eq(StringUtils.isNotBlank(status), "status", status)
                        .eq(StringUtils.isNotBlank(yearAndMonth), "year_and_month", yearAndMonth)
                        .orderByDesc("create_time") //根据创建排序
        );
        return new PageUtils(page);
    }


    public List<InputSqpMatchResultEntity> queryMatchCurTime(Map<String, Object> params){
        List<InputSqpMatchResultEntity> matchResultList = new ArrayList<>();
        return matchResultList;
    }

    // 红字通知单账票匹配结果
    public MatchResultVo getRedInvoiceVo(Map<String, Object> params) {
        BigDecimal certificationTax = BigDecimal.ZERO;  // 认证税额
        BigDecimal monthCertTax = BigDecimal.ZERO;  //本月认证未入账税额
        BigDecimal monthCertBeforeTax = BigDecimal.ZERO; // 本月认证前月入账
        BigDecimal creditTax = BigDecimal.ZERO; // 入账税额
        BigDecimal monthCredTax = BigDecimal.ZERO; //本月入账未认证税额
        BigDecimal monthCredBeforeTax = BigDecimal.ZERO; // 本月入账前月认证
        MatchResultVo resultVo = new MatchResultVo();
        List<InputRedInvoiceEntity> customsEntitys = inputRedInvoiceService.getCertification(params);
        for (InputRedInvoiceEntity customsEntity : customsEntitys) {
            certificationTax = certificationTax.add(new BigDecimal(customsEntity.getTaxPrice().toString()));
            if ((customsEntity.getEntryStatus()).equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCertTax = monthCertTax.add(new BigDecimal(customsEntity.getTaxPrice().toString()));
            } else {
                Date date = DateUtils.stringToDate(customsEntity.getEntryDate(), "yyyy-MM-dd");
                Date dates = DateUtils.stringToDate(((String) params.get("date")).substring(0, 7) + "-01", "yyyy-MM-dd");
                if (date.before(dates)) {
                    monthCertBeforeTax = monthCertBeforeTax.add(new BigDecimal(customsEntity.getTaxPrice().toString()));
                }
            }

        }
        // 入账
        List<InputInvoiceSapEntity> sapEntitys = inputInvoiceSapService.getEntityByDateAndStatus(((String) params.get("date")).substring(0, 7), "2");
        for (InputInvoiceSapEntity sapEntity : sapEntitys) {
            creditTax = creditTax.add(sapEntity.getAmountInDoc());
            if (sapEntity.getSapMatch().equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCredTax = monthCredTax.add(sapEntity.getAmountInDoc());
            } else {

            }
        }
        resultVo.setMonth((String) params.get("date"));
        resultVo.setSort("红字通知单");
        resultVo.setCreditTax(creditTax.toString());
        resultVo.setCertificationTax(certificationTax.toString());
        BigDecimal differenceTax = creditTax.subtract(certificationTax);
        resultVo.setDifferenceTax(differenceTax.toString());
        BigDecimal adjustmentTax = (monthCredTax.add(monthCredBeforeTax)).subtract(monthCertTax.add(monthCertBeforeTax));
        resultVo.setAdjustmentTax(adjustmentTax.toString());
        resultVo.setCheckTax((differenceTax.subtract(adjustmentTax)).toString());
        resultVo.setMonthCredTax(monthCredTax.toString());
        resultVo.setMonthCertTax(monthCertTax.toString());
        resultVo.setMonthCredBeforeTax(monthCredBeforeTax.toString());
        resultVo.setMonthCertBeforeTax(monthCertBeforeTax.toString());
        return resultVo;
    }

    //海关通知单账票匹配结果
    public MatchResultVo getCustomsVo(Map<String, Object> params) {
        BigDecimal certificationTax = BigDecimal.ZERO;  // 认证税额
        BigDecimal monthCertTax = BigDecimal.ZERO;  //本月认证未入账税额
        BigDecimal monthCertBeforeTax = BigDecimal.ZERO; // 本月认证前月入账
        BigDecimal creditTax = BigDecimal.ZERO; // 入账税额
        BigDecimal monthCredTax = BigDecimal.ZERO; //本月入账未认证税额
        BigDecimal monthCredBeforeTax = BigDecimal.ZERO; // 本月入账前月认证
        MatchResultVo resultVo = new MatchResultVo();
        List<InputInvoiceCustomsEntity> customsEntitys = inputInvoiceCustomsService.getCertification(params);
        for (InputInvoiceCustomsEntity customsEntity : customsEntitys) {
            certificationTax = certificationTax.add(new BigDecimal(customsEntity.getTotalTax()));
            if ((customsEntity.getEntryState()).equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCertTax = monthCertTax.add(new BigDecimal(customsEntity.getTotalTax()));
            } else {
                Date date = DateUtils.stringToDate(customsEntity.getEntryDate(), "yyyy-MM-dd");
                Date dates = DateUtils.stringToDate(((String) params.get("date")).substring(0, 7) + "-01", "yyyy-MM-dd");
                if (date.before(dates)) {
                    monthCertBeforeTax = monthCertBeforeTax.add(new BigDecimal(customsEntity.getTotalTax()));
                }
            }
        }
        // 入账
        List<InputInvoiceSapEntity> sapEntitys = inputInvoiceSapService.getEntityByDateAndStatus(((String) params.get("date")).substring(0, 7), "2");
        for (InputInvoiceSapEntity sapEntity : sapEntitys) {
            creditTax = creditTax.add(sapEntity.getAmountInDoc());
            if (sapEntity.getSapMatch().equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCredTax = monthCredTax.add(sapEntity.getAmountInDoc());
            } else {

            }
        }
        resultVo.setMonth((String) params.get("date"));
        resultVo.setSort("海关缴款书");
        resultVo.setCreditTax(creditTax.toString());
        resultVo.setCertificationTax(certificationTax.toString());
        BigDecimal differenceTax = creditTax.subtract(certificationTax);
        resultVo.setDifferenceTax(differenceTax.toString());
        BigDecimal adjustmentTax = (monthCredTax.add(monthCredBeforeTax)).subtract(monthCertTax.add(monthCertBeforeTax));
        resultVo.setAdjustmentTax(adjustmentTax.toString());
        resultVo.setCheckTax((differenceTax.subtract(adjustmentTax)).toString());
        resultVo.setMonthCredTax(monthCredTax.toString());
        resultVo.setMonthCertTax(monthCertTax.toString());
        resultVo.setMonthCredBeforeTax(monthCredBeforeTax.toString());
        resultVo.setMonthCertBeforeTax(monthCertBeforeTax.toString());
        return resultVo;
    }


    //发票账票匹配结果
    public MatchResultVo getInvoiceVo(Map<String, Object> params) {
        BigDecimal certificationTax = BigDecimal.ZERO;  // 认证税额
        BigDecimal monthCertTax = BigDecimal.ZERO;  //本月认证未入账税额
        BigDecimal monthCertBeforeTax = BigDecimal.ZERO; // 本月认证前月入账
        BigDecimal creditTax = BigDecimal.ZERO; // 入账税额
        BigDecimal monthCredTax = BigDecimal.ZERO; //本月入账未认证税额
        BigDecimal monthCredBeforeTax = BigDecimal.ZERO; // 本月入账前月认证
        MatchResultVo resultVo = new MatchResultVo();
        List<InputInvoiceEntity> customsEntitys = inputInvoiceService.getCertificationList(params);
        for (InputInvoiceEntity customsEntity : customsEntitys) {
            certificationTax = certificationTax.add(new BigDecimal(customsEntity.getInvoiceTaxPrice().toString()));
            if ((customsEntity.getApplyStatus()).equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCertTax = monthCertTax.add(new BigDecimal(customsEntity.getInvoiceTaxPrice().toString()));
            } else {
                Date date = DateUtils.stringToDate(customsEntity.getEntryDate(), "yyyy-MM-dd");
                Date dates = DateUtils.stringToDate(((String) params.get("date")).substring(0, 7) + "-01", "yyyy-MM-dd");
                if (date.before(dates)) {
                    monthCertBeforeTax = monthCertBeforeTax.add(new BigDecimal(customsEntity.getInvoiceTaxPrice().toString()));
                }
            }
        }
        // 入账
        List<InputInvoiceSapEntity> sapEntitys = inputInvoiceSapService.getEntityByDateAndStatus(((String) params.get("date")).substring(0, 7), "2");
        for (InputInvoiceSapEntity sapEntity : sapEntitys) {
            creditTax = creditTax.add(sapEntity.getAmountInDoc());
            if (sapEntity.getSapMatch().equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCredTax = monthCredTax.add(sapEntity.getAmountInDoc());
            } else {

            }
        }
        resultVo.setMonth((String) params.get("date"));
        resultVo.setSort("海关缴款书");
        resultVo.setCreditTax(creditTax.toString());
        resultVo.setCertificationTax(certificationTax.toString());
        BigDecimal differenceTax = creditTax.subtract(certificationTax);
        resultVo.setDifferenceTax(differenceTax.toString());
        BigDecimal adjustmentTax = (monthCredTax.add(monthCredBeforeTax)).subtract(monthCertTax.add(monthCertBeforeTax));
        resultVo.setAdjustmentTax(adjustmentTax.toString());
        resultVo.setCheckTax((differenceTax.subtract(adjustmentTax)).toString());
        resultVo.setMonthCredTax(monthCredTax.toString());
        resultVo.setMonthCertTax(monthCertTax.toString());
        resultVo.setMonthCredBeforeTax(monthCredBeforeTax.toString());
        resultVo.setMonthCertBeforeTax(monthCertBeforeTax.toString());
        return resultVo;
    }

}

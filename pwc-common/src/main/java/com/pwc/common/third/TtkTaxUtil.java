package com.pwc.common.third;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.pwc.common.third.common.CityCode;
import com.pwc.common.third.common.ExcelExtractUtil;
import com.pwc.common.third.common.ExcelHandlerUtil;
import com.pwc.common.third.common.TtkConstants;
import com.pwc.common.third.request.*;
import com.pwc.common.third.response.TtkResponse;
import com.pwc.common.third.response.TtkTaxResultResponse;
import com.ttk.jchl.openapi.sdk.TtkOpenAPI;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 税务计算
 *
 * @author zk
 */
public class TtkTaxUtil {
    private static final Log log = Log.get(TtkTaxUtil.class);
    private static TtkOpenAPI ttkOpenAPI = TtkOpenApiClientFactory.getTtkOpenAPI();

    /**
     * 上传财报
     */
    public static void writeFinancialReportData(Long orgId, String taxCode, String companyName, String filepath) {
        // 小类代码
        String zlbsxlDm = TtkOrgUtil.queryAccountStandardId(orgId);
        JSONObject reqJSON = JSONUtil.createObj();
        reqJSON.put("orgId", orgId);
        reqJSON.put("year", DateUtil.thisYear());
        reqJSON.put("month", DateUtil.thisMonth() + 1);

//        File file = FileUtil.file("/Users/zk/Desktop/财务推送报文样例.json");
//        JSONObject jsonObject = JSONUtil.readJSONObject(file, Charset.defaultCharset());
//
//
        JSONArray bodyArray = JSONUtil.createArray();

        //资产负债json
        JSONObject balance = JSONUtil.createObj();
        JSONObject balanceHeader = JSONUtil.createObj();
        JSONArray balanceBody = JSONUtil.createArray();

        String ssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(), -3)), DatePattern.NORM_DATE_PATTERN);
        String ssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        balanceHeader.put("nsrsbh", taxCode);
        balanceHeader.put("ssqq", ssqq);
        balanceHeader.put("nsrmc", companyName);
        balanceHeader.put("zlbsxlDm", zlbsxlDm);
        balanceHeader.put("ssqz", ssqz);
        balanceHeader.put("reportType", "1");
        balanceHeader.put("templateCode", 1);
        // put balance header
        balance.put("header", balanceHeader);

        //读取Excel
        // apple 2 zara 3
//        int sheetIndex = 2;
        ExcelReader balanceReader = ExcelUtil.getReader(FileUtil.file(filepath), "Balance Sheet总公司");
//        ExcelReader balanceReader = ExcelUtil.getReader()
        List banlanceRowlist = balanceReader.read();

//        balanceBody = ExcelHandlerUtil.financeRepordHandler(banlanceRowlist, 1, "BJ", "Apple");
        balanceBody = ExcelHandlerUtil.financeRepordHandler(banlanceRowlist, 1, "", "Zara");
        // put balance body
        balance.put("body", balanceBody);
        // put balance
        bodyArray.put(balance);


        // 利润JSON
        JSONObject pl = JSONUtil.createObj();
        JSONObject plHeader = JSONUtil.createObj();
        JSONArray plBody = JSONUtil.createArray();
        plHeader.put("nsrsbh", taxCode);
        plHeader.put("ssqq", ssqq);
        plHeader.put("nsrmc", companyName);
        plHeader.put("zlbsxlDm", zlbsxlDm);
        plHeader.put("ssqz", ssqz);
        plHeader.put("reportType", "2");
        plHeader.put("templateCode", 1);
        // put pl header
        pl.put("header", plHeader);
        //读取Excel
        // apple 3 zara 4
        ExcelReader plReader = ExcelUtil.getReader(FileUtil.file(filepath), "P&L总公司");
        List plRowlist = plReader.read();

//        plBody = ExcelHandlerUtil.financeRepordHandler(plRowlist, 2, "BJ", "Apple");
        plBody = ExcelHandlerUtil.financeRepordHandler(plRowlist, 2, "", "Zara");
        // put pl body
        pl.put("body", plBody);
        // put pl
        bodyArray.put(pl);
        // put req body
        reqJSON.put("body", bodyArray);


        System.out.println("request: " + reqJSON.toString());
        System.out.println("response: " + ttkOpenAPI.writeFinancialReportData(reqJSON.toString()).toJSONString());


    }

    /**
     * 上传财报
     */
    public static void writeFinancialReportDataForSmall(Long orgId, String taxCode, String companyName, String filepath) {
        // 小类代码
        String zlbsxlDm = TtkOrgUtil.queryAccountStandardId(orgId);
        JSONObject reqJSON = JSONUtil.createObj();
        reqJSON.put("orgId", orgId);
        reqJSON.put("year", DateUtil.thisYear());
        reqJSON.put("month", DateUtil.thisMonth() + 1);
        JSONArray bodyArray = JSONUtil.createArray();

        //资产负债json
        JSONObject balance = JSONUtil.createObj();
        JSONObject balanceHeader = JSONUtil.createObj();
        JSONArray balanceBody = JSONUtil.createArray();

        String ssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(), -3)), DatePattern.NORM_DATE_PATTERN);
        String ssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        balanceHeader.put("nsrsbh", taxCode);
        balanceHeader.put("ssqq", ssqq);
        balanceHeader.put("nsrmc", companyName);
        balanceHeader.put("zlbsxlDm", zlbsxlDm);
        balanceHeader.put("ssqz", ssqz);
        balanceHeader.put("reportType", "1");
//        balanceHeader.put("templateCode", 1);
        // put balance header
        balance.put("header", balanceHeader);

        //读取Excel
        ExcelReader balanceReader = ExcelUtil.getReader(FileUtil.file(filepath), "Balance Sheet总公司");
        List banlanceRowlist = balanceReader.read();

        balanceBody = ExcelHandlerUtil.smallFinanceRepordHandler(banlanceRowlist, 1, "", "Zara");
        // put balance body
        balance.put("body", balanceBody);
        // put balance
        bodyArray.put(balance);


        // 利润JSON
        JSONObject pl = JSONUtil.createObj();
        JSONObject plHeader = JSONUtil.createObj();
        JSONArray plBody = JSONUtil.createArray();
        plHeader.put("nsrsbh", taxCode);
        plHeader.put("ssqq", ssqq);
        plHeader.put("nsrmc", companyName);
        plHeader.put("zlbsxlDm", zlbsxlDm);
        plHeader.put("ssqz", ssqz);
        plHeader.put("reportType", "2");
//        plHeader.put("templateCode", 1);
        // put pl header
        pl.put("header", plHeader);
        //读取Excel
        ExcelReader plReader = ExcelUtil.getReader(FileUtil.file(filepath), "P&L总公司");
        List plRowlist = plReader.read();

        plBody = ExcelHandlerUtil.smallFinanceRepordHandler(plRowlist, 2, "", "Zara");
        // put pl body
        pl.put("body", plBody);
        // put pl
        bodyArray.put(pl);
        // put req body
        reqJSON.put("body", bodyArray);


        System.out.println("request: " + reqJSON.toString());
        System.out.println("response: " + ttkOpenAPI.writeFinancialReportData(reqJSON.toString()).toJSONString());


    }

    /**
     * 上传税报接口
     */
    public static void writeValueAddedTaxData() {
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", "241571900840320");

        File file = FileUtil.file(ClassUtil.getClassPath() + "taxJson/small_taxpayer.json");
        JSONObject jsonObject = JSONUtil.readJSONObject(file, Charset.defaultCharset());
        System.out.println(jsonObject.toString());
        req.put("body", jsonObject);
        com.alibaba.fastjson.JSONObject jsonObject1 = ttkOpenAPI.writeValueAddedTaxData(req.toString());
        System.out.println(jsonObject1.toJSONString());
    }

    /**
     * 上传税报接口
     */
    public static void writeValueAddedTaxDataForGeneral(Long orgId, FilingVat vat) {
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);
        File file = FileUtil.file(ClassUtil.getClassPath() + "taxJson/general_taxpayer.json");
        JSONObject jsonObject = JSONUtil.readJSONObject(file, Charset.defaultCharset());
        //申报参数，日期类型等
        JSONObject taxParam = jsonObject.getJSONObject("taxParam");
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610606"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqq", skssqq);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqz", skssqz);
        //常量 一般纳税人
        taxParam.put("yzpzzlDm", "BDA0610606");

        //数据
        JSONObject ybData = jsonObject.getJSONObject("ybData");

//        主表
        JSONObject zzssyyybnsrzb = ybData.getJSONObject("zzssyyybnsrzb");
        JSONObject zbGrid = zzssyyybnsrzb.getJSONObject("zbGrid");
        JSONArray zbGridlbVO = zbGrid.getJSONArray("zbGridlbVO");

        JSONObject zglbvo0 = (JSONObject) zbGridlbVO.get(0);
        //        json['ybData']['zzssyyybnsrzb']['zbGrid']['zbGridlbVO'][0]['jxse'] 进项合计-进项税额
        zglbvo0.put("jxse", vat.getJxhjJxse());

        //销售税额 256586.3 销项合计-销项税额 json['ybData']['zzssyyybnsrzb']['zbGrid']['zbGridlbVO'][0]['xxse']
        zglbvo0.put("xxse", vat.getXxhjXxse());

//        进项明细 -> 增值税税控系统专用设备费及技术维护费 0.00 json['ybData']['zzssyyybnsrzb']['zbGrid']['zbGridlbVO'][0]['ynsejze']
        zglbvo0.put("ynsejze", vat.getJxmxZzsskxtzysbfjjswhf());

//        本月应交增值税 -> 上期应纳税额（留抵为负数）json['ybData']['zzssyyybnsrzb']['zbGrid']['zbGridlbVO'][0]['sqldse'] 0.00
        zglbvo0.put("sqldse", vat.getByyjzzsSqynseLdwfs());

//        销项合计 -> 销项销售额 json['ybData']['zzssyyybnsrzb']['zbGrid']['zbGridlbVO'][0]['asysljsxse'] 256,586.30
//        json['ybData']['zzssyyybnsrzb']['zbGrid']['zbGridlbVO'][0]['yshwxse']
        zglbvo0.put("yshwxse", vat.getXxhjXxxse());
        zglbvo0.put("asysljsxse", vat.getXxhjXxxse());

        //附表 1
        JSONObject zzssyyybnsr01bqxsqkmxb = ybData.getJSONObject("zzssyyybnsr01bqxsqkmxb");
        JSONObject bqxsqkmxbGrid = zzssyyybnsr01bqxsqkmxb.getJSONObject("bqxsqkmxbGrid");
        JSONArray bqxsqkmxbGridlbVO = bqxsqkmxbGrid.getJSONArray("bqxsqkmxbGridlbVO");

        JSONObject bglbvo0 = (JSONObject) bqxsqkmxbGridlbVO.get(0);
//        销项明细 -> 销售金额（普票）json['ybData']['zzssyyybnsr01bqxsqkmxb']['bqxsqkmxbGrid']['bqxsqkmxbGridlbVO'][0]['kjqtfpXse'] 2,357.52
        bglbvo0.put("kjqtfpXse", vat.getXxmxXsjePp());

//        销项明细 -> 增值税税额（普票）json['ybData']['zzssyyybnsr01bqxsqkmxb']['bqxsqkmxbGrid']['bqxsqkmxbGridlbVO'][0]['kjqtfpXxynse'] 306.48
        bglbvo0.put("kjqtfpXxynse", vat.getXxmxZzssePp());

//        销项明细 -> 未开票金额 json['ybData']['zzssyyybnsr01bqxsqkmxb']['bqxsqkmxbGrid']['bqxsqkmxbGridlbVO'][0]['wkjfpXse'] 254,228.78
        bglbvo0.put("wkjfpXse", vat.getXxmxWkpje());

//        销项明细 -> 未开票税额 json['ybData']['zzssyyybnsr01bqxsqkmxb']['bqxsqkmxbGrid']['bqxsqkmxbGridlbVO'][0]['wkjfpXxynse'] 33,049.74
        bglbvo0.put("wkjfpXxynse", vat.getXxmxWkpse());

//        销项明细->销售金额（专票）	json['ybData']['zzssyyybnsr01bqxsqkmxb']['bqxsqkmxbGrid']['bqxsqkmxbGridlbVO'][0]['kjskzzszyfpXse']
        bglbvo0.put("kjskzzszyfpXse", vat.getXxmxXsjeZp());

//        销项明细->增值税税额（专票）	json['ybData']['zzssyyybnsr01bqxsqkmxb']['bqxsqkmxbGrid']['bqxsqkmxbGridlbVO'][0]['kjskzzszyfpXxynse']
        bglbvo0.put("kjskzzszyfpXxynse", vat.getXxmxZzsseZp());

        //附表 2
        JSONObject zzssyyybnsr02bqjxsemxb = ybData.getJSONObject("zzssyyybnsr02bqjxsemxb");
        JSONObject bqjxsemxbGrid = zzssyyybnsr02bqjxsemxb.getJSONObject("bqjxsemxbGrid");
        JSONArray bqjxsemxbGridlbVO = bqjxsemxbGrid.getJSONArray("bqjxsemxbGridlbVO");

//        进项明细 -> 进项专用发票份数  json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][35]['fs'] 7.00
        JSONObject bqjglbvo35 = (JSONObject) bqjxsemxbGridlbVO.get(35);
        bqjglbvo35.put("fs", vat.getJxmxJxzyfpfs());

//        进项明细 -> 进项销售额json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][35]['je'] 142,078.49
        bqjglbvo35.put("je", vat.getJxmxJxxse());

//        进项明细 -> 进项税额 json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][35]['se'] 16,058.19
        bqjglbvo35.put("se", vat.getJxmxJxse());

        JSONObject bqjglbvo1 = (JSONObject) bqjxsemxbGridlbVO.get(1);
        bqjglbvo1.put("fs", vat.getJxmxJxzyfpfs());

//        进项明细 -> 进项销售额json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][1]['je'] 142,078.49
        bqjglbvo1.put("je", vat.getJxmxJxxse());

//        进项明细 -> 进项税额 json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][1]['se'] 16,058.19
        bqjglbvo1.put("se", vat.getJxmxJxse());

//        进项明细 -> 进项转出税额（红字专用发票） json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][20]['se']
        JSONObject bqjglbvo20 = (JSONObject) bqjxsemxbGridlbVO.get(20);
        bqjglbvo20.put("se", vat.getJxmxJxzcseHzzyfp());

//        进项明细 -> 进项转出税额（非正常损失）json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][16]['se']
        JSONObject bqjglbvo16 = (JSONObject) bqjxsemxbGridlbVO.get(16);
        bqjglbvo16.put("se", vat.getJxmxJxzcseFzcss());

//        进项明细 -> 进项转出税额（集体福利个人消费）
//        json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][15]['se']
        JSONObject bqjglbvo15 = (JSONObject) bqjxsemxbGridlbVO.get(15);
        bqjglbvo15.put("se", vat.getJxmxJxzcseJtflgrxf());

//        进项明细 -> 进项转出税额（其他，如有请注明项目）json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][23]['se']
        JSONObject bqjglbvo23 = (JSONObject) bqjxsemxbGridlbVO.get(23);
        bqjglbvo23.put("se", vat.getJxmxJxzcseQt());

//        进项明细->旅客运输数量 json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][10]['fs']
//        进项明细->旅客运输金额 json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][10]['je']
//        进项明细->旅客运输税额 json['ybData'][zzssyyybnsr02bqjxsemxb']['bqjxsemxbGrid'][bqjxsemxbGridlbVO'][10]['se']
        JSONObject bqjglbvo10 = (JSONObject) bqjxsemxbGridlbVO.get(10);
        bqjglbvo10.put("fs", StrUtil.isNotBlank(vat.getJxmxLkyssl()) ? vat.getJxmxLkyssl() : "");
        bqjglbvo10.put("je", StrUtil.isNotBlank(vat.getJxmxLkysje()) ? vat.getJxmxLkysje() : "");
        bqjglbvo10.put("se", StrUtil.isNotBlank(vat.getJxmxLkysse()) ? vat.getJxmxLkysse() : "");

        //不是北京地区判断移除此节点
        if (!vat.getCityCode().equals(CityCode.BEIJING.getValue())) {
            ybData.remove("qtkspzmxb");
        }

        //不是山东地区判断移除此节点
        if (!vat.getCityCode().equals(CityCode.SHANDONG.getValue())) {
            ybData.remove("zzsbcsbb");
        }

        log.info("request body :", jsonObject.toString());
        System.out.println(jsonObject.toString());
        req.put("body", jsonObject);
        com.alibaba.fastjson.JSONObject jsonObject1 = ttkOpenAPI.writeValueAddedTaxData(req.toString());
        log.info("response body :", jsonObject1.toJSONString());
        System.out.println(jsonObject1.toString());
    }


    /**
     * 上传税报接口
     */
    public static void writeValueAddedTaxDataForSmall(Long orgId, FilingVat vat, Integer period) {

        File file = FileUtil.file(ClassUtil.getClassPath() + "taxJson/small_scale_taxpayer.json");
        JSONObject jsonObject = JSONUtil.readJSONObject(file, Charset.defaultCharset());
        //申报参数，日期类型等
        JSONObject taxParam = jsonObject.getJSONObject("taxParam");
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610606"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        String skssqq = "", skssqz = "";
        if (1 == period) {
            skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
            skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        } else {
            skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.lastMonth(), -2)), DatePattern.NORM_DATE_PATTERN);
            skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        }

        taxParam.put("skssqq", skssqq);
        taxParam.put("skssqz", skssqz);
        //常量 小规模纳税人
        taxParam.put("yzpzzlDm", "BDA0610611");
        //
        BigDecimal yzzzsbhsxse = new BigDecimal(vat.getXxhjXxxse());
        BigDecimal bqynsejze = new BigDecimal("0");
        //数据
        JSONObject xgmData = jsonObject.getJSONObject("xgmData");
        JSONObject zzssyyxgmnsr = xgmData.getJSONObject("zzssyyxgmnsr");
        JSONObject zzsxgmGrid = zzssyyxgmnsr.getJSONObject("zzsxgmGrid");
        JSONObject zzsjmssbmxb = xgmData.getJSONObject("zzsjmssbmxb");
        JSONObject zzsxgmGridlb0 = zzsxgmGrid.getJSONArray("zzsxgmGridlb").getJSONObject(0);
        JSONObject zzsjmssbmxbjsxmGrid = zzsjmssbmxb.getJSONObject("zzsjmssbmxbjsxmGrid");
        JSONArray zzsjmssbmxbjsxmGridlbVO = zzsjmssbmxbjsxmGrid.getJSONArray("zzsjmssbmxbjsxmGridlbVO");

        Integer ewbhxh = 2;
        // 非增值税税控专设备费及技术维护费
        if (StrUtil.isNotBlank(vat.getJxmxZzsskxtzysbfjjswhf())) {
            BigDecimal jxmxZzsskxtzysbfjjswhf = new BigDecimal(vat.getJxmxZzsskxtzysbfjjswhf());
            if (jxmxZzsskxtzysbfjjswhf.compareTo(new BigDecimal("0")) != 0) {
                JSONObject zzsjmssbmxbjsxmGridlbVO0 = new JSONObject();
                zzsjmssbmxbjsxmGridlbVO0.put("ewbhxh", ewbhxh);
                zzsjmssbmxbjsxmGridlbVO0.put("hmc", "0001129914");
                zzsjmssbmxbjsxmGridlbVO0.put("swsxDm", "SXA031900185");
                zzsjmssbmxbjsxmGridlbVO0.put("bqfse", vat.getJxmxZzsskxtzysbfjjswhf());
                zzsjmssbmxbjsxmGridlbVO0.put("bqydjse", vat.getJxmxZzsskxtzysbfjjswhf());
                zzsjmssbmxbjsxmGridlbVO0.put("bqsjdjse", vat.getJxmxZzsskxtzysbfjjswhf());
                zzsjmssbmxbjsxmGridlbVO.put(zzsjmssbmxbjsxmGridlbVO0);
                bqynsejze = bqynsejze.add(jxmxZzsskxtzysbfjjswhf);
                ewbhxh ++;
            }
        }
        // 非湖北地区小规模减征
        if (StrUtil.isNotBlank(vat.getJxmxFhbdqxgmjz())) {
            BigDecimal jxmxFhbdqxgmjz = new BigDecimal(vat.getJxmxFhbdqxgmjz());
            if (jxmxFhbdqxgmjz.compareTo(new BigDecimal("0")) != 0) {
                JSONObject zzsjmssbmxbjsxmGridlbVO1 = new JSONObject();
                zzsjmssbmxbjsxmGridlbVO1.put("ewbhxh", ewbhxh);
                zzsjmssbmxbjsxmGridlbVO1.put("hmc", "0001011608");
                zzsjmssbmxbjsxmGridlbVO1.put("swsxDm", "SXA031901121");
                zzsjmssbmxbjsxmGridlbVO1.put("bqfse", vat.getJxmxFhbdqxgmjz());
                zzsjmssbmxbjsxmGridlbVO1.put("bqydjse", vat.getJxmxFhbdqxgmjz());
                zzsjmssbmxbjsxmGridlbVO1.put("bqsjdjse", vat.getJxmxFhbdqxgmjz());
                zzsjmssbmxbjsxmGridlbVO.put(zzsjmssbmxbjsxmGridlbVO1);
                bqynsejze = bqynsejze.add(jxmxFhbdqxgmjz);
            }
        }

        zzsxgmGridlb0.put("skqjkjdptfpbhsxse", vat.getXxmxXsjePp());
        zzsxgmGridlb0.put("yzzzsbhsxse", yzzzsbhsxse.toPlainString());
        zzsxgmGridlb0.put("bqynsejze", bqynsejze.toPlainString());

        // 封装提交数据
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);
        req.put("body", jsonObject);
        log.info(req.toString());
        log.info(ttkOpenAPI.writeValueAddedTaxData(req.toString()).toJSONString());
    }
    /**
     * 打开税报清册页面【嵌入财税云页面】
     *
     * @param orgId 企业ID
     * @param year  年份
     * @param month 月份
     * @param json  自定义数据， 没有请传 null
     */
    public static String getWebUrlForShenBao(Long orgId, Integer year, Integer month, String json) {
        if (null != year && null != month) {
            return ttkOpenAPI.getWebUrlForShenBao(orgId.toString(), year, month, json);
        } else {
            return ttkOpenAPI.getWebUrlForShenBao(orgId.toString());
        }
    }

    /**
     * (1)申报结果接口(getTaxResult)
     */
    public static TtkResponse<TtkTaxResultResponse> getTaxResult(Long userId, Long orgId, String year, String month) {
        String json = "{\"userId\":" + userId + ",\"orgId\":" + orgId + ",\"year\":\"" + year + "\",\"month\":\"" + month + "\"}";
        com.alibaba.fastjson.JSONObject res = ttkOpenAPI.getTaxResult(json);
        Type type = new TypeReference<TtkResponse<TtkTaxResultResponse>>() {
        }.getType();
        System.out.println(res.toJSONString());
        return res.toJavaObject(type);
    }

    /**
     * (1)申报结果接口(getTaxResult)
     */
    @Deprecated
    public static void getTaxResultBatch() {
        String json = "{\"userId\":0,\"orgId\":[241571900840320,241571900840320],\"year\":\"2011\",\"month\":\"11\"}";
        com.alibaba.fastjson.JSONObject result = ttkOpenAPI.getTaxResult(json);
        System.out.println(result.toJSONString());
    }

    /**
     * 批量申报页面
     */
    public static String getWebUrlForShenBaoBatch(List<Long> orgIds) {
        String url = ttkOpenAPI.getWebUrlForShenBaoBatch(orgIds);
        System.out.println(url);
        return url;
    }


    /**
     * 申报表XML格式下载接口
     *
     * @param orgId    企业ID
     * @param yzpzzlDm 代码
     * @param sqqs     所属期始
     * @param sqqz     所属期止
     */
    public static void downloadTaxReportXML(Long orgId, String yzpzzlDm, String sqqs, String sqqz) {
        String json = "{\n" +
                " \"orgId\":" + orgId + ",\n" +
                " \"yzpzzlDm\": \"" + yzpzzlDm + "\",\n" +
                " \"sqqs\": \"" + sqqs + "\",\n" +
                " \"sqqz\": \"" + sqqz + "\"\n" +
                "}";
        com.alibaba.fastjson.JSONObject jsonObject = ttkOpenAPI.downloadTaxReportXML(json);
        System.out.println(jsonObject.toJSONString());
    }

    /**
     * 申报表PDF格式下载接口
     *
     * @param orgId    企业ID
     * @param year     年
     * @param fromDate 开始日前
     * @param toDate   结束日期
     * @param yzpzzlDm 代码
     */
    public static String downloadTaxReportPDF(Long orgId, Integer year, String fromDate, String toDate, String yzpzzlDm) {
        String json = "{\n" +
                " \"orgId\":" + orgId + ",\n" +
                " \"year\": \"" + year + "\",\n" +
                " \"fromDate\": \"" + fromDate + "\",\n" +
                " \"toDate\": \"" + toDate + "\",\n" +
                " \"yzpzzlDm\": \"" + yzpzzlDm + "\"\n" +
                "}";
        com.alibaba.fastjson.JSONObject jsonObject = ttkOpenAPI.downloadTaxReportPDF(json);
        System.out.println(jsonObject.toJSONString());
        jsonObject.getJSONObject("body").get("content");

        String fileName = orgId + "-" + IdUtil.objectId() + ".pdf";
        String filePath = ClassUtil.getClassPath() + "statics/upload/" + fileName;

        TtkTaxUtil.base64StringToPdf(jsonObject.getJSONObject("body").get("content").toString(), filePath);

        return filePath;
    }

    /**
     * 申报表PDF格式下载接口
     *
     * @param orgId    企业ID
     * @param year     年
     * @param fromDate 开始日前
     * @param toDate   结束日期
     * @param yzpzzlDm 代码
     */
    public static String downloadTaxReportPDF(Long orgId, Integer year, String fromDate, String toDate, String yzpzzlDm, String taxType, String simpleName) {
        String json = "{\n" +
                " \"orgId\":" + orgId + ",\n" +
                " \"year\": \"" + year + "\",\n" +
                " \"fromDate\": \"" + fromDate + "\",\n" +
                " \"toDate\": \"" + toDate + "\",\n" +
                " \"yzpzzlDm\": \"" + yzpzzlDm + "\"\n" +
                "}";
        com.alibaba.fastjson.JSONObject jsonObject = ttkOpenAPI.downloadTaxReportPDF(json);
        System.out.println(jsonObject.toJSONString());
        jsonObject.getJSONObject("body").get("content");
        String fileName = simpleName + " " + taxType + " " + DateUtil.format(DateUtil.parseDate(fromDate), "yyyyMM") + ".pdf";
        String filePath = ClassUtil.getClassPath() + "statics/upload/" + yzpzzlDm;
        File file = FileUtil.file(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        TtkTaxUtil.base64StringToPdf(jsonObject.getJSONObject("body").get("content").toString(), filePath+"/"+fileName);

        return filePath;
    }

    /**
     * 当前申报日期
     *
     * @param orgId 企业ID
     * @return
     */
    public static String downloadTaxReportPDF(Long orgId) {
        Integer year = DateUtil.thisYear();

        String fromDate = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String toDate = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        //常量
        String yzpzzlDm = "BDA0610606";

        return downloadTaxReportPDF(orgId, year, fromDate, toDate, yzpzzlDm);
    }


    /**
     * 推送数据苹果上海 vat
     *
     * @param orgId     企业ID
     * @param filepath  所推Excel的文件路径
     * @param sheetName Excel sheet name
     */
    public static void writeValueAddedTaxDataGeneralForAppleSH(Long orgId, String filepath, String sheetName) {
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);
        //读取上传报文格式
        File file = FileUtil.file(ClassUtil.getClassPath() + "taxJson/general_taxpayer.json");
        JSONObject jsonObject = JSONUtil.readJSONObject(file, Charset.defaultCharset());
        //申报参数，日期类型等
        JSONObject taxParam = jsonObject.getJSONObject("taxParam");
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610606"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqq", skssqq);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqz", skssqz);
        //常量 一般纳税人
        taxParam.put("yzpzzlDm", "BDA0610606");

        //数据
        JSONObject ybData = jsonObject.getJSONObject("ybData");

        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(filepath);
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);

        TaxDataAppleSHInfo taxDataAppleSHInfo = ExcelExtractUtil.cellValByMonthForSH(sheet, thisMonth == 1 ? 12 : thisMonth - 1);

        // Tax Base Amount
        List<BigDecimal> taxBaseAmountList = taxDataAppleSHInfo.getTaxBaseAmount();
        BigDecimal bv11 = taxBaseAmountList.get(0);
        BigDecimal bv13 = taxBaseAmountList.get(1);
        BigDecimal bv15 = taxBaseAmountList.get(2);
        BigDecimal bv27 = taxBaseAmountList.get(3);
        BigDecimal bv29 = taxBaseAmountList.get(4);
        BigDecimal bv31 = taxBaseAmountList.get(5);
        BigDecimal bv44 = taxBaseAmountList.get(6);
        BigDecimal bv46 = taxBaseAmountList.get(7);
        BigDecimal bv48 = taxBaseAmountList.get(8);
        BigDecimal bv61 = taxBaseAmountList.get(9);
        BigDecimal bv64 = taxBaseAmountList.get(10);
        BigDecimal bv67 = taxBaseAmountList.get(11);
        BigDecimal bv74 = taxBaseAmountList.get(12);
        BigDecimal bv88 = taxBaseAmountList.get(13);
        BigDecimal bv91 = taxBaseAmountList.get(14);
        BigDecimal bv94 = taxBaseAmountList.get(15);

        // Tax Amount
        List<BigDecimal> taxAmountList = taxDataAppleSHInfo.getTaxAmount();
        BigDecimal bx11 = taxAmountList.get(0);
        BigDecimal bx13 = taxAmountList.get(1);
        BigDecimal bx15 = taxAmountList.get(2);
        BigDecimal bx27 = taxAmountList.get(3);
        BigDecimal bx29 = taxAmountList.get(4);
        BigDecimal bx31 = taxAmountList.get(5);
        BigDecimal bx44 = taxAmountList.get(6);
        BigDecimal bx46 = taxAmountList.get(7);
        BigDecimal bx48 = taxAmountList.get(8);
        BigDecimal bx61 = taxAmountList.get(9);
        BigDecimal bx64 = taxAmountList.get(10);
        BigDecimal bx67 = taxAmountList.get(11);
        BigDecimal bx74 = taxAmountList.get(12);
        BigDecimal bx88 = taxAmountList.get(13);
        BigDecimal bx91 = taxAmountList.get(14);
        BigDecimal bx94 = taxAmountList.get(15);
        BigDecimal bx101 = taxAmountList.get(16);
        BigDecimal bx102 = taxAmountList.get(17);
        BigDecimal bx103 = taxAmountList.get(18);
        BigDecimal bx104 = taxAmountList.get(19);
        BigDecimal bx111 = taxAmountList.get(20);

        //qty
        List<BigDecimal> qtyList = taxDataAppleSHInfo.getQty();
        BigDecimal bt74 = qtyList.get(0);
        BigDecimal bt88 = qtyList.get(1);
        BigDecimal bt91 = qtyList.get(2);
        BigDecimal bt94 = qtyList.get(3);

        //主表 ----------------------
        //        主表
        JSONObject zzssyyybnsrzb = ybData.getJSONObject("zzssyyybnsrzb");
        JSONObject zbGrid = zzssyyybnsrzb.getJSONObject("zbGrid");
        JSONArray zbGridlbVO = zbGrid.getJSONArray("zbGridlbVO");
        JSONObject zbGridlbVO0 = zbGridlbVO.getJSONObject(0);
//        BV11+BV27+BV44+ BV13+BV29+BV46+BV15+BV31+BV48+BV61+BV64+BV67  json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘asysljsxse’]
        BigDecimal asysljsxse = bv11.add(bv27).add(bv44).add(bv13).add(bv29).add(bv46).add(bv15).add(bv31).add(bv48).add(bv61).add(bv64).add(bv67);
        zbGridlbVO0.put("asysljsxse", asysljsxse.toPlainString());

//        BV11+BV27+BV44+ BV13+BV29+BV46+BV15+BV31+BV48   json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘yshwxse’]
        BigDecimal yshwxse = bv11.add(bv27).add(bv44).add(bv13).add(bv29).add(bv46).add(bv15).add(bv31).add(bv48);
        zbGridlbVO0.put("yshwxse", yshwxse.toPlainString());

//        BV61+BV64+BV67 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘yslwxse’]
        BigDecimal yslwxse = bv61.add(bv64).add(bv67);
        zbGridlbVO0.put("yslwxse", yslwxse.toPlainString());

//        BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48+BX61+BX64+BX67  json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘xxse’]
        BigDecimal xxse = bx11.add(bx27).add(bx44).add(bx13).add(bx29).add(bx46).add(bx15).add(bx31).add(bx48).add(bx61).add(bx64).add(bx67);
        zbGridlbVO0.put("xxse", xxse.toPlainString());

//        BX74+BX88+BX91+BX94 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘jxse’]
        BigDecimal jxse = bx74.add(bx88).add(bx91).add(bx94);
        zbGridlbVO0.put("jxse", jxse.toPlainString());

//        (-1) * BX111   json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][’sqldse’]
        BigDecimal sqldse = new BigDecimal("-1").multiply(bx111);
        zbGridlbVO0.put("sqldse", sqldse.toPlainString());

//        BX101+BX102+BX103+BX104 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][’jxsezc’]
        BigDecimal jxsezc = new BigDecimal("-1").multiply(bx101.add(bx102).add(bx103).add(bx104));
        zbGridlbVO0.put("jxsezc", jxsezc.toPlainString());

//        应抵扣税额合计			17=12+13-14-15+16					 8,485,560,653.49
//                                  jxse+sqldse-jxsezc-
//        BX74+BX88+BX91+BX94+BX111 - (BX101+BX102+BX103+BX104)  json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['ydksehj’]
//        BigDecimal ydksehj = bx74.add(bx88).add(bx91).add(bx94).add(bx111).subtract(jxsezc);
        BigDecimal ydksehj = jxse.add(sqldse).subtract(jxsezc);
        zbGridlbVO0.put("ydksehj", ydksehj.toPlainString());

//        json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['sjdkse’]
//                                 ydksehj                             <                          xxse
//        "if ((BX74+BX88+BX91+BX94+BX111 - (BX101+BX102+BX103+BX104)) < (BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48+BX61+BX64+BX67)
//        {       return (BX74+BX88+BX91+BX94+BX111 - (BX101+BX102+BX103+BX104))      }
//          else
//        {       return (BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48+BX61+BX64+BX67)      }"
        BigDecimal sjdkse = ydksehj.compareTo(xxse) < 0 ? ydksehj : xxse;
        zbGridlbVO0.put("sjdkse", sjdkse.toPlainString());

//        A9 - A10的返回值 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['qmldse’]
//        期末留抵税额			20=17-18					 4,509,538,959.23
//                              ydksehj-sjdkse
//        BigDecimal qmldse = a9.subtract(a10);
        BigDecimal qmldse = ydksehj.subtract(sjdkse);
        zbGridlbVO0.put("qmldse", qmldse.toPlainString());

        //附表1   ------------------------------------------------
        JSONObject bqxsqkmxbGridlbVO0 = vatSchedule1(ybData, 0);
//        BV11+BV27+BV44 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjskzzszyfpXse’]
        BigDecimal kjskzzszyfpXse = bv11.add(bv27).add(bv44);
        bqxsqkmxbGridlbVO0.put("kjskzzszyfpXse", kjskzzszyfpXse.toPlainString());

//        BX11+BX27+BX44  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjskzzszyfpXxynse’]
        BigDecimal kjskzzszyfpXxynse = bx11.add(bx27).add(bx44);
        bqxsqkmxbGridlbVO0.put("kjskzzszyfpXxynse", kjskzzszyfpXxynse.toPlainString());

//        BV13+BV29+BV46 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][’kjqtfpXse’]
        BigDecimal kjqtfpXse = bv13.add(bv29).add(bv46);
        bqxsqkmxbGridlbVO0.put("kjqtfpXse", kjqtfpXse.toPlainString());

//        BX13+BX29+BX46  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjqtfpXxynse’]
        BigDecimal kjqtfpXxynse = bx13.add(bx29).add(bx46);
        bqxsqkmxbGridlbVO0.put("kjqtfpXxynse", kjqtfpXxynse.toPlainString());

//        BV15+BV31+BV48  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][’wkjfpXse’]
        BigDecimal wkjfpXse = bv15.add(bv31).add(bv48);
        bqxsqkmxbGridlbVO0.put("wkjfpXse", wkjfpXse.toPlainString());

//        BX15+BX31+BX48  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘wkjfpXxynse’]
        BigDecimal wkjfpXxynse = bx15.add(bx31).add(bx48);
        bqxsqkmxbGridlbVO0.put("wkjfpXxynse", wkjfpXxynse.toPlainString());

//   32146643777.33     BV11+BV27+BV44+ BV13+BV29+BV46+BV15+BV31+BV48  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘xse’]
//        9=1+3+5+7   kjskzzszyfpXse+kjskzzszyfpXxynse+kjqtfpXse  36326160907.33
//        BigDecimal xse = bv11.add(bv27).add(bv44);
        BigDecimal xse = kjskzzszyfpXse.add(wkjfpXse).add(kjqtfpXse);
        bqxsqkmxbGridlbVO0.put("xse", xse.toPlainString());

//        BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0]['hjXxynse']
        BigDecimal hjXxynse = bx11.add(bx27).add(bx44).add(bx13).add(bx29).add(bx46).add(bx15).add(bx31).add(bx48);
        bqxsqkmxbGridlbVO0.put("hjXxynse", hjXxynse.toPlainString());

//        BV11+BV27+BV44+ BV13+BV29+BV46+BV15+BV31+BV48+BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0]['jshj']
        BigDecimal jshj = yshwxse.add(hjXxynse);
        bqxsqkmxbGridlbVO0.put("jshj", jshj.toPlainString());

        /*--------bqxsqkmxbGridlbVO5-------*/
        JSONObject bqxsqkmxbGridlbVO5 = vatSchedule1(ybData, 5);
//        BV61  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjskzzszyfpXse’]
        BigDecimal kjskzzszyfpXse5 = bv61;
        bqxsqkmxbGridlbVO5.put("kjskzzszyfpXse", kjskzzszyfpXse5.toPlainString());

//        BX61  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjskzzszyfpXxynse’]
        BigDecimal kjskzzszyfpXxynse5 = bx61;
        bqxsqkmxbGridlbVO5.put("kjskzzszyfpXxynse", kjskzzszyfpXxynse5.toPlainString());

//        BV64  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][’kjqtfpXse’]
        BigDecimal kjqtfpXse5 = bv64;
        bqxsqkmxbGridlbVO5.put("kjqtfpXse", kjqtfpXse5.toPlainString());

//        BX64  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjqtfpXxynse’]
        BigDecimal kjqtfpXxynse5 = bx64;
        bqxsqkmxbGridlbVO5.put("kjqtfpXxynse", kjqtfpXxynse5.toPlainString());

//        BV67 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][’wkjfpXse’]
        BigDecimal wkjfpXse5 = bv67;
        bqxsqkmxbGridlbVO5.put("wkjfpXse", wkjfpXse5.toPlainString());

//        BX67  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘wkjfpXxynse’]
        BigDecimal wkjfpXxynse5 = bx67;
        bqxsqkmxbGridlbVO5.put("wkjfpXxynse", wkjfpXxynse5.toPlainString());

//        BV61+BV64+BV67   json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘xse’]
        BigDecimal xse5 = bv61.add(bv64).add(bv67);
        bqxsqkmxbGridlbVO5.put("xse", xse5.toPlainString());

//        BX61+BX64+BX67  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['hjXxynse']
        BigDecimal hjXxynse5 = bx61.add(bx64).add(bx67);
        bqxsqkmxbGridlbVO5.put("hjXxynse", hjXxynse5.toPlainString());

//        BV61+BV64+BV67+BX61+BX64+BX67  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['jshj']
        BigDecimal jshj5 = bv61.add(bv64).add(bv67).add(bx61).add(bx64).add(bx67);
        bqxsqkmxbGridlbVO5.put("jshj", jshj5.toPlainString());

//        BV61+BV64+BV67+BX61+BX64+BX67 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['kchHsmsxse']
        BigDecimal kchHsmsxse = jshj5;
        bqxsqkmxbGridlbVO5.put("kchHsmsxse", kchHsmsxse.toPlainString());

//        (BV61+BV64+BV67+BX61+BX64+BX67)/1.06*0.06  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['kchXxynse']
        BigDecimal kchXxynse = kchHsmsxse.divide(new BigDecimal("1.06"), 2).multiply(new BigDecimal("0.06"));
        bqxsqkmxbGridlbVO5.put("kchXxynse", kchXxynse.toPlainString());


        //附表 2 ------------------------------------
        /*-------------bqjxsemxbGridlbVO 1---------------*/
        JSONObject bqjxsemxbGridlbVO1 = vatSchedule2(ybData, 1);
//        BT74 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][1][‘fs’]
        Integer fs = bt74.intValue();
//       BV74 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][1][‘je’]
        String je = bv74.toPlainString();
//        BX74 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][1][‘se’]
        String se = bx74.toPlainString();
        vatSchedule2PutVal(bqjxsemxbGridlbVO1, fs, se, je);

        /*-------------bqjxsemxbGridlbVO 0 ---------------*/
        JSONObject bqjxsemxbGridlbVO0 = vatSchedule2(ybData, 0);
//      BT74  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][0][‘fs’]
        Integer fs0 = bt74.intValue();
//        BV74  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][0][‘je’]
        BigDecimal je0 = bv74;
//      BX74  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][0][‘se’]
        String se0 = bt74.toPlainString();
        vatSchedule2PutVal(bqjxsemxbGridlbVO0, fs0, se0, je0.toPlainString());

        /*-------------bqjxsemxbGridlbVO 4 ---------------*/
        JSONObject bqjxsemxbGridlbVO4 = vatSchedule2(ybData, 4);
//        BT88 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][4][‘fs’]
        BigDecimal fs4 = bt88;
//      BV88  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][4][‘je’]
        BigDecimal je4 = bv88;
//        BX88 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][4][‘se’]
        String se4 = bx88.toPlainString();
        vatSchedule2PutVal(bqjxsemxbGridlbVO4, fs4.intValue(), se4, je4.toPlainString());

        /*-------------bqjxsemxbGridlbVO 6 ---------------*/
        JSONObject bqjxsemxbGridlbVO6 = vatSchedule2(ybData, 6);
//      BT91  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][6][‘fs’]
        BigDecimal fs6 = bt91;
//      BV91  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][6][‘je’]
//        BigDecimal je6 = bv91;
//        BX91 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][6][‘se’]
        String se6 = bx91.toPlainString();
        vatSchedule2PutVal(bqjxsemxbGridlbVO6, fs6.intValue(), se6, null);

        /*-------------bqjxsemxbGridlbVO 8 ---------------*/
        JSONObject bqjxsemxbGridlbVO8 = vatSchedule2(ybData, 8);
//       BT94 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][8][‘fs’]
        BigDecimal fs8 = bt94;
//        BV94 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][8][‘je’]
        BigDecimal je8 = bv94;
//      BX94  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][8][‘se’]
        String se8 = bx94.toPlainString();
        vatSchedule2PutVal(bqjxsemxbGridlbVO8, fs8.intValue(), se8, je8.toPlainString());

        /*-------------bqjxsemxbGridlbVO 10 ---------------*/
        JSONObject bqjxsemxbGridlbVO10 = vatSchedule2(ybData, 10);
//        BT94  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][10][‘fs’]
        Integer fs10 = bt94.intValue();
//        BV94	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][10][‘je’]
        String je10 = bv94.toPlainString();
//        BX94	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][10][‘se’]
        String se10 = bx94.toPlainString();
        vatSchedule2PutVal(bqjxsemxbGridlbVO10, fs10, se10, je10);

        /*-------------bqjxsemxbGridlbVO 3 ---------------*/
        JSONObject bqjxsemxbGridlbVO3 = vatSchedule2(ybData, 3);
//        BT88+BT91+BT94	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][3][‘fs’]	必须转换成int类型
//        4=5+6+7+8a+8b je4 + je6 + je8
//        BigDecimal fs3 = bt88.add(bt91).add(bt94);
        BigDecimal fs3 = fs4.add(fs6).add(fs8);
//        BV88+BV94	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][3][‘je’]
        BigDecimal je3 = bv88.add(bv94);
//        BX88+BX91+BX94	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][3][‘se’]
        BigDecimal se3 = bx88.add(bx91).add(bx94);
        vatSchedule2PutVal(bqjxsemxbGridlbVO3, fs3.intValue(), se3.toPlainString(), je3.toPlainString());

        /*-------------bqjxsemxbGridlbVO 12 ---------------*/
        JSONObject bqjxsemxbGridlbVO12 = vatSchedule2(ybData, 12);
//        BT74+BT88+BT91+BT94	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][12][‘fs’]	必须转换成int类型
        BigDecimal fs12 = bt74.add(bt88).add(bt91).add(bt94);
//        BV74+BV88+BV91+BV94	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][12][‘je’]
//        12=1+4+11    je0 + je3 + je 10
//        BigDecimal je12 = bv74.add(bv88).add(bv91).add(bv94);
        BigDecimal je12 = je0.add(je3);
//        BX74+BX88+BX91+BX94	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][12][‘se’]
        BigDecimal se12 = bx74.add(bx88).add(bx91).add(bx94);
        vatSchedule2PutVal(bqjxsemxbGridlbVO12, fs12.intValue(), se12.toPlainString(), je12.toPlainString());

        /*-------------bqjxsemxbGridlbVO 15 ---------------*/
        JSONObject bqjxsemxbGridlbVO15 = vatSchedule2(ybData, 15);
//        BX103	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][15][‘se’]
        BigDecimal se15 = new BigDecimal("-1").multiply(bx103);
        vatSchedule2PutVal(bqjxsemxbGridlbVO15, null, se15.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 16 ---------------*/
        JSONObject bqjxsemxbGridlbVO16 = vatSchedule2(ybData, 16);
//        BX102	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][16][‘se’]
        BigDecimal se16 = new BigDecimal("-1").multiply(bx102);
        vatSchedule2PutVal(bqjxsemxbGridlbVO16, null, se16.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 20 ---------------*/
        JSONObject bqjxsemxbGridlbVO20 = vatSchedule2(ybData, 20);
//        BX101	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][20][‘se’]
        BigDecimal se20 = new BigDecimal("-1").multiply(bx101);
        vatSchedule2PutVal(bqjxsemxbGridlbVO20, null, se20.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 22 ---------------*/
        JSONObject bqjxsemxbGridlbVO22 = vatSchedule2(ybData, 22);
//        BX104	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][22][‘se’]
        BigDecimal se22 = new BigDecimal("-1").multiply(bx104);
        vatSchedule2PutVal(bqjxsemxbGridlbVO22, null, se22.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 13 ---------------*/
        JSONObject bqjxsemxbGridlbVO13 = vatSchedule2(ybData, 13);
//        BX101+BX102+BX103+BX104	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][13][‘se’]
        BigDecimal se13 = new BigDecimal("-1").multiply(bx101.add(bx102).add(bx103).add(bx104));
        vatSchedule2PutVal(bqjxsemxbGridlbVO13, null, se13.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 35 ---------------*/
        JSONObject bqjxsemxbGridlbVO35 = vatSchedule2(ybData, 35);
//        BT74	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][35][‘fs’]	必须转换成int类型
        Integer fs35 = bt74.intValue();
//        BV74	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][35][‘je’]
        String je35 = bv74.toPlainString();
//        BX74	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][35][‘se’]
        String se35 = bx74.toPlainString();
        vatSchedule2PutVal(bqjxsemxbGridlbVO35, fs35, se35, je35);

        /*-------------bqjxsemxbGridlbVO 36 ---------------*/
        JSONObject bqjxsemxbGridlbVO36 = vatSchedule2(ybData, 36);
//        BX91	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][36][‘se’]
        String se36 = bx91.toPlainString();
        vatSchedule2PutVal(bqjxsemxbGridlbVO36, null, se36, null);


//        附表3 ----------------------------------
        /*--------------ysfwkcxmmxGridlbVO 2 ----------------*/
        JSONObject ysfwkcxmmxGridlbVO2 = vatSchedule3(ybData, 2);
//        BV61+BV64+BV67+BX61+BX64+BX67	json[‘ybData’][‘zzssyyybnsr03ysfwkcxmmx’][‘ysfwkcxmmxGrid’][‘ysfwkcxmmxGridlbVO’][2][‘msxse’]
        BigDecimal msxse = bv61.add(bv64).add(bv67).add(bx61).add(bx64).add(bx67);
        ysfwkcxmmxGridlbVO2.put("msxse", msxse.toPlainString());

        //TODO：是否北京地区判断
        ybData.remove("qtkspzmxb");
        System.out.println(jsonObject.toString());
        req.put("body", jsonObject);
        com.alibaba.fastjson.JSONObject jsonObject1 = ttkOpenAPI.writeValueAddedTaxData(req.toString());
        System.out.println(jsonObject1.toJSONString());
    }

    /**
     * 苹果北京 vat
     *
     * @param orgId
     * @param filepath
     * @param sheetName
     */
    public static void writeValueAddedTaxDataGeneralForAppleBJ(Long orgId, String filepath, String sheetName) {
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);
        //读取上传报文格式
        File file = FileUtil.file(ClassUtil.getClassPath() + "taxJson/general_taxpayer.json");
//        File file = FileUtil.file("/Users/zk/work/shinetech/pwc/filing-platform/pwc-filing/src/main/resources/taxJson/general_taxpayer.json");
        JSONObject jsonObject = JSONUtil.readJSONObject(file, Charset.defaultCharset());
        //申报参数，日期类型等
        JSONObject taxParam = jsonObject.getJSONObject("taxParam");
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610606"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqq", skssqq);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqz", skssqz);
        //常量 一般纳税人
        taxParam.put("yzpzzlDm", "BDA0610606");

        //数据
        JSONObject ybData = jsonObject.getJSONObject("ybData");

        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(filepath);
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);

        TaxDataAppleBJInfo taxDataAppleBJInfo = ExcelExtractUtil.cellValByMonth(sheet, thisMonth == 1 ? 12 : thisMonth - 1);

        // Tax Base Amount
        List<BigDecimal> taxBaseAmountList = taxDataAppleBJInfo.getTaxBaseAmount();
        BigDecimal bw10 = taxBaseAmountList.get(0);
        BigDecimal bw12 = taxBaseAmountList.get(1);
        BigDecimal bw14 = taxBaseAmountList.get(2);
        BigDecimal bw18 = taxBaseAmountList.get(3);
        BigDecimal bw20 = taxBaseAmountList.get(4);
        BigDecimal bw22 = taxBaseAmountList.get(5);
        BigDecimal bw29 = taxBaseAmountList.get(6);
        BigDecimal bw31 = taxBaseAmountList.get(7);
        BigDecimal bw32 = taxBaseAmountList.get(8);
        BigDecimal bw34 = taxBaseAmountList.get(9);


        // Tax Amount
        List<BigDecimal> taxAmountList = taxDataAppleBJInfo.getTaxAmount();
        BigDecimal by10 = taxAmountList.get(0);
        BigDecimal by12 = taxAmountList.get(1);
        BigDecimal by14 = taxAmountList.get(2);
        BigDecimal by18 = taxAmountList.get(3);
        BigDecimal by20 = taxAmountList.get(4);
        BigDecimal by22 = taxAmountList.get(5);
        BigDecimal by29 = taxAmountList.get(6);
        BigDecimal by31 = taxAmountList.get(7);
        BigDecimal by32 = taxAmountList.get(8);
        BigDecimal by34 = taxAmountList.get(9);
        BigDecimal by42 = taxAmountList.get(10);
        BigDecimal by44 = taxAmountList.get(11);
        BigDecimal by51 = taxAmountList.get(12);


        // Qty
        List<BigDecimal> qtyList = taxDataAppleBJInfo.getQty();
        BigDecimal bu29 = qtyList.get(0);
        BigDecimal bu31 = qtyList.get(1);
        BigDecimal bu32 = qtyList.get(2);
        BigDecimal bu34 = qtyList.get(3);


        //主表 ----------------------
        JSONObject zbGridlbVO0 = vatMasterTable(ybData, 0);
//        BW10+BW12+BW14+BW18+BW20+BW22    json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘asysljsxse’]
        BigDecimal asysljsxse = bw10.add(bw12).add(bw14).add(bw18).add(bw20).add(bw22);
        zbGridlbVO0.put("asysljsxse", asysljsxse.toPlainString());

//      BW10+BW12+BW14   json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘yshwxse’]
        BigDecimal yshwxse = bw10.add(bw12).add(bw14);
        zbGridlbVO0.put("yshwxse", yshwxse.toPlainString());

//        BW18+BW20+BW22 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘yslwxse’]
        BigDecimal yslwxse = bw18.add(bw20).add(bw22);
        zbGridlbVO0.put("yslwxse", yslwxse.toPlainString());

//        BY10+BY12+BY14  json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘xxse’]
        BigDecimal xxse = by10.add(by12).add(by14);
        zbGridlbVO0.put("xxse", xxse.toPlainString());

//        BY34+BY32+BY31 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘jxse’]
        BigDecimal jxse = by34.add(by32).add(by31);
        zbGridlbVO0.put("jxse", jxse.toPlainString());

//        (-1) * BY51   json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][’sqldse’]
        BigDecimal sqldse = new BigDecimal("-1").multiply(by51);
        zbGridlbVO0.put("sqldse", sqldse.toPlainString());

//        (-1) * (BY42 + BY44) json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][’jxsezc’]
        BigDecimal jxsezc = new BigDecimal("-1").multiply(by42.add(by44));
        zbGridlbVO0.put("jxsezc", jxsezc.toPlainString());

//        应抵扣税额合计			17=12+13-14-15+16					 8,485,560,653.49
//                                  jxse+sqldse-jxsezc-
//        BY34+BY32+BY31+(-1) * BY51-(-1) * (BY42 + BY44)  json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['ydksehj’]
//        BigDecimal ydksehj = by34.add(by32).add(by31).add(sqldse).subtract(jxsezc);
        BigDecimal ydksehj = jxse.add(sqldse).subtract(jxsezc);
        zbGridlbVO0.put("ydksehj", ydksehj.toPlainString());

//        json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['sjdkse’]
//                                 ydksehj                             <                          xxse
        BigDecimal sjdkse = ydksehj.compareTo(xxse) < 0 ? ydksehj : xxse;
        zbGridlbVO0.put("sjdkse", sjdkse.toPlainString());

//         json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['qmldse’]
//        期末留抵税额			20=17-18					 4,509,538,959.23
//                              ydksehj-sjdkse
        BigDecimal qmldse = ydksehj.subtract(sjdkse);
        zbGridlbVO0.put("qmldse", qmldse.toPlainString());

        //附表1   ------------------------------------------------
        JSONObject bqxsqkmxbGridlbVO0 = vatSchedule1(ybData, 0);
//        BW10 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjskzzszyfpXse’]
        BigDecimal kjskzzszyfpXse = bw10;
        bqxsqkmxbGridlbVO0.put("kjskzzszyfpXse", kjskzzszyfpXse.toPlainString());

//        BY10  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjskzzszyfpXxynse’]
        BigDecimal kjskzzszyfpXxynse = by10;
        bqxsqkmxbGridlbVO0.put("kjskzzszyfpXxynse", kjskzzszyfpXxynse.toPlainString());

//        BW12 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][’kjqtfpXse’]
        BigDecimal kjqtfpXse = bw12;
        bqxsqkmxbGridlbVO0.put("kjqtfpXse", kjqtfpXse.toPlainString());

//        BY12  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjqtfpXxynse’]
        BigDecimal kjqtfpXxynse = by12;
        bqxsqkmxbGridlbVO0.put("kjqtfpXxynse", kjqtfpXxynse.toPlainString());

//        BW14  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][’wkjfpXse’]
        BigDecimal wkjfpXse = bw14;
        bqxsqkmxbGridlbVO0.put("wkjfpXse", wkjfpXse.toPlainString());

//        BY14  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘wkjfpXxynse’]
        BigDecimal wkjfpXxynse = by14;
        bqxsqkmxbGridlbVO0.put("wkjfpXxynse", wkjfpXxynse.toPlainString());

//      BW10+BW12+BW14  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘xse’]
//        9=1+3+5+7   kjskzzszyfpXse+kjskzzszyfpXxynse+kjqtfpXse  36326160907.33
        BigDecimal xse = kjskzzszyfpXse.add(wkjfpXse).add(kjqtfpXse);
        bqxsqkmxbGridlbVO0.put("xse", xse.toPlainString());

//        BY10+BY12+BY14  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0]['hjXxynse']
        BigDecimal hjXxynse = by10.add(by12).add(by14);
        bqxsqkmxbGridlbVO0.put("hjXxynse", hjXxynse.toPlainString());

//       BW10+BW12+BW14+BY10+BY12+BY14 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0]['jshj']
        BigDecimal jshj = yshwxse.add(hjXxynse);
        bqxsqkmxbGridlbVO0.put("jshj", jshj.toPlainString());

        /*--------bqxsqkmxbGridlbVO5-------*/
        JSONObject bqxsqkmxbGridlbVO5 = vatSchedule1(ybData, 5);
//        BW18  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjskzzszyfpXse’]
        BigDecimal kjskzzszyfpXse5 = bw18;
        bqxsqkmxbGridlbVO5.put("kjskzzszyfpXse", kjskzzszyfpXse5.toPlainString());

//        BY18  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjskzzszyfpXxynse’]
        BigDecimal kjskzzszyfpXxynse5 = by18;
        bqxsqkmxbGridlbVO5.put("kjskzzszyfpXxynse", kjskzzszyfpXxynse5.toPlainString());

//        BW20  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][’kjqtfpXse’]
        BigDecimal kjqtfpXse5 = bw20;
        bqxsqkmxbGridlbVO5.put("kjqtfpXse", kjqtfpXse5.toPlainString());

//        BY20  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjqtfpXxynse’]
        BigDecimal kjqtfpXxynse5 = by20;
        bqxsqkmxbGridlbVO5.put("kjqtfpXxynse", kjqtfpXxynse5.toPlainString());

//        BW22 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][’wkjfpXse’]
        BigDecimal wkjfpXse5 = bw22;
        bqxsqkmxbGridlbVO5.put("wkjfpXse", wkjfpXse5.toPlainString());

//        BY22  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘wkjfpXxynse’]
        BigDecimal wkjfpXxynse5 = by22;
        bqxsqkmxbGridlbVO5.put("wkjfpXxynse", wkjfpXxynse5.toPlainString());

//        BW18+BW20+BW22   json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘xse’]
        BigDecimal xse5 = bw18.add(bw20).add(bw22);
        bqxsqkmxbGridlbVO5.put("xse", xse5.toPlainString());

//        BY18+BY20+BY22  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['hjXxynse']
        BigDecimal hjXxynse5 = by18.add(by20).add(by22);
        bqxsqkmxbGridlbVO5.put("hjXxynse", hjXxynse5.toPlainString());

//        BW18+BW20+BW22+BY18+BY20+BY22  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['jshj']
        BigDecimal jshj5 = bw18.add(bw20).add(bw22).add(by18).add(by20).add(by22);
        bqxsqkmxbGridlbVO5.put("jshj", jshj5.toPlainString());

//        BW18+BW20+BW22+BY18+BY20+BY22 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['kchHsmsxse']
        BigDecimal kchHsmsxse = jshj5;
        bqxsqkmxbGridlbVO5.put("kchHsmsxse", kchHsmsxse.toPlainString());

//        (BW18+BW20+BW22+BY18+BY20+BY22)/1.06*0.06  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['kchXxynse']
        BigDecimal kchXxynse = kchHsmsxse.divide(new BigDecimal("1.06"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal("0.06"));
        bqxsqkmxbGridlbVO5.put("kchXxynse", kchXxynse.toPlainString());


        //附表 2 ------------------------------------
        /*-------------bqjxsemxbGridlbVO 1---------------*/
        JSONObject bqjxsemxbGridlbVO1 = vatSchedule2(ybData, 1);
//        BU29 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][1][‘fs’]
        Integer fs = bu29.intValue();
//       BW29 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][1][‘je’]
        BigDecimal je = bw29;
//        BY29 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][1][‘se’]
        BigDecimal se = by29;
        vatSchedule2PutVal(bqjxsemxbGridlbVO1, fs, se.toPlainString(), je.toPlainString());

        /*-------------bqjxsemxbGridlbVO 0 ---------------*/
        JSONObject bqjxsemxbGridlbVO0 = vatSchedule2(ybData, 0);
//      BU29  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][0][‘fs’]
        BigDecimal fs0 = bu29;
//        BW29  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][0][‘je’]
        BigDecimal je0 = bw29;
//      BY29  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][0][‘se’]
        BigDecimal se0 = by29;
        vatSchedule2PutVal(bqjxsemxbGridlbVO0, fs0.intValue(), se0.toPlainString(), je0.toPlainString());

        /*-------------bqjxsemxbGridlbVO 4 ---------------*/
        JSONObject bqjxsemxbGridlbVO4 = vatSchedule2(ybData, 4);
//        BU34 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][4][‘fs’]
        BigDecimal fs4 = bu34;
//      BW34  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][4][‘je’]
        BigDecimal je4 = bw34;
//        BY34 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][4][‘se’]
        BigDecimal se4 = by34;
        vatSchedule2PutVal(bqjxsemxbGridlbVO4, fs4.intValue(), se4.toPlainString(), je4.toPlainString());

        /*-------------bqjxsemxbGridlbVO 6 ---------------*/
        JSONObject bqjxsemxbGridlbVO6 = vatSchedule2(ybData, 6);
//      BU32  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][6][‘fs’]
        BigDecimal fs6 = bu32;
//      BY32  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][6][‘je’]
//        BigDecimal je6 = by32;
//        System.out.println("附表2 je6:" + je6.toPlainString());
//        bqjxsemxbGridlbVO6.put("je", je6.toPlainString());

//        BY32 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][6][‘se’]
        BigDecimal se6 = by32;
        vatSchedule2PutVal(bqjxsemxbGridlbVO6, fs6.intValue(), se6.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 8 ---------------*/
        JSONObject bqjxsemxbGridlbVO8 = vatSchedule2(ybData, 8);
//       BU31 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][8][‘fs’]
        BigDecimal fs8 = bu31;
//        BW31 json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][8][‘je’]
        BigDecimal je8 = bw31;
//      BY31  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][8][‘se’]
        BigDecimal se8 = by31;
        vatSchedule2PutVal(bqjxsemxbGridlbVO8, fs8.intValue(), se8.toPlainString(), je8.toPlainString());

        /*-------------bqjxsemxbGridlbVO 10 ---------------*/
        JSONObject bqjxsemxbGridlbVO10 = vatSchedule2(ybData, 10);
//        BU31  json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][10][‘fs’]
        BigDecimal fs10 = bu31;
//        BW31	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][10][‘je’]
        BigDecimal je10 = bw31;
//        BY31	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][10][‘se’]
        BigDecimal se10 = by31;
        vatSchedule2PutVal(bqjxsemxbGridlbVO10, fs10.intValue(), se10.toPlainString(), je10.toPlainString());

        /*-------------bqjxsemxbGridlbVO 3 ---------------*/
        JSONObject bqjxsemxbGridlbVO3 = vatSchedule2(ybData, 3);
//        BU29+BU34+BU32+BU31	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][3][‘fs’]	必须转换成int类型
//        4=5+6+7+8a+8b je4 + je6 + je8
        BigDecimal fs3 = bu29.add(bu34).add(bu32).add(bu31);
//        BW29+BW34+BW32+BW31	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][3][‘je’]
        BigDecimal je3 = bw29.add(bw34).add(bw32).add(bw31);
//        BY29 + BY34+BY32+BY31	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][3][‘se’]
        BigDecimal se3 = by29.add(by34).add(by32).add(by31);
        vatSchedule2PutVal(bqjxsemxbGridlbVO3, fs3.intValue(), se3.toPlainString(), je3.toPlainString());

        /*-------------bqjxsemxbGridlbVO 12 ---------------*/
        JSONObject bqjxsemxbGridlbVO12 = vatSchedule2(ybData, 12);
//        BU34+BU32+BU31	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][12][‘fs’]	必须转换成int类型
        BigDecimal fs12 = bu34.add(bu32).add(bu31);
//       BW34+BW32+BW31	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][12][‘je’]
//        12=1+4+11    je0 + je3 + je 10
        BigDecimal je12 = bw34.add(bw32).add(bw31);
//        BY34+BY32+BY31	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][12][‘se’]
        BigDecimal se12 = by34.add(by32).add(by31);
        vatSchedule2PutVal(bqjxsemxbGridlbVO12, fs12.intValue(), se12.toPlainString(), je12.toPlainString());

        /*-------------bqjxsemxbGridlbVO 20 ---------------*/
        JSONObject bqjxsemxbGridlbVO20 = vatSchedule2(ybData, 20);
//        (-1) * BY42	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][20][‘se’]
        BigDecimal se20 = new BigDecimal("-1").multiply(by42);
        vatSchedule2PutVal(bqjxsemxbGridlbVO20, null, se20.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 23 ---------------*/
        JSONObject bqjxsemxbGridlbVO23 = vatSchedule2(ybData, 23);
//        (-1) * BY44	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][22][‘se’]
        BigDecimal se23 = new BigDecimal("-1").multiply(by44);
        vatSchedule2PutVal(bqjxsemxbGridlbVO23, null, se23.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 13 ---------------*/
        JSONObject bqjxsemxbGridlbVO13 = vatSchedule2(ybData, 13);
//       (-1) * (BY42 + BY44)	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][13][‘se’]
        BigDecimal se13 = new BigDecimal("-1").multiply(by42.add(by44));
        vatSchedule2PutVal(bqjxsemxbGridlbVO13, null, se13.toPlainString(), null);

        /*-------------bqjxsemxbGridlbVO 35 ---------------*/
        JSONObject bqjxsemxbGridlbVO35 = vatSchedule2(ybData, 35);
//        BU29	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][35][‘fs’]	必须转换成int类型
        BigDecimal fs35 = bu29;
//        BW29	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][35][‘je’]
        BigDecimal je35 = bw29;
//        BY29	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][35][‘se’]
        BigDecimal se35 = by29;
        vatSchedule2PutVal(bqjxsemxbGridlbVO35, fs35.intValue(), se35.toPlainString(), je35.toPlainString());

        /*-------------bqjxsemxbGridlbVO 36 ---------------*/
        JSONObject bqjxsemxbGridlbVO36 = vatSchedule2(ybData, 36);
//        BY32	json[‘ybData’][‘zzssyyybnsr02bqjxsemxb’][‘bqjxsemxbGrid’][‘bqjxsemxbGridlbVO’][36][‘se’]
        BigDecimal se36 = by32;
        vatSchedule2PutVal(bqjxsemxbGridlbVO36, null, se36.toPlainString(), null);


//        附表3 ----------------------------------
        /*--------------ysfwkcxmmxGridlbVO 2 ----------------*/
        JSONObject ysfwkcxmmxGridlbVO2 = vatSchedule3(ybData, 2);
//        BW18+BW20+BW22+BY18+BY20+BY22	json[‘ybData’][‘zzssyyybnsr03ysfwkcxmmx’][‘ysfwkcxmmxGrid’][‘ysfwkcxmmxGridlbVO’][2][‘msxse’]
        BigDecimal msxse = bw18.add(bw20).add(bw22).add(by18).add(by20).add(by22);
        ysfwkcxmmxGridlbVO2.put("msxse", msxse.toPlainString());

        System.out.println(jsonObject.toString());
        req.put("body", jsonObject);
        com.alibaba.fastjson.JSONObject jsonObject1 = ttkOpenAPI.writeValueAddedTaxData(req.toString());
        System.out.println(jsonObject1.toJSONString());
    }

    /**
     * 苹果上海 SD
     */
    public static void writeValueAddedSdDataForApple(Long orgId, String filepath, String sheetName) {
        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(filepath);
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);

        // 请求的json数据
        JSONObject reqJSON = JSONUtil.createObj();
        reqJSON.put("orgId", orgId);
        //读取上传报文格式
        JSONObject body = JSONUtil.createObj();
        //申报参数，日期类型等
        JSONObject taxParam = JSONUtil.createObj();
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610794"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqq", skssqq);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqz", skssqz);
        //常量 印花税
        taxParam.put("yzpzzlDm", "BDA0610794");
        taxParam.put("nsqxdm", "11");
        body.put("taxParam", taxParam);
        // yhsData
        JSONObject yhsData = JSONUtil.createObj();
        // yhssb
        JSONObject yhssb = JSONUtil.createObj();
        // yhssbGrid
        JSONObject yhssbGrid = JSONUtil.createObj();
        // yhssbGridlb
        JSONArray yhssbGridlb = JSONUtil.createArray();
        for (String agreement : TtkConstants.SD_AGREEMENTS) {
            switch (agreement) {
                case "101110101":
                    BigDecimal f5 = ExcelExtractUtil.cellVal(sheet, 5, "F");
                    BigDecimal f6 = ExcelExtractUtil.cellVal(sheet, 6, "F");
                    BigDecimal jsje = f5.add(f6);
                    yhssbGridlb.put(sdGrid(agreement, jsje.toPlainString()));
                    break;
                case "101110102":
                    BigDecimal f7 = ExcelExtractUtil.cellVal(sheet, 7, "F");
                    yhssbGridlb.put(sdGrid(agreement, f7.toPlainString()));
                    break;
                case "101110103":
                    BigDecimal f8 = ExcelExtractUtil.cellVal(sheet, 8, "F");
                    yhssbGridlb.put(sdGrid(agreement, f8.toPlainString()));
                    break;
                case "101110104":
                    BigDecimal f9 = ExcelExtractUtil.cellVal(sheet, 9, "F");
                    yhssbGridlb.put(sdGrid(agreement, f9.toPlainString()));
                    break;
                case "101110105":
                    BigDecimal f10 = ExcelExtractUtil.cellVal(sheet, 10, "F");
                    yhssbGridlb.put(sdGrid(agreement, f10.toPlainString()));
                    break;
                case "101110106":
                    BigDecimal f11 = ExcelExtractUtil.cellVal(sheet, 11, "F");
                    yhssbGridlb.put(sdGrid(agreement, f11.toPlainString()));
                    break;
                case "101110107":
                    BigDecimal f12 = ExcelExtractUtil.cellVal(sheet, 12, "F");
                    yhssbGridlb.put(sdGrid(agreement, f12.toPlainString()));
                    break;
                case "101110108":
                    BigDecimal f13 = ExcelExtractUtil.cellVal(sheet, 13, "F");
                    yhssbGridlb.put(sdGrid(agreement, f13.toPlainString()));
                    break;
                case "101110109":
                    BigDecimal f14 = ExcelExtractUtil.cellVal(sheet, 14, "F");
                    yhssbGridlb.put(sdGrid(agreement, f14.toPlainString()));
                    break;
                case "101110110":
                    BigDecimal f15 = ExcelExtractUtil.cellVal(sheet, 15, "F");
                    yhssbGridlb.put(sdGrid(agreement, f15.toPlainString()));
                    break;
                case "101110200":
                    BigDecimal f16 = ExcelExtractUtil.cellVal(sheet, 16, "F");
                    yhssbGridlb.put(sdGrid(agreement, f16.toPlainString()));
                    break;
                case "101110500":
                    yhssbGridlb.put(sdGrid(agreement, "0"));
                    break;
                case "101110599":
                    yhssbGridlb.put(sdGrid(agreement, "0"));
                    break;
                case "101110400":
                    BigDecimal f18 = ExcelExtractUtil.cellVal(sheet, 18, "F");
                    yhssbGridlb.put(sdGrid(agreement, f18.toPlainString()));
                    break;
                default:
                    break;
            }
        }
        yhssbGrid.put("yhssbGridlb", yhssbGridlb);
        yhssb.put("yhssbGrid", yhssbGrid);
        yhsData.put("yhssb", yhssb);
        // 是否需要，不需要可以不传
        JSONObject slrxxForm = JSONUtil.createObj();
        // 办税人
        slrxxForm.put("bsr", "");
        // 代理人身份证件类型
        slrxxForm.put("dlrsfzjzlDm1", "");
        // 代理人
        slrxxForm.put("dlr", "");
        // 受理人
        slrxxForm.put("slr", "");
        // 代理人身份证件号码
        slrxxForm.put("dlrsfzjhm1", "");
//        yhsData.put("slrxxForm", slrxxForm);
        body.put("yhsData", yhsData);
        reqJSON.put("body", body);
        System.out.println(reqJSON.toString());
        System.out.println(ttkOpenAPI.writeValueAddedTaxData(reqJSON.toString()).toJSONString());
    }

//-----------------------------------------------------------------private method

    /**
     * 印花税 数据组成
     *
     * @param zspmDm
     * @param jsje
     * @return
     */
    private static JSONObject sdGrid(String zspmDm, String jsje) {
        JSONObject jsonObject = JSONUtil.createObj();
        jsonObject.put("ssjmxzDm", "");
        jsonObject.put("jsje", jsje);
        jsonObject.put("hdzsHdde", "0");
        jsonObject.put("jmse", "0");
        jsonObject.put("hdzsHdbl", "0");
        jsonObject.put("bqyjse1", "0");
        jsonObject.put("zspmDm", zspmDm);
        return jsonObject;

    }

    /**
     * vat 主表数据
     *
     * @param ybData
     */
    private static JSONObject vatMasterTable(JSONObject ybData, int key) {
        JSONObject zzssyyybnsrzb = ybData.getJSONObject("zzssyyybnsrzb");
        JSONObject zbGrid = zzssyyybnsrzb.getJSONObject("zbGrid");
        JSONArray zbGridlbVO = zbGrid.getJSONArray("zbGridlbVO");
        return zbGridlbVO.getJSONObject(key);
    }

    /**
     * vat 附表1
     *
     * @param ybData
     * @param key
     * @return
     */
    private static JSONObject vatSchedule1(JSONObject ybData, int key) {
        JSONObject zzssyyybnsr01bqxsqkmxb = ybData.getJSONObject("zzssyyybnsr01bqxsqkmxb");
        JSONObject bqxsqkmxbGrid = zzssyyybnsr01bqxsqkmxb.getJSONObject("bqxsqkmxbGrid");
        JSONArray bqxsqkmxbGridlbVOs = bqxsqkmxbGrid.getJSONArray("bqxsqkmxbGridlbVO");

        return bqxsqkmxbGridlbVOs.getJSONObject(key);
    }

    /**
     * vat 附表2
     *
     * @param ybData
     * @param key
     * @return
     */
    private static JSONObject vatSchedule2(JSONObject ybData, int key) {
        JSONObject zzssyyybnsr02bqjxsemxb = ybData.getJSONObject("zzssyyybnsr02bqjxsemxb");
        JSONObject bqjxsemxbGrid = zzssyyybnsr02bqjxsemxb.getJSONObject("bqjxsemxbGrid");
        JSONArray bqjxsemxbGridlbVOs = bqjxsemxbGrid.getJSONArray("bqjxsemxbGridlbVO");
        return bqjxsemxbGridlbVOs.getJSONObject(key);
    }

    /**
     * vat 附表3
     *
     * @param ybData
     * @param key
     * @return
     */
    private static JSONObject vatSchedule3(JSONObject ybData, int key) {
        JSONObject zzssyyybnsr03ysfwkcxmmx = ybData.getJSONObject("zzssyyybnsr03ysfwkcxmmx");
        JSONObject ysfwkcxmmxGrid = zzssyyybnsr03ysfwkcxmmx.getJSONObject("ysfwkcxmmxGrid");
        JSONArray ysfwkcxmmxGridlbVOs = ysfwkcxmmxGrid.getJSONArray("ysfwkcxmmxGridlbVO");
        return ysfwkcxmmxGridlbVOs.getJSONObject(key);
    }

    /**
     * 附表2 添加上传数据
     *
     * @param jsonObject
     * @param fs
     * @param se
     * @param je
     */
    private static void vatSchedule2PutVal(JSONObject jsonObject, Integer fs, String se, String je) {
        if (null != fs) {
            jsonObject.put("fs", fs);
        }
        if (null != je) {
            jsonObject.put("je", je);
        }
        if (null != se) {
            jsonObject.put("se", se);
        }
    }


    /**
     * 转pdf
     *
     * @param base64Content
     * @param filePath
     */
    private static void base64StringToPdf(String base64Content, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            byte[] bytes = Base64.decode(base64Content, Charset.defaultCharset());//base64编码内容转换为字节数组
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 2018企业所得税A类申报
     * 苹果北京
     *
     * @param orgId
     * @param excelPath excel文件的路径
     */
    public static void apply2018CorporateTaxTypeAForAppleBJ(Long orgId, String excelPath) {
        int year = DateUtil.thisYear();
        int thisMonth = DateUtil.thisMonth() + 1;
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        apply2018CorporateTaxTypeAForAppleBJ(orgId, excelPath, "Sheet1"
                , year, thisMonth, thisMonth, thisMonth, skssqq, skssqz);
    }

    /**
     * 2018企业所得税A类申报
     * 苹果北京
     *
     * @param orgId
     * @param excelPath excel文件的路径
     * @param year      int 如2018
     * @param month     int 0...11
     */
    public static void apply2018CorporateTaxTypeAForAppleBJ(Long orgId, String excelPath, int year, int month) {
        apply2018CorporateTaxTypeAForAppleBJ(orgId, excelPath, "Sheet1", year, month);
    }

    /**
     * 2018企业所得税A类申报
     * 苹果北京
     *
     * @param orgId
     * @param excelPath excel文件的路径
     * @param year      int 如2018
     * @param month     int 1...12
     */
    public static void apply2018CorporateTaxTypeAForAppleBJ(Long orgId, String excelPath, String sheetName, int year, int month) {

//        DateTime time = new DateTime();
//        time.setYear(year - 1900);
//        time.setMonth(month - 1);

        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        apply2018CorporateTaxTypeAForAppleBJ(orgId, excelPath, sheetName
                , year, month, month, month, skssqq, skssqz);
    }

    /**
     * 2018企业所得税A类申报
     * 苹果北京
     *
     * @param orgId
     * @param excelPath              excel文件的路径
     * @param sheetName              对应数据的sheet名称
     * @param "year":2018,
     * @param "beginMonth":3,
     * @param "endMonth":3,
     * @param "period":3,
     * @param "skssqq":"2018-02-01",
     * @param "skssqz":"2018-02-28",
     */
    public static void apply2018CorporateTaxTypeAForAppleBJ(Long orgId, String excelPath, String sheetName,
                                                            int year, int beginMonth, int endMonth, int period,
                                                            String skssqq, String skssqz
    ) {

        // 读取2018企业所得税A类申报数据结构
        File taxStructureFile = FileUtil.file(ClassUtil.getClassPath()
                + "taxJson/2018_corporate_tax_type_a_structure.json");
        JSONObject taxStructureJSON = JSONUtil.readJSONObject(taxStructureFile, Charset.defaultCharset());

        // 打开excel并读取对应数据
        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(excelPath);
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);

        BigDecimal L9 = ExcelExtractUtil.cellVal(sheet, 9, "L");
        BigDecimal L10 = ExcelExtractUtil.cellVal(sheet, 10, "L");
        BigDecimal L25 = ExcelExtractUtil.cellVal(sheet, 25, "L");

        // taxStructureJSON["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["yysrLj"] = L9;
        taxStructureJSON.getJSONObject("qysdsAData")
                .getJSONObject("a200000Ywbd")
                .getJSONObject("sbbxxForm")
                .put("yysrLj", L9.toPlainString());

        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["yycbLj"]
        taxStructureJSON.getJSONObject("qysdsAData")
                .getJSONObject("a200000Ywbd")
                .getJSONObject("sbbxxForm")
                .put("yycbLj", L10.toPlainString());

        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["lrzeLj"]
        taxStructureJSON.getJSONObject("qysdsAData")
                .getJSONObject("a200000Ywbd")
                .getJSONObject("sbbxxForm")
                .put("lrzeLj", L25.toPlainString());

        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["sfsyxxwlqy"]
        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["sfkjxzxqy"]
        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["sfgxjsqy"]
        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["sffsjsrgdynssx"]
        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["gjxzhjzhy"]

        // N 值项
        String[] NValueKeys = new String[]{
                "sfsyxxwlqy", "sfkjxzxqy", "sfgxjsqy", "sffsjsrgdynssx", "gjxzhjzhy"
        };
        for (String key : NValueKeys) {
            taxStructureJSON.getJSONObject("qysdsAData")
                    .getJSONObject("a200000Ywbd")
                    .getJSONObject("sbbxxForm")
                    .put(key, "N");
        }

        // Test Keys
        String[] testKeys = new String[]{
                "yysrLj", "yycbLj", "lrzeLj", "sfsyxxwlqy", "sfkjxzxqy", "sfgxjsqy", "sffsjsrgdynssx", "gjxzhjzhy"
        };

        // 输入填入的值，用于测试是否正确
        for (String key : testKeys) {
            System.out.println(key + " | " +
                    taxStructureJSON.getJSONObject("qysdsAData")
                            .getJSONObject("a200000Ywbd")
                            .getJSONObject("sbbxxForm")
                            .getStr(key)
            );
        }


        // 封装提交数据
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);

        String[] taxKeys = new String[]{
                "year", "beginMonth", "endMonth", "period", "skssqq", "skssqz", "yzpzzlDm"
        };

        // 填入 taxParam
        taxStructureJSON.getJSONObject("taxParam").put("year", year);
        taxStructureJSON.getJSONObject("taxParam").put("beginMonth", beginMonth);
        taxStructureJSON.getJSONObject("taxParam").put("endMonth", endMonth);
        taxStructureJSON.getJSONObject("taxParam").put("period", period);
        taxStructureJSON.getJSONObject("taxParam").put("skssqq", skssqq);
        taxStructureJSON.getJSONObject("taxParam").put("skssqz", skssqz);

        // Test TaxParam
        for (String key : taxKeys) {
            System.out.println("taxParam: " + key + " | " +
                    taxStructureJSON.getJSONObject("taxParam")
                            .get(key)
            );
        }

        req.put("body", taxStructureJSON);
        System.out.println(req.toString());
        System.out.println(ttkOpenAPI.writeValueAddedTaxData(req.toString()).toJSONString());
    }

    public static void applyGenericCorporateTaxTypeA(Long orgId, String excelPath, String sheetName,
                                                     int year, int beginMonth, int endMonth, int period,
                                                     String skssqq, String skssqz) {

        // 读取2018企业所得税A类申报数据结构
        File taxStructureFile = FileUtil.file(ClassUtil.getClassPath()
                + "taxJson/2018_corporate_tax_type_a_structure.json");
        JSONObject taxStructureJSON = JSONUtil.readJSONObject(taxStructureFile, Charset.defaultCharset());

        // 打开excel并读取对应数据
        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(excelPath);
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);

        BigDecimal fpbl = ExcelExtractUtil.cellVal(sheet, 3, "CM");
        BigDecimal fzjgfpsdseBq = ExcelExtractUtil.cellVal(sheet, 3, "CN");

        taxStructureJSON.getJSONObject("qysdsAData")
                .getJSONObject("a200000Ywbd")
                .getJSONObject("sbbxxForm")
                .put("fpbl", fpbl.toPlainString());

        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["yycbLj"]
        taxStructureJSON.getJSONObject("qysdsAData")
                .getJSONObject("a200000Ywbd")
                .getJSONObject("sbbxxForm")
                .put("yycbLj", fzjgfpsdseBq.toPlainString());


        // N 值项
        String[] NValueKeys = new String[]{
                "sfsyxxwlqy",
//                "sfkjxzxqy", "sfgxjsqy", "sffsjsrgdynssx", "gjxzhjzhy"
        };
        for (String key : NValueKeys) {
            taxStructureJSON.getJSONObject("qysdsAData")
                    .getJSONObject("a200000Ywbd")
                    .getJSONObject("sbbxxForm")
                    .put(key, "N");
        }

        // Test Keys
        String[] testKeys = new String[]{
                "yysrLj", "yycbLj", "lrzeLj", "sfsyxxwlqy", "sfkjxzxqy", "sfgxjsqy", "sffsjsrgdynssx", "gjxzhjzhy"
        };

        // 输入填入的值，用于测试是否正确
        for (String key : testKeys) {
            System.out.println(key + " | " +
                    taxStructureJSON.getJSONObject("qysdsAData")
                            .getJSONObject("a200000Ywbd")
                            .getJSONObject("sbbxxForm")
                            .getStr(key)
            );
        }


        // 封装提交数据
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);

        String[] taxKeys = new String[]{
                "year", "beginMonth", "endMonth", "period", "skssqq", "skssqz", "yzpzzlDm"
        };

        // 填入 taxParam
        taxStructureJSON.getJSONObject("taxParam").put("year", year);
        taxStructureJSON.getJSONObject("taxParam").put("beginMonth", beginMonth);
        taxStructureJSON.getJSONObject("taxParam").put("endMonth", endMonth);
        taxStructureJSON.getJSONObject("taxParam").put("period", period);
        taxStructureJSON.getJSONObject("taxParam").put("skssqq", skssqq);
        taxStructureJSON.getJSONObject("taxParam").put("skssqz", skssqz);

        // Test TaxParam
        for (String key : taxKeys) {
            System.out.println("taxParam: " + key + " | " +
                    taxStructureJSON.getJSONObject("taxParam")
                            .get(key)
            );
        }

        req.put("body", taxStructureJSON);
        System.out.println(req.toString());
        System.out.println(ttkOpenAPI.writeValueAddedTaxData(req.toString()).toJSONString());
    }

    /**
     * cit 根据名称取值
     * @param orgId
     * @param excelPath
     * @param sheetName
     * @param year
     * @param beginMonth
     * @param endMonth
     * @param period
     * @param skssqq
     * @param skssqz
     * @param filingCit
     */
    public static void applyGenericCorporateTaxByCellName(Long orgId, String excelPath,
                                                     int year, int beginMonth, int endMonth, int period,
                                                     String skssqq, String skssqz, FilingVat filingCit) {

        // 读取2018企业所得税A类申报数据结构
        File taxStructureFile = FileUtil.file(ClassUtil.getClassPath()
                + "taxJson/2018_corporate_tax_type_a_structure.json");
        JSONObject taxStructureJSON = JSONUtil.readJSONObject(taxStructureFile, Charset.defaultCharset());

        taxStructureJSON.getJSONObject("qysdsAData")
                .getJSONObject("a200000Ywbd")
                .getJSONObject("sbbxxForm")
                .put("fpbl",filingCit.getQysdsFtbl());

        // json["qysdsAData"]["a200000Ywbd"]["sbbxxForm"]["yycbLj"]
        taxStructureJSON.getJSONObject("qysdsAData")
                .getJSONObject("a200000Ywbd")
                .getJSONObject("sbbxxForm")
                .put("yycbLj", filingCit.getQysdsBjdftse());
        // N 值项
        String[] NValueKeys = new String[]{
                "sfsyxxwlqy",
//                "sfkjxzxqy", "sfgxjsqy", "sffsjsrgdynssx", "gjxzhjzhy"
        };
        for (String key : NValueKeys) {
            taxStructureJSON.getJSONObject("qysdsAData")
                    .getJSONObject("a200000Ywbd")
                    .getJSONObject("sbbxxForm")
                    .put(key, "N");
        }

        // 封装提交数据
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);
        // 填入 taxParam
        taxStructureJSON.getJSONObject("taxParam").put("year", year);
        taxStructureJSON.getJSONObject("taxParam").put("beginMonth", beginMonth);
        taxStructureJSON.getJSONObject("taxParam").put("endMonth", endMonth);
        taxStructureJSON.getJSONObject("taxParam").put("period", period);
        taxStructureJSON.getJSONObject("taxParam").put("skssqq", skssqq);
        taxStructureJSON.getJSONObject("taxParam").put("skssqz", skssqz);
        req.put("body", taxStructureJSON);
        System.out.println(req.toString());
        System.out.println(ttkOpenAPI.writeValueAddedTaxData(req.toString()).toJSONString());
    }

    public static void writeValueAddedTaxDataGeneralForBeLLE(Long orgId, String excelPath, String sheetName, Integer rowIndex) {

        File taxStructureFile = FileUtil.file(ClassUtil.getClassPath() + "taxJson/small_scale_taxpayer.json");
//        File taxStructureFile = FileUtil.file("/Users/louxin/Desktop/sunday/Sunday_backend/pwc-filing/src/main/resources/taxJson/small_scale_taxpayer.json");
        JSONObject taxStructureJSON = JSONUtil.readJSONObject(taxStructureFile, Charset.defaultCharset());

        //申报参数，日期类型等
        JSONObject taxParam = taxStructureJSON.getJSONObject("taxParam");
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610606"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqq", skssqq);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        taxParam.put("skssqz", skssqz);
        //常量 小规模纳税人税务
        taxParam.put("yzpzzlDm", "BDA0610611");


        // 打开excel并读取对应数据
        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(excelPath);
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);
        TaxDataBeLLEInfo infoForArray0 = ExcelExtractUtil.cellValForBeLLEArray0(sheet, rowIndex);
        TaxDataBeLLEInfo infoForArray1 = ExcelExtractUtil.cellValForBeLLEArray1(sheet, rowIndex);

        // json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["yzzzsbhsxse"]
        JSONObject zzsxgmGridlb0 = taxStructureJSON.getJSONObject("xgmData")
                .getJSONObject("zzssyyxgmnsr")
                .getJSONObject("zzsxgmGrid")
                .getJSONArray("zzsxgmGridlb").getJSONObject(0);

        JSONObject zzsxgmGridlb1 = taxStructureJSON.getJSONObject("xgmData")
                .getJSONObject("zzssyyxgmnsr")
                .getJSONObject("zzsxgmGrid")
                .getJSONArray("zzsxgmGridlb").getJSONObject(1);


        zzsxgmGridlb0.put("yzzzsbhsxse", infoForArray0.getYzzzsbhsxse());
        zzsxgmGridlb0.put("swjgdkdzzszyfpbhsxse", infoForArray0.getYzzzsbhsxse());
        zzsxgmGridlb0.put("skqjkjdptfpbhsxse", infoForArray0.getSkqjkjdptfpbhsxse());
        zzsxgmGridlb0.put("xsczbdcbhsxse", infoForArray0.getXsczbdcbhsxse());
        zzsxgmGridlb0.put("swjgdkdzzszyfpbhsxse1", infoForArray0.getSwjgdkdzzszyfpbhsxse1());
        zzsxgmGridlb0.put("skqjkjdptfpbhsxse2", infoForArray0.getSkqjkjdptfpbhsxse2());
        zzsxgmGridlb0.put("xssygdysgdzcbhsxse", infoForArray0.getXssygdysgdzcbhsxse());
        zzsxgmGridlb0.put("msxse", infoForArray0.getMsxse());
        zzsxgmGridlb0.put("xwqymsxse", infoForArray0.getXwqymsxse());
        zzsxgmGridlb0.put("wdqzdxse", infoForArray0.getWdqzdxse());
        zzsxgmGridlb0.put("qtmsxse", infoForArray0.getQtmsxse());
        zzsxgmGridlb0.put("skqjkjdptfpxse1", infoForArray0.getSkqjkjdptfpxse1());
        zzsxgmGridlb0.put("hdxse", infoForArray0.getHdxse());
        zzsxgmGridlb0.put("bqynse", infoForArray0.getBqynse());
        zzsxgmGridlb0.put("hdynse", infoForArray0.getHdynse());
        zzsxgmGridlb0.put("bqynsejze", infoForArray0.getBqynsejze());
        zzsxgmGridlb0.put("bqmse", infoForArray0.getBqmse());
        zzsxgmGridlb0.put("xwqymse", infoForArray0.getXwqymse());
        zzsxgmGridlb0.put("wdqzdmse", infoForArray0.getWdqzdmse());
        zzsxgmGridlb0.put("ynsehj", infoForArray0.getYnsehj());
        zzsxgmGridlb0.put("bqyjse1", infoForArray0.getBqyjse1());
        zzsxgmGridlb0.put("bqybtse", infoForArray0.getBqybtse());
        zzsxgmGridlb0.put("bdcxse", infoForArray0.getBdcxse());

        zzsxgmGridlb1.put("yzzzsbhsxse", infoForArray1.getYzzzsbhsxse());
        zzsxgmGridlb1.put("swjgdkdzzszyfpbhsxse", infoForArray1.getYzzzsbhsxse());
        zzsxgmGridlb1.put("skqjkjdptfpbhsxse", infoForArray1.getSkqjkjdptfpbhsxse());
        zzsxgmGridlb1.put("xsczbdcbhsxse", infoForArray1.getXsczbdcbhsxse());
        zzsxgmGridlb1.put("swjgdkdzzszyfpbhsxse1", infoForArray1.getSwjgdkdzzszyfpbhsxse1());
        zzsxgmGridlb1.put("skqjkjdptfpbhsxse2", infoForArray1.getSkqjkjdptfpbhsxse2());
        zzsxgmGridlb1.put("xssygdysgdzcbhsxse", infoForArray1.getXssygdysgdzcbhsxse());
        zzsxgmGridlb1.put("msxse", infoForArray1.getMsxse());
        zzsxgmGridlb1.put("xwqymsxse", infoForArray1.getXwqymsxse());
        zzsxgmGridlb1.put("wdqzdxse", infoForArray1.getWdqzdxse());
        zzsxgmGridlb1.put("qtmsxse", infoForArray1.getQtmsxse());
        zzsxgmGridlb1.put("skqjkjdptfpxse1", infoForArray1.getSkqjkjdptfpxse1());
        zzsxgmGridlb1.put("hdxse", infoForArray1.getHdxse());
        zzsxgmGridlb1.put("bqynse", infoForArray1.getBqynse());
        zzsxgmGridlb1.put("hdynse", infoForArray1.getHdynse());
        zzsxgmGridlb1.put("bqynsejze", infoForArray1.getBqynsejze());
        zzsxgmGridlb1.put("bqmse", infoForArray1.getBqmse());
        zzsxgmGridlb1.put("xwqymse", infoForArray1.getXwqymse());
        zzsxgmGridlb1.put("wdqzdmse", infoForArray1.getWdqzdmse());
        zzsxgmGridlb1.put("ynsehj", infoForArray1.getYnsehj());
        zzsxgmGridlb1.put("bqyjse1", infoForArray1.getBqyjse1());
        zzsxgmGridlb1.put("bqybtse", infoForArray1.getBqybtse());
        zzsxgmGridlb1.put("bdcxse", infoForArray1.getBdcxse());


        // 封装提交数据
        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);
        req.put("body", taxStructureJSON);
        System.out.println(req.toString());
        System.out.println(ttkOpenAPI.writeValueAddedTaxData(req.toString()).toJSONString());

    }


    public static void main(String[] args) {
//        TtkTaxUtil.writeValueAddedTaxData();
//        TtkTaxUtil.writeValueAddedTaxDataForGeneral(243306356408384L, new FilingVatEntity());
//        TtkTaxUtil.getWebUrlForShenBao("241571900840320", 2019,11,null);
//        TtkTaxUtil.getTaxResult(0L, 241571900840320L, "2019", "11");
//        TtkTaxUtil.getTaxResultBatch();
//        TtkTaxUtil.getWebUrlForShenBaoBatch();
//        TtkTaxUtil.getWebUrlForShenBaoBatch();

        System.out.println(DateUtil.beginOfMonth(DateUtil.lastMonth()));
    }



    /**
     * SD 通用
     *
     * @param orgId
     * @param filepath
     */
    public static void writeSDFromExcelByCellName(Long orgId, String filepath, String taxPerioCode, FilingVat filingSD) {

        // 请求的json数据
        JSONObject reqJSON = JSONUtil.createObj();
        reqJSON.put("orgId", orgId);
        //读取上传报文格式
        JSONObject body = JSONUtil.createObj();
        //申报参数，日期类型等
        JSONObject taxParam = JSONUtil.createObj();
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610794"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        // 按期
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);

        //常量 印花税
        taxParam.put("yzpzzlDm", "BDA0610794");
        if (StrUtil.isBlank(taxPerioCode)) {
            taxPerioCode = "06";
        }
        if ("11".equals(taxPerioCode)) {
            // 按次
            skssqq = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN);
            skssqz = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN);
        }
        taxParam.put("skssqq", skssqq);
        taxParam.put("skssqz", skssqz);
        taxParam.put("nsqxdm", taxPerioCode);
        body.put("taxParam", taxParam);
        // yhsData
        JSONObject yhsData = JSONUtil.createObj();
        // yhssb
        JSONObject yhssb = JSONUtil.createObj();
        // yhssbGrid
        JSONObject yhssbGrid = JSONUtil.createObj();
        // yhssbGridlb
        JSONArray yhssbGridlb = JSONUtil.createArray();
        for (String agreement : TtkConstants.SD_AGREEMENTS) {
            switch (agreement) {
                case "101110101":
                    yhssbGridlb.put(sdGrid(agreement, filingSD.getDsjsyjYhsjsyjgxhtje()));
                    break;
                case "101110105":
                    yhssbGridlb.put(sdGrid(agreement, filingSD.getDsjsyjYhsjsyjcczlhtje()));
                    break;
                default:
                    break;
            }
        }
        yhssbGrid.put("yhssbGridlb", yhssbGridlb);
        yhssb.put("yhssbGrid", yhssbGrid);
        yhsData.put("yhssb", yhssb);
        // 是否需要，不需要可以不传
        JSONObject slrxxForm = JSONUtil.createObj();
        // 办税人
        slrxxForm.put("bsr", "");
        // 代理人身份证件类型
        slrxxForm.put("dlrsfzjzlDm1", "");
        // 代理人
        slrxxForm.put("dlr", "");
        // 受理人
        slrxxForm.put("slr", "");
        // 代理人身份证件号码
        slrxxForm.put("dlrsfzjhm1", "");
//        yhsData.put("slrxxForm", slrxxForm);
        body.put("yhsData", yhsData);
        reqJSON.put("body", body);
        System.out.println(reqJSON.toString());
        System.out.println(ttkOpenAPI.writeValueAddedTaxData(reqJSON.toString()).toJSONString());
    }


    /**
     * SD 通用
     *
     * @param orgId
     * @param filepath
     */
    public static void writeSDFromExcelByCellName(Long orgId, String filepath, String taxPerioCode) {
        // TODO push SD data to third party
        FilingSD filingSD = new FilingSD();
        ExcelUtil.readBySax(ResourceUtil.getStream(filepath), 0, sdRowHandler(filingSD));
        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(filepath);
        String sheetName = "数据填写";
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);

        // 请求的json数据
        JSONObject reqJSON = JSONUtil.createObj();
        reqJSON.put("orgId", orgId);
        //读取上传报文格式
        JSONObject body = JSONUtil.createObj();
        //申报参数，日期类型等
        JSONObject taxParam = JSONUtil.createObj();
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610794"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        // 按期
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);

        //常量 印花税
        taxParam.put("yzpzzlDm", "BDA0610794");
        if (StrUtil.isBlank(taxPerioCode)) {
            taxPerioCode = "06";
        }
        if ("11".equals(taxPerioCode)) {
            // 按次
            skssqq = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN);
            skssqz = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN);
        }
        taxParam.put("skssqq", skssqq);
        taxParam.put("skssqz", skssqz);
        taxParam.put("nsqxdm", taxPerioCode);
        body.put("taxParam", taxParam);
        // yhsData
        JSONObject yhsData = JSONUtil.createObj();
        // yhssb
        JSONObject yhssb = JSONUtil.createObj();
        // yhssbGrid
        JSONObject yhssbGrid = JSONUtil.createObj();
        // yhssbGridlb
        JSONArray yhssbGridlb = JSONUtil.createArray();
        for (String agreement : TtkConstants.SD_AGREEMENTS) {
            switch (agreement) {
                case "101110101":
                    yhssbGridlb.put(sdGrid(agreement, filingSD.getDsjsyjYhsjsyjgxhtje()));
                    break;
                case "101110105":
                    yhssbGridlb.put(sdGrid(agreement, filingSD.getDsjsyjYhsjsyjcczlhtje()));
                    break;
                default:
                    break;
            }
        }
        yhssbGrid.put("yhssbGridlb", yhssbGridlb);
        yhssb.put("yhssbGrid", yhssbGrid);
        yhsData.put("yhssb", yhssb);
        // 是否需要，不需要可以不传
        JSONObject slrxxForm = JSONUtil.createObj();
        // 办税人
        slrxxForm.put("bsr", "");
        // 代理人身份证件类型
        slrxxForm.put("dlrsfzjzlDm1", "");
        // 代理人
        slrxxForm.put("dlr", "");
        // 受理人
        slrxxForm.put("slr", "");
        // 代理人身份证件号码
        slrxxForm.put("dlrsfzjhm1", "");
//        yhsData.put("slrxxForm", slrxxForm);
        body.put("yhsData", yhsData);
        reqJSON.put("body", body);
        System.out.println(reqJSON.toString());
        System.out.println(ttkOpenAPI.writeValueAddedTaxData(reqJSON.toString()).toJSONString());
    }

    /**
     * SD 通用
     *
     * @param orgId
     * @param filepath
     */
    public static void writeSDFromExcel(Long orgId, String filepath, String taxPerioCode) {
        // TODO push SD data to third party
        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(filepath);
        String sheetName = "数据填写";
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);

        // 请求的json数据
        JSONObject reqJSON = JSONUtil.createObj();
        reqJSON.put("orgId", orgId);
        //读取上传报文格式
        JSONObject body = JSONUtil.createObj();
        //申报参数，日期类型等
        JSONObject taxParam = JSONUtil.createObj();
//        "year": 2019, "beginMonth": 10, "endMonth": 10,  "period": 10, "skssqq": "2019-10-01", "skssqz": "2019-10-30", "yzpzzlDm": "BDA0610794"
        taxParam.put("year", DateUtil.thisYear());
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        taxParam.put("beginMonth", thisMonth);
        taxParam.put("endMonth", thisMonth);
        taxParam.put("period", thisMonth);
        // 按期
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);

        //常量 印花税
        taxParam.put("yzpzzlDm", "BDA0610794");
        if (StrUtil.isBlank(taxPerioCode)) {
            taxPerioCode = "06";
        }
        if ("11".equals(taxPerioCode)) {
            // 按次
            skssqq = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN);
            skssqz = DateUtil.format(DateUtil.date(), DatePattern.NORM_DATE_PATTERN);
        }
        taxParam.put("skssqq", skssqq);
        taxParam.put("skssqz", skssqz);
        taxParam.put("nsqxdm", taxPerioCode);
        body.put("taxParam", taxParam);
        // yhsData
        JSONObject yhsData = JSONUtil.createObj();
        // yhssb
        JSONObject yhssb = JSONUtil.createObj();
        // yhssbGrid
        JSONObject yhssbGrid = JSONUtil.createObj();
        // yhssbGridlb
        JSONArray yhssbGridlb = JSONUtil.createArray();
        for (String agreement : TtkConstants.SD_AGREEMENTS) {
            switch (agreement) {
                case "101110101":
                    BigDecimal jsje = ExcelExtractUtil.cellVal(sheet, 3, "BO");
                    yhssbGridlb.put(sdGrid(agreement, jsje.toPlainString()));
                    break;
//                case "101110102":
//                    BigDecimal f7 = ExcelExtractUtil.cellVal(sheet, 7, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f7.toPlainString()));
//                    break;
//                case "101110103":
//                    BigDecimal f8 = ExcelExtractUtil.cellVal(sheet, 8, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f8.toPlainString()));
//                    break;
//                case "101110104":
//                    BigDecimal f9 = ExcelExtractUtil.cellVal(sheet, 9, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f9.toPlainString()));
//                    break;
                case "101110105":
                    BigDecimal jsje05 = ExcelExtractUtil.cellVal(sheet, 3, "BP");
                    yhssbGridlb.put(sdGrid(agreement, jsje05.toPlainString()));
                    break;
//                case "101110106":
//                    BigDecimal f11 = ExcelExtractUtil.cellVal(sheet, 11, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f11.toPlainString()));
//                    break;
//                case "101110107":
//                    BigDecimal f12 = ExcelExtractUtil.cellVal(sheet, 12, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f12.toPlainString()));
//                    break;
//                case "101110108":
//                    BigDecimal f13 = ExcelExtractUtil.cellVal(sheet, 13, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f13.toPlainString()));
//                    break;
//                case "101110109":
//                    BigDecimal f14 = ExcelExtractUtil.cellVal(sheet, 14, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f14.toPlainString()));
//                    break;
//                case "101110110":
//                    BigDecimal f15 = ExcelExtractUtil.cellVal(sheet, 15, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f15.toPlainString()));
//                    break;
//                case "101110200":
//                    BigDecimal f16 = ExcelExtractUtil.cellVal(sheet, 16, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f16.toPlainString()));
//                    break;
//                case "101110500":
//                    yhssbGridlb.put(sdGrid(agreement, "0"));
//                    break;
//                case "101110599":
//                    yhssbGridlb.put(sdGrid(agreement, "0"));
//                    break;
//                case "101110400":
//                    BigDecimal f18 = ExcelExtractUtil.cellVal(sheet, 18, "F");
//                    yhssbGridlb.put(sdGrid(agreement, f18.toPlainString()));
//                    break;
                default:
                    break;
            }
        }
        yhssbGrid.put("yhssbGridlb", yhssbGridlb);
        yhssb.put("yhssbGrid", yhssbGrid);
        yhsData.put("yhssb", yhssb);
        // 是否需要，不需要可以不传
        JSONObject slrxxForm = JSONUtil.createObj();
        // 办税人
        slrxxForm.put("bsr", "");
        // 代理人身份证件类型
        slrxxForm.put("dlrsfzjzlDm1", "");
        // 代理人
        slrxxForm.put("dlr", "");
        // 受理人
        slrxxForm.put("slr", "");
        // 代理人身份证件号码
        slrxxForm.put("dlrsfzjhm1", "");
//        yhsData.put("slrxxForm", slrxxForm);
        body.put("yhsData", yhsData);
        reqJSON.put("body", body);
        System.out.println(reqJSON.toString());
        System.out.println(ttkOpenAPI.writeValueAddedTaxData(reqJSON.toString()).toJSONString());
    }

    /**
     * CIT 通用
     *
     * @param orgId
     * @param filepath
     */
    public static void writeCITFromExcel(Long orgId, String filepath) {
        // TODO push CIT data to third party
        Workbook workbook = WorkbookUtil.createBook(filepath);
        String sheetName = "数据填写";
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
//        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
//        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(), -3)), DatePattern.NORM_DATE_PATTERN);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        applyGenericCorporateTaxTypeA(
                orgId,
                filepath,
                sheetName,
                DateUtil.thisYear(),
                thisMonth,
                thisMonth,
                thisMonth,
                skssqq,
                skssqz);
    }

    /**
     * CIT 通用
     *
     * @param orgId
     * @param filepath
     */
    public static void writeCITFromExcelByCellName(Long orgId, String filepath, FilingVat filingCit) {
        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
//        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
//        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(), -3)), DatePattern.NORM_DATE_PATTERN);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        applyGenericCorporateTaxByCellName(
                orgId,
                filepath,
                DateUtil.thisYear(),
                thisMonth,
                thisMonth,
                thisMonth,
                skssqq,
                skssqz,
                filingCit);
    }

    /**
     * FS 通用
     *
     * @param orgId
     * @param filepath
     */
    public static void writeFSFromExcel(Long orgId, String taxCode, String name, String filepath) {
        TtkTaxUtil.writeFinancialReportData(orgId, taxCode, name, filepath);
    }

    /**
     * 推送附加税通用
     *
     * @param orgId
     * @param filepath
     */
    public static void writeSupertaxFromExcel(Long orgId, String filepath) {
        File taxStructureFile = FileUtil.file(ClassUtil.getClassPath()
                + "taxJson/supertax.json");
        JSONObject taxStructureJSON = JSONUtil.readJSONObject(taxStructureFile, Charset.defaultCharset());

        System.out.println(taxStructureJSON.toString());
        Workbook workbook = WorkbookUtil.createBook(filepath);
        String sheetName = "数据填写";
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook, sheetName);

        BigDecimal vat = ExcelExtractUtil.cellVal(sheet, 3, "BL");

        //thisMonth 为从0计数，如：本月为12月 thisMonth = 12 -1
        Integer thisMonth = DateUtil.thisMonth() + 1;
        String skssqq = DateUtil.format(DateUtil.beginOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);
        String skssqz = DateUtil.format(DateUtil.endOfMonth(DateUtil.lastMonth()), DatePattern.NORM_DATE_PATTERN);

        taxStructureJSON.getJSONObject("taxParam").put("year", DateUtil.thisYear());
        taxStructureJSON.getJSONObject("taxParam").put("beginMonth", thisMonth);
        taxStructureJSON.getJSONObject("taxParam").put("endMonth", thisMonth);
        taxStructureJSON.getJSONObject("taxParam").put("period", thisMonth);
        taxStructureJSON.getJSONObject("taxParam").put("skssqq", skssqq);
        taxStructureJSON.getJSONObject("taxParam").put("skssqz", skssqz);

        ((JSONObject) taxStructureJSON
                .getJSONObject("fjsData")
                .getJSONObject("fjssbb")
                .getJSONObject("sbxxGrid")
                .getJSONArray("sbxxGridlbVO").get(0))
                .put("ybzzs", vat.toPlainString());

        ((JSONObject) taxStructureJSON
                .getJSONObject("fjsData")
                .getJSONObject("fjssbb")
                .getJSONObject("sbxxGrid")
                .getJSONArray("sbxxGridlbVO").get(1))
                .put("ybzzs", vat.toPlainString());

        ((JSONObject) taxStructureJSON
                .getJSONObject("fjsData")
                .getJSONObject("fjssbb")
                .getJSONObject("sbxxGrid")
                .getJSONArray("sbxxGridlbVO").get(2))
                .put("ybzzs", vat.toPlainString());


        JSONObject req = JSONUtil.createObj();
        req.put("orgId", orgId);
        req.put("body", taxStructureJSON);
        System.out.println(req.toString());
        System.out.println("Supertax: "+ttkOpenAPI.writeValueAddedTaxData(req.toString()).toJSONString());
    }


    /**
     * row handler
     * @return
     */
    private static RowHandler sdRowHandler(FilingSD filingSD) {
        Map<String, Integer> map = Maps.newHashMap();
        AtomicReference<List<Object>> vatAtomicReference = new AtomicReference<>();
        AtomicReference<FilingSD> sdAtomicReference = new AtomicReference<>();
        sdAtomicReference.set(filingSD);
        return (sheetIndex, rowIndex, rowlist) -> {
            FilingSD sd = sdAtomicReference.get();
            if (rowIndex == 0) {
                vatAtomicReference.set(rowlist);
            }
            if (rowIndex == 1) {
                List<Object> headerList = vatAtomicReference.get();
                String name = "";
                for (int i = 0; i < headerList.size(); i ++) {
                    if(null != headerList.get(i)) {
                        name = headerList.get(i).toString();
                    }
                    map.put(name + "-" + rowlist.get(i), i);
                }
            }
            if (0 == sheetIndex) {
                if (rowIndex != 1 && rowIndex != 0) {

                    //处理vat
                    ExcelHandlerUtil.sdHandler(rowIndex, rowlist, sd, map);
                }
            }
        };
    }
}

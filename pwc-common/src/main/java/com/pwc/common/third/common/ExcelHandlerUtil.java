package com.pwc.common.third.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.pwc.common.third.request.FilingBelleVat;
import com.pwc.common.third.request.FilingSD;
import com.pwc.common.third.request.FilingVat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zk
 */
public class ExcelHandlerUtil {
    /**
     * 处理vatExcel
     *
     * @param rowIndex
     * @param rowlist
     * @param filingVatEntity
     */
    public static void vatHandler(Integer rowIndex, List rowlist, FilingVat filingVatEntity, Map<String, Integer> map) {
        if (CollUtil.isNotEmpty(map)) {
            //本月应交税额
            if (null != map.get(ExcelTitle.VatRead.BYYJZZS_BYYJZZS.getValue())) {
                String BYYJZZS_BYYJZZS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.BYYJZZS_BYYJZZS.getValue())));
                filingVatEntity.setByyjzzsByyjzzs("null".equals(BYYJZZS_BYYJZZS) ? "0.00" : BYYJZZS_BYYJZZS);
            } else {
                filingVatEntity.setByyjzzsByyjzzs("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.BYYJZZS_BYZMYJZZS.getValue())) {
                String BYYJZZS_BYZMYJZZS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.BYYJZZS_BYZMYJZZS.getValue())));
                filingVatEntity.setByyjzzsByzmyjzzs("null".equals(BYYJZZS_BYZMYJZZS) ? "0.00" : BYYJZZS_BYZMYJZZS);
            } else {
                filingVatEntity.setByyjzzsByzmyjzzs("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.BYYJZZS_SQYNSE_LDWFS.getValue())) {
                String BYYJZZS_SQYNSE_LDWFS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.BYYJZZS_SQYNSE_LDWFS.getValue())));
                filingVatEntity.setByyjzzsSqynseLdwfs("null".equals(BYYJZZS_SQYNSE_LDWFS) ? "0.00" : BYYJZZS_SQYNSE_LDWFS);
            } else {
                filingVatEntity.setByyjzzsSqynseLdwfs("0.00");
            }
            //进项明细
            if (null != map.get(ExcelTitle.VatRead.JXMX_JXSE.getValue())) {
                String JXMX_JXSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_JXSE.getValue())));
                filingVatEntity.setJxmxJxse("null".equals(JXMX_JXSE) ? "0.00" : JXMX_JXSE);
            } else {
                filingVatEntity.setJxmxJxse("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_JXZYFPFS.getValue())) {
                String JXMX_JXZYFPFS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_JXZYFPFS.getValue())));
                filingVatEntity.setJxmxJxzyfpfs("null".equals(JXMX_JXZYFPFS) ? "" : JXMX_JXZYFPFS);
            } else {
                filingVatEntity.setJxmxJxzyfpfs("");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_JXXSE.getValue())) {
                String JXMX_JXXSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_JXXSE.getValue())));
                filingVatEntity.setJxmxJxxse("null".equals(JXMX_JXXSE) ? "0.00" : JXMX_JXXSE);
            } else {
                filingVatEntity.setJxmxJxxse("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_JXZCSE_HZZYFP.getValue())) {
                String JXMX_JXZCSE_HZZYFP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_JXZCSE_HZZYFP.getValue())));
                filingVatEntity.setJxmxJxzcseHzzyfp("null".equals(JXMX_JXZCSE_HZZYFP) ? "0.00" : JXMX_JXZCSE_HZZYFP);
            } else {
                filingVatEntity.setJxmxJxzcseHzzyfp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_JXZCSE_FZCSS.getValue())) {
                String JXMX_JXZCSE_FZCSS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_JXZCSE_FZCSS.getValue())));
                filingVatEntity.setJxmxJxzcseFzcss("null".equals(JXMX_JXZCSE_FZCSS) ? "0.00" : JXMX_JXZCSE_FZCSS);
            } else {
                filingVatEntity.setJxmxJxzcseFzcss("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_JXZCSE_JTFLGRXF.getValue())) {
                String JXMX_JXZCSE_JTFLGRXF = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_JXZCSE_JTFLGRXF.getValue())));
                filingVatEntity.setJxmxJxzcseJtflgrxf("null".equals(JXMX_JXZCSE_JTFLGRXF) ? "0.00" : JXMX_JXZCSE_JTFLGRXF);
            } else {
                filingVatEntity.setJxmxJxzcseJtflgrxf("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_JXZCSE_QT.getValue())) {
                String JXMX_JXZCSE_QT = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_JXZCSE_QT.getValue())));
                filingVatEntity.setJxmxJxzcseQt("null".equals(JXMX_JXZCSE_QT) ? "0.00" : JXMX_JXZCSE_QT);
            } else {
                filingVatEntity.setJxmxJxzcseQt("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_ZZSSKXTZYSBFJJSWHF.getValue())) {
                String JXMX_ZZSSKXTZYSBFJJSWHF = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_ZZSSKXTZYSBFJJSWHF.getValue())));
                filingVatEntity.setJxmxZzsskxtzysbfjjswhf("null".equals(JXMX_ZZSSKXTZYSBFJJSWHF) ? "0.00" : JXMX_ZZSSKXTZYSBFJJSWHF);
            } else {
                filingVatEntity.setJxmxZzsskxtzysbfjjswhf("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_FHBDQXGMJZ.getValue())) {
                String JXMX_FHBDQXGMJZ = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_FHBDQXGMJZ.getValue())));
                filingVatEntity.setJxmxFhbdqxgmjz("null".equals(JXMX_FHBDQXGMJZ) ? "0.00" : JXMX_FHBDQXGMJZ);
            } else {
                filingVatEntity.setJxmxFhbdqxgmjz("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_LKYSSL.getValue())) {
                String JXMX_LKYSSL = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_LKYSSL.getValue())));
                filingVatEntity.setJxmxLkyssl("null".equals(JXMX_LKYSSL) || StrUtil.isBlank(JXMX_LKYSSL) ? "" : JXMX_LKYSSL);
            } else {
                filingVatEntity.setJxmxLkyssl("");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_LKYSJE.getValue())) {
                String JXMX_LKYSJE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_LKYSJE.getValue())));
                filingVatEntity.setJxmxLkysje("null".equals(JXMX_LKYSJE) || StrUtil.isBlank(JXMX_LKYSJE) ? "0.00" : JXMX_LKYSJE);
            } else {
                filingVatEntity.setJxmxLkysje("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXMX_LKYSSE.getValue())) {
                String JXMX_LKYSSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXMX_LKYSSE.getValue())));
                filingVatEntity.setJxmxLkysse("null".equals(JXMX_LKYSSE) || StrUtil.isBlank(JXMX_LKYSSE) ? "0.00" : JXMX_LKYSSE);
            } else {
                filingVatEntity.setJxmxLkysse("0.00");
            }
            //进项合计
            if (null != map.get(ExcelTitle.VatRead.JXHJ_JXSE.getValue())) {
                String JXHJ_JXSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXHJ_JXSE.getValue())));
                filingVatEntity.setJxhjJxse("null".equals(JXHJ_JXSE) ? "0.00" : JXHJ_JXSE);
            } else {
                filingVatEntity.setJxhjJxse("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.JXHJ_JXXSE.getValue())) {
                String JXHJ_JXXSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.JXHJ_JXXSE.getValue())));
                filingVatEntity.setJxhjJxxse("null".equals(JXHJ_JXXSE) ? "0.00" : JXHJ_JXXSE);
            } else {
                filingVatEntity.setJxhjJxxse("0.00");
            }
            //销项明细
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZYFPFS.getValue())) {
                String XXMX_ZYFPFS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZYFPFS.getValue())));
                filingVatEntity.setXxmxZyfpfs("null".equals(XXMX_ZYFPFS) ? "" : XXMX_ZYFPFS);
            } else {
                filingVatEntity.setXxmxZyfpfs("");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_PTFPFS.getValue())) {
                String XXMX_PTFPFS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_PTFPFS.getValue())));
                filingVatEntity.setXxmxPtfpfs("null".equals(XXMX_PTFPFS) ? "" : XXMX_PTFPFS);
            } else {
                filingVatEntity.setXxmxPtfpfs("");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_QTFPFS.getValue())) {
                String XXMX_QTFPFS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_QTFPFS.getValue())));
                filingVatEntity.setXxmxQtfpfs("null".equals(XXMX_QTFPFS) ? "" : XXMX_QTFPFS);
            } else {
                filingVatEntity.setXxmxQtfpfs("");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_XSJE_ZP.getValue())) {
                String XXMX_XSJE_ZP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_XSJE_ZP.getValue())));
                filingVatEntity.setXxmxXsjeZp("null".equals(XXMX_XSJE_ZP) ? "0.00" : XXMX_XSJE_ZP);
            } else {
                filingVatEntity.setXxmxXsjeZp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSSE_ZP.getValue())) {
                String XXMX_ZZSSE_ZP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSSE_ZP.getValue())));
                filingVatEntity.setXxmxZzsseZp("null".equals(XXMX_ZZSSE_ZP) ? "0.00" : XXMX_ZZSSE_ZP);
            } else {
                filingVatEntity.setXxmxZzsseZp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_XSJE_PP.getValue())) {
                String XXMX_XSJE_PP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_XSJE_PP.getValue())));
                filingVatEntity.setXxmxXsjePp("null".equals(XXMX_XSJE_PP) ? "0.00" : XXMX_XSJE_PP);
            } else if (null != map.get(ExcelTitle.VatRead.XXMX_XSJE_PP_13.getValue())) {
                String XXMX_XSJE_PP_13 = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_XSJE_PP_13.getValue())));
                filingVatEntity.setXxmxXsjePp("null".equals(XXMX_XSJE_PP_13) ? "0.00" : XXMX_XSJE_PP_13);
            } else {
                filingVatEntity.setXxmxXsjePp("0.00");
            }
//            if (null != map.get(ExcelTitle.VatRead.XXMX_XSJE_PP_13.getValue())) {
//                String XXMX_XSJE_PP_13 = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_XSJE_PP_13.getValue())));
//                filingVatEntity.setXxmxXsjePp("null".equals(XXMX_XSJE_PP_13) ? "" : XXMX_XSJE_PP_13);
//            } else {
//                filingVatEntity.setXxmxXsjePp("");
//            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSSE_PP.getValue())) {
                String XXMX_ZZSSE_PP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSSE_PP.getValue())));
                filingVatEntity.setXxmxZzssePp("null".equals(XXMX_ZZSSE_PP) ? "0.00" : XXMX_ZZSSE_PP);
            } else if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSSE_PP_13.getValue())) {
                String XXMX_ZZSSE_PP_13 = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSSE_PP_13.getValue())));
                filingVatEntity.setXxmxZzssePp("null".equals(XXMX_ZZSSE_PP_13) ? "0.00" : XXMX_ZZSSE_PP_13);
            } else {
                filingVatEntity.setXxmxZzssePp("0.00");
            }
//            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSSE_PP_13.getValue())) {
//                String XXMX_ZZSSE_PP_13 = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSSE_PP_13.getValue())));
//                filingVatEntity.setXxmxZzssePp("null".equals(XXMX_ZZSSE_PP_13) ? "" : XXMX_ZZSSE_PP_13);
//            } else {
//                filingVatEntity.setXxmxZzssePp("");
//            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_XSJE16_QTFP.getValue())) {
                String XXMX_XSJE16_QTFP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_XSJE16_QTFP.getValue())));
                filingVatEntity.setXxmxXsje16Qtfp("null".equals(XXMX_XSJE16_QTFP) ? "0.00" : XXMX_XSJE16_QTFP);
            } else {
                filingVatEntity.setXxmxXsje16Qtfp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSSE16_QTFP.getValue())) {
                String XXMX_ZZSSE16_QTFP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSSE16_QTFP.getValue())));
                filingVatEntity.setXxmxZzsse16Qtfp("null".equals(XXMX_ZZSSE16_QTFP) ? "0.00" : XXMX_ZZSSE16_QTFP);
            } else {
                filingVatEntity.setXxmxZzsse16Qtfp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_XSJE17_ZP.getValue())) {
                String XXMX_XSJE17_ZP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_XSJE17_ZP.getValue())));
                filingVatEntity.setXxmxXsje17Zp("null".equals(XXMX_XSJE17_ZP) ? "0.00" : XXMX_XSJE17_ZP);
            } else {
                filingVatEntity.setXxmxXsje17Zp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSSE17_ZP.getValue())) {
                String XXMX_ZZSSE17_ZP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSSE17_ZP.getValue())));
                filingVatEntity.setXxmxZzsse17Zp("null".equals(XXMX_ZZSSE17_ZP) ? "0.00" : XXMX_ZZSSE17_ZP);
            } else {
                filingVatEntity.setXxmxZzsse17Zp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_XSJE17_PP.getValue())) {
                String XXMX_XSJE17_PP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_XSJE17_PP.getValue())));
                filingVatEntity.setXxmxXsje17Pp("null".equals(XXMX_XSJE17_PP) ? "0.00" : XXMX_XSJE17_PP);
            } else {
                filingVatEntity.setXxmxXsje17Pp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSSE17_PP.getValue())) {
                String XXMX_ZZSSE17_PP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSSE17_PP.getValue())));
                filingVatEntity.setXxmxZzsse17Pp("null".equals(XXMX_ZZSSE17_PP) ? "0.00" : XXMX_ZZSSE17_PP);
            } else {
                filingVatEntity.setXxmxZzsse17Pp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_XSJE17_QTFP.getValue())) {
                String XXMX_XSJE17_QTFP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_XSJE17_QTFP.getValue())));
                filingVatEntity.setXxmxXsje17Qtfp("null".equals(XXMX_XSJE17_QTFP) ? "0.00" : XXMX_XSJE17_QTFP);
            } else {
                filingVatEntity.setXxmxXsje17Qtfp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSSE17_QTFP.getValue())) {
                String XXMX_ZZSSE17_QTFP = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSSE17_QTFP.getValue())));
                filingVatEntity.setXxmxZzsse17Qtfp("null".equals(XXMX_ZZSSE17_QTFP) ? "0.00" : XXMX_ZZSSE17_QTFP);
            } else {
                filingVatEntity.setXxmxZzsse17Qtfp("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSPTFPJE0_CK.getValue())) {
                String XXMX_ZZSPTFPJE0_CK = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSPTFPJE0_CK.getValue())));
                filingVatEntity.setXxmxZzsptfpje0Ck("null".equals(XXMX_ZZSPTFPJE0_CK) ? "0.00" : XXMX_ZZSPTFPJE0_CK);
            } else {
                filingVatEntity.setXxmxZzsptfpje0Ck("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_ZZSPTFPSE.getValue())) {
                String XXMX_ZZSPTFPSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_ZZSPTFPSE.getValue())));
                filingVatEntity.setXxmxZzsptfpse0("null".equals(XXMX_ZZSPTFPSE) ? "0.00" : XXMX_ZZSPTFPSE);
            } else {
                filingVatEntity.setXxmxZzsptfpse0("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_WKPJE3.getValue())) {
                String XXMX_WKPJE3 = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_WKPJE3.getValue())));
                filingVatEntity.setXxmxWkpje3("null".equals(XXMX_WKPJE3) ? "0.00" : XXMX_WKPJE3);
            } else {
                filingVatEntity.setXxmxWkpje3("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_WKPSE3.getValue())) {
                String XXMX_WKPSE3 = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_WKPSE3.getValue())));
                filingVatEntity.setXxmxWkpse3("null".equals(XXMX_WKPSE3) ? "0.00" : XXMX_WKPSE3);
            } else {
                filingVatEntity.setXxmxWkpse3("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_WKPJE.getValue())) {
                String XXMX_WKPJE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_WKPJE.getValue())));
                filingVatEntity.setXxmxWkpje("null".equals(XXMX_WKPJE) ? "0.00" : XXMX_WKPJE);
            } else {
                filingVatEntity.setXxmxWkpje("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXMX_WKPSE.getValue())) {
                String XXMX_WKPSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXMX_WKPSE.getValue())));
                filingVatEntity.setXxmxWkpse("null".equals(XXMX_WKPSE) ? "0.00" : XXMX_WKPSE);
            } else {
                filingVatEntity.setXxmxWkpse("0.00");
            }
            //销项合计
            if (null != map.get(ExcelTitle.VatRead.XXHJ_XXSE.getValue())) {
                String XXHJ_XXSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXHJ_XXSE.getValue())));
                filingVatEntity.setXxhjXxse("null".equals(XXHJ_XXSE) ? "0.00" : XXHJ_XXSE);
            } else {
                filingVatEntity.setXxhjXxse("0.00");
            }
            if (null != map.get(ExcelTitle.VatRead.XXHJ_XXXSE.getValue())) {
                String XXHJ_XXXSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.VatRead.XXHJ_XXXSE.getValue())));
                filingVatEntity.setXxhjXxxse("null".equals(XXHJ_XXXSE) ? "0.00" : XXHJ_XXXSE);
            } else {
                filingVatEntity.setXxhjXxxse("0.00");
            }
            //门店信息
            if (null != map.get(ExcelTitle.StoresInformation.STORES_EIN.getValue())) {
                String STORES_EIN = StrUtil.toString(rowlist.get(map.get(ExcelTitle.StoresInformation.STORES_EIN.getValue())));
                filingVatEntity.setStoresEin("null".equals(STORES_EIN) ? "" : STORES_EIN);
            } else {
                filingVatEntity.setStoresEin("");
            }
            if (null != map.get(ExcelTitle.StoresInformation.STORES_NO.getValue())) {
                String STORES_NO = StrUtil.toString(rowlist.get(map.get(ExcelTitle.StoresInformation.STORES_NO.getValue())));
                filingVatEntity.setStoresNo("null".equals(STORES_NO) ? "" : STORES_NO);
            } else {
                filingVatEntity.setStoresNo("");
            }
            if (null != map.get(ExcelTitle.StoresInformation.PROVINCE.getValue())) {
                String PROVINCE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.StoresInformation.PROVINCE.getValue())));
                filingVatEntity.setProvince("null".equals(PROVINCE) ? "" : PROVINCE);
            } else {
                filingVatEntity.setProvince("");
            }
            if (null != map.get(ExcelTitle.StoresInformation.CITY.getValue())) {
                String CITY = StrUtil.toString(rowlist.get(map.get(ExcelTitle.StoresInformation.CITY.getValue())));
                filingVatEntity.setCity("null".equals(CITY) ? "" : CITY);
            } else {
                filingVatEntity.setCity("");
            }
            if (null != map.get(ExcelTitle.StoresInformation.COMPANY_NAME.getValue())) {
                String COMPANY_NAME = StrUtil.toString(rowlist.get(map.get(ExcelTitle.StoresInformation.COMPANY_NAME.getValue())));
                filingVatEntity.setCompanyName("null".equals(COMPANY_NAME) ? "" : COMPANY_NAME);
            } else {
                filingVatEntity.setCompanyName("");
            }
            if (null != map.get(ExcelTitle.StoresInformation.TAX_RATE.getValue())) {
                String TAX_RATE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.StoresInformation.TAX_RATE.getValue())));
                filingVatEntity.setTaxRate("null".equals(TAX_RATE) ? "" : TAX_RATE);
            } else {
                filingVatEntity.setTaxRate("");
            }
            // 印花税计税依据（购销合同）金额
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_YHSJSYJGXHTJE.getValue())) {
                String DSJSYJ_YHSJSYJGXHTJE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_YHSJSYJGXHTJE.getValue())));
                filingVatEntity.setDsjsyjYhsjsyjgxhtje("null".equals(DSJSYJ_YHSJSYJGXHTJE) ? "0.00" : DSJSYJ_YHSJSYJGXHTJE);
            } else {
                filingVatEntity.setDsjsyjYhsjsyjgxhtje("0.00");
            }
            // 印花税计税依据（财产租赁合同）金额
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_YHSJSYJCCZLHTJE.getValue())) {
                String DSJSYJ_YHSJSYJCCZLHTJE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_YHSJSYJCCZLHTJE.getValue())));
                filingVatEntity.setDsjsyjYhsjsyjcczlhtje("null".equals(DSJSYJ_YHSJSYJCCZLHTJE) ? "0.00" : DSJSYJ_YHSJSYJCCZLHTJE);
            } else {
                filingVatEntity.setDsjsyjYhsjsyjcczlhtje("0.00");
            }
            // 工人经费人数
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_GRJFRS.getValue())) {
                String DSJSYJ_GRJFRS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_GRJFRS.getValue())));
                filingVatEntity.setDsjsyjGrjfrs("null".equals(DSJSYJ_GRJFRS) ? "0.00" : DSJSYJ_GRJFRS);
            } else {
                filingVatEntity.setDsjsyjGrjfrs("0.00");
            }
            // 工会经费（工资）
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_GHJFGZ.getValue())) {
                String DSJSYJ_GHJFGZ = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_GHJFGZ.getValue())));
                filingVatEntity.setDsjsyjGhjfgz("null".equals(DSJSYJ_GHJFGZ) ? "0.00" : DSJSYJ_GHJFGZ);
            } else {
                filingVatEntity.setDsjsyjGhjfgz("0.00");
            }
            // 残保金人数
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_CBJRS.getValue())) {
                String DSJSYJ_CBJRS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_CBJRS.getValue())));
                filingVatEntity.setDsjsyjCbjrs("null".equals(DSJSYJ_CBJRS) ? "0.00" : DSJSYJ_CBJRS);
            } else {
                filingVatEntity.setDsjsyjCbjrs("0.00");
            }
            // 残保金计税依据（工资等）
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_CBJJSYJGZD.getValue())) {
                String DSJSYJ_CBJJSYJGZD = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_CBJJSYJGZD.getValue())));
                filingVatEntity.setDsjsyjCbjjsyjgzd("null".equals(DSJSYJ_CBJJSYJGZD) ? "0.00" : DSJSYJ_CBJJSYJGZD);
            } else {
                filingVatEntity.setDsjsyjCbjjsyjgzd("0.00");
            }

            // 企业所得税-由于六费二税减征造成的账面与申报数据的差异
            if (null != map.get(ExcelTitle.CITRead.QYSDS_YYLFESJZZCDZMYSBSJDCY.getValue())) {
                String QYSDS_YYLFESJZZCDZMYSBSJDCY = StrUtil.toString(rowlist.get(map.get(ExcelTitle.CITRead.QYSDS_YYLFESJZZCDZMYSBSJDCY.getValue())));
                filingVatEntity.setQysdsYylfesjzzcdzmysbsjdcy("null".equals(QYSDS_YYLFESJZZCDZMYSBSJDCY) ? "0.00" : QYSDS_YYLFESJZZCDZMYSBSJDCY);
            } else {
                filingVatEntity.setQysdsYylfesjzzcdzmysbsjdcy("0.00");
            }
            // 企业所得税-上年度职工薪酬
            if (null != map.get(ExcelTitle.CITRead.QYSDS_SNDZGXC.getValue())) {
                String QYSDS_SNDZGXC = StrUtil.toString(rowlist.get(map.get(ExcelTitle.CITRead.QYSDS_SNDZGXC.getValue())));
                filingVatEntity.setQysdsSndzgxc("null".equals(QYSDS_SNDZGXC) ? "0.00" : QYSDS_SNDZGXC);
            } else {
                filingVatEntity.setQysdsSndzgxc("0.00");
            }
            // 企业所得税-上年度资产总额
            if (null != map.get(ExcelTitle.CITRead.QYSDS_SNDZCZE.getValue())) {
                String QYSDS_SNDZCZE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.CITRead.QYSDS_SNDZCZE.getValue())));
                filingVatEntity.setQysdsSndzcze("null".equals(QYSDS_SNDZCZE) ? "0.00" : QYSDS_SNDZCZE);
            } else {
                filingVatEntity.setQysdsSndzcze("0.00");
            }
            // 企业所得税-从业人数
            if (null != map.get(ExcelTitle.CITRead.QYSDS_CYRS.getValue())) {
                String QYSDS_CYRS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.CITRead.QYSDS_CYRS.getValue())));
                filingVatEntity.setQysdsCyrs("null".equals(QYSDS_CYRS) ? "0" : QYSDS_CYRS);
            } else {
                filingVatEntity.setQysdsCyrs("0");
            }
            // 企业所得税-分摊比例
            if (null != map.get(ExcelTitle.CITRead.QYSDS_FTBL.getValue())) {
                String QYSDS_FTBL = StrUtil.toString(rowlist.get(map.get(ExcelTitle.CITRead.QYSDS_FTBL.getValue())));
                filingVatEntity.setQysdsFtbl("null".equals(QYSDS_FTBL) ? "0" : QYSDS_FTBL);
            } else {
                filingVatEntity.setQysdsFtbl("0");
            }
            // 企业所得税-本季度分摊税额
            if (null != map.get(ExcelTitle.CITRead.QYSDS_BJDFTSE.getValue())) {
                String QYSDS_BJDFTSE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.CITRead.QYSDS_BJDFTSE.getValue())));
                filingVatEntity.setQysdsBjdftse("null".equals(QYSDS_BJDFTSE) ? "0" : QYSDS_BJDFTSE);
            } else {
                filingVatEntity.setQysdsBjdftse("0");
            }
        }
    }


    /**
     * 处理belle vat Excel
     *
     * @param rowIndex
     * @param rowlist
     * @param filingVatEntity
     */
    public static void vatHandler(Integer rowIndex, List rowlist, FilingBelleVat filingVatEntity, Map<String, Integer> map) {
        if (CollUtil.isNotEmpty(map)) {
            //本月应交税额
        }
    }


    /**
     * 财报数据处理
     *
     * @param rowlist 当前行
     * @param finType 财报类型 1. balance sheet 2. P&L
     * @return body
     */
    public static JSONArray financeRepordHandler(List rowlist, int finType, String region, String enterpriseType) {
        Map<String, String> map = MapUtil.newHashMap();
        Map<String, String> mapping = MapUtil.newHashMap();
        int cellIndex = 0, cellIndex2 = 0;
        if (finType == 1) {
            map = TtkConstants.BANLANCE_SHEET_MAP;
            if ("Apple".equals(enterpriseType)) {
                mapping = TtkConstants.APPLE_BS_MAP;
                if ("SH".equals(region)) {
                    cellIndex = 1;
                    cellIndex2 = 3;
                } else if ("BJ".equals(region)) {
                    cellIndex = 1;
                    cellIndex2 = 2;
                }
            } else if ("Zara".equals(enterpriseType)) {
                mapping = TtkConstants.ZARA_BS_MAP;
                cellIndex = 2;
                cellIndex2 = 3;
            }
        } else if (finType == 2) {
            map = TtkConstants.PL_MAP;
            if ("Apple".equals(enterpriseType)) {
                mapping = TtkConstants.APPLE_PL_MAP;
                if ("SH".equals(region)) {
                    cellIndex = 2;
                    cellIndex2 = 6;
                } else if ("BJ".equals(region)) {
                    cellIndex = 1;
                    cellIndex2 = 6;
                }
            } else if ("Zara".equals(enterpriseType)) {
                mapping = TtkConstants.ZARA_PL_MAP;
                cellIndex = 2;
                cellIndex2 = 3;
            }
        }
        JSONArray jsonArray = JSONUtil.createArray();
        for (Object o : rowlist) {
            List row = (List) o;
            for (int j = 0; j < row.size(); j++) {
                if (null == row.get(j)) {
                    continue;
                }
                String title = row.get(j).toString().trim();
                title = StrUtil.trim(title);
                System.out.println("title:    " + title);
                if (mapping.containsKey(title)) {
                    title = mapping.get(title).trim();
                }
                if ("套期工具".equals(title)) {
                    String colName = ExcelUtil.indexToColName(j + 1);
                    if (colName.toUpperCase().contains("D")) {
                        title = "衍生金融资产";
                    } else if (colName.toUpperCase().contains("N")) {
                        title = "衍生金融负债";
                    }
                }
                if (map.containsKey(title)) {
                    String helpCode = map.get(title);
                    JSONObject jsonObject = JSONUtil.createObj();
                    if (finType == 1) {
                        String val = String.valueOf(row.get(j + cellIndex));
                        String val2 = String.valueOf(row.get(j + cellIndex2));
                        String value1 = NumberUtil.isNumber(val) ? new BigDecimal(val).toPlainString() : "0.00";
                        String value2 = NumberUtil.isNumber(val2) ? new BigDecimal(val2).toPlainString() : "0.00";
                        jsonObject.put("value1", value2);
                        jsonObject.put("value2", value1);
                        // 处理重复的几个名字
                        String index = row.get(j + 1).toString().trim();
                        if ("其中：优先股".equals(title) && "51".equals(index)) {
                            helpCode = "yfzqqzyxg";
                        } else if ("其中：优先股".equals(title) && "63".equals(index)) {
                            helpCode = "qtqygjqzyxg";
                        }
                        if ("永续债".equals(title) && "52".equals(index)) {
                            helpCode = "yfzqyxz";
                        } else if ("永续债".equals(title) && "64".equals(index)) {
                            helpCode = "qtqygjyxz";
                        }
                    } else if (finType == 2) {
                        if (row.size() < j + cellIndex || row.size() < j + cellIndex2) {
                            continue;
                        }
                        String val = String.valueOf(row.get(j + cellIndex));
                        String val2 = String.valueOf(row.get(j + cellIndex2));
                        String value1 = NumberUtil.isNumber(val) ? new BigDecimal(val).toPlainString() : "0.00";
                        String value2 = NumberUtil.isNumber(val2) ? new BigDecimal(val2).toPlainString() : "0.00";
                        jsonObject.put("value1", value1);
                        jsonObject.put("value2", value2);
                    }
                    jsonObject.put("name", title);
                    jsonObject.put("helpCode", helpCode);
                    jsonArray.put(jsonObject);
                }
            }
        }
        return jsonArray;
    }

    /**
     * 小规模财报数据处理
     *
     * @param rowlist 当前行
     * @param finType 财报类型 1. balance sheet 2. P&L
     * @return body
     */
    public static JSONArray smallFinanceRepordHandler(List rowlist, int finType, String region, String enterpriseType) {
        Map<String, String> map = MapUtil.newHashMap();
        Map<String, String> mapping = MapUtil.newHashMap();
        int cellIndex = 0, cellIndex2 = 0;
        if (finType == 1) {
            map = TtkConstants.BANLANCE_SHEET_SMALL_MAP;
            if ("Apple".equals(enterpriseType)) {
                mapping = TtkConstants.APPLE_BS_MAP;
                if ("SH".equals(region)) {
                    cellIndex = 1;
                    cellIndex2 = 3;
                } else if ("BJ".equals(region)) {
                    cellIndex = 1;
                    cellIndex2 = 2;
                }
            } else if ("Zara".equals(enterpriseType)) {
                mapping = TtkConstants.ZARA_BS_SMALL_MAP;
                cellIndex = 2;
                cellIndex2 = 3;
            }
        } else if (finType == 2) {
            map = TtkConstants.PL_SMALL_MAP;
            if ("Apple".equals(enterpriseType)) {
                mapping = TtkConstants.APPLE_PL_MAP;
                if ("SH".equals(region)) {
                    cellIndex = 2;
                    cellIndex2 = 6;
                } else if ("BJ".equals(region)) {
                    cellIndex = 1;
                    cellIndex2 = 6;
                }
            } else if ("Zara".equals(enterpriseType)) {
                mapping = TtkConstants.ZARA_PL_SMALL_MAP;
                cellIndex = 2;
                cellIndex2 = 4;
            }
        }
        JSONArray jsonArray = JSONUtil.createArray();
        for (Object o : rowlist) {
            List row = (List) o;
            for (int j = 0; j < row.size(); j++) {
                if (null == row.get(j)) {
                    continue;
                }
                String title = row.get(j).toString().trim();
                title = StrUtil.trim(title);
                System.out.println("title:    " + title);
                if (mapping.containsKey(title)) {
                    title = mapping.get(title).trim();
                }
                if (map.containsKey(title)) {
                    String helpCode = map.get(title);
                    JSONObject jsonObject = JSONUtil.createObj();
                    if (finType == 1) {
                        String val = String.valueOf(row.get(j + cellIndex));
                        String val2 = String.valueOf(row.get(j + cellIndex2));
                        String value1 = NumberUtil.isNumber(val) ? new BigDecimal(val).toPlainString() : "0.00";
                        String value2 = NumberUtil.isNumber(val2) ? new BigDecimal(val2).toPlainString() : "0.00";
                        jsonObject.put("value1", value2);
                        jsonObject.put("value2", value1);
                    } else if (finType == 2) {
                        if (row.size() < j + cellIndex || row.size() < j + cellIndex2) {
                            continue;
                        }
                        String val = String.valueOf(row.get(j + cellIndex));
                        String val2 = String.valueOf(row.get(j + cellIndex2));
                        String value1 = NumberUtil.isNumber(val) ? new BigDecimal(val).toPlainString() : "0.00";
                        String value2 = NumberUtil.isNumber(val2) ? new BigDecimal(val2).toPlainString() : "0.00";
                        jsonObject.put("value1", value2);
                        jsonObject.put("value2", value1);
                    }
                    jsonObject.put("name", title);
                    jsonObject.put("helpCode", helpCode);
                    jsonArray.put(jsonObject);
                }
            }
        }
        return jsonArray;
    }


    /**
     * 处理sd Excel
     *
     * @param rowIndex
     * @param rowlist
     * @param filingVatEntity
     */
    public static void sdHandler(Integer rowIndex, List rowlist, FilingSD filingSD, Map<String, Integer> map) {
        if (CollUtil.isNotEmpty(map)) {
            // 印花税计税依据（购销合同）金额
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_YHSJSYJGXHTJE.getValue())) {
                String DSJSYJ_YHSJSYJGXHTJE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_YHSJSYJGXHTJE.getValue())));
                filingSD.setDsjsyjYhsjsyjgxhtje("null".equals(DSJSYJ_YHSJSYJGXHTJE) ? "0.00" : DSJSYJ_YHSJSYJGXHTJE);
            } else {
                filingSD.setDsjsyjYhsjsyjgxhtje("0.00");
            }
            // 印花税计税依据（财产租赁合同）金额
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_YHSJSYJCCZLHTJE.getValue())) {
                String DSJSYJ_YHSJSYJCCZLHTJE = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_YHSJSYJCCZLHTJE.getValue())));
                filingSD.setDsjsyjYhsjsyjcczlhtje("null".equals(DSJSYJ_YHSJSYJCCZLHTJE) ? "0.00" : DSJSYJ_YHSJSYJCCZLHTJE);
            } else {
                filingSD.setDsjsyjYhsjsyjcczlhtje("0.00");
            }
            // 工人经费人数
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_GRJFRS.getValue())) {
                String DSJSYJ_GRJFRS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_GRJFRS.getValue())));
                filingSD.setDsjsyjGrjfrs("null".equals(DSJSYJ_GRJFRS) ? "0.00" : DSJSYJ_GRJFRS);
            } else {
                filingSD.setDsjsyjGrjfrs("0.00");
            }
            // 工会经费（工资）
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_GHJFGZ.getValue())) {
                String DSJSYJ_GHJFGZ = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_GHJFGZ.getValue())));
                filingSD.setDsjsyjGhjfgz("null".equals(DSJSYJ_GHJFGZ) ? "0.00" : DSJSYJ_GHJFGZ);
            } else {
                filingSD.setDsjsyjGhjfgz("0.00");
            }
            // 残保金人数
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_CBJRS.getValue())) {
                String DSJSYJ_CBJRS = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_CBJRS.getValue())));
                filingSD.setDsjsyjCbjrs("null".equals(DSJSYJ_CBJRS) ? "0.00" : DSJSYJ_CBJRS);
            } else {
                filingSD.setDsjsyjCbjrs("0.00");
            }
            // 残保金计税依据（工资等）
            if (null != map.get(ExcelTitle.SDRead.DSJSYJ_CBJJSYJGZD.getValue())) {
                String DSJSYJ_CBJJSYJGZD = StrUtil.toString(rowlist.get(map.get(ExcelTitle.SDRead.DSJSYJ_CBJJSYJGZD.getValue())));
                filingSD.setDsjsyjCbjjsyjgzd("null".equals(DSJSYJ_CBJJSYJGZD) ? "0.00" : DSJSYJ_CBJJSYJGZD);
            } else {
                filingSD.setDsjsyjCbjjsyjgzd("0.00");
            }
        }
    }
}

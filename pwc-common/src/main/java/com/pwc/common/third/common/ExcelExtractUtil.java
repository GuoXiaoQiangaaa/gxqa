package com.pwc.common.third.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.RowUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import cn.hutool.poi.excel.cell.CellUtil;
import com.pwc.common.third.request.TaxDataAppleBJInfo;
import com.pwc.common.third.request.TaxDataAppleSHInfo;
import com.pwc.common.third.request.TaxDataBeLLEInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zk
 */
public class ExcelExtractUtil {


    /**
     *
     * @param pdfJSON
     * @param filepath
     * @param sheetName
     * @param format
     * @return
     */
    public static Map<String, Object> extractExcelForAppleBJ(JSONObject pdfJSON, String filepath, String sheetName, String format) {

        Map<String, Object> result = MapUtil.newHashMap();
        //读取Excel
        Workbook workbook = WorkbookUtil.createBook(filepath);
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook,sheetName);

        // by

        BigDecimal by32 = cellVal(sheet,32,"BY");


        BigDecimal by53 = cellVal(sheet,53,"BY");
        BigDecimal by38 = cellVal(sheet,38,"BY");

        JSONArray jsonArray = pdfJSON.getJSONArray("fileExtraResult");

        List<Map<String, Object>> list = CollUtil.newArrayList();
        for (Object obj: jsonArray) {
            JSONObject jsonObject = JSONUtil.parseObj(obj);
            String itemType = jsonObject.getStr("item_type");
            String key =  jsonObject.getStr("key");
            String value = jsonObject.getStr("value");
            //主表
            if ("VAT".equals(format)) {
                String valueType = jsonObject.getStr("value_type");
                if ("normal".equals(itemType) && "end_term_retained_tax".equals(key) && "this_month".equals(valueType)) {
                    //期末留抵税额
                    BigDecimal excelEndTermRetainedTax = new BigDecimal("-1").multiply(by53);
                    BigDecimal pdfEndTermRetainedTax = StrUtil.isNotBlank(value) ? new BigDecimal(value) : new BigDecimal("0");
                    if (excelEndTermRetainedTax.compareTo(pdfEndTermRetainedTax) != 0) {
                        Map<String, Object> endTermRetainedTax = MapUtil.newHashMap();
                        endTermRetainedTax.put("title", "期末留抵税额");
                        endTermRetainedTax.put("excel", excelEndTermRetainedTax);
                        endTermRetainedTax.put("pdf", pdfEndTermRetainedTax);
                        list.add(endTermRetainedTax);
                    }
                }
            } else if ("VAT2".equals(format)) { //附表2
                //本期进项税额转出额
                if ("amount_of_current_input_tax_transferred_out".equals(itemType) && "amount_of_current_input_tax_transferred_out".equals(key)) {
                    BigDecimal excelVal = new BigDecimal("-1").multiply(by38);
                    BigDecimal pdfVal = StrUtil.isNotBlank(value) ? new BigDecimal(value) : new BigDecimal("0");
                    if (excelVal.compareTo(pdfVal) != 0) {
                        Map<String, Object> valMap = MapUtil.newHashMap();
                        valMap.put("title", "本期进项税额转出额");
                        valMap.put("excel", excelVal);
                        valMap.put("pdf", pdfVal);
                        list.add(valMap);
                    }
                }
//                代扣代缴税收缴款凭证
                if ("declare_the_input_tax_deducted_tax".equals(itemType) && "withholding_tax_payment_voucher".equals(key)) {
                    BigDecimal excelVal = by32;
                    BigDecimal pdfVal = StrUtil.isNotBlank(value) ? new BigDecimal(value) : new BigDecimal("0");
                    if (excelVal.compareTo(pdfVal) != 0) {
                        Map<String, Object> valMap = MapUtil.newHashMap();
                        valMap.put("title", "代扣代缴税收缴款凭证");
                        valMap.put("excel", excelVal);
                        valMap.put("pdf", pdfVal);
                        list.add(valMap);
                    }
                }

            }
        }
        result.put("list", list);
        //如果为空对比不通过
        if (CollUtil.isNotEmpty(list)) {
            result.put("compare", "false");
        } else {
            result.put("compare", "true");
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param sheet 需要读取的sheet
     * @param rowIndex 行坐标
     * @param columnName 列名 如 BA
     * @return big decimal
     */
    public static BigDecimal cellVal(Sheet sheet, Integer rowIndex, String columnName) {
        int columnIndex = ExcelUtil.colNameToIndex(columnName);
        Row row = RowUtil.getOrCreateRow(sheet, rowIndex-1);
        Object cell = CellUtil.getCellValue(row.getCell(columnIndex));
        return StrUtil.isBlankIfStr(cell) ? new BigDecimal("0") : new BigDecimal(cell.toString());
    }

    /**
     * 北京
     * @param sheet 需要读取的sheet
     * @param month 月份
     * @param spce 月份间隔
     * @return
     */
    public static TaxDataAppleBJInfo cellValByMonth(Sheet sheet, Integer month, Integer[] spce) {
        Integer baseTaxBaseAmount = 6; // Tax Base Amount
        Integer spceTotal = 0;
        if(month != 1) {
            for (int i = 0; i < month - 1; i++) {
                spceTotal = spceTotal + spce[i];
            }
        }

        int taxBaseAmountIndex = (month - 1) * 5 + spceTotal + baseTaxBaseAmount;
        int qtyIndex = taxBaseAmountIndex - 2;
        int taxAmountIndex = taxBaseAmountIndex + 2;
        TaxDataAppleBJInfo info = new TaxDataAppleBJInfo();
        info.setQty(getValueList(sheet, TaxDataAppleBJInfo.QtyRow, qtyIndex));
        info.setTaxAmount(getValueList(sheet, TaxDataAppleBJInfo.TaxAmountRow, taxAmountIndex));
        info.setTaxBaseAmount(getValueList(sheet, TaxDataAppleBJInfo.TaxBaseAmountRow, taxBaseAmountIndex));
        System.out.println(info.toString());

        return info;
    }

    /**
     * 北京
     * @param sheet 需要读取的sheet
     * @param month 月份
     * @return
     */
    public static TaxDataAppleBJInfo cellValByMonth(Sheet sheet, Integer month) {
        return cellValByMonth(sheet, month, TaxDataAppleBJInfo.SpceRow);
    }

    private static List<BigDecimal> getValueList(Sheet sheet, Integer[] rowData, Integer rowIndex) {
        List<BigDecimal> listData = new ArrayList<>();
        for (int index : rowData){
            Row row = RowUtil.getOrCreateRow(sheet, index - 1);
            Object cell = CellUtil.getCellValue(row.getCell(rowIndex));
            BigDecimal value = StrUtil.isBlankIfStr(cell) ? new BigDecimal("0") : new BigDecimal(cell.toString());
            listData.add(value);
        }
        return listData;
    }

    /**
     * 上海
     * @param sheet 需要读取的sheet
     * @param month 月份
     * @param spce 月份间隔
     * @return
     */
    public static TaxDataAppleSHInfo cellValByMonthForSh(Sheet sheet, Integer month, Integer[] spce) {
        Integer baseTaxBaseAmount = 5; // Tax Base Amount
        Integer spceTotal = 0;
        if(month != 1) {
            for (int i = 0; i < month - 1; i++) {
                spceTotal = spceTotal + spce[i];
            }
        }

        int taxBaseAmountIndex = (month - 1) * 5 + spceTotal + baseTaxBaseAmount;
        int qtyIndex = taxBaseAmountIndex - 2;
        int taxAmountIndex = taxBaseAmountIndex + 2;
        TaxDataAppleSHInfo info = new TaxDataAppleSHInfo();
        info.setQty(getValueList(sheet, TaxDataAppleSHInfo.QtyRow, qtyIndex));
        info.setTaxAmount(getValueList(sheet, TaxDataAppleSHInfo.TaxAmountRow, taxAmountIndex));
        info.setTaxBaseAmount(getValueList(sheet, TaxDataAppleSHInfo.TaxBaseAmountRow, taxBaseAmountIndex));
        System.out.println(info.toString());

        return info;
    }

    /**
     * 上海
     * @param sheet 需要读取的sheet
     * @param month 月份
     * @return
     */
    public static TaxDataAppleSHInfo cellValByMonthForSH(Sheet sheet, Integer month) {
        return cellValByMonthForSh(sheet, month, TaxDataAppleSHInfo.SpceRow);
    }

    /**
     * BeLLE
     * @param sheet 需要读取的sheet
     * @return
     */
    public static TaxDataBeLLEInfo cellValForBeLLEArray0(Sheet sheet, Integer rowIndex) {
        TaxDataBeLLEInfo info = new TaxDataBeLLEInfo();
        info.setStoresEin(String.valueOf(cellValForString(sheet, rowIndex, "A")));
        info.setProvince(String.valueOf(cellValForString(sheet, rowIndex, "B")));
        info.setCity(String.valueOf(cellValForString(sheet, rowIndex, "C")));
        info.setCompanyName(String.valueOf(cellValForString(sheet, rowIndex, "D")));
        info.setYzzzsbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "E")));
        info.setSwjgdkdzzszyfpbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "F")));
        info.setSkqjkjdptfpbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "G")));
        info.setXsczbdcbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "H")));
        info.setSwjgdkdzzszyfpbhsxse1(String.valueOf(cellValForString(sheet, rowIndex, "I")));
        info.setSkqjkjdptfpbhsxse2(String.valueOf(cellValForString(sheet, rowIndex, "J")));
        info.setXssygdysgdzcbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "K")));
        info.setSkqjkjdptfpbhsxse1(String.valueOf(cellValForString(sheet, rowIndex, "L")));
        info.setMsxse(String.valueOf(cellValForString(sheet, rowIndex, "M")));
        info.setXwqymsxse(String.valueOf(cellValForString(sheet, rowIndex, "N")));
        info.setWdqzdxse(String.valueOf(cellValForString(sheet, rowIndex, "O")));
        info.setQtmsxse(String.valueOf(cellValForString(sheet, rowIndex, "P")));
        info.setCkmsxse(String.valueOf(cellValForString(sheet, rowIndex, "Q")));
        info.setSkqjkjdptfpxse1(String.valueOf(cellValForString(sheet, rowIndex, "R")));
        info.setHdxse(String.valueOf(cellValForString(sheet, rowIndex, "S")));
        info.setBqynse(String.valueOf(cellValForString(sheet, rowIndex, "T")));
        info.setHdynse(String.valueOf(cellValForString(sheet, rowIndex, "U")));
        info.setBqynsejze(String.valueOf(cellValForString(sheet, rowIndex, "V")));
        info.setBqmse(String.valueOf(cellValForString(sheet, rowIndex, "W")));
        info.setXwqymse(String.valueOf(cellValForString(sheet, rowIndex, "S")));
        info.setWdqzdmse(String.valueOf(cellValForString(sheet, rowIndex, "Y")));
        info.setYnsehj(String.valueOf(cellValForString(sheet, rowIndex, "Z")));
        info.setBqyjse1(String.valueOf(cellValForString(sheet, rowIndex, "AA")));
        info.setBqybtse(String.valueOf(cellValForString(sheet, rowIndex, "AB")));
        info.setBdcxse(String.valueOf(cellValForString(sheet, rowIndex, "AC")));

        System.out.println(info.toString());
        return info;
    }

    /**
     *
     * @param sheet 需要读取的sheet
     * @param rowIndex 行坐标
     * @param columnName 列名 如 BA
     * @return big decimal
     */
    public static String cellValForString(Sheet sheet, Integer rowIndex, String columnName) {
        int columnIndex = ExcelUtil.colNameToIndex(columnName);
        Row row = RowUtil.getOrCreateRow(sheet, rowIndex - 1);
        Object cell = CellUtil.getCellValue(row.getCell(columnIndex));
        return StrUtil.isBlankIfStr(cell) ? "": cell.toString();
    }

    /**
     * BeLLE
     * @param sheet 需要读取的sheet
     * @return
     */
    public static TaxDataBeLLEInfo cellValForBeLLEArray1(Sheet sheet, Integer rowIndex) {
        TaxDataBeLLEInfo info = new TaxDataBeLLEInfo();
        info.setYzzzsbhsxse(String.valueOf(cellVal(sheet, rowIndex, "AD")));
        info.setSwjgdkdzzszyfpbhsxse(String.valueOf(cellVal(sheet, rowIndex, "AE")));
        info.setSkqjkjdptfpbhsxse(String.valueOf(cellVal(sheet, rowIndex, "AF")));
        info.setXsczbdcbhsxse(String.valueOf(cellVal(sheet, rowIndex, "AG")));
        info.setSwjgdkdzzszyfpbhsxse1(String.valueOf(cellVal(sheet, rowIndex, "AH")));
        info.setSkqjkjdptfpbhsxse2(String.valueOf(cellVal(sheet, rowIndex, "AI")));
        info.setXssygdysgdzcbhsxse(String.valueOf(cellVal(sheet, rowIndex, "AJ")));
        info.setSkqjkjdptfpbhsxse1(String.valueOf(cellVal(sheet, rowIndex, "AK")));
        info.setMsxse(String.valueOf(cellVal(sheet, rowIndex, "AL")));
        info.setXwqymsxse(String.valueOf(cellVal(sheet, rowIndex, "AM")));
        info.setWdqzdxse(String.valueOf(cellVal(sheet, rowIndex, "AN")));
        info.setQtmsxse(String.valueOf(cellVal(sheet, rowIndex, "AO")));
        info.setCkmsxse(String.valueOf(cellVal(sheet, rowIndex, "AP")));
        info.setSkqjkjdptfpxse1(String.valueOf(cellVal(sheet, rowIndex, "AQ")));
        info.setHdxse(String.valueOf(cellVal(sheet, rowIndex, "AR")));
        info.setBqynse(String.valueOf(cellVal(sheet, rowIndex, "AS")));
        info.setHdynse(String.valueOf(cellVal(sheet, rowIndex, "AT")));
        info.setBqynsejze(String.valueOf(cellVal(sheet, rowIndex, "AU")));
        info.setBqmse(String.valueOf(cellVal(sheet, rowIndex, "AV")));
        info.setXwqymse(String.valueOf(cellVal(sheet, rowIndex, "AW")));
        info.setWdqzdmse(String.valueOf(cellVal(sheet, rowIndex, "AX")));
        info.setYnsehj(String.valueOf(cellVal(sheet, rowIndex, "AY")));
        info.setBqyjse1(String.valueOf(cellVal(sheet, rowIndex, "AZ")));
        info.setBqybtse(String.valueOf(cellVal(sheet, rowIndex, "BA")));
        info.setBdcxse(String.valueOf(cellVal(sheet, rowIndex, "BB")));
        System.out.println(info.toString());
        return info;
    }


    /**
     * BeLLE
     * @param sheet 需要读取的sheet
     * @return
     */
    public static List<TaxDataBeLLEInfo> cellValForBeLLeList(Sheet sheet) {
        List<TaxDataBeLLEInfo> listData = new ArrayList<>();
        Integer rowIndex = 3;

        do {
            TaxDataBeLLEInfo info = new TaxDataBeLLEInfo();
            info.setStoresEin(String.valueOf(cellValForString(sheet, rowIndex, "A")));
            info.setProvince(String.valueOf(cellValForString(sheet, rowIndex, "B")));
            info.setCity(String.valueOf(cellValForString(sheet, rowIndex, "C")));
            info.setCompanyName(String.valueOf(cellValForString(sheet, rowIndex, "D")));

            info.setYzzzsbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "E")));
            info.setSwjgdkdzzszyfpbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "F")));
            info.setSkqjkjdptfpbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "G")));
            info.setXsczbdcbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "H")));
            info.setSwjgdkdzzszyfpbhsxse1(String.valueOf(cellValForString(sheet, rowIndex, "I")));
            info.setSkqjkjdptfpbhsxse2(String.valueOf(cellValForString(sheet, rowIndex, "J")));
            info.setXssygdysgdzcbhsxse(String.valueOf(cellValForString(sheet, rowIndex, "K")));
            info.setSkqjkjdptfpbhsxse1(String.valueOf(cellValForString(sheet, rowIndex, "L")));
            info.setMsxse(String.valueOf(cellValForString(sheet, rowIndex, "M")));
            info.setXwqymsxse(String.valueOf(cellValForString(sheet, rowIndex, "N")));
            info.setWdqzdxse(String.valueOf(cellValForString(sheet, rowIndex, "O")));
            info.setQtmsxse(String.valueOf(cellValForString(sheet, rowIndex, "P")));
            info.setCkmsxse(String.valueOf(cellValForString(sheet, rowIndex, "Q")));
            info.setSkqjkjdptfpxse1(String.valueOf(cellValForString(sheet, rowIndex, "R")));
            info.setHdxse(String.valueOf(cellValForString(sheet, rowIndex, "S")));
            info.setBqynse(String.valueOf(cellValForString(sheet, rowIndex, "T")));
            info.setHdynse(String.valueOf(cellValForString(sheet, rowIndex, "U")));
            info.setBqynsejze(String.valueOf(cellValForString(sheet, rowIndex, "V")));
            info.setBqmse(String.valueOf(cellValForString(sheet, rowIndex, "W")));
            info.setXwqymse(String.valueOf(cellValForString(sheet, rowIndex, "S")));
            info.setWdqzdmse(String.valueOf(cellValForString(sheet, rowIndex, "Y")));
            info.setYnsehj(String.valueOf(cellValForString(sheet, rowIndex, "Z")));
            info.setBqyjse1(String.valueOf(cellValForString(sheet, rowIndex, "AA")));
            info.setBqybtse(String.valueOf(cellValForString(sheet, rowIndex, "AB")));
            info.setBdcxse(String.valueOf(cellValForString(sheet, rowIndex, "AC")));

            info.setYzzzsbhsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AD")));
            info.setSwjgdkdzzszyfpbhsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AE")));
            info.setSkqjkjdptfpbhsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AF")));
            info.setXsczbdcbhsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AG")));
            info.setSwjgdkdzzszyfpbhsxse1_1(String.valueOf(cellVal(sheet, rowIndex, "AH")));
            info.setSkqjkjdptfpbhsxse2_1(String.valueOf(cellVal(sheet, rowIndex, "AI")));
            info.setXssygdysgdzcbhsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AJ")));
            info.setSkqjkjdptfpbhsxse1_1(String.valueOf(cellVal(sheet, rowIndex, "AK")));
            info.setMsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AL")));
            info.setXwqymsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AM")));
            info.setWdqzdxse_1(String.valueOf(cellVal(sheet, rowIndex, "AN")));
            info.setQtmsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AO")));
            info.setCkmsxse_1(String.valueOf(cellVal(sheet, rowIndex, "AP")));
            info.setSkqjkjdptfpxse1_1(String.valueOf(cellVal(sheet, rowIndex, "AQ")));
            info.setHdxse_1(String.valueOf(cellVal(sheet, rowIndex, "AR")));
            info.setBqynse_1(String.valueOf(cellVal(sheet, rowIndex, "AS")));
            info.setHdynse_1(String.valueOf(cellVal(sheet, rowIndex, "AT")));
            info.setBqynsejze_1(String.valueOf(cellVal(sheet, rowIndex, "AU")));
            info.setBqmse_1(String.valueOf(cellVal(sheet, rowIndex, "AV")));
            info.setXwqymse_1(String.valueOf(cellVal(sheet, rowIndex, "AW")));
            info.setWdqzdmse_1(String.valueOf(cellVal(sheet, rowIndex, "AX")));
            info.setYnsehj_1(String.valueOf(cellVal(sheet, rowIndex, "AY")));
            info.setBqyjse1_1(String.valueOf(cellVal(sheet, rowIndex, "AZ")));
            info.setBqybtse_1(String.valueOf(cellVal(sheet, rowIndex, "BA")));
            info.setBdcxse_1(String.valueOf(cellVal(sheet, rowIndex, "BB")));
            listData.add(info);
            rowIndex ++;
        } while (!cellValForString(sheet, rowIndex - 1, "A").isEmpty());
        
        System.out.println(listData);

        return listData;
    }


    /**
     *
     * @param sheet 需要读取的sheet
     * @param rowIndex 行坐标
     * @param columnName 列名 如 BA
     * @return string
     */
    public static String cellValStr(Sheet sheet, Integer rowIndex, String columnName) {
        return cellVal(sheet,rowIndex,columnName).toPlainString();
    }
}
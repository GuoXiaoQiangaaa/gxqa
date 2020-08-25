import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.RowUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import cn.hutool.poi.excel.cell.CellUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.math.BigDecimal;

public class ExcelTest {

    @Test
    public void CellTest() {
//        System.out.println(RowUtil.getOrCreateRow());
        Workbook workbook = WorkbookUtil.createBook("/Users/zk/Downloads/测试2019-10.xls");
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook,"SH_D151_VAT Review 2019");
        //主表

        // bv
        BigDecimal bv11 = cellVal(sheet,11,"BV");
        BigDecimal bv27 = cellVal(sheet,27,"BV");
        BigDecimal bv44 = cellVal(sheet,44,"BV");
        BigDecimal bv13 = cellVal(sheet,13,"BV");
        BigDecimal bv29 = cellVal(sheet,29,"BV");
        BigDecimal bv46 = cellVal(sheet,46,"BV");
        BigDecimal bv15 = cellVal(sheet,15,"BV");
        BigDecimal bv31 = cellVal(sheet,31,"BV");
        BigDecimal bv48 = cellVal(sheet,48,"BV");
        BigDecimal bv61 = cellVal(sheet,61,"BV");
        BigDecimal bv64 = cellVal(sheet,64,"BV");
        BigDecimal bv67 = cellVal(sheet,67,"BV");
        //BX
        BigDecimal bx11 = cellVal(sheet,11,"BX");
        BigDecimal bx27 = cellVal(sheet,27,"BX");
        BigDecimal bx44 = cellVal(sheet,44,"BX");
        BigDecimal bx13 = cellVal(sheet,13,"BX");
        BigDecimal bx29 = cellVal(sheet,29,"BX");
        BigDecimal bx46 = cellVal(sheet,46,"BX");
        BigDecimal bx15 = cellVal(sheet,15,"BX");
        BigDecimal bx31 = cellVal(sheet,31,"BX");
        BigDecimal bx48 = cellVal(sheet,48,"BX");
        BigDecimal bx61 = cellVal(sheet,61,"BX");
        BigDecimal bx64 = cellVal(sheet,64,"BX");
        BigDecimal bx67 = cellVal(sheet,67,"BX");
        BigDecimal bx74 = cellVal(sheet,74,"BX");
        BigDecimal bx88 = cellVal(sheet,88,"BX");
        BigDecimal bx91 = cellVal(sheet,91,"BX");
        BigDecimal bx94 = cellVal(sheet,94,"BX");
        BigDecimal bx111 = cellVal(sheet,111,"BX");
        BigDecimal bx101 = cellVal(sheet,101,"BX");
        BigDecimal bx102 = cellVal(sheet,102,"BX");
        BigDecimal bx103 = cellVal(sheet,103,"BX");
        BigDecimal bx104 = cellVal(sheet,104,"BX");
        //A
        BigDecimal a9 = cellVal(sheet,9,"A");
        BigDecimal a10 = cellVal(sheet,10,"A");

//        BV11+BV27+BV44+ BV13+BV29+BV46+BV15+BV31+BV48+BV61+BV64+BV67  json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘asysljsxse’]
        BigDecimal asysljsxse = bv11.add(bv27).add(bv44).add(bv13).add(bv29).add(bv46).add(bv15).add(bv31).add(bv48).add(bv61).add(bv64).add(bv67);
        System.out.println("asysljsxse :"+asysljsxse.toPlainString());

//        BV11+BV27+BV44+ BV13+BV29+BV46+BV15+BV31+BV48   json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘yshwxse’]
        BigDecimal yshwxse = bv11.add(bv27).add(bv44).add(bv13).add(bv29).add(bv46).add(bv15).add(bv31).add(bv48);
        System.out.println("yshwxse :"+yshwxse.toPlainString());

//        BV61+BV64+BV67 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘yslwxse’]
        BigDecimal yslwxse = bv61.add(bv64).add(bv67);
        System.out.println("yslwxse :"+yslwxse.toPlainString());

//        BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48+BX61+BX64+BX67  json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘xxse’]
        BigDecimal xxse = bx11.add(bx27).add(bx44).add(bx13).add(bx29).add(bx46).add(bx15).add(bx31).add(bx48).add(bx61).add(bx64).add(bx67);
        System.out.println("xxse :"+xxse.toPlainString());

//        BX74+BX88+BX91+BX94 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][‘jxse’]
        BigDecimal jxse = bx74.add(bx88).add(bx91).add(bx94);
        System.out.println("jxse :"+jxse.toPlainString());

//        (-1) * BX111   json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][’sqldse’]
        BigDecimal sqldse = new BigDecimal("-1").multiply(bx111);
        System.out.println("sqldse :"+sqldse.toPlainString());

//        BX101+BX102+BX103+BX104 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0][’jxsezc’]
        BigDecimal jxsezc = bx101.add(bx102).add(bx103).add(bx104);
        System.out.println("jxsezc :"+jxsezc.toPlainString());

//        BX74+BX88+BX91+BX94+BX111 - (BX101+BX102+BX103+BX104)  json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['ydksehj’]
        BigDecimal ydksehj = bx74.add(bx88).add(bx91).add(bx94).add(bx111).subtract(jxsezc);
        System.out.println("ydksehj :"+ydksehj.toPlainString());

//        json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['sjdkse’]
//                                 ydksehj                             <                          xxse
//        "if ((BX74+BX88+BX91+BX94+BX111 - (BX101+BX102+BX103+BX104)) < (BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48+BX61+BX64+BX67)
//        {       return (BX74+BX88+BX91+BX94+BX111 - (BX101+BX102+BX103+BX104))      }
//          else
//        {       return (BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48+BX61+BX64+BX67)      }"
        BigDecimal sjdkse = ydksehj.compareTo(xxse) < 0 ? ydksehj : xxse;
        System.out.println("sjdkse :"+sjdkse.toPlainString());

//        A9 - A10的返回值 json[‘ybData’][‘zzssyyybnsrzb’][‘zbGrid’][‘zbGridlbVO’][0]['qmldse’]
        BigDecimal qmldse = a9.subtract(a10);
        System.out.println("qmldse :"+qmldse.toPlainString());

        //附表1
//        BV11+BV27+BV44 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjskzzszyfpXse’]
        BigDecimal kjskzzszyfpXse = bv11.add(bv27).add(bv44);
        System.out.println("kjskzzszyfpXse :"+kjskzzszyfpXse.toPlainString());

//        BX11+BX27+BX44  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjskzzszyfpXxynse’]
        BigDecimal kjskzzszyfpXxynse = bx11.add(bx27).add(bx44);
        System.out.println("kjskzzszyfpXxynse :"+kjskzzszyfpXxynse.toPlainString());

//        BV13+BV29+BV46 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][’kjqtfpXse’]
        BigDecimal kjqtfpXse = bv13.add(bv29).add(bv46);
        System.out.println("kjqtfpXse :"+kjqtfpXse.toPlainString());

//        BX13+BX29+BX46  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘kjqtfpXxynse’]
        BigDecimal kjqtfpXxynse = bx13.add(bx29).add(bx46);
        System.out.println("kjqtfpXxynse :"+kjqtfpXxynse.toPlainString());

//        BV15+BV31+BV48  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][’wkjfpXse’]
        BigDecimal wkjfpXse = bv15.add(bv31).add(bv48);
        System.out.println("wkjfpXse :" + wkjfpXse);

//        BX15+BX31+BX48  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘wkjfpXxynse’]
        BigDecimal wkjfpXxynse = bx15.add(bx31).add(bx48);
        System.out.println("wkjfpXxynse :"+wkjfpXxynse.toPlainString());

//        BV11+BV27+BV44+ BV13+BV29+BV46+BV15+BV31+BV48  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0][‘xse’]
        BigDecimal xse = bv11.add(bv27).add(bv44);
        System.out.println("xse :"+xse.toPlainString());

//        BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0]['hjXxynse']
        BigDecimal hjXxynse = bx11.add(bx27).add(bx44).add(bx13).add(bx29).add(bx46).add(bx15).add(bx31).add(bx48);
        System.out.println("hjXxynse :" + hjXxynse);

//        BV11+BV27+BV44+ BV13+BV29+BV46+BV15+BV31+BV48+BX11+BX27+BX44+ BX13+BX29+BX46+BX15+BX31+BX48 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][0]['jshj']
        BigDecimal jshj = yshwxse.add(hjXxynse);
        System.out.println("jshj :"+jshj.toPlainString());

//        BV61  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjskzzszyfpXse’]
        BigDecimal kjskzzszyfpXse5 = bv61;
        System.out.println("kjskzzszyfpXse5 :" + kjskzzszyfpXse5.toPlainString());

//        BX61  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjskzzszyfpXxynse’]
        BigDecimal kjskzzszyfpXxynse5 = bx61;
        System.out.println("kjskzzszyfpXxynse5 :" + kjskzzszyfpXxynse5.toPlainString());

//        BV64  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][’kjqtfpXse’]
        BigDecimal kjqtfpXse5 = bv64;
        System.out.println("kjqtfpXse5 :" + kjqtfpXse5.toPlainString());

//        BX64  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘kjqtfpXxynse’]
        BigDecimal kjqtfpXxynse5 = bx64;
        System.out.println("kjqtfpXxynse5 :"+kjqtfpXxynse5.toPlainString());

//        BV67 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][’wkjfpXse’]
        BigDecimal wkjfpXse5 =bv67;
        System.out.println("wkjfpXse5 :"+wkjfpXse5.toPlainString());

//        BX67  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘wkjfpXxynse’]
        BigDecimal wkjfpXxynse5 = bx67;
        System.out.println("wkjfpXxynse5 :" + wkjfpXxynse5.toPlainString());

//        BV61+BV64+BV67   json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5][‘xse’]
        BigDecimal xse5 = bv61.add(bv64).add(bv67);
        System.out.println("xse5 :"+xse5.toPlainString());

//        BX61+BX64+BX67  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['hjXxynse']
        BigDecimal hjXxynse5 = bx61.add(bx64).add(bx67);
        System.out.println("hjXxynse5 :" + hjXxynse5.toPlainString());

//        BV61+BV64+BV67+BX61+BX64+BX67  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['jshj']
        BigDecimal jshj5 = bv61.add(bv64).add(bv67).add(bx61).add(bx64).add(bx67);
        System.out.println("jshj5 :"+jshj5.toPlainString());

//        BV61+BV64+BV67+BX61+BX64+BX67 json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['kchHsmsxse']
        BigDecimal kchHsmsxse = jshj5;
        System.out.println("kchHsmsxse :"+kchHsmsxse.toPlainString());

//        (BV61+BV64+BV67+BX61+BX64+BX67)/1.06*0.06  json[‘ybData’][‘zzssyyybnsr01bqxsqkmxb’][‘bqxsqkmxbGrid’][‘bqxsqkmxbGridlbVO’][5]['kchXxynse']
        BigDecimal kchXxynse = kchHsmsxse.divide(new BigDecimal("1.06"),2).multiply(new BigDecimal("0.06"));
        System.out.println("kchXxynse :"+kchXxynse.toPlainString());

    }


    /**
     *
     * @param sheet 需要读取的sheet
     * @param rowIndex 行坐标
     * @param columnName 列名 如 BA
     * @return
     */
    public BigDecimal cellVal(Sheet sheet, Integer rowIndex, String columnName) {
        int columnIndex = ExcelUtil.colNameToIndex(columnName);
        Row row = RowUtil.getOrCreateRow(sheet, rowIndex-1);
        Object cell = CellUtil.getCellValue(row.getCell(columnIndex));
        return StrUtil.isBlankIfStr(cell) ? new BigDecimal("0") : new BigDecimal(cell.toString());
    }
}

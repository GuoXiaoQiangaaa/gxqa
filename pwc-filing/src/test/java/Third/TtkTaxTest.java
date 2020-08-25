package Third;

import cn.hutool.poi.excel.WorkbookUtil;
import com.pwc.common.third.TtkTaxUtil;
import com.pwc.common.third.common.ExcelExtractUtil;
import com.pwc.common.third.response.TtkResponse;
import com.pwc.common.third.response.TtkTaxResultResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TtkTaxTest {

    //生产id 斯特拉迪瓦里斯商业(上海)有限公司福州浦上大道分公司
    private final Long orgId_erp = 242990792396800L;

    //生产demo 斯特拉迪瓦里斯商业(上海)有限公司福州浦上大道分公司
    private final Long orgId_erp_demo = 242243439903296L;

    //test 斯特拉迪瓦里斯商业(上海)有限公司福州浦上大道分公司
    private final Long orgId_test = 242249092731072L;
    // 上海地区账号 test环境
    private final Long orgId_test_sh = 91310114631386907L;
    //    苹果电脑贸易（上海）有限公司
    private final Long orgId_test_apple = 243306356408384L;
    //苹果北京
    private final Long orgId_prod_apple_bj = 244591359889408L;

    private Long orgId = orgId_prod_apple_bj;

    ///Users/zk/work/shinetech/pwc/filing-platform/pwc-filing/target/test-classes/taxJson/small_taxpayer.json
    @Test
    public void testWriteValueAddedTaxData(){
        TtkTaxUtil.writeValueAddedTaxData();
    }

    /**
     * 查询申报结果
     */
    @Test
    public void testGetTaxResult() {
        TtkResponse<TtkTaxResultResponse> res = TtkTaxUtil.getTaxResult(0L, orgId, "2019", "11");
        System.out.println(res.toString());
    }

    @Test
    public void testGetWebUrlForShenBao() {
        String url = TtkTaxUtil.getWebUrlForShenBao(orgId,null,null,null);
        System.out.println("url: "+url);
        String url1 = url.replace("page=ttk-taxapply-app-taxlist", "page=ttk-tax-app-robot-declare-payment");
        System.out.println("url1: "+url1);
    }

    @Test
    public void testDownloadTaxReportXML() {
     TtkTaxUtil.downloadTaxReportXML(orgId,"BDA0610606", "2019-11-01","2019-11-30");
    }

    @Test
    public void testDownloadTaxReportPDF() {
        Long orgId = 264042385690816L;
        String simpleName = "ZARA6441";
        String vatTaxpayer = "2000010001";
        List<String> taxTypeList = new ArrayList<String>() {{
           add("VAT");
           add("Surtax");
           add("SD");
        }};
        for (String taxType : taxTypeList) {
            String yzpzzldm = "";
            if (taxType.equals("VAT")) {
                if ("2000010001".equals(vatTaxpayer)) {
                    yzpzzldm = "BDA0610606";
                } else if ("2000010002".equals(vatTaxpayer)) {
                    yzpzzldm = "BDA0610611";
                }
            } else if (taxType.equals("Surtax")) {
                yzpzzldm = "BDA0610678";
            } else if (taxType.equals("SD")) {
                yzpzzldm = "BDA0610794";
            }
            TtkTaxUtil.downloadTaxReportPDF(orgId,2020, "2020-05-01","2020-05-31", yzpzzldm, taxType, simpleName);

        }
    }


    @Test
    public void testBase64() {

//        TtkTaxUtil.base64StringToPdf(base64,"/Users/zk");

//        Base64.decode
//        System.out.println(Base64.decodeStr(base64));;
    }

    @Test
    public void testWriteFinanceReport(){
        TtkTaxUtil.writeFinancialReportData(orgId, "911100006684048308", "苹果电脑贸易（北京）有限公司", "/Users/zk/Downloads/apple_北京_fs.xls");
//        TtkTaxUtil.writeFinancialReportData(orgId, "911100006684048308", "ZARA贸易（北京）有限公司", "/Users/zk/Downloads/ZARA/1456 201906 BalanceReport.xls");
    }

    @Test
    public void testWriteSD() {
        TtkTaxUtil.writeValueAddedSdDataForApple(orgId, "/Users/zk/Downloads/apple_sh_sd.xlsx", "D151_SD");
    }

    @Test
    public void testParser() {
//        System.out.println(ExcelExtractUtil.cellValByMonth(10));
        TtkTaxUtil.writeValueAddedTaxDataGeneralForAppleBJ(orgId, "/Users/zk/Downloads/苹果电子产品商贸（北京）有限公司_911100006684048308_VAT_202001.xls", "VAT");
    }

    @Test
    public void testApply2018CorporateTaxTypeAForAppleBJ(){
//        TtkTaxUtil.apply2018CorporateTaxTypeAForAppleBJ(orgId,"/Users/heapow/Downloads/D183 CIT.xlsx");
//        TtkTaxUtil.apply2018CorporateTaxTypeAForAppleBJ(orgId,"/Users/heapow/Downloads/D183 CIT.xlsx", 2019, 11);
//        TtkTaxUtil.writeValueAddedTaxDataGeneralForBeLLE(orgId,"/Users/louxin/Downloads/NewBelle小规模纳税人.xlsx", "VAT", 4);
        Workbook workbook = WorkbookUtil.createBook("/Users/zk/Downloads/NewBelle小规模纳税人.xlsx");
        Sheet sheet = WorkbookUtil.getOrCreateSheet(workbook,"VAT");
        ExcelExtractUtil.cellValForBeLLeList(sheet);
    }
}

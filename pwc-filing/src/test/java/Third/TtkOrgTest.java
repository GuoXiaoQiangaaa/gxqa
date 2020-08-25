package Third;

import com.alibaba.fastjson.JSONObject;
import com.pwc.common.third.TtkOrgUtil;
import com.pwc.common.third.common.CityCode;
import com.pwc.common.third.common.TtkConstants;
import com.pwc.common.third.request.TaxLoginInfo;
import com.pwc.common.third.request.TtkOrgRequest;
import com.pwc.common.third.request.TtkTaxLoginInfoRequest;
import com.pwc.common.third.response.TtkOrgResponse;
import com.pwc.common.third.response.TtkResponse;
import com.ttk.jchl.openapi.sdk.TtkOpenAPI;
import org.junit.Test;

import java.util.Arrays;


public class TtkOrgTest {

    private final Long orgId_dev_debug = 241571900840320L;
    //生产id 斯特拉迪瓦里斯商业(上海)有限公司福州浦上大道分公司
    private final Long orgId_erp = 242990792396800L;

    //生产demo 斯特拉迪瓦里斯商业(上海)有限公司福州浦上大道分公司
    private final Long orgId_erp_demo = 242243439903296L;

    //test 斯特拉迪瓦里斯商业(上海)有限公司福州浦上大道分公司
    private final Long orgId_test = 242249092731072L;

//    苹果电脑贸易（上海）有限公司
    private final Long orgId_erp_apple = 243305283969152L;

//    苹果电脑贸易（上海）有限公司
    private final Long orgId_test_apple = 243306356408384L;




    private Long orgId = orgId_erp;

    @Test
    public void testCreateOrg() {
        TtkOrgRequest request = new TtkOrgRequest();
//        request.setName("斯特拉迪瓦里斯商业(上海)有限公司福州浦上大道分公司");
//        request.setAccountingStandards(2000020001L);
//        request.setEnabledMonth("1");
//        request.setEnabledYear("2019");
//        request.setVatTaxpayer(2000010001L);
        request.setName("佛山信森佳商贸有限公司");
        request.setAccountingStandards(2000020002L);
        request.setEnabledMonth("1");
        request.setEnabledYear("2020");
        request.setVatTaxpayer(2000010002L);

        //可选
//        request.setTaxReportAccessTypeDtoList(Arrays.asList(new TaxReportAccessType()));

        TtkResponse<TtkOrgResponse> response = TtkOrgUtil.createOrg(request);
        System.out.println(response.toString());
    }

    @Test
    public void testGetOrg(){
        TtkResponse<TtkOrgResponse> response = TtkOrgUtil.getOrg(264041862578496L);
        System.out.println(response.toString());
    }

    @Test
    public void testSaveTaxLoginInfo() {
        TtkTaxLoginInfoRequest request = new TtkTaxLoginInfoRequest();
//        request.setId(this.orgId);
//        request.setId(244240179003392L);
        request.setId(249147891752960L);
        request.setReturnValue(true);
        TaxLoginInfo loginInfo = new TaxLoginInfo();
        loginInfo.setDLFS("6");
        loginInfo.setNSRSBH("91440204MA53CKELX1");
        loginInfo.setQYMC("韶关市丽百商贸有限公司");
        loginInfo.setSS(CityCode.SHANDONG.getValue());
        loginInfo.setCanChange(false);
        loginInfo.setDLZH("13411141116");
        loginInfo.setDLMM("Yc112903");
        loginInfo.setGssbmm("");
//        loginInfo.setDLFS("1");
//        loginInfo.setNSRSBH("91310000607426876P");
//        loginInfo.setQYMC("苹果电脑贸易（上海）有限公司");
//        loginInfo.setSS(CityCode.SHANGHAI.getValue());
//        loginInfo.setCanChange(false);
//        loginInfo.setDLZH("");
//        loginInfo.setDLMM("");
//        loginInfo.setGssbmm("");
        request.setDlxxDto(loginInfo);
        System.out.println(request.toString());
        TtkResponse<String> response = TtkOrgUtil.saveTaxLoginInfo(request);
        System.out.println(response.toString());
    }

    @Test
    public void testHasReadSJInfo() {
        TtkResponse<String> response = TtkOrgUtil.hasReadSJInfo(orgId);
        System.out.println(response.toString());
    }

    @Test
    public void testGetWebUrl() {
        System.out.println(TtkOrgUtil.getWebUrlForCompanyInformation(orgId));
    }

    @Test
    public void testUpdateOrg() {
        TtkOrgRequest request = new TtkOrgRequest();
        request.setOrgId(orgId);
        request.setName("1125测试公司");
        request.setAccountingStandards(2000020007L);
        request.setEnabledMonth("12");
        request.setEnabledYear("2039");
        request.setVatTaxpayer(2000010001L);

        TtkResponse<String> response = TtkOrgUtil.updateOrg(request);
        System.out.println(response.toString());
    }



    @Test
    public void queryAccountStandardId(){
        TtkOpenAPI ttkOpenAPI = TtkOpenApiClientFactory.getTtkOpenAPI();

        String json = "{\"orgId\":281198021949440,\"year\":2020,\"month\":7}";

        JSONObject jsonObject = ttkOpenAPI.queryAccountStandardId(json);

        String errorCode = jsonObject.getJSONObject("head").getString("errorCode");

        if (!"0".equals(errorCode)) {

            System.out.println("错误:"+errorCode+"，"+jsonObject.getJSONObject("head").getString("errorMsg"));

        } else {

            System.out.println(jsonObject.get("body").toString());

        }
    }
}

class TtkOpenApiClientFactory {

    private static String apiHost = TtkConstants.API_HOST;
    private static String webHost = TtkConstants.WEB_HOST;
    private static String appKey = TtkConstants.APP_KEY;
    private static String appSecret = TtkConstants.APP_SECRET;
    private static TtkOpenAPI ttkOpenAPI = new TtkOpenAPI(apiHost, appKey, appSecret, webHost);

    public static TtkOpenAPI getTtkOpenAPI(){
        return ttkOpenAPI;
    }

}
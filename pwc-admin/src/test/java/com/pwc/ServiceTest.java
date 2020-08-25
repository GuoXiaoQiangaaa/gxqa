package com.pwc;

import com.pwc.common.third.TtkTaxUtil;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {
    @Autowired
    private SysDeptService sysDeptService;

    @Test
    public void testGetPdf(){
        List<Map<String,String>> taxCodes = new ArrayList<Map<String,String>>() {{
            add(new HashMap<String, String>(){{put("taxcode","91440605084524441N");put("simplename","ZARA6441");}});
            add(new HashMap<String, String>(){{put("taxcode","914420000685137422");put("simplename","ZARA1365");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101565974259D");put("simplename","MD4691");}});
            add(new HashMap<String, String>(){{put("taxcode","9144010156794116X6");put("simplename","BSK8789");}});
            add(new HashMap<String, String>(){{put("taxcode","914401015697751341");put("simplename","ZARA9194");}});
            add(new HashMap<String, String>(){{put("taxcode","9144010156977510XB");put("simplename","STR7641");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101567916060K");put("simplename","ZARA9143");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101574008714H");put("simplename","PB5794");}});
            add(new HashMap<String, String>(){{put("taxcode","914401015799979960");put("simplename","OY7128");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101581890776X");put("simplename","HOME1661");}});
            add(new HashMap<String, String>(){{put("taxcode","91440113058909233T");put("simplename","ZARA1457");}});
            add(new HashMap<String, String>(){{put("taxcode","91440113058909268D");put("simplename","HOME1819");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101058909217Q");put("simplename","OY7927");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101061148930N");put("simplename","ZARA9301");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101061148949K");put("simplename","PB6263");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101061148957E");put("simplename","STR7338");}});
            add(new HashMap<String, String>(){{put("taxcode","91440113321054261K");put("simplename","MD4696");}});
            add(new HashMap<String, String>(){{put("taxcode","91440113321053832E");put("simplename","ZARA6311");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101347394977J");put("simplename","MD4067");}});
            add(new HashMap<String, String>(){{put("taxcode","9144010134017527X6");put("simplename","ZARA3764");}});
            add(new HashMap<String, String>(){{put("taxcode","9144050007020085XQ");put("simplename","STR7293");}});
            add(new HashMap<String, String>(){{put("taxcode","91441300584673013J");put("simplename","MD4733");}});
            add(new HashMap<String, String>(){{put("taxcode","9144130058467303X5");put("simplename","PB5838");}});
            add(new HashMap<String, String>(){{put("taxcode","91441300584672897D");put("simplename","OY7122");}});
            add(new HashMap<String, String>(){{put("taxcode","91441300584672766H");put("simplename","BSK8840");}});
            add(new HashMap<String, String>(){{put("taxcode","91441300584673259L");put("simplename","ZARA9274");}});
            add(new HashMap<String, String>(){{put("taxcode","91440101MA5CCH0L00");put("simplename","OYHO12702");}});
            add(new HashMap<String, String>(){{put("taxcode","91610000593311866T");put("simplename","MD4058");}});
            add(new HashMap<String, String>(){{put("taxcode","91610133397115024U");put("simplename","ZARAHOME5982");}});
            add(new HashMap<String, String>(){{put("taxcode","91610000593311882G");put("simplename","ZARA1383");}});
            add(new HashMap<String, String>(){{put("taxcode","916100000817259189");put("simplename","OYSHO4945");}});
            add(new HashMap<String, String>(){{put("taxcode","91610104561460437J");put("simplename","ZARA9142");}});
            add(new HashMap<String, String>(){{put("taxcode","91610132MA6TXUP48Y");put("simplename","BSK10943");}});
            add(new HashMap<String, String>(){{put("taxcode","91610132MA6TXUP64L");put("simplename","MD10894");}});
            add(new HashMap<String, String>(){{put("taxcode","91610132MA6TXUP80A");put("simplename","OYSHO10883");}});
            add(new HashMap<String, String>(){{put("taxcode","91610132MA6TXUP56R");put("simplename","STR10269");}});
            add(new HashMap<String, String>(){{put("taxcode","91610132MA6TXUP3XY");put("simplename","ZARAHOME10643");}});
            add(new HashMap<String, String>(){{put("taxcode","91610132MA6TXT2C4C");put("simplename","ZARA10805");}});
            add(new HashMap<String, String>(){{put("taxcode","91610104MA6TYPGQ2J");put("simplename","OYSHO11764");}});
            add(new HashMap<String, String>(){{put("taxcode","91610104MA6TYLLN97");put("simplename","PB11770");}});
            add(new HashMap<String, String>(){{put("taxcode","91610104MA6TYMKB77");put("simplename","ZARA11592");}});
            add(new HashMap<String, String>(){{put("taxcode","91610131MA6U903548");put("simplename","ZARAHOME12369");}});
            add(new HashMap<String, String>(){{put("taxcode","91610131MA6U907767");put("simplename","ZARA12315");}});
            add(new HashMap<String, String>(){{put("taxcode","91610131MA6U907174");put("simplename","ZARA12433");}});
            add(new HashMap<String, String>(){{put("taxcode","91610131MA6U90792W");put("simplename","OYSHO12405");}});
            add(new HashMap<String, String>(){{put("taxcode","91610133MA6W611L0F");put("simplename","ZARAHOME12885");}});
            add(new HashMap<String, String>(){{put("taxcode","91610133MA6W612M5M");put("simplename","ZARA12775");}});
            add(new HashMap<String, String>(){{put("taxcode","91610133MA6W61395Y");put("simplename","OYSHO12845");}});
            add(new HashMap<String, String>(){{put("taxcode","91610133MA6W615G5H");put("simplename","MD12915");}});
            add(new HashMap<String, String>(){{put("taxcode","91610133MA6W617W9M");put("simplename","BSK12826");}});
        }};
        List<String> taxTypeList = new ArrayList<String>() {{
            add("VAT");
            add("Surtax");
            add("SD");
        }};
        for (Map<String, String> map: taxCodes) {

            SysDeptEntity sysDeptEntity = sysDeptService.getByTaxCode(map.get("taxcode"));
            if (null != sysDeptEntity) {
                Long orgId = sysDeptEntity.getThirdOrgId();
                String simpleName = map.get("simplename");
                String vatTaxpayer = sysDeptEntity.getThirdVatTaxpayer();
                try {
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
                        TtkTaxUtil.downloadTaxReportPDF(orgId,2020, "2020-06-01","2020-06-30", yzpzzldm, taxType, simpleName);
                    }
                } catch (Exception e) {
                    continue;
                }

            }
        }
    }
}

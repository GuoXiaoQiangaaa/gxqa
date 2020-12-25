package com.pwc;

import cn.hutool.json.JSONUtil;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.customer.entity.CustomerEntity;
import com.pwc.modules.customer.service.CustomerService;
import com.pwc.modules.output.service.OutputInvoiceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerTest {
    Logger logger = LoggerFactory.getLogger(CustomerTest.class);
    @Autowired
    private OutputInvoiceService outputInvoiceService;

    @Test
    public void selectAll(){
        HashMap<String, Object> parameter = new HashMap<>();
        parameter.put("invoice_id", "1");
        parameter.put("apply_number", "1");
        parameter.put("agreement","");
        parameter.put("apply_user","admin");
        parameter.put("apply_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        parameter.put("purchase_name","AAA有限公司");
        parameter.put("seller_name","CBC-SH");
        parameter.put("invoice_type","增值随专用发票");
        parameter.put("invoice_entity","纸质发票");
        parameter.put("Invoice_requisition_status","2");
        parameter.put("invoice_status","1");
        PageUtils customerEntityList = outputInvoiceService.queryPage(parameter);
        logger.info("打印请求结果：Result{}", customerEntityList);
    }
}

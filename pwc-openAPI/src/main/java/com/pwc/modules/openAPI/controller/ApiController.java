package com.pwc.modules.openAPI.controller;


import com.pwc.common.utils.*;
import com.pwc.modules.openAPI.service.ApiRequestCountService;
import com.pwc.modules.openAPI.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * openAPI
 */
@RestController
@RequestMapping("/openAPI")
public class ApiController {

    @Autowired
    private ApiService apiService;
    @Autowired
    private ApiRequestCountService apiRequestCountService;

    /**
     * 根据四要素验真
     */
    @RequestMapping("/invoiceCheckTrue")
    public R checkTrue(@RequestParam Map<String, Object> params) {
        return apiService.invoiceCheck(params);
    }


    /**
     * 获取企业请求接口次数
     */
    @RequestMapping("/getRequestCount")
    public R getRequestCount(@RequestParam Map<String, Object> params){
        PageUtils page = apiRequestCountService.queryPage(params);
        return R.ok().put("list",page);
    }
    /**
     * 调用增值税发票OCR接口
     */
    @RequestMapping("/vatInvoice")
    public R vatInvoice(@RequestParam Map<String, Object> params, @RequestParam("file") MultipartFile file){
        return  apiService.vatInvoice(params,file);
    }
}

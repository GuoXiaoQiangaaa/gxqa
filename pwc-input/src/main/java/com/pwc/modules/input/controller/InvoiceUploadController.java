package com.pwc.modules.input.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;
import com.pwc.modules.input.service.InputInvoiceUploadService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description: 票据上传
 * @author: Gxw
 * @create: 2020-09-03 16:10
 **/
@RestController
@RequestMapping("/input/invoiceupload")
public class InvoiceUploadController {
    @Autowired
    private  InputInvoiceUploadService  inputInvoiceUploadService;

    // 查询
    @RequestMapping("/findUpload")
//    @RequiresPermissions("input:invoiceupload:findUpload")
    public R findUpload(@RequestParam Map<String, Object> params) {
        PageUtils page = inputInvoiceUploadService.findUploadList(params);
        return R.ok().put("page",page);
    }
    // 补录
    @RequestMapping("/updateUpload")
    //    @RequiresPermissions("input:invoiceupload:updateUpload")
    public R updateUpload(@RequestBody InputInvoiceUploadEntity uploadEntity) {
        inputInvoiceUploadService.updateById(uploadEntity);
        return R.ok();
    }

}

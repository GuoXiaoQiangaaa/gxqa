package com.pwc.modules.input.controller;

import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputInvoicePoEntity;
import com.pwc.modules.input.service.InputInvoicePoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description: Po票据
 * @author: Gxw
 * @create: 2020-09-03 15:07
 **/
@RestController
@RequestMapping("/input/invoicepo")
public class InvoicePoController {

    @Autowired
    private InputInvoicePoService inputInvoicePoService;

    /**
     * po总览
     * @param params
     * @return
     */
    @RequestMapping("/findPoList")
    @RequiresPermissions("input:invoicepo:findPoList")
    public R findPoList(@RequestParam Map<String, Object> params){
        inputInvoicePoService.getPoEntity(params);
        return R.ok();
    }

    /**
     * 补录
     * @param poEntity
     * @return
     */
    @RequestMapping("/updatePo")
    @RequiresPermissions("input:invoicepo:updatePo")
    public R updatePo(@RequestBody InputInvoicePoEntity  poEntity){
        inputInvoicePoService.updateById(poEntity);
        return R.ok();
    }

    /**
     *  识别失败手动添加
     * @param poEntity
     * @return
     */
    @RequestMapping("/savePo")
    @RequiresPermissions("input:invoicepo:savePo")
    public R savePo(@RequestBody InputInvoicePoEntity  poEntity){
        inputInvoicePoService.save(poEntity);
        return R.ok();
    }


}

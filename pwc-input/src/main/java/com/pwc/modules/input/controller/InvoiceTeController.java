package com.pwc.modules.input.controller;

import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.Log;
import com.pwc.modules.input.entity.vo.InvoiceTeVo;
import com.pwc.modules.input.service.InputInvoiceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: TE发票
 * @author: Gxw
 * @create: 2020-08-27 16:08
 **/
@RestController
@RequestMapping("/input/invoicete")
public class InvoiceTeController {
    @Autowired
    private InputInvoiceService invoiceService;

    @RequestMapping(value = "updoldTeExcel",method = RequestMethod.POST)
//    @RequiresPermissions("input:invoicete:updoldTeExcel")
    public R updoldTeExcel(@RequestParam("file") MultipartFile file) {
        List<String> lists = null;
        try {
            lists = invoiceService.receiveInvoice(file);
            if(lists.isEmpty()){
                return R.ok();
            }else{
                return R.error().put("data",lists);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入TE发票失败");
        }
    }

    /**
     * 总览页面
     * @return
     */
    @RequestMapping("findTeInvoice")
 //   @RequiresPermissions("input:invoicete:findTeInvoice")
    public R findTeInvoice(@RequestParam Map<String, Object> params) {
        InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
        params.put("invoiceStypl", InputConstant.InvoiceStyle.TE.getValue());
        PageUtils page = invoiceService.queryPage(params, invoiceEntity);
        return R.ok().put("page", page);
    }

    /**
     * 认证页面
     * @param params
     * @return
     */
    @RequestMapping("getInvoiceByCertification")
    //   @RequiresPermissions("input:invoicete:getInvoiceByCertification")
    public R getInvoiceByCertification(@RequestParam Map<String, Object> params){
        InputInvoiceEntity invoiceEntity =new InputInvoiceEntity();
        PageUtils page = invoiceService.queryPage(params,invoiceEntity);
        return R.ok();
    }

    /**
     * 异常页面
     * @param params
     * @return
     */
    @RequestMapping("getInvoiceByException")
    //   @RequiresPermissions("input:invoicete:getInvoiceByException")
    public R getInvoiceByException(@RequestParam Map<String, Object> params){
        InputInvoiceEntity invoiceEntity =new InputInvoiceEntity();
        PageUtils page = invoiceService.queryPage(params,invoiceEntity);
        return R.ok();
    }

    /**
     *
     * @param params
     * @param response
     * @return
     */
    @RequestMapping("downloadTeExcel")
    //   @RequiresPermissions("input:invoicete:downloadTeExcel")
    public R downloadTeExcel(@RequestParam Map<String, Object> params, HttpServletResponse response){
        String title = params.get("title").toString();
        List<InvoiceTeVo> invoiceTeVos = new ArrayList();
        try {
                String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceEntity.class).setDataList(invoiceTeVos).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    // 认证
    // 撤销认证
    // 作废



}

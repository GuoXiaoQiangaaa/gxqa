package com.pwc.modules.input.controller;

import com.pwc.common.utils.*;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.modules.input.entity.InputInvoiceCustomsEntity;
import com.pwc.modules.input.entity.InputInvoiceSapEntity;
import com.pwc.modules.input.service.InputInvoiceSapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: Sap数据导入
 * @author: Gxw
 * @create: 2020-09-23 14:14
 **/
@RestController
@RequestMapping("/input/invoicesap")
public class InvoiceSapController {
    @Autowired
    private InputInvoiceSapService inputInvoiceSapService;

    @RequestMapping("/getImportBySap")
    // @RequiresPermissions("input:invoicesap:getImportBySap")
    public R getImportBySap(@RequestParam("files") MultipartFile file){
        try {
            inputInvoiceSapService.getImportBySap(file);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败，请稍后");
        }
        return R.ok();
    }

    /**
     * 列表查询
     * @param params
     * @return
     */
    @RequestMapping("/getListBySap")
        // @RequiresPermissions("input:invoicesap:getListBySap")
    public R  getListBySap(@RequestParam Map<String, Object> params){
        PageUtils page=inputInvoiceSapService.getListBySap(params);
        return R.ok().put("page",page);
    }

    /**
     * 自动匹配库里deptId
     * @param params
     * @return
     */
    @RequestMapping("/updateListByDeptId")
    // @RequiresPermissions("input:invoicesap:getListBySap")
    public R  updateListByDeptId(@RequestParam Map<String, Object> params){
        boolean page=inputInvoiceSapService.updateListByDeptId(params);
        return R.ok().put("page",page);
    }

    /**
     * 查询有帐无票数据
     * @param params
     * @return
     */
    @RequestMapping("/getListByMatch")
    // @RequiresPermissions("input:invoicesap:getListByMatch")
    public R  getListByMatch(@RequestParam Map<String, Object> params){
        String[] status = {InputConstant.InvoiceMatch.MATCH_NO.getValue()};
        params.put("match",status);
        PageUtils page=inputInvoiceSapService.getListBySap(params);
        return R.ok().put("page",page);
    }


    /**
     * 查询本月入账未认证数据
     * @param params
     * @return
     */
    @RequestMapping("/getListByNoMatch")
    public R  getListByNoMatch(@RequestParam Map<String, Object> params){
        String[] status = {InputConstant.InvoiceMatch.MATCH_NO.getValue(),InputConstant.InvoiceMatch.MATCH_ERROR.getValue()};
        params.put("match",status);
        String pstngDate= ParamsMap.findMap(params, "pstngDate");
        String companyCode=ParamsMap.findMap(params, "companyCode");
        if(pstngDate != null  && companyCode != null){
            PageUtils page=inputInvoiceSapService.getListBySap(params);
            return R.ok().put("page",page);
        }else{
            return  null;
        }
    }

    /**
     * 根据id导出
     *
     * @param params
     * @param response
     * @return
     */
    @GetMapping(value = "/exportRecordListByIds")
    // @RequiresPermissions("input:invoiceCustoms:exportRecordListByIds")
    public R exportRecordListByIds(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String ids = String.valueOf(params.get("ids"));
        List<InputInvoiceSapEntity> entities = (List<InputInvoiceSapEntity>) inputInvoiceSapService.listByIds(Arrays.asList(ids.split(",")));
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceSapEntity.class).setDataList(entities).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }


    /**
     * 查询本月入账未认证数据
     * @param params
     * @return
     */
    @RequestMapping("/exportGetListByNoMatch")
    public R exportGetListByNoMatch(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String[] status = {InputConstant.InvoiceMatch.MATCH_NO.getValue(),InputConstant.InvoiceMatch.MATCH_ERROR.getValue()};
        params.put("match",status);
        int count = inputInvoiceSapService.getListByShow();
        params.put("limit", count + "");
        PageUtils matchResultList  = inputInvoiceSapService.getListBySap(params);
        List<InputInvoiceSapEntity> invoiceEntityList = (List<InputInvoiceSapEntity>) matchResultList.getList();
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new com.pwc.common.utils.excel.ExportExcel("", InputInvoiceSapEntity.class).setDataList(invoiceEntityList).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }
}

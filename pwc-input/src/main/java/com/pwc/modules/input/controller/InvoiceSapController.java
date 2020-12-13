package com.pwc.modules.input.controller;

import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.service.InputInvoiceSapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}

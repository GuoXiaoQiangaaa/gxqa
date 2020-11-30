package com.pwc.modules.input.controller;

import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.Page;
import com.fapiao.neon.model.in.SyncInvoiceInfo;
import com.fapiao.neon.model.in.inspect.BaseInvoice;
import com.fapiao.neon.param.in.InvoiceCollectionParamBody;
import com.fapiao.neon.param.in.InvoiceInspectionParamBody;
import com.fapiao.neon.param.in.SyncInvoiceParamBody;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputInvoiceSyncEntity;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author zk
 * @email
 * @date 2020-01-19 18:27:48
 */
@RestController
@RequestMapping("input/invoicesync")
public class InvoiceSyncController {
    @Autowired
    private InputInvoiceSyncService invoiceSyncService;
    @Autowired
    private InputCompanyService companyService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private InputCompanyTaskDetailService companyTaskDetailService;
    @Autowired
    private InputCompanyTaskDifferenceService companyTaskDifferenceService;
    @Autowired
    private InputCompanyTaskInvoicesService companyTaskInvoicesService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("input:invoicesync:list")
    public R list(@RequestParam Map<String, Object> params, InputInvoiceSyncEntity invoiceSyncEntity){
        PageUtils page = invoiceSyncService.queryPage(params, invoiceSyncEntity);

        return R.ok().put("page", page);
    }

    /**
     * 获取税款所属期
     * @return
     */

    @RequestMapping("/getSkssq")
    public R getSkssq() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");//设置日期格式
        String dateString = df.format(date);
        invoiceSyncService.sync();

        return R.ok();
    }

    /**
     * 发票同步接口
     * @return
     */
    @RequestMapping("/invoiceSync")
    @RequiresPermissions("input:invoicesync:invoiceSync")
    public R invoiceSync() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd");//设置日期格式
        String dateString = df.format(date);
        /*SyncInvoiceParamBody syncInvoiceParamBody = new SyncInvoiceParamBody();
        syncInvoiceParamBody.setTaxNo("911100007693505528");
        syncInvoiceParamBody.setSyncType("1");
        syncInvoiceParamBody.setStartBillingDate("2020-10-01");
        syncInvoiceParamBody.setEndBillingDate("2020-10-31");
        syncInvoiceParamBody.setPage(1);
        syncInvoiceParamBody.setPageSize(1);
        CallResult<SyncInvoiceInfo> results = invoiceSyncService.invoiceSync(syncInvoiceParamBody);
        System.out.println(results.getData().getInvoices().toString());*/
        invoiceSyncService.syncInvoice(1, "2020-07-01", "2020-07-31", "911100007693505528");
        return R.ok();
    }





    /**
     * 单票查验
     * @return
     */
    @RequestMapping("/invoiceCheck")
    public R invoiceCheck() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");//设置日期格式
        String dateString = df.format(date);
//        invoiceSyncService.invoiceSync();
        InvoiceInspectionParamBody invoiceInspectionParamBody = new InvoiceInspectionParamBody();
        invoiceInspectionParamBody.setInvoiceCode("3200192130");
        invoiceInspectionParamBody.setInvoiceNumber("38153868");
        invoiceInspectionParamBody.setBillingDate("2020-03-06");
        invoiceInspectionParamBody.setTotalAmount("103189.62");
//        invoiceInspectionParamBody.setCheckCode("1");
        CallResult<BaseInvoice> results = invoiceSyncService.invoiceCheck(invoiceInspectionParamBody);
        return R.ok();
    }

    /**
     * 申请/撤销统计
     */
    @RequestMapping("/statistics")
//    @RequiresPermissions("input:invoicesync:statistics")
    public R statistics(@RequestParam Map<String, Object> params) {
        invoiceSyncService.statistics(params);

        return R.ok();
    }

    /**
     * 确认统计
     * @param taxNo 企业税号
     * @return
     */
    @RequestMapping("/confirm")
//    @RequiresPermissions("input:invoicesync:confirm")
    public R confirm(@RequestParam Map<String, Object> params) {
        invoiceSyncService.applyConfirm(params);

        return R.ok();
    }


    /**
     *
     * @param invoiceIds
     * @return
     */
    @PostMapping("/deductInvoices")
    @RequiresPermissions("input:invoicesync:deductInvoices")
    public R deductInvoices(String invoiceIds, String type) {
        invoiceSyncService.deductInvoices(invoiceIds, type);
        return R.ok();
    }

    /**
     *
     * @return
     */
    @PostMapping("/invoiceCollection")
    @RequiresPermissions("input:invoicesync:invoiceCollection")
    public R invoiceCollection() {
        this.collectionInvoice(1);

        return R.ok();
    }

    private void collectionInvoice(int page){
        InvoiceCollectionParamBody invoiceCollectionParamBody = new InvoiceCollectionParamBody();
//        91530100678739044M    91110302600035733E
        invoiceCollectionParamBody.setTaxNo("91530100678739044M");
        invoiceCollectionParamBody.setBillingDateStart("2019-10-01");
        invoiceCollectionParamBody.setBillingDateEnd("2020-03-30");
//        invoiceCollectionParamBody.setStartDate("2020-01-01");
//        invoiceCollectionParamBody.setEndDate("2020-03-30");
//        invoiceCollectionParamBody.setInvoiceType("01");
        invoiceCollectionParamBody.setDataType("2");
        invoiceCollectionParamBody.setPage(page);
        invoiceCollectionParamBody.setPageSize(200);
        CallResult<Page> pageCallResult = invoiceSyncService.invoiceCollection(invoiceCollectionParamBody);
        if (pageCallResult.isSuccess()) {
            if (page < pageCallResult.getData().getTotalPage()) {
                System.out.println(pageCallResult.getData().getRows());
                this.collectionInvoice(page ++);
            }
        }
    }
    /**
     * 查看统计信息
     * @return
     */

    @RequestMapping("/getTaskResultDetail")
    @RequiresPermissions("input:invoicesync:resultDetail")
    public R getTaskResultDetail(@RequestParam Map<String, Object> params) {
        InputCompanyEntity byDeptId = companyService.getByDeptId( Long.valueOf((String)params.get("companyId")));
        params.put("companyId",byDeptId.getId());
        PageUtils page = companyTaskDetailService.getDetailList(params);
        return R.ok().put("page", page);
    }
    /**
     * 查看差异发票信息
     * @return
     */

    @RequestMapping("/getTaskResultDifference")
    @RequiresPermissions("input:invoicesync:resultDifference")
    public R getTaskResultDifference(@RequestParam Map<String, Object> params) {
        InputCompanyEntity byDeptId = companyService.getByDeptId( Long.valueOf((String)params.get("companyId")));
        params.put("companyId",byDeptId.getId());
        PageUtils page = companyTaskDifferenceService.getDifferenceList(params);
        // PageUtils page = companyTaskInvoicesService.getInvoicesList(params);
        return R.ok().put("page", page);
    }

    /**
     * 发票同步接口
     * @return
     */
    @RequestMapping("/invoiceSyncAutoPage")
    public R invoiceSyncAutoPage(String taxNo, String startDate, String endDate) {
         // TODO 缺少接口信息 不能同步底账库
        String[] taxNos = taxNo.split(",");
        List<SysDeptEntity> deptEntitys= sysDeptService.getDeptByStatus();
        SysDeptEntity  deptEntity =sysDeptService.getById(ShiroUtils.getUserEntity().getDeptId());
        if(deptEntity.getTaxCode()!=null &&!"".equals(deptEntity.getTaxCode())){
            invoiceSyncService.syncInvoice(1, startDate, endDate, deptEntity.getTaxCode());
        }

//        for (String tax : taxNos) {
//            this.syncInvoice(1, startDate, endDate, tax);
//        }
        return R.ok();
    }

}



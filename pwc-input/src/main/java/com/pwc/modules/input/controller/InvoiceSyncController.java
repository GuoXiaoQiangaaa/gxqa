package com.pwc.modules.input.controller;

import cn.hutool.core.util.StrUtil;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.Page;
import com.fapiao.neon.model.in.SyncInvoiceInfo;
import com.fapiao.neon.model.in.inspect.BaseInvoice;
import com.fapiao.neon.param.in.*;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputInvoiceSyncEntity;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    @RequiresPermissions("input:invoicesync:list")
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
        SyncInvoiceParamBody syncInvoiceParamBody = new SyncInvoiceParamBody();
        syncInvoiceParamBody.setTaxNo("91310000059352422R");
        syncInvoiceParamBody.setSyncType("1");
        syncInvoiceParamBody.setStartDate("2020-01-01");
        syncInvoiceParamBody.setEndDate("2020-04-30");
        syncInvoiceParamBody.setPage(1);
        syncInvoiceParamBody.setPageSize(200);
        CallResult<SyncInvoiceInfo> results = invoiceSyncService.invoiceSync(syncInvoiceParamBody);
        System.out.println(results.getData().getInvoices().toString());
        return R.ok();
    }

    /**
     * 发票同步接口
     * @return
     */
    @RequestMapping("/invoiceSyncAutoPage")
    public R invoiceSyncAutoPage(String taxNo, String startDate, String endDate) {
        String[] taxNos = taxNo.split(",");
        for (String tax : taxNos) {
            this.syncInvoice(1, startDate, endDate, tax);
        }
        return R.ok();
    }

    private void syncInvoice(int page, String startTime, String endTime, String taxNo) {
        SyncInvoiceParamBody syncInvoiceParamBody = new SyncInvoiceParamBody();
        syncInvoiceParamBody.setTaxNo(taxNo);
        syncInvoiceParamBody.setSyncType("1");
        syncInvoiceParamBody.setStartDate(startTime);
        syncInvoiceParamBody.setEndDate(endTime);
        syncInvoiceParamBody.setPage(page);
        syncInvoiceParamBody.setPageSize(200);
        CallResult<SyncInvoiceInfo> results = invoiceSyncService.invoiceSync(syncInvoiceParamBody);
        int currentPage = results.getData().getCurrentPage();
        int totalPage = results.getData().getPages();
//        if(results.getData() != null && results.getData().getInvoices() != null) {
//            invoiceSyncService.saveInvoice(results.getData().getInvoices());
//            System.out.println("==save==" + results.getData().getInvoices().toString());
//        }
        if(currentPage < totalPage) {
            this.syncInvoice(currentPage + 1, startTime, endTime, taxNo);
            System.out.println("==nextPage==" + (currentPage+1));
        }

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
     * 申请统计
     * @param type 1 申请统计 2 撤销统计
     * @param taxNo 企业税号
     * @return
     */
    @RequestMapping("/statistics")
    @RequiresPermissions("input:invoicesync:statistics")
    public R statistics(String type, String taxNo) {
        if (StrUtil.isBlank(type)) {
            type = "1";
        }
        StatisticsParamBody statisticsParamBody = new StatisticsParamBody();
        statisticsParamBody.setStatisticsType(type);
        statisticsParamBody.setTaxNo(taxNo);
        String requestId = invoiceSyncService.statistics(statisticsParamBody);
        if (StrUtil.isNotBlank(requestId)) {
            return R.ok().put("requestId", requestId);
        }
        return R.error("统计失败");
    }

    /**
     * 申请统计
     * @param taxNo 企业税号
     * @return
     */
    @RequestMapping("/confirm")
    @RequiresPermissions("input:invoicesync:confirm")
    public R confirm(String taxNo) {
        if (StrUtil.isBlank(taxNo)) {
            return R.error("税号不能为空");
        }
        SysDeptEntity deptEntity = sysDeptService.getByTaxCode(taxNo);

        ConfirmParamBody confirmParamBody = new ConfirmParamBody();
        if (deptEntity == null) {
            return R.error("未查询到企业");
        }
        InputCompanyEntity companyEntity = companyService.getByDeptId(deptEntity.getDeptId());
        if (null == companyEntity) {
            return R.error("未查询到企业进项信息");
        }
        confirmParamBody.setTaxNo(taxNo);
        confirmParamBody.setStatisticsTime(companyEntity.getStatisticsTime());
        String requestId = invoiceSyncService.applyConfirm(confirmParamBody);
        if (StrUtil.isNotBlank(requestId)) {
            return R.ok().put("requestId", requestId);
        } else {
            return R.error("确认失败");
        }
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
     * @param invoiceIds
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
}

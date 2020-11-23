package com.pwc.modules.input.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.CustomsInvoiceInfo;
import com.fapiao.neon.param.in.SyncInvoiceParamBody;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.input.entity.InputInvoiceCustomsEntity;
import com.pwc.modules.input.service.InputInvoiceCustomsService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 海关缴款书（同步）
 *
 * @author zlb
 * @date 2020-08-10 18:53:50
 */
@RestController
@RequestMapping("input/invoiceCustoms")
public class InputInvoiceCustomsController {
    @Autowired
    private InputInvoiceCustomsService inputInvoiceCustomsService;

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 海关缴款书SAP入账或冲销凭证信息推送
     *
     * @param params
     * @param response
     * @return
     */
    @PostMapping("/voucherPush")
    public Map voucherInfo(@RequestBody List<Map<String, Object>> params, HttpServletResponse response) {
        System.out.println(JSON.toJSONString(params));
        Map r = inputInvoiceCustomsService.saveVoucherPush(params);
        response.setStatus((Integer) r.get("HttpStatusCode"));
        r.remove("HttpStatusCode");
        return r;
    }

    /**
     * 海关缴款书同步(废弃)
     *
     * @return
     */
    @RequestMapping("/syncInvoices")
    public R syncInvoices(int page, String startTime, String endTime, String taxNos) {
        for (String taxNo : taxNos.split(",")) {
            SyncInvoiceParamBody syncInvoiceParamBody = new SyncInvoiceParamBody();
            syncInvoiceParamBody.setTaxNo(taxNo);//91110302600035733E
            syncInvoiceParamBody.setSyncType("1");
            syncInvoiceParamBody.setStartBillingDate(startTime);//填发日期
            syncInvoiceParamBody.setEndBillingDate(endTime);
            syncInvoiceParamBody.setPage(page);
            syncInvoiceParamBody.setPageSize(200);
            CallResult<CustomsInvoiceInfo> results = inputInvoiceCustomsService.invoices(syncInvoiceParamBody);
            int currentPage = results.getData().getCurrentPage();
            int totalPage = results.getData().getPages();
            if (results.getData() != null && results.getData().getInvoices() != null) {
                inputInvoiceCustomsService.saveInvoice(results.getData().getInvoices());
                System.out.println("==save==" + results.getData().getInvoices().toString());
            }
            if (currentPage < totalPage) {
                this.syncInvoices(currentPage + 1, startTime, endTime, taxNo);
                System.out.println("==nextPage==" + (currentPage + 1));
            }
        }

        return R.ok();
    }

    /**
     * 同步海关缴款书
     */
    @GetMapping("/sync")
    public R sync(@RequestParam Map<String, Object> params) {
        String deptId = (String) params.get("deptId");
        String billingDateArray = (String) params.get("billingDateArray");
        if (StringUtils.isBlank(deptId) || StringUtils.isBlank(billingDateArray)) {
            throw new RRException("参数不能为空");
        }
        SysDeptEntity deptEntity = sysDeptService.getById(Long.valueOf(deptId));
        if (null == deptEntity) {
            throw new RRException("未获取到企业信息");
        }
        String startBillingDate = billingDateArray.split(",")[0];
        String endBillingDate = billingDateArray.split(",")[1];
        inputInvoiceCustomsService.sync(deptEntity.getTaxCode(), startBillingDate, endBillingDate, 1);

        return R.ok();
    }


    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("input:invoicecustoms:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = inputInvoiceCustomsService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 勾选/撤销
     */
    @GetMapping("/deductOrCancel")
    public R deductOrCancel(@RequestParam Map<String, Object> params) {
        inputInvoiceCustomsService.deductOrCancel(params);

        return R.ok();
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    //@RequiresPermissions("input:invoicecustoms:info")
    public R info(@PathVariable("id") Long id) {
        InputInvoiceCustomsEntity inputInvoiceCustoms = inputInvoiceCustomsService.getById(id);

        return R.ok().put("inputInvoiceCustoms", inputInvoiceCustoms);
    }

    /**
     * 根据id导出
     *
     * @param params
     * @param response
     * @return
     */
    @GetMapping(value = "exportRecordListByIds")
    // @RequiresPermissions("input:invoiceCustoms:exportRecordListByIds")
    public R exportRecordListByIds(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String ids = (String) params.get("ids");
        List<InputInvoiceCustomsEntity> inputInvoiceCustomsEntities = (List<InputInvoiceCustomsEntity>) inputInvoiceCustomsService.listByIds(Arrays.asList(ids.split(",")));
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceCustomsEntity.class).setDataList(inputInvoiceCustomsEntities).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 根据条件导出
     *
     * @param params
     * @param response
     * @return
     */
    @GetMapping(value = "exportRecordList")
    //@RequiresPermissions("input:invoice:exportRecordList")
    public R exportRecordList(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        InputInvoiceCustomsEntity invoiceEntity = JSON.parseObject(JSON.toJSONString(params), InputInvoiceCustomsEntity.class);

        int count = inputInvoiceCustomsService.getListByShow();
        params.put("limit", count + "");

        PageUtils pageUtils = inputInvoiceCustomsService.queryPage(params);
        List<InputInvoiceCustomsEntity> invoiceEntityList = (List<InputInvoiceCustomsEntity>) pageUtils.getList();
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceCustomsEntity.class).setDataList(invoiceEntityList).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }
    /**
     *海关缴款书入账或冲销
     * @param payNos
     * @param type
     * @param period
     * @return
     */
    /*@PostMapping("/entryInvoices")
    public R entryInvoices(String payNos, String type,String period) {

        inputInvoiceCustomsService.entryInvoices(payNos,type);
        return R.ok();
    }*/

    /**
     * 海关缴款书入账或冲销（人工）
     *
     * @return
     */
    @PostMapping("/manualEntry")
    //@RequiresPermissions("input:invoice:manualEntry")
    public R entry(@RequestBody Map<String, Object> params, String invoiceId, String voucherNumber) {
        String entryState = (String) params.get("entryState");
        invoiceId = (String) params.get("invoiceId");
        voucherNumber = (String) params.get("voucherNumber");
        inputInvoiceCustomsService.entryByManual(entryState, invoiceId, voucherNumber);
        return R.ok();
    }

    /**
     * 凭证抽取
     *
     * @return
     */
    @GetMapping("/voucherExtract")
    public R voucherExtract() {
        inputInvoiceCustomsService.voucherExtract();
        return R.ok();
    }

    /**
     * 获取税款所属期
     *
     * @return
     */
    @RequestMapping("/getTaxPeriod")
    public R getTaxPeriod(String invoiceIds) {
        String[] split = invoiceIds.split(",");
        List<InputInvoiceCustomsEntity> list = new ArrayList<InputInvoiceCustomsEntity>();
        for (String id : split) {
            InputInvoiceCustomsEntity byId = inputInvoiceCustomsService.getPeriodById(id);
            list.add(byId);

        }
        return R.ok().put("result", list);
    }

    /**
     * 海关缴款书手动勾选
     *
     * @param invoiceIds
     * @return
     */
    @PostMapping("/deductInvoices")
    // @RequiresPermissions("input:invoicesync:deductInvoices")
    public R deductInvoices(String invoiceIds, String type, String period, String isAuto) {
        try {
            //inputInvoiceCustomsService.deductInvoices(invoiceIds, type,period,"N");

            String[] invoiceArray = invoiceIds.split(",");
            for (String invoiceId : invoiceArray) {
                InputInvoiceCustomsEntity invoice = inputInvoiceCustomsService.getById(invoiceId);
                period = inputInvoiceCustomsService.getPeriodById(invoiceId).getDeductiblePeriod();
                if (!StringUtils.isNotBlank(invoiceIds) || !StringUtils.isNotBlank(type) || !StringUtils.isNotBlank(period)) {
                    invoice.setDeductible("2");
                    inputInvoiceCustomsService.updateById(invoice);
                    int i = 1 / 0;
                }
                if ("1".equals(type)) {
                    invoice.setDeductible("1");
                    invoice.setDeductiblePeriod(period);
                    invoice.setDeductibleDate(DateUtils.format(new Date()));
                } else if ("6".equals(type)) {
                    invoice.setDeductible("0");
                    invoice.setDeductiblePeriod(period);
                    invoice.setDeductibleDate(DateUtils.format(new Date()));
                }
                inputInvoiceCustomsService.updateById(invoice);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("操作失败");
        }
        return R.ok("操作成功");
    }

    /**
     * 作废(退票)
     */
    @GetMapping("/pendingReturn")
    //@RequiresPermissions("input:invoice:pendingReturn")
    public R pendingReturn(@RequestParam Map<String, Object> params) {
        Map<String, Object> resMap = inputInvoiceCustomsService.updateByIdReturn(params);

        return R.ok().put("res", resMap);
    }

    /**
     * 获取退单原因
     *
     * @param id
     * @return
     */
    @RequestMapping("/getReturnReason/{id}")
    //@RequiresPermissions("input:invoice:pendingReturn")
    public R getReturnReason(@PathVariable("id") Integer id) {
        InputInvoiceCustomsEntity byId = inputInvoiceCustomsService.getById(id);
        return R.ok().put("reason", byId.getReturnReason());
    }

    /**
     * 手工上传已认证海关缴款书清单
     */
    @PostMapping(value = "/importInvoices")
    //@RequiresPermissions("input:invoice:importInvoices")
    public R importInvoices(MultipartFile[] files) {
        List<InputInvoiceCustomsEntity> read1 = null;
        List<InputInvoiceCustomsEntity> saveList = new ArrayList<>();
        List<InputInvoiceCustomsEntity> updateList = new ArrayList<>();
        List<InputInvoiceCustomsEntity> errorList = new ArrayList<>();
        int total = 0;
        try {
            for (MultipartFile file : files) {
                ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
                String[] excelHead = {"序号", "海关交款书编号", "填发日期", "购方税号", "税额", "有效税额", "勾选日期", "认证期间"};
                String[] excelHeadAlias = {"serialNo", "payNo", "billingDate", "purchaserTaxNo", "totalTax", "effectiveTaxAmount", "deductibleDate", "deductiblePeriod"};
                for (int i = 0; i < excelHead.length; i++) {
                    reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
                }
                read1 = reader.read(1, 2, InputInvoiceCustomsEntity.class);
                total += read1.size();
                for (InputInvoiceCustomsEntity e : read1) {
                    if (!(StringUtils.isNotBlank(e.getPayNo()) && StringUtils.isNotBlank(e.getPurchaserTaxNo()))) {
                        errorList.add(e);
                        continue;
                    }
                    InputInvoiceCustomsEntity one = inputInvoiceCustomsService.getOne(new QueryWrapper<InputInvoiceCustomsEntity>()
                            .eq("pay_no", e.getPayNo())
                            .eq("purchaser_tax_no", e.getPurchaserTaxNo()));
                    if (one == null) {
                        SysDeptEntity byTaxCode = sysDeptService.getByTaxCode(e.getPurchaserTaxNo());
                        if (byTaxCode != null) {
                            e.setPurchaserName(byTaxCode.getName());
                        }
                        e.setUploadType("1");
                        e.setDeductible("1");
                        e.setUploadDate(new Date());
                        saveList.add(e);
                    } else {
                        one.setBillingDate(e.getBillingDate());
                        one.setTotalTax(e.getTotalTax());
                        one.setEffectiveTaxAmount(e.getEffectiveTaxAmount());
                        one.setDeductibleDate(e.getDeductibleDate());
                        one.setDeductiblePeriod(e.getDeductiblePeriod());
                        one.setUploadType("1");
                        one.setDeductible("1");
                        one.setUploadDate(new Date());
                        updateList.add(one);
                    }

                }
                inputInvoiceCustomsService.saveBatch(saveList);
                inputInvoiceCustomsService.updateBatchById(updateList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return R.ok().put("total", total).put("save", saveList.size()).put("update", updateList.size()).put("error", errorList.size());
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    //@RequiresPermissions("input:invoicecustoms:save")
    public R save(@RequestBody InputInvoiceCustomsEntity inputInvoiceCustoms) {
        inputInvoiceCustomsService.save(inputInvoiceCustoms);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("input:invoicecustoms:update")
    public R update(@RequestBody InputInvoiceCustomsEntity inputInvoiceCustoms) {
        ValidatorUtils.validateEntity(inputInvoiceCustoms);
        inputInvoiceCustomsService.updateById(inputInvoiceCustoms);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    //@RequiresPermissions("input:invoicecustoms:delete")
    public R delete(@RequestBody Long[] ids) {
        inputInvoiceCustomsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询账票匹配成功数据
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListBySuccess")
    //@RequiresPermissions("input:invoicecustoms:getListBySuccess")
    public R getListBySuccess(@RequestParam Map<String, Object> params) {
        PageUtils page = inputInvoiceCustomsService.getListBySuccess(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询有票无帐
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListByNo")
    //@RequiresPermissions("input:invoicecustoms:getListByNo")
    public R getListByNo(@RequestParam Map<String, Object> params) {
        PageUtils page = inputInvoiceCustomsService.getListByNo(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询有差异数据
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListByError")
    //@RequiresPermissions("input:invoicecustoms:getListByError")
    public R getListByError(@RequestParam Map<String, Object> params) {
        PageUtils page = inputInvoiceCustomsService.getListByError(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询账票匹配成功数据(下载)
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListBySuccessAndExcel")
    //@RequiresPermissions("input:invoicecustoms:getListBySuccessAndExcel")
    public R getListBySuccessAndExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        List list = new ArrayList();
        if (StringUtils.isNotBlank((String) params.get("ids"))) {
            String[] ids = ((String) params.get("ids")).split(",");
            for (String id : ids) {
                list.add(inputInvoiceCustomsService.getById(id));
            }
        } else {
            int count = inputInvoiceCustomsService.getListByShow();
            params.put("limit", count + "");
            PageUtils page = inputInvoiceCustomsService.getListBySuccess(params);
            list = page.getList();
        }
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceCustomsEntity.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 查询有票无帐(下载)
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListByNoAndExcel")
    //@RequiresPermissions("input:invoicecustoms:getListByNoAndExcel")
    public R getListByNoAndExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        List list = new ArrayList();
        if (StringUtils.isNotBlank((String) params.get("ids"))) {
            String[] ids = ((String) params.get("ids")).split(",");
            for (String id : ids) {
                list.add(inputInvoiceCustomsService.getById(id));
            }
        } else {
            int count = inputInvoiceCustomsService.getListByShow();
            params.put("limit", count + "");
            PageUtils page = inputInvoiceCustomsService.getListByNo(params);
            list = page.getList();
        }
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceCustomsEntity.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 查询有差异数据(下载)
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListByErrorAndExcel")
    //@RequiresPermissions("input:invoicecustoms:getListByErrorAndExcel")
    public R getListByErrorAndExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        List list = new ArrayList();
        if (StringUtils.isNotBlank((String) params.get("ids"))) {
            String[] ids = ((String) params.get("ids")).split(",");
            for (String id : ids) {
                list.add(inputInvoiceCustomsService.getById(id));
            }
        } else {
            int count = inputInvoiceCustomsService.getListByShow();
            params.put("limit", count + "");
            PageUtils page = inputInvoiceCustomsService.getListByError(params);
            list = page.getList();
        }
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceCustomsEntity.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 手工入账
     *
     * @param params
     * @return
     */
    @RequestMapping("/manualEntry")
    //@RequiresPermissions("input:invoicecustoms:manualEntry")
    public R manualEntry(@RequestParam Map<String, Object> params) {
        inputInvoiceCustomsService.manualEntry(params);
        return R.ok();
    }

}

package com.pwc.modules.input.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.modules.input.entity.InputInvoiceBatchEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;
import com.pwc.modules.input.entity.InputInvoiceVoucherEntity;
import com.pwc.modules.input.service.InputInvoiceBatchService;
import com.pwc.modules.input.service.InputInvoiceMaterialService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysUserService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author zk
 */
//@Component
@RestController
//@EnableScheduling
@RequestMapping("/input/invoice")
public class InvoiceController {

    @Autowired
    private InputInvoiceService invoiceService;
    @Autowired
    private InputInvoiceBatchService invoiceBatchService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private InputInvoiceMaterialService invoiceMaterialService;
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 票据总览列表
     */
    @RequestMapping("/invoiceList")
    @RequiresPermissions("input:invoice:invoiceList")
    public R invoiceList(@RequestParam Map<String, Object> params, InputInvoiceEntity invoiceEntity) {
        if (invoiceEntity.getInvoiceBatchNumber() != null && !"".equals(invoiceEntity.getInvoiceBatchNumber())) {
            params.put("invoiceBatchNumberList", invoiceEntity.getInvoiceBatchNumber());
        } else {
            params.put("invoiceBatchNumberList", "");
        }
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        if (!"".equals(invoiceUploadDateArray) && invoiceUploadDateArray != null) {
            String[] range = invoiceUploadDateArray.split(",");
            String begin = range[0];
            String end = range[1];
            params.put("invoiceUploadDateArray", (begin + "," + end));
        }

        Long userId = 0L;
        String createUserName = (String)params.get("createUserName");
        if (StringUtils.isNotBlank(createUserName)) {
            userId = sysUserService.getIdByName(params.get("createUserName").toString());
        }
        invoiceEntity.setCreateBy(null == userId ? 0 : userId.intValue());
        PageUtils page = invoiceService.queryPage(params, invoiceEntity);
        List<InputInvoiceEntity> invoiceEntityList = (List<InputInvoiceEntity>) page.getList();
        if (!invoiceEntityList.isEmpty()) {
            List<InputInvoiceEntity> list = invoiceService.getListAndCreateName(invoiceEntityList, createUserName);
            if (!list.isEmpty()) {
                invoiceService.setBatchNumber(list);
            }
            page.setList(list);
        }
        List<InputInvoiceBatchEntity> invoiceBatchEntityList = invoiceBatchService.getList();
        R r = new R();
        r.put("page", page);
        r.put("invoiceBatchEntityList", invoiceBatchEntityList);
        return r;
    }

    /**
     * 查看发票信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("input:invoice:info")
    public R info(@PathVariable("id") Integer id) {
        InputInvoiceEntity invoice = new InputInvoiceEntity();
        InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
        InputInvoiceBatchEntity invoiceBatchEntity = new InputInvoiceBatchEntity();
        invoice.setInvoiceBatchNumber(String.valueOf(invoiceBatchEntity.getId()));
        invoice = invoiceService.getById(id);
        List<InputInvoiceEntity> invoiceArrayList = new ArrayList<>();
        List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = new ArrayList<>();
        R r = new R();
        invoiceArrayList = invoiceService.getListByBatchId(invoice);
        List<String> invoiceIds = new ArrayList<>();
        for (int i = 0; i < invoiceArrayList.size(); i++) {
            invoiceIds.add(String.valueOf(invoiceArrayList.get(i).getId()));
        }
        invoiceMaterialEntity.setInvoiceIds(invoiceIds);
        invoiceMaterialEntity.setInvoiceId(id);
        invoiceMaterialEntityList = invoiceMaterialService.getByInvoiceId(invoiceMaterialEntity);
        float sphJe = 0;
        float sphSe = 0;
        BigDecimal sphJe2 = new BigDecimal(Float.toString(sphJe));
        BigDecimal sphSe2 = new BigDecimal(Float.toString(sphSe));
        for (int j = 0; j < invoiceMaterialEntityList.size(); j++) {
            //循环相加获得金额的总量，税额的总量
            sphJe2 = sphJe2.add(invoiceMaterialEntityList.get(j).getSphJe());
            sphSe2 = sphSe2.add(invoiceMaterialEntityList.get(j).getSphSe());
        }
        r.put("invoice", invoice);
        r.put("sphJe", sphJe2);
        r.put("sphSe", sphSe2);
        r.put("valorem", sphJe2.add(sphSe2));
        r.put("invoiceMaterialEntityList", invoiceMaterialEntityList);
        return r;
    }

    /**
     * 单票查询
     * @param params
     * @param httpRequest
     * @param invoiceEntity
     * @return
     */
    @RequestMapping("/billList")
    @RequiresPermissions("input:invoice:billList")
    public R billList(@RequestParam Map<String, Object> params, HttpServletRequest httpRequest, InputInvoiceEntity invoiceEntity) {

//        params.put("invoice_return","0");
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        if (!"".equals(invoiceUploadDateArray) && invoiceUploadDateArray != null) {
            String[] range = invoiceUploadDateArray.split(",");
            String begin = range[0];
            String end = range[1];
            params.put("invoiceUploadDateArray", (begin + "," + end));
        }
        String createUserName = (String)params.get("createUserName");
        Long userId = 0L;
        if (StringUtils.isNotBlank(createUserName)) {
            userId = sysUserService.getIdByName(createUserName);
        }
        invoiceEntity.setCreateBy(userId.intValue());
        R r = new R();

        PageUtils page = invoiceService.findBillList(params, invoiceEntity);
        r.put("page", page);
        return r;
    }


    /**
     * 采购单管理界面的手工上传
     * @param invoiceEntity
     * @param request
     * @return
     */
    @RequestMapping("/saveBatch")
    @RequiresPermissions("input:invoice:saveBatch")
    public R saveBatch(@RequestBody InputInvoiceEntity invoiceEntity, HttpServletRequest request) {
        R r = new R();
        try {

            int tolSum = 0; //一共多少张
            int recSum = 0; //已识别
            int partSum = 0; //部分识别
            int failSum = 0; //识别失败

            for (int i = 0; i < invoiceEntity.getImagesList().size(); i++) {
//
                InputInvoiceEntity invoice = new InputInvoiceEntity();
                invoice.setInvoiceImage(invoiceEntity.getImagesList().get(i));

                String fileType = StrUtil.subAfter(invoiceEntity.getImagesList().get(i), ".", true);
                if ("png".equals(fileType) || "jpg".equals(fileType) || "jpeg".equalsIgnoreCase(fileType)) {
                    invoice.setInvoiceType("picture");
                } else if ("pdf".equals(fileType)) {
                    invoice.setInvoiceType("pdf");
                }
                invoice.setCreateBy(ShiroUtils.getUserId().intValue());
                invoice.setUploadType(invoiceEntity.getUploadType());
                invoice.setInvoiceFromto(invoiceEntity.getInvoiceFromto());

                String str = invoiceService.functionSaveInvoice(invoice);

                invoiceService.functionCheckTrue(invoice);
                if (InputConstant.InvoiceStatus.PENDING_VERIFICATION.getValue().equals(invoice.getInvoiceStatus())) {
                    recSum++;
                } else if (InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue().equals(invoice.getInvoiceStatus())) {
                    failSum++;
                }
                if(str.equals("19")) {
                    return R.error(510,"网络不稳定，请重新操作！");
                }
            }


        r.put("tolSum", tolSum);
        r.put("recSum", recSum);
        r.put("partSum", partSum);
        r.put("failSum", failSum);


        } catch (Exception e) {
            e.printStackTrace();
            return r.error(601, "系统异常，请稍后再试");
        }
        return r;
    }

    /**
     *     批量验真
     */
    @RequestMapping("/batchCheckTrue")
    @ResponseBody
    @RequiresPermissions("input:invoice:batchCheckTrue")
    public R batchCheckTrue(InputInvoiceEntity invoiceEntity) {
        try {
            invoiceEntity = invoiceService.get(invoiceEntity);
            String result = invoiceService.functionCheckTrue(invoiceEntity);
            if (!"验真成功".equals(result)) {
                return R.error("验真失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    @RequestMapping("/pendingReturn")
    @RequiresPermissions("input:invoice:pendingReturn")
    public R pendingReturn(@RequestBody Map<String,Object> ids) {

        String idlist = ids.get("ids").toString();

        try {
            String str[] = idlist.replace("[","").replace("]","").split(",");
            List<String> ids_string = Arrays.asList(str);


            invoiceService.updateByIdPendingRefund(ids_string);
            return R.ok();

        } catch (Exception e) {
            return R.error("退票失败，请联系管理员");

        }
    }

    /**
     * 发票异常列表
     */
    @RequestMapping("/invoiceException")
    @RequiresPermissions("input:invoice:invoiceException")
    public R invoiceException(@RequestParam Map<String, Object> params, HttpServletRequest httpRequest, InputInvoiceEntity invoiceEntity) {
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        if (!"".equals(invoiceUploadDateArray) && invoiceUploadDateArray != null) {
            String[] range = invoiceUploadDateArray.split(",");
            String begin = range[0];
            String end = range[1];
            params.put("invoiceUploadDateArray", (begin + "," + end));
        }
        if (invoiceEntity.getInvoiceBatchNumber() != null && !"".equals(invoiceEntity.getInvoiceBatchNumber())) {
            params.put("invoiceBatchNumberList", invoiceEntity.getInvoiceBatchNumber());
        } else {
            params.put("invoiceBatchNumberList", "");
        }
        PageUtils page = invoiceService.invoiceException(params);
        List<InputInvoiceEntity> invoiceEntityList = (List<InputInvoiceEntity>) page.getList();
        if (!invoiceEntityList.isEmpty()) {
            invoiceService.setBatchNumber(invoiceEntityList);
        }
        R r = new R();
        r.put("page", page);
        List<InputInvoiceBatchEntity> invoiceBatchEntityList = invoiceBatchService.getList();
        r.put("invoiceBatchEntityList", invoiceBatchEntityList);
        return r;
    }

    @RequestMapping("/getById")
    @RequiresPermissions("input:invoice:getById")
    public R getById(InputInvoiceEntity invoiceEntity) {
        try {
            invoiceEntity = invoiceService.get(invoiceEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
        return R.ok().put("invoiceEntity", invoiceEntity);
    }

    /**
     * 补充信息，5要素齐全，自动触发验真
     */
    @RequestMapping("/update")
    @RequiresPermissions("input:invoice:update")
    public R update(@RequestBody InputInvoiceEntity invoice) {
        invoiceService.updateById(invoice);
        invoiceService.functionCheckTrue(invoice);
        return R.ok();
    }


    /**
     * OCR扫描识别
     * 请求识别接口
     */
    @RequestMapping(value = "/recognition")
    @RequiresPermissions("input:invoice:recognition")
    @ResponseBody
    public R recognition(InputInvoiceEntity invoiceEntity) {
        R r = new R();
        invoiceEntity = invoiceService.get(invoiceEntity);
        String result = invoiceService.functionSaveInvoice(invoiceEntity);
        if(result.equals("19")) {
            return R.error(510,"网络不稳定，请重新操作！");
        }
        List<InputInvoiceEntity> invoiceEntityList = invoiceService.getListByBatchId(invoiceEntity);
        InputInvoiceVoucherEntity invoiceVoucherEntity = new InputInvoiceVoucherEntity();
        invoiceVoucherEntity.setInvoiceBatchNumber(Integer.parseInt(invoiceEntity.getInvoiceBatchNumber()));
        if (invoiceService.functionGroupByInvoice(invoiceEntityList, invoiceVoucherEntity)) {
            List<InputInvoiceEntity> invoiceEntityList1 = invoiceService.getListByBatchId(invoiceEntity);
            for (int i = 0; i < invoiceEntityList1.size(); i++) {
                int data = 0;
                if ("2".equals(invoiceEntityList1.get(i).getInvoiceStatus())) {
                    r.put("result", "2");
                    data = invoiceService.functionCheckTrue(invoiceEntityList1.get(i), "1");
                    if (data == 1) {
                        if (invoiceEntityList1.get(i).getInvoiceGroup() != null && !"".equals(invoiceEntityList1.get(i).getInvoiceGroup())) {
                            String status = invoiceService.mate(invoiceEntityList1.get(i));
                            InputInvoiceBatchEntity invoiceBatchEntity = new InputInvoiceBatchEntity();
                            invoiceBatchEntity.setId(Integer.parseInt(invoiceEntity.getInvoiceBatchNumber()));
                            invoiceBatchService.get(invoiceBatchEntity);
//                            updateBatchStatus(invoiceEntityList1, invoiceBatchEntity);
                            if ("1".equals(status)) {
                                //自动匹配通过
                            } else if ("2".equals(status)) {
                                //发票的购方信息校验不通过
                            }
                            //TODO 更多状态
                        }
                    }
                } else if ("3".equals(invoiceEntityList1.get(i).getInvoiceStatus())) {
                    r.put("result", "3");
                } else if ("4".equals(invoiceEntityList1.get(i).getInvoiceStatus())) {
                    r.put("result", "4");
                }
            }
        }
        return R.ok();
    }

    /**
     * 勾选认证
     * @param invoiceEntity
     * @return
     */
    @RequestMapping("/batcheVerty")
    @RequiresPermissions("input:invoice:batcheVerty")
    public R batcheVerty(InputInvoiceEntity invoiceEntity) {
        try {
            invoiceEntity = invoiceService.get(invoiceEntity);

//            invoiceService.functionVerfy(invoiceEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    @GetMapping(value = "exportRecordListByIds")
    @RequiresPermissions("input:invoice:exportRecordListByIds")
    public R exportRecordListByIds(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        InputInvoiceEntity invoiceEntity = JSON.parseObject(JSON.toJSONString(params), InputInvoiceEntity.class);
        System.out.println(invoiceEntity);
        List<InputInvoiceEntity> invoiceEntityList = invoiceService.getListById(invoiceEntity);
        invoiceService.checkStstus(invoiceEntityList);
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceEntity.class).setDataList(invoiceEntityList).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    @GetMapping(value = "exportRecordList")
    @RequiresPermissions("input:invoice:exportRecordList")
    public R exportRecordList(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        InputInvoiceEntity invoiceEntity = JSON.parseObject(JSON.toJSONString(params), InputInvoiceEntity.class);
        if (invoiceEntity.getInvoiceBatchNumber() != null && !"".equals(invoiceEntity.getInvoiceBatchNumber())) {
            params.put("invoiceBatchNumberList", invoiceEntity.getInvoiceBatchNumber());
        } else {
            params.put("invoiceBatchNumberList", "");
        }
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        if (!"".equals(invoiceUploadDateArray) && invoiceUploadDateArray != null) {
            String[] range = invoiceUploadDateArray.split(",");
            String begin = range[0];
            String end = range[1];
            params.put("invoiceUploadDateArray", (begin + "," + end));
        }
        int count = invoiceService.getListByShow();
        params.put("limit", count + "");
        Long userId = 0L;
        String createUserName = (String)params.get("createUserName");
        if (StringUtils.isNotBlank(createUserName)) {
            userId = sysUserService.getIdByName(params.get("createUserName").toString());
        }
        invoiceEntity.setCreateBy(userId.intValue());
        PageUtils pageUtils = invoiceService.queryPage(params, invoiceEntity);
        List<InputInvoiceEntity> invoiceEntityList = (List<InputInvoiceEntity>) pageUtils.getList();
        List<InputInvoiceEntity> invoiceEntities = invoiceService.getListByItems(invoiceEntityList);
        invoiceService.checkStstus(invoiceEntities);
        try {
            String fileName = "发票总览列表" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("发票总览列表", InputInvoiceEntity.class).setDataList(invoiceEntities).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("input:invoice:delete")
    public R delete(@RequestBody Integer[] ids) {
        invoiceService.updateByIdArray(ids);
        return R.ok();
    }

    /**
     * 发票关联采购单
     * @param invoiceIds
     * @param batchId
     * @return
     */
    @PostMapping("/relate")
    @RequiresPermissions("input:invoice:related")
    public R relate(String invoiceIds, String batchId) {
        invoiceService.relatedBatch(invoiceIds,batchId);
        return R.ok();
    }

    /**
     * 人工入账
     * @return
     */
    @PostMapping("/manualEntry")
    @RequiresPermissions("input:invoice:manualEntry")
    public R entry(String invoiceIds) {
        invoiceService.manualEntry(invoiceIds);
        return R.ok();
    }

    /**
     * 补充信息，5要素齐全，自动触发验真
     */
    @RequestMapping("/updateFromHome")
    @RequiresPermissions("input:invoice:updateFromHome")
    public R updateFromHome(@RequestBody InputInvoiceEntity invoice) {
        invoiceService.updateById(invoice);
        return R.ok();
    }

    /**
     * 人工匹配
     * @return
     */
    @PostMapping("/manualMatch")
    @RequiresPermissions("input:invoice:manualMatch")
    public R match(String invoiceIds) {
        invoiceService.manualMatch(invoiceIds);
        return R.ok();
    }

    @GetMapping("/relatedInvoice")
    @RequiresPermissions("input:invoice:relatedInvoice")
    public R relatedInvoice() {
        List<SysDeptEntity> deptList = sysDeptService.list();
        List<InputInvoiceEntity> invoiceList = invoiceService.list();
        for (SysDeptEntity dept : deptList) {
            if (StrUtil.isBlank(dept.getTaxCode())) {
                continue;
            }
            for (InputInvoiceEntity invoice: invoiceList) {
                if (dept.getTaxCode().equals(invoice.getInvoicePurchaserParagraph())) {
                    invoice.setCompanyId(dept.getDeptId().intValue());
                    invoiceService.update(invoice);
                }
            }
        }
        return R.ok();
    }

    @GetMapping("/relatedBatch")
    @RequiresPermissions("input:invoice:relatedInvoice")
    public R relatedBatch() {
        List<SysDeptEntity> deptList = sysDeptService.list();
        List<InputInvoiceBatchEntity> batchList = invoiceBatchService.list();
        for (SysDeptEntity dept : deptList) {
            if (StrUtil.isBlank(dept.getTaxCode())) {
                continue;
            }
            for (InputInvoiceBatchEntity batch : batchList) {
                InputInvoiceEntity inputInvoiceEntity = new InputInvoiceEntity();
                inputInvoiceEntity.setInvoiceBatchNumber(batch.getId()+"");
                List<InputInvoiceEntity> invoiceList = invoiceService.getListByBatchId(inputInvoiceEntity);
                for (InputInvoiceEntity invoice: invoiceList) {
                    if (dept.getTaxCode().equals(invoice.getInvoicePurchaserParagraph())) {
                        inputInvoiceEntity.setCompanyId(dept.getDeptId().intValue());
                        invoiceBatchService.update(batch);
                        break;
                    }
                }
            }

        }
        return R.ok();
    }

    /**
     * 勾选认证
     */
    @RequestMapping("/certificationList")
    @RequiresPermissions("input:invoice:invoiceList")
    public R certification(@RequestParam Map<String, Object> params, InputInvoiceEntity invoiceEntity) {
        if (invoiceEntity.getInvoiceBatchNumber() != null && !"".equals(invoiceEntity.getInvoiceBatchNumber())) {
            params.put("invoiceBatchNumberList", invoiceEntity.getInvoiceBatchNumber());
        } else {
            params.put("invoiceBatchNumberList", "");
        }
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        if (!"".equals(invoiceUploadDateArray) && invoiceUploadDateArray != null) {
            String[] range = invoiceUploadDateArray.split(",");
            String begin = range[0];
            String end = range[1];
            params.put("invoiceUploadDateArray", (begin + "," + end));
        }

        Long userId = 0L;
        String createUserName = (String)params.get("createUserName");
        if (StringUtils.isNotBlank(createUserName)) {
            userId = sysUserService.getIdByName(params.get("createUserName").toString());
        }
        invoiceEntity.setCreateBy(null == userId ? 0 : userId.intValue());
        // 区分是总览还是认证列表
        params.put("certification", "0");
        PageUtils page = invoiceService.queryPage(params, invoiceEntity);
        List<InputInvoiceEntity> invoiceEntityList = (List<InputInvoiceEntity>) page.getList();
        if (!invoiceEntityList.isEmpty()) {
            List<InputInvoiceEntity> list = invoiceService.getListAndCreateName(invoiceEntityList, createUserName);
            if (!list.isEmpty()) {
                invoiceService.setBatchNumber(list);
            }
            page.setList(list);
        }
        List<InputInvoiceBatchEntity> invoiceBatchEntityList = invoiceBatchService.getList();
        R r = new R();
        r.put("page", page);
        r.put("invoiceBatchEntityList", invoiceBatchEntityList);
        return r;
    }

    /**
     * 手动上传
     * @param
     * @param request
     * @return
     */
    @RequestMapping("/saveInvoiceByManual")
    @RequiresPermissions("input:invoice:invoiceList")
    public R saveInvoiceByManual(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
        invoiceEntity.setInvoiceCode((String) params.get("invoiceCode"));
        invoiceEntity.setInvoiceNumber((String) params.get("invoiceNumber"));
        invoiceEntity.setInvoiceEntity((String) params.get("invoiceEntity"));
        invoiceEntity.setInvoiceCreateDate((String) params.get("invoiceCreateDate"));
        invoiceEntity.setInvoiceFromto((String) params.get("invoiceFromto"));
        if (StringUtils.isNotBlank((String) params.get("invoiceFreePrice"))) {
            invoiceEntity.setInvoiceFreePrice(new BigDecimal((String) params.get("invoiceFreePrice")));
        }
        invoiceEntity.setInvoiceCheckCode((String) params.get("invoiceCheckCode"));
        invoiceEntity.setCreateBy(ShiroUtils.getUserEntity().getUserId().intValue());
        invoiceEntity.setCreateUserName(ShiroUtils.getUserEntity().getUsername());
        invoiceEntity.setInvoiceImage((String) params.get("invoiceImage"));
        invoiceEntity.setInvoiceUploadType("1"); //手动上传


        Date date = DateUtil.date();
        String invoiceSeq = DateUtil.format(date, "yyyyMMdd_HHmm");
        String uploadDate = DateUtil.format(date, DatePattern.NORM_DATE_PATTERN);
        String invoiceSeq2 = DateUtil.format(date, "yyyyMMdd");
        int index=0;
        String seq=invoiceService.getLastSeq(invoiceSeq2);
        if(StrUtil.isNotBlank(seq)){
            String[] s = seq.split("_");
            String lastindex = s[2];
            index=Integer.parseInt(lastindex)+1;
        }else{
            index=1;
        }
        invoiceEntity.setInvoiceSeq(invoiceSeq + "_" + index);

        invoiceEntity.setCreateBy(ShiroUtils.getUserId().intValue());
        invoiceEntity.setInvoiceUploadDate(uploadDate);
        if (invoiceService.save(invoiceEntity)) {
            invoiceService.functionCheckTrue(invoiceEntity);
        } else {
            return R.error("保存失败");
        }
        return R.ok();
    }

}

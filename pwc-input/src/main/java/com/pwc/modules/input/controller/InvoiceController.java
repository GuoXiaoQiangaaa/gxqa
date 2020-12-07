package com.pwc.modules.input.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.utils.*;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.entity.vo.*;
import com.pwc.modules.input.service.InputInvoiceBatchService;
import com.pwc.modules.input.service.InputInvoiceMaterialService;
import com.pwc.modules.input.service.InputInvoiceRefundService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.sys.entity.SysConfigEntity;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.service.SysConfigService;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysUserService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.NumberFormat;
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
    @Autowired
    private InputInvoiceRefundService invoiceRefundService;
    @Autowired
    private SysConfigService sysConfigService;


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
            if (invoiceMaterialEntityList.get(j).getSphJe() != null && !"".equals(invoiceMaterialEntityList.get(j).getSphJe())) {
                sphJe2 = sphJe2.add(invoiceMaterialEntityList.get(j).getSphJe());
            }
            if (invoiceMaterialEntityList.get(j).getSphSe() != null && !"".equals(invoiceMaterialEntityList.get(j).getSphSe())) {
                sphSe2 = sphSe2.add(invoiceMaterialEntityList.get(j).getSphSe());
            }

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
     *
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
        String createUserName = (String) params.get("createUserName");
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
     * 批量验真
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
    public R pendingReturn(@RequestBody Map<String, Object> ids) {

        String idlist = ids.get("ids").toString();

        try {
            String str[] = idlist.replace("[", "").replace("]", "").split(",");
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
        if (result.equals("19")) {
            return R.error(510, "网络不稳定，请重新操作！");
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
     *
     * @param invoiceEntity
     * @return
     */
    @RequestMapping("/batcheVerty")
    @RequiresPermissions("input:invoice:batcheVerty")
    public R batcheVerty(InputInvoiceEntity invoiceEntity) {
        try {
            invoiceEntity = invoiceService.get(invoiceEntity);

            invoiceService.functionVerfy(invoiceEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
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
     *
     * @param invoiceIds
     * @param batchId
     * @return
     */
    @PostMapping("/relate")
    @RequiresPermissions("input:invoice:related")
    public R relate(String invoiceIds, String batchId) {
        invoiceService.relatedBatch(invoiceIds, batchId);
        return R.ok();
    }

    /**
     * 人工入账
     *
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
     *
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
            for (InputInvoiceEntity invoice : invoiceList) {
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
                inputInvoiceEntity.setInvoiceBatchNumber(batch.getId() + "");
                List<InputInvoiceEntity> invoiceList = invoiceService.getListByBatchId(inputInvoiceEntity);
                for (InputInvoiceEntity invoice : invoiceList) {
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
        String createUserName = (String) params.get("createUserName");
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
     *
     * @param
     * @param request
     * @return
     */
    @RequestMapping("/saveInvoiceByManual")
//    @RequiresPermissions("input:invoice:saveInvoiceByManual")
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
        int index = 0;
        String seq = invoiceService.getLastSeq(invoiceSeq2);
        if (StrUtil.isNotBlank(seq)) {
            String[] s = seq.split("_");
            String lastindex = s[2];
            index = Integer.parseInt(lastindex) + 1;
        } else {
            index = 1;
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

    @RequestMapping("/poListUpload")
    @RequiresPermissions("input:invoice:poListUpload")
    public R poListUpload(@RequestBody InputInvoiceEntity invoiceEntity) {

        return R.ok();
    }

    /**
     * 手工上传
     *
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
            List listYes = new ArrayList();
            List listNo = new ArrayList();
            for (int i = 0; i < invoiceEntity.getImagesList().size(); i++) {
                InputInvoiceEntity invoice = new InputInvoiceEntity();
                invoice.setInvoiceImage(invoiceEntity.getImagesList().get(i));
                String fileType = StrUtil.subAfter(invoiceEntity.getImagesList().get(i), ".", true);
                if ("png".equals(fileType) || "jpg".equals(fileType) || "jpeg".equalsIgnoreCase(fileType)) {
                    invoice.setInvoiceType("picture");
                } else if ("pdf".equals(fileType)) {
                    invoice.setInvoiceType("pdf");
                } else if ("ofd".equals(fileType)) {
                    invoice.setInvoiceType("ofd");
                }
                invoice.setCreateBy(ShiroUtils.getUserId().intValue());
                invoice.setCompanyId(ShiroUtils.getUserEntity().getDeptId().intValue());
                invoice.setUploadType("2");  // 手动上传
                invoice.setInvoiceFromto(invoiceEntity.getInvoiceFromto());
                invoice.setImageName(invoiceEntity.getImagesNameList().get(i));
                invoice.setInvoiceBatchNumber(DateUtils.format(new Date(), "yyyyMMddHHmmss"));
                String str = invoiceService.functionSaveInvoice(invoice);
                if (InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue().equals(invoice.getInvoiceStatus())) {
                    failSum++;
                    listNo.add(invoiceEntity.getImagesNameList().get(i));
                } else if (InputConstant.InvoiceStatus.REPEAT.getValue().equals(invoice.getInvoiceStatus())) {
                    listNo.add(invoiceEntity.getImagesNameList().get(i));
                } else {
                    //  (InputConstant.InvoiceStatus.PENDING_VERIFICATION.getValue().equals(invoice.getInvoiceStatus()))
                    recSum++;
                    listYes.add(invoiceEntity.getImagesNameList().get(i));
                }
                if (str.equals("19")) {
                    return R.error(510, "网络不稳定，请重新操作！");
                }
            }
//        r.put("tolSum", tolSum);
//        r.put("recSum", recSum);
//        r.put("partSum", partSum);
//        r.put("failSum", failSum);
            r.put("ok", listYes);
            r.put("error", listNo);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(601, "系统异常，请稍后再试");
        }
        return r;
    }

    /**
     * 小程序扫描上传
     *
     * @param invoiceEntity
     * @param request
     * @return
     */
    @RequestMapping("/saveScanning")
    public R saveScanning(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        // 文件保存路径
        String filepath = "statics/db/pic/";
        UploadFileEntity fileEntity = UploadKitUtil.uploadFile(file, filepath, true, false);
        R r = new R();
        try {
            InputInvoiceEntity invoice = new InputInvoiceEntity();
            invoice.setInvoiceImage(fileEntity.getServerPath());
            String fileType = StrUtil.subAfter(fileEntity.getServerPath(), ".", true);
            if ("png".equals(fileType) || "jpg".equals(fileType) || "jpeg".equalsIgnoreCase(fileType)) {
                invoice.setInvoiceType("picture");
            } else if ("pdf".equals(fileType)) {
                invoice.setInvoiceType("pdf");
            } else if ("ofd".equals(fileType)) {
                invoice.setInvoiceType("ofd");
            } else {
                return R.error(500, "不支持此格式上传");
            }
            invoice.setCreateBy(0);
            invoice.setCompanyId(0);
            // 1  扫描上传
            invoice.setUploadType(params.get("from").toString());
            invoice.setInvoiceBatchNumber(params.get("code").toString());
            invoice.setInvoiceFromto("");
            invoice.setImageName(file.getOriginalFilename());
            String str = invoiceService.functionSaveInvoice(invoice);
            if (str.equals("19")) {
                return R.error(510, "网络不稳定，请重新操作！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(601, "系统异常，请稍后再试");
        }
        return r;
    }

    /**
     * 作废
     *
     * @param params
     * @return
     */
    @RequestMapping("updateVoid")
    //   @RequiresPermissions("input:invoicete:updateVoid")
    public R updateVoid(@RequestParam Map<String, Object> params) {
        String[] ids = params.get("ids").toString().split(",");
        for (String id : ids) {
            InputInvoiceEntity invoiceEntity = invoiceService.getById(Integer.valueOf(id));
//            if((InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue()).equals(invoiceEntity.getInvoiceStatus())){ //未认证 可以 PENDING_CERTIFIED
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.INVALID.getValue());
            invoiceService.updateById(invoiceEntity);
//        }
        }
        return R.ok();
    }


    /**
     * 异常页面展示
     *
     * @param params
     * @param invoiceEntity
     * @return
     */
    @RequestMapping("/findException")
//    @RequiresPermissions("input:invoice:findException")
    public R findException(@RequestParam Map<String, Object> params) {
        String createUserName = (String) params.get("createUserName");
        String[] state = null;
        if (StringUtils.isNotBlank((String) params.get("invoiceStatus"))) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue(),
                    InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue(),
                    InputConstant.InvoiceStatus.FIRST_VERIFICATION_FAILED.getValue(),
                    InputConstant.InvoiceStatus.DIFFERENT_MESSAGE.getValue(),
                    InputConstant.InvoiceStatus.CHARGE_AGAINST.getValue(),
                    InputConstant.InvoiceStatus.DIFFERENCE.getValue(),
                    InputConstant.InvoiceStatus.REPEAT.getValue(),
                    InputConstant.InvoiceStatus.REFUND.getValue(),
                    InputConstant.InvoiceStatus.REVERSE.getValue(),
                    InputConstant.InvoiceStatus.INVALID.getValue(),
            };
        }
        params.put("invoiceStatus", state);
        InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
        PageUtils page = invoiceService.queryPage(params, invoiceEntity);
        List<InputInvoiceEntity> invoiceEntityList = (List<InputInvoiceEntity>) page.getList();
        if (!invoiceEntityList.isEmpty()) {
            List<InputInvoiceEntity> list = invoiceService.getListAndCreateName(invoiceEntityList, createUserName);
            page.setList(list);
        }
        List<InputInvoiceBatchEntity> invoiceBatchEntityList = invoiceBatchService.getList();
        R r = new R();
        r.put("page", page);
        return r;
    }

    /**
     * 补录
     *
     * @param invoiceEntity
     * @return
     */
    @RequestMapping("/makeUpInvoice")
//    @RequiresPermissions("input:invoice:makeUpInvoice")
    public R makeUpInvoice(@RequestBody InputInvoiceEntity invoiceEntity) {
        invoiceService.makeUpInvoice(invoiceEntity);
        return R.ok();
    }

    /**
     * 操作退票
     *
     * @param params
     * @return
     */
    @RequestMapping("/refundInvoice")
//    @RequiresPermissions("input:invoice:refundInvoice")
    public R refundInvoice(@RequestParam Map<String, Object> params) {
        String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
        String[] invoiceIds = StrUtil.isNotBlank((String) params.get("ids")) ? ((String) params.get("ids")).split(",") : null;
        String reason = (String) params.get("reason");
        String expressCompany = (String) params.get("expressCompany");
        String expressNo = (String) params.get("expressNo");
        if (null != invoiceIds) {
            Date date = new Date();
            for (String invoiceId : invoiceIds) {
                InputInvoiceRefundEntity invoiceRefundEntity = new InputInvoiceRefundEntity();
                invoiceRefundEntity.setInvoiceId(Integer.parseInt(invoiceId));
                invoiceRefundEntity.setRefundTime(date);
                invoiceRefundEntity.setRefundUser(username);
                invoiceRefundEntity.setDetailedReason(reason);
                invoiceRefundEntity.setExpressCompany(expressCompany);
                invoiceRefundEntity.setExpressNo(expressNo);
                invoiceRefundService.save(invoiceRefundEntity);
                InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                String invoiceReturn = invoiceRefundEntity.getRefundStatus();
                // TODO: 现有状态为2，不明不白，因为invoice退款数据库为1，所以判断一下 Author zk
                if ("2".equals(invoiceReturn)) {
                    invoiceReturn = "1";
                }

                invoiceEntity.setInvoiceReturn(invoiceReturn);
                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.REFUND.getValue());
                invoiceEntity.setExpressNumber(expressNo);
                invoiceEntity.setId(invoiceRefundEntity.getInvoiceId());
                invoiceService.update(invoiceEntity);
            }
        }

        return R.ok();
    }

    /**
     * 查看退票信息
     */
    @RequestMapping("/findRefund/{id}")
    //   @RequiresPermissions("inpput:invoice:findRefund")
    public R findRefund(@PathVariable("id") Integer id) {
        InputInvoiceRefundEntity invoiceRefund = invoiceRefundService.getOne(new QueryWrapper<InputInvoiceRefundEntity>().eq("invoice_id", id));
        return R.ok().put("invoiceRefund", invoiceRefund);
    }

    @GetMapping(value = "exportRecordListByIds")
    //   @RequiresPermissions("input:invoice:exportRecordListByIds")
    public R exportRecordListByIds(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        InputInvoiceEntity invoiceEntity = JSON.parseObject(JSON.toJSONString(params), InputInvoiceEntity.class);
        System.out.println(invoiceEntity);
        List<InputInvoiceEntity> invoiceEntityList = invoiceService.getListById(invoiceEntity);
        invoiceService.checkStstus(invoiceEntityList);
//        for (InputInvoiceEntity Entity:invoiceEntityList){
//            if(Entity.getInvoiceFreePrice()!=null && Entity.getInvoiceTaxPrice()!=null){
//                NumberFormat nf = NumberFormat.getPercentInstance();
//                Entity.getInvoiceTaxPrice().divide(Entity.getInvoiceFreePrice(),2);
//                Entity.setTax(nf.format(Entity.getInvoiceTaxPrice().divide(Entity.getInvoiceFreePrice(),2)));
//            }
//        }
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
    //  @RequiresPermissions("input:invoice:exportRecordList")
    public R exportRecordList(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String[] state = null;
        if (StringUtils.isNotBlank((String) params.get("invoiceStatus"))) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue(),
                    InputConstant.InvoiceStatus.REPEAT.getValue(),
                    InputConstant.InvoiceStatus.INVALID.getValue(),
                    InputConstant.InvoiceStatus.REFUND.getValue(),
                    InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue(),
                    InputConstant.InvoiceStatus.FIRST_VERIFICATION_FAILED.getValue(),
            };
        }
        params.put("invoiceStatus", state);
        int count = invoiceService.getListByShow();
        params.put("limit", count + "");
        InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
        PageUtils pageUtils = invoiceService.queryPage(params, invoiceEntity);
        List<InputInvoiceEntity> invoiceEntityList = (List<InputInvoiceEntity>) pageUtils.getList();
        List<InputInvoiceEntity> invoiceEntities = invoiceService.getListByItems(invoiceEntityList);
        invoiceService.checkStstus(invoiceEntities);
//        for (InputInvoiceEntity Entity:invoiceEntities){
//            if(Entity.getInvoiceFreePrice()!=null && Entity.getInvoiceTaxPrice()!=null){
//                NumberFormat nf = NumberFormat.getPercentInstance();
//                Entity.getInvoiceTaxPrice().divide(Entity.getInvoiceFreePrice(),2);
//                Entity.setTax(nf.format(Entity.getInvoiceTaxPrice().divide(Entity.getInvoiceFreePrice(),2)));
//            }
//        }
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceEntity.class).setDataList(invoiceEntities).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 查询验真页面数据
     *
     * @param params
     * @return
     */
    @GetMapping(value = "getVerification")
    //  @RequiresPermissions("input:invoice:getVerification")
    public R getVerification(@RequestParam Map<String, Object> params) {
        String createUserName = (String) params.get("createUserName");
        String[] state = null;
        if (StringUtils.isNotBlank((String) params.get("invoiceStatus"))) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue(),
                    InputConstant.InvoiceStatus.FIRST_VERIFICATION_FAILED.getValue(),
                    InputConstant.InvoiceStatus.PENDING_MATCHED.getValue(),
//                    InputConstant.InvoiceStatus.REPEAT.getValue(),
//                    InputConstant.InvoiceStatus.REFUND.getValue(),
                    InputConstant.InvoiceStatus.DIFFERENT_MESSAGE.getValue(),
                    InputConstant.InvoiceStatus.CHARGE_AGAINST.getValue(),
                    InputConstant.InvoiceStatus.DIFFERENCE.getValue(),
                    InputConstant.InvoiceStatus.REVERSE.getValue(),
//                    InputConstant.InvoiceStatus.INVALID.getValue(),

            };
        }
        params.put("invoiceStatus", state);
        InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
        PageUtils page = invoiceService.queryPage(params, invoiceEntity);
        List<InputInvoiceEntity> invoiceEntityList = (List<InputInvoiceEntity>) page.getList();


        if (!invoiceEntityList.isEmpty()) {
            List<InputInvoiceEntity> list = invoiceService.getListAndCreateName(invoiceEntityList, createUserName);
//            for (InputInvoiceEntity Entity:list){
//                if(Entity.getInvoiceFreePrice()!=null && Entity.getInvoiceTaxPrice()!=null){
//                    NumberFormat nf = NumberFormat.getPercentInstance();
//                    Entity.getInvoiceTaxPrice().divide(Entity.getInvoiceFreePrice(),2);
//                    Entity.setTax(nf.format(Entity.getInvoiceTaxPrice().divide(Entity.getInvoiceFreePrice(),2)));
//                }
//            }
            page.setList(list);
        }
        R r = new R();
        r.put("page", page);
        return r;
    }

    @GetMapping(value = "updateVerificationToMany")
    //  @RequiresPermissions("input:invoice:updateVerificationToMany")
    public R updateVerificationToMany(@RequestParam Map<String, Object> params) {
        String id = (String) params.get("id");
        if (StringUtils.isNotBlank(id)) {
            InputInvoiceEntity entity = invoiceService.VerificationToMany(id);
            if (entity.getInvoiceStatus().equals(InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue()) || entity.getInvoiceStatus().equals(InputConstant.InvoiceStatus.FIRST_VERIFICATION_FAILED.getValue())) {
                return R.error("验证失败");
            } else {
                return R.ok();
            }
        } else {
            return R.error("id不能为空");
        }
    }

    @GetMapping(value = "exportVerification")
    //  @RequiresPermissions("input:invoice:exportVerification")
    public R exportVerification(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String createUserName = (String) params.get("createUserName");
        List<InputInvoiceEntity> invoiceEntityList = new ArrayList<>();
        if (StringUtils.isNotBlank((String) params.get("ids"))) {
            String[] ids = ((String) params.get("ids")).split(",");
            for (String id : ids) {
                invoiceEntityList.add(invoiceService.getById(id));
            }
        } else {
            String[] state = null;
            if (StringUtils.isNotBlank((String) params.get("invoiceStatus"))) {
                state = new String[]{
                        (String) params.get("invoiceStatus")
                };
            } else {
                state = new String[]{
                        InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue(),
                        InputConstant.InvoiceStatus.FIRST_VERIFICATION_FAILED.getValue(),
                        InputConstant.InvoiceStatus.PENDING_MATCHED.getValue(),
                        InputConstant.InvoiceStatus.CHARGE_AGAINST.getValue(),
                        InputConstant.InvoiceStatus.DIFFERENCE.getValue(),
                        InputConstant.InvoiceStatus.REVERSE.getValue(),
                        InputConstant.InvoiceStatus.REFUND.getValue(),
                        InputConstant.InvoiceStatus.INVALID.getValue(),
                        InputConstant.InvoiceStatus.REPEAT.getValue(),
                };
            }
            int count = invoiceService.getListByShow();
            params.put("limit", count + "");
            params.put("invoiceStatus", state);
            InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
            PageUtils page = invoiceService.queryPage(params, invoiceEntity);
            invoiceEntityList = (List<InputInvoiceEntity>) page.getList();
        }
        List<InputInvoiceEntity> list = invoiceService.getListAndCreateName(invoiceEntityList, createUserName);
        for (InputInvoiceEntity Entity : list) {
            if (Entity.getInvoiceFreePrice() != null && Entity.getInvoiceTaxPrice() != null) {
                NumberFormat nf = NumberFormat.getPercentInstance();
                Entity.getInvoiceTaxPrice().divide(Entity.getInvoiceFreePrice(), 2);
                Entity.setTax(nf.format(Entity.getInvoiceTaxPrice().divide(Entity.getInvoiceFreePrice(), 2)));
            }
        }
        invoiceService.checkStstus(list);
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InvoiceVerificationVo.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 票据总览列表
     */
    @RequestMapping("/invoiceList")
    //@RequiresPermissions("input:invoice:invoiceList")
    public R invoiceList(@RequestParam Map<String, Object> params) {
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        }
        params.put("invoiceStatus", state);
        PageUtils page = invoiceService.getPageList(params);
        return R.ok().put("page", page);
    }

    @RequestMapping("/exportInvoiceList")
    //@RequiresPermissions("input:invoice:exportInvoiceList")
    public R exportInvoiceList(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        }
        int count = invoiceService.getListByShow();
        params.put("limit", count + "");
        params.put("invoiceStatus", state);
        List<InputInvoiceEntity> list = invoiceService.exportPageList(params);
        invoiceService.checkStstus(list);
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceListVo.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 修改分类
     *
     * @param invoiceEntity
     * @return
     */
    @RequestMapping("/updateClass")
//    @RequiresPermissions("input:invoice:updateClass")
    public R updateClass(@RequestParam Map<String, Object> params) {
        String id = (String) params.get("id");
        String reason = (String) params.get("reason");
        String invoiceClass = (String) params.get("invoiceClass");
        InputInvoiceEntity entity = invoiceService.getById(id);
        if (entity != null) {
            entity.setInvoiceClass(invoiceClass);
            entity.setClassReason(reason);
            invoiceService.updateById(entity);
        } else {
            return R.error("未查询到数据");
        }
        return R.ok();
    }

    /**
     * 更新进度
     *
     * @param params
     * @return
     */
    @RequestMapping("/updateProgressRate")
//    @RequiresPermissions("input:invoice:updateProgressRate")
    public R updateProgressRate(@RequestParam Map<String, Object> params) {
        String id = (String) params.get("id");
        String progressRate = (String) params.get("progressRate");
        InputInvoiceEntity entity = invoiceService.getById(id);
        if (entity != null) {
            entity.setProgressRate(progressRate);
            invoiceService.updateById(entity);
        } else {
            return R.error("未查询到数据");
        }
        return R.ok();
    }

    /**
     * 认证列表
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListByCertification")
//    @RequiresPermissions("input:invoice:getListByCertification")
    public R getListByCertification(@RequestParam Map<String, Object> params) {
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    // 待认证
                    InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue(),
                    // 认证中
                    InputConstant.InvoiceStatus.CERTIFICATION.getValue(),
                    // 撤销认证中
                    InputConstant.InvoiceStatus.UNDO_CERTIFICATION.getValue(),
                    // 认证成功
                    InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue(),
                    // 认证失败
                    InputConstant.InvoiceStatus.AUTHENTICATION_FAILED.getValue(),
                    // 撤销失败
                    InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue(),
            };
        }
        params.put("invoiceStatus", state);
        PageUtils page = invoiceService.getPageList(params);
        return R.ok().put("page", page);
    }

    /**
     * 勾选认证
     */
    @RequestMapping("/getOperateByCertification")
//    @RequiresPermissions("input:invoice:getOperateByCertification")
    public R getOperateByCertification(@RequestParam Map<String, Object> params) {
        String ids = (String) params.get("ids");
        if (StringUtils.isNotBlank(ids)) {
            invoiceService.getCertification(ids, "1");
        }
        return R.ok();
    }

    /**
     * 撤销认证
     */
    @RequestMapping("/getRevokeByCertification")
//    @RequiresPermissions("input:invoice:getRevokeByCertification")
    public R getRevokeByCertification(@RequestParam Map<String, Object> params) {
        String ids = (String) params.get("ids");
        if (StringUtils.isNotBlank(ids)) {
            invoiceService.getCertification(ids, "6");
        }
        return R.ok();
    }

    /**
     * 导入认证
     */
    @RequestMapping("/getImportByCertification")
//    @RequiresPermissions("input:invoice:getImportByCertification")
    public R getImportByCertification(@RequestParam("files") MultipartFile file) {
        List<String> lists = null;
        try {
            lists = invoiceService.ImportByCertification(file);
            return R.ok().put("data", lists);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入TE发票失败");
        }
    }

    /**
     * 导出模版
     *
     * @param params
     * @param response
     * @return
     */
    @RequestMapping("/getUploadByCertification")
//    @RequiresPermissions("input:invoice:getUploadByCertification")
    public R getUploadByCertification(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        List<CertificationVo> voList = new ArrayList();
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", CertificationVo.class).setDataList(voList).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 从抵账库同步发票数据
     *
     * @param params
     * @return
     */
    @RequestMapping("/updateInvoice")
//    @RequiresPermissions("input:invoice:updateInvoice")
    public R updateInvoice(@RequestParam Map<String, Object> params) {
        // 同步规则
        invoiceService.saveInvoiceBySync(params);
        return R.ok();
    }

    /**
     * 查询账票匹配成功
     *
     * @param params
     * @return
     */
    @RequestMapping("/getMatchSuccess")
//    @RequiresPermissions("input:invoice:getMatchSuccess")
    public R getMatchSuccess(@RequestParam Map<String, Object> params) {
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    // 认证成功
                    InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue(),
                    // 撤销认证失败
                    InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue(),
            };
        }
        // 账票匹配成功
        params.put("invoiceMatch", InputConstant.InvoiceMatch.MATCH_YES.getValue());
        params.put("invoiceStatus", state);
        PageUtils page = invoiceService.getPageListBySpecial(params);
        return R.ok().put("page", page);
    }

    /**
     * 账票匹配成功下载
     *
     * @param params
     * @return
     */
    @RequestMapping("/getMatchSuccessByExcel")
//    @RequiresPermissions("input:invoice:getMatchSuccessByExcel")
    public R getMatchSuccessByExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    // 认证成功
                    InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue(),
                    // 撤销认证失败
                    InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue(),
            };
        }
        // 账票匹配成功
        params.put("invoiceMatch", InputConstant.InvoiceMatch.MATCH_YES.getValue());
        params.put("invoiceStatus", state);
        List<InputInvoiceEntity> list = invoiceService.exportPageList(params);
        invoiceService.checkStstus(list);
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InvoiceMatchVo.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 查询未匹配数据（有票无帐数据）
     *
     * @param params
     * @return
     */
    @RequestMapping("/getMatchByNo")
//    @RequiresPermissions("input:invoice:getMatchByNo")
    public R getMatchByNo(@RequestParam Map<String, Object> params) {
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    // 认证成功
                    InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue(),
                    // 撤销认证失败
                    InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue(),
            };
        }
        // 有票无帐的
        params.put("invoiceMactch", InputConstant.InvoiceMatch.MATCH_NO.getValue());
        params.put("invoiceStatus", state);
        PageUtils page = invoiceService.getPageListBySpecial(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询未匹配数据（有票无帐数据）下载
     *
     * @param params
     * @return
     */
    @RequestMapping("/getMatchByNoByExcel")
//    @RequiresPermissions("input:invoice:getMatchByNoByExcel")
    public R getMatchByNoByExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    // 认证成功
                    InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue(),
                    // 撤销认证失败
                    InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue(),
            };
        }
        // 有票无帐的
        params.put("invoiceMactch", InputConstant.InvoiceMatch.MATCH_NO.getValue());
        params.put("invoiceStatus", state);
        List<InputInvoiceEntity> list = invoiceService.exportPageList(params);
        invoiceService.checkStstus(list);
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InvoiceMatchByNoVo.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }


    /**
     * 查询匹配差异数据
     *
     * @param params
     * @return
     */
    @RequestMapping("/getMatchByError")
//    @RequiresPermissions("input:invoice:getMatchByError")
    public R getMatchByError(@RequestParam Map<String, Object> params) {
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    // 认证成功
                    InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue(),
                    // 撤销认证失败
                    InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue(),
            };
        }
        // 有票无帐的
        params.put("invoiceMactch", InputConstant.InvoiceMatch.MATCH_ERROR.getValue());
        params.put("invoiceStatus", state);
        PageUtils page = invoiceService.getPageListBySpecial(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询匹配差异数据下载
     *
     * @param params
     * @return
     */
    @RequestMapping("/getMatchByErrorByExcel")
//    @RequiresPermissions("input:invoice:getMatchByErrorByExcel")
    public R getMatchByErrorByExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String[] state = null;
        String invoiceStatus = (String) params.get("invoiceStatus");
        if (StringUtils.isNotBlank(invoiceStatus)) {
            state = new String[]{
                    (String) params.get("invoiceStatus")
            };
        } else {
            state = new String[]{
                    // 认证成功
                    InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue(),
                    // 撤销认证失败
                    InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue(),
            };
        }
        // 有票无帐的
        params.put("invoiceMactch", InputConstant.InvoiceMatch.MATCH_ERROR.getValue());
        params.put("invoiceStatus", state);
        List<InputInvoiceEntity> list = invoiceService.exportPageList(params);
        invoiceService.checkStstus(list);
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InvoiceMatchByErrorVo.class).setDataList(list).write(response, fileName).dispose();
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
    @RequestMapping("/saveByEntry")
//    @RequiresPermissions("input:invoice:saveByEntry")
    public R saveByEntry(@RequestParam Map<String, Object> params) {
        invoiceService.manualEntryBySap(params);
        return R.ok();
    }

    /**
     * 设置容差
     *
     * @param params
     * @return
     */
    @RequestMapping("/saveTolerance")
//    @RequiresPermissions("input:invoice:saveTolerance")
    public R saveTolerance(@RequestParam Map<String, Object> params) {
        SysConfigEntity entity = new SysConfigEntity();
        sysConfigService.updateValueByKey("TOLERANCE_VALUE", (String) params.get("value"));
        return R.ok();
    }

    /**
     * 两次验真
     */
    @RequestMapping("/getVerificationByTwo")
    public R getVerificationByTwo(@RequestParam Map<String, Object> params) {
        String id = params.get("id").toString();
        InputInvoiceEntity invoiceEntity = invoiceService.getById(id);
        invoiceService.mainProcess(invoiceEntity);
        return R.ok();
    }

    public R getMatchByResult(@RequestParam Map<String, Object> params) {
        return R.ok();
    }


}

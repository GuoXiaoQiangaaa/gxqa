package com.pwc.modules.input.controller;

import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zk
 */
@RestController
@RequestMapping("/input/invoiceBatch")
public class InvoiceBatchController {

    @Autowired
    private InputInvoiceBatchService invoiceBatchService;
    @Autowired
    private InputInvoiceConditionMapService invoiceConditionMapService;
    @Autowired
    private InputInvoiceMaterialSapService invoiceMaterialSapService;
    @Autowired
    private InputInvoiceService invoiceService;
    @Autowired
    private InputInvoiceMaterialService invoiceMaterialService;
    @Autowired
    private InputInvoiceVoucherService invoiceVoucherService;


    @RequestMapping("listForThree")
    @RequiresPermissions("input:invoiceBatch:listForThree")
    public R listForThree(@RequestParam Map<String, Object> params, HttpServletRequest httpRequest) {

        Integer currPage = 0;
        Integer limit = 0;
        if (params.get("page") != null) {
            currPage = Integer.parseInt((String) params.get("page"));
        }
        if (params.get("limit") != null) {
            limit = Integer.parseInt((String) params.get("limit"));
        }
        params.put("offset", (currPage - 1) * limit);
        params.put("outFlag", "");
        PageUtils page = invoiceBatchService.findListForThree(params);
        List<InputInvoiceBatchEntity> invoiceBatchEntityList = invoiceBatchService.getList();

        R r = new R();
        r.put("page", page);
        r.put("invoiceBatchEntityList", invoiceBatchEntityList);
        return r;
    }

    // 获取批次中第一组的物料信息和sap物料信息
    @RequestMapping("/info2")
    public R info2(InputInvoiceEntity invoiceEntity) {
        R r = new R();
        //获取总计组数
        Integer pageSize = invoiceService.getGroupSizeByBatchId(invoiceEntity);
        // 根据批次和组号获取小组发票
        List<InputInvoiceEntity> invoiceEntityList = invoiceService.getListByBatchIdAndInvoiceGroup(invoiceEntity);
        // 根据发票Id获取发票物料信息
        List<String> invoiceIds = new ArrayList<>();
        List<String> invoiceNumbers = new ArrayList<>();
        for (int i = 0; i < invoiceEntityList.size(); i++) {
            invoiceIds.add(String.valueOf(invoiceEntityList.get(i).getId()));
            invoiceNumbers.add(invoiceEntityList.get(i).getInvoiceNumber());
        }
        InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
        invoiceMaterialEntity.setInvoiceIds(invoiceIds);
//        List<InputInvoiceMaterialEntity> entityList = invoiceMaterialService.getListByInvoiceId(invoiceMaterialEntity);
        if (StringUtils.isNotBlank(invoiceEntity.getPitch())) {
            invoiceMaterialEntity.setPitch(invoiceEntity.getPitch().split("&amp;"));
        }
        List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = invoiceMaterialService.getListByInvoiceId(invoiceMaterialEntity);
        // 根据批次id获取所有sap物料信息
        InputInvoiceMaterialSapEntity invoiceMaterialSapEntity = new InputInvoiceMaterialSapEntity();
        invoiceMaterialSapEntity.setBatchId(invoiceEntity.getInvoiceBatchNumber());

        if (StringUtils.isNotBlank(invoiceEntity.getPitch2())) {
            invoiceMaterialSapEntity.setPitch(invoiceEntity.getPitch2().split("&amp;"));
        }
        List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList = invoiceMaterialSapService.getListByBatchId(invoiceMaterialSapEntity);

        // 未匹配发票物料
        List<InputInvoiceMaterialEntity> materialEntityList = new ArrayList<>();
        // 未匹配sap物料
        List<InputInvoiceMaterialSapEntity> materialSapEntityList = new ArrayList<>();
        // 已匹配sap物料
        List<InputInvoiceMaterialSapEntity> materialSapEntities = new ArrayList<>();
        List<List<InputInvoiceVo>> invoiceVoList = new ArrayList<>();
        // 获取发票物料信息id
        List<String> mIds = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
//            if(null == invoiceMaterialEntityList.get(i).getMatchStatus() || !invoiceMaterialEntityList.get(i).getMatchStatus().equals("1111111111") || !invoiceMaterialEntityList.get(i).getStatus().equals("1")) {
//                //未匹配发票明细
//                materialEntityList.add(invoiceMaterialEntityList.get(i));
//            }
            if (null == invoiceMaterialEntityList.get(i).getMatchStatus() || !invoiceMaterialEntityList.get(i).getStatus().equals("1")) {
                //未匹配发票明细
                materialEntityList.add(invoiceMaterialEntityList.get(i));
            }
            mIds.add(String.valueOf(invoiceMaterialEntityList.get(i).getId()));
        }
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            if (StringUtils.isNotBlank(invoiceMaterialSapEntityList.get(i).getMaterialIds())) {
                String[] materialIds = invoiceMaterialSapEntityList.get(i).getMaterialIds().split(",");
                for (int k = 0; k < materialIds.length; k++) {
                    if (mIds.indexOf(materialIds[k]) > -1) {
                        if (null == invoiceMaterialSapEntityList.get(i).getMatchStatus() || !invoiceMaterialSapEntityList.get(i).getMatchStatus().equals("1111111111") || !invoiceMaterialSapEntityList.get(i).getMate().equals("1")) {
                            // 未匹配sap明细
                            String slv = invoiceMaterialSapEntityList.get(i).getKbetr();
                            if (StringUtils.isNotBlank(slv)) {
                                String kbetr2 = new BigDecimal(slv).multiply(new BigDecimal(100)).toString();
                                String kettr3 = kbetr2.substring(0, kbetr2.indexOf("."));
//                                String kbetr = slv.substring(2);
                                invoiceMaterialSapEntityList.get(i).setKbetr(kettr3 + "%");
                            }
                            materialSapEntityList.add(invoiceMaterialSapEntityList.get(i));
                        } else if (null != invoiceMaterialSapEntityList.get(i).getMatchStatus()) {
                            // 已匹配sap明细
                            materialSapEntities.add(invoiceMaterialSapEntityList.get(i));
                        }
                        break;
                    }
                    materialSapEntityList.add(invoiceMaterialSapEntityList.get(i));
                    materialSapEntities.add(invoiceMaterialSapEntityList.get(i));


                }
            }
        }
        List<List<InputInvoiceMaterialSapEntity>> sapEntities = new ArrayList<>();
        for (int i = 0; i < materialSapEntities.size(); i++) {
            if (i == 0) {
                List<InputInvoiceMaterialSapEntity> sapEntityList = new ArrayList<>();
                sapEntityList.add(materialSapEntities.get(i));
                sapEntities.add(sapEntityList);
            } else {
                for (int k = 0; k < sapEntities.size(); k++) {
                    if (sapEntities.get(k).get(0).getMaterialLineId().equals(materialSapEntities.get(i).getMaterialLineId())) {
                        sapEntities.get(k).add(materialSapEntities.get(i));
                        break;
                    } else {
                        if (k == sapEntities.size() - 1) {
                            List<InputInvoiceMaterialSapEntity> sapEntityList = new ArrayList<>();
                            sapEntityList.add(materialSapEntities.get(i));
                            sapEntities.add(sapEntityList);
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < sapEntities.size(); i++) {
            List<Integer> mids = new ArrayList<>();
            List<Integer> sids = new ArrayList<>();
            for (int k = 0; k < sapEntities.get(i).size(); k++) {
                sids.add(sapEntities.get(i).get(k).getId());
            }
            String[] strId = sapEntities.get(i).get(0).getMaterialLineId().split(",");
            for (int k = 0; k < strId.length; k++) {
                mids.add(Integer.valueOf(strId[k]));
            }
            InputInvoiceVo invoiceVo = new InputInvoiceVo();
            invoiceVo.setMaterialIds(mids);
            invoiceVo.setSapIds(sids);
            List<InputInvoiceVo> invoiceVos = invoiceService.getVoListById(invoiceVo);
            invoiceVoList.add(invoiceVos);
        }

        InputInvoiceBatchEntity invoiceBatchEntity = new InputInvoiceBatchEntity();
        invoiceBatchEntity.setId(Integer.parseInt(invoiceEntity.getInvoiceBatchNumber()));
        invoiceBatchEntity = invoiceBatchService.get(invoiceBatchEntity);
        // 获取物料凭证清单
        InputInvoiceVoucherEntity invoiceVoucherEntity = new InputInvoiceVoucherEntity();
        invoiceVoucherEntity.setInvoiceBatchNumber(Integer.valueOf(invoiceEntity.getInvoiceBatchNumber()));
        invoiceVoucherEntity.setInvoiceIds(invoiceNumbers);
        // 根据发票号码获取这一组的物料清单信息
        List<InputInvoiceVoucherEntity> invoiceVoucherEntityList = invoiceVoucherService.getListByInvoiceNumberAndBatchNumber(invoiceVoucherEntity);
        //List<InputInvoiceVoucherEntity> invoiceVoucherEntityList = invoiceVoucherService.getListByBatchId(invoiceVoucherEntity);
        try {
            r.put("invoiceVoucherEntity", invoiceVoucherEntityList.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            r.put("invoiceVoucherEntityList", invoiceVoucherEntityList.subList(1, invoiceVoucherEntityList.size()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 物料名称筛选集合
        List<String> search = new ArrayList<>();
        for (int i = 0; i < materialEntityList.size(); i++) {
            //未匹配发票明细
            if (!search.contains(materialEntityList.get(i).getSphSpmc())) {
                search.add(materialEntityList.get(i).getSphSpmc());
            }
        }
        List<String> search2 = new ArrayList<>();
        for (int i = 0; i < materialSapEntityList.size(); i++) {
            if (!search2.contains(materialSapEntityList.get(i).getMaktx())) {
                search2.add(materialSapEntityList.get(i).getMaktx());
            }
        }
        if (invoiceMaterialSapEntityList.size() == 0) {
            r.put("invoiceConditionMap", null
            );
        } else {
            r.put("invoiceConditionMap", invoiceConditionMapService.getMaxConditionCode(invoiceMaterialSapEntityList.get(0)));
        }

        r.put("invoiceConditionMapList", invoiceConditionMapService.getAllList());
        r.put("invoiceEntity", invoiceEntityList.get(0));
        // 右侧物料列表
        r.put("invoiceMaterialEntityList", materialEntityList);
        // 左侧sap列表
        r.put("invoiceMaterialSapEntityList", materialSapEntityList);
        r.put("invoiceBatchEntity", invoiceBatchEntity);
        // 匹配成功项
        r.put("invoiceVoList", invoiceVoList);
        // 分页
        r.put("pageSize", pageSize);
        r.put("search", search);
        r.put("search2", search2);
        return r;
    }
    @PostMapping("/save")
    @RequiresPermissions("input:invoiceBatch:save")
    public R save(@RequestBody InputInvoiceBatchEntity invoiceBatchEntity) {
        invoiceBatchEntity = new InputInvoiceBatchEntity();
        String batchNumber = invoiceBatchService.calculateBatchNumber();
        invoiceBatchEntity.setInvoiceBatchNumber(batchNumber);
        invoiceBatchEntity.setCreateTime(new Date());
        invoiceBatchEntity.setUpdateTime(new Date());
        invoiceBatchEntity.setInvoiceBatchStatus("1");
        invoiceBatchEntity.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        invoiceBatchEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId().intValue());
        if (invoiceBatchService.save(invoiceBatchEntity)) {
            return R.ok();
        }
        return R.error();
    }
}

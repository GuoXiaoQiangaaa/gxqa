package com.pwc.modules.input.controller;

import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.common.utils.excel.ImportExcel;
import com.pwc.modules.input.entity.InputInvoiceResponsibleEntity;
import com.pwc.modules.input.service.InputInvoiceResponsibleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-12-26 16:58:47
 */
@RestController
@RequestMapping("input/invoiceResponsible")
public class InvoiceResponsibleController {
    @Autowired
    private InputInvoiceResponsibleService invoiceResponsibleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("input:invoiceResponsible:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = invoiceResponsibleService.findList(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("input:invoiceResponsible:info")
    public R info(@PathVariable("id") Integer id) {
        InputInvoiceResponsibleEntity invoiceResponsible = invoiceResponsibleService.getById(id);
        return R.ok().put("invoiceResponsible", invoiceResponsible);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("input:invoiceResponsible:update")
    public R update(@RequestBody InputInvoiceResponsibleEntity invoiceResponsible) {
        invoiceResponsibleService.updateById(invoiceResponsible);
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("input:invoiceResponsible:save")
    public R save(@RequestBody InputInvoiceResponsibleEntity invoiceResponsible) {
        InputInvoiceResponsibleEntity responsible = invoiceResponsibleService.get(invoiceResponsible);
        if (responsible != null) {
            return R.error();
        } else {
            invoiceResponsibleService.save(invoiceResponsible);
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("input:invoiceResponsible:delete")
    public R delete(String ids) {
        String[] strings = ids.split(",");
        Integer[] ints = new Integer[strings.length];
        for (int i = 0; i < strings.length; i++) {
            ints[i] = Integer.parseInt(strings[i]);
        }
        invoiceResponsibleService.responsibleDelete(ints);
        return R.ok();
    }


    /**
     * 导出信息
     */
    @GetMapping(value = "exportSap")
    @RequiresPermissions("input:invoiceResponsible:exportSap")
    public R exportSap(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<InputInvoiceResponsibleEntity> invoiceResponsibleEntities = invoiceResponsibleService.getList();
        for (int i = 0; i < invoiceResponsibleEntities.size(); i++) {
            if (invoiceResponsibleEntities.get(i).getResponsibleDelete().equals("0")) {
                invoiceResponsibleEntities.get(i).setResponsibleDelete("正常");
            } else {
                invoiceResponsibleEntities.get(i).setResponsibleDelete("已失效");
            }
        }
        try {
            String fileName = "物料组与税收分类列表" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("物料组与税收分类列表", InputInvoiceResponsibleEntity.class).setDataList(invoiceResponsibleEntities).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
//            e.printStackTrace();
            return R.error("导出失败");
        }
    }


    /**
     * 批量导入
     */
    @RequestMapping(value = "/importSap", method = RequestMethod.POST)
    @RequiresPermissions("input:invoiceResponsible:importSap")
    public R importSap(MultipartFile file) {
        R r = new R();
        try {
            invoiceResponsibleService.deleteAll();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<InputInvoiceResponsibleEntity> list = ei.getDataList(InputInvoiceResponsibleEntity.class);
            List<InputInvoiceResponsibleEntity> invoiceResponsibleEntityList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                InputInvoiceResponsibleEntity invoiceResponsibleEntity = new InputInvoiceResponsibleEntity();
                invoiceResponsibleEntity.setInvoiceResponsible(list.get(i).getInvoiceResponsible());
                invoiceResponsibleEntity.setGoodsCategory(list.get(i).getGoodsCategory());
                if (list.get(i).getResponsibleDelete().equals("正常")) {
                    invoiceResponsibleEntity.setResponsibleDelete("0");
                } else {
                    invoiceResponsibleEntity.setResponsibleDelete("1");
                }
                invoiceResponsibleEntityList.add(invoiceResponsibleEntity);
            }
            invoiceResponsibleService.insertAll(invoiceResponsibleEntityList);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return r;
    }

}

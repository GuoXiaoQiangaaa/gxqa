package com.pwc.modules.input.controller;

import com.alibaba.fastjson.JSON;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceResponsibleEntity;
import com.pwc.modules.input.entity.InputTansOutCategoryEntity;
import com.pwc.modules.input.service.InputTransOutCategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("input/invoiceTransOut")
public class InvoiceTransOutCategoryController {

    @Autowired
    private InputTransOutCategoryService inputTransOutCategoryService;

    @RequestMapping("/list")
    @RequiresPermissions("input:invoiceTransOut:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = inputTransOutCategoryService.findList(params);
        return R.ok().put("page", page);
    }

    @RequestMapping("/info/{id}")
    @RequiresPermissions("input:invoiceTransOut:info")
    public R info(@PathVariable("id") Integer id) {
        InputTansOutCategoryEntity inputTansOutCategoryEntity = inputTransOutCategoryService.getById(id);
        return R.ok().put("inputTansOutCategoryEntity", inputTansOutCategoryEntity);
    }
    @RequestMapping("/save")
    @RequiresPermissions("input:invoiceTransOut:save")
    public R save(@RequestBody InputTansOutCategoryEntity inputTansOutCategoryEntity) {
        try {
            inputTransOutCategoryService.save(inputTansOutCategoryEntity);
            return R.ok();
        } catch (Exception e) {
                e.printStackTrace();
                return R.error("添加失败");
            }
        }
    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("input:invoiceTransOut:delete")
    public R delete(String ids) {
        String[] strings = ids.split(",");
        inputTransOutCategoryService.removeByIds(Arrays.asList(strings));
        return R.ok();
    }
    /**
     * 根据选中导出信息
     */
    @GetMapping(value = "/exportRecordListByIds")
    @RequiresPermissions("input:invoiceTransOut:exportRecordListByIds")
    public R exportRecordListByIds(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            String title = (String) params.get("title");
            String ids = (String) params.get("ids");
            String[] strings = ids.split(",");
            List<Integer> idList = new ArrayList<Integer>();
            for (int i = 0; i < strings.length; i++) {
            idList.add( Integer.parseInt(strings[i]));
            }
            List<InputTansOutCategoryEntity> list = (List<InputTansOutCategoryEntity>) inputTransOutCategoryService.listByIds(idList);

            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputTansOutCategoryEntity.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }
    /**
     * 根据查询导出信息
     */
    @GetMapping(value = "/exportRecordListBySearch")
    @RequiresPermissions("input:invoiceTransOut:exportRecordListBySearch")
    public R exportRecordListBySearch(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        PageUtils page = inputTransOutCategoryService.findList(params);
        List<InputTansOutCategoryEntity> list = (List<InputTansOutCategoryEntity>) page.getList();
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputTansOutCategoryEntity.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }


    }





package com.pwc.modules.data.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.data.entity.InputTaxCheckEntity;
import com.pwc.modules.data.service.InputTaxCheckService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 进项税率校验
 *
 * @author fanpf
 * @date 2020/8/29
 */
@RestController
@RequestMapping("data/inputtaxcheck")
@Slf4j
public class InputTaxCheckController {
    @Autowired
    private InputTaxCheckService inputTaxCheckService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("data:inputtaxcheck:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inputTaxCheckService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{checkId}")
//    @RequiresPermissions("data:inputtaxcheck:info")
    public R info(@PathVariable("checkId") Long checkId){
        InputTaxCheckEntity inputTaxCheck = inputTaxCheckService.getById(checkId);

        return R.ok().put("inputTaxCheck", inputTaxCheck);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
//    @RequiresPermissions("data:inputtaxcheck:save")
    public R save(@RequestBody InputTaxCheckEntity inputTaxCheck){
        ValidatorUtils.validateEntity(inputTaxCheck);
        inputTaxCheckService.save(inputTaxCheck);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("data:inputtaxcheck:update")
    public R update(@RequestBody InputTaxCheckEntity inputTaxCheck){
        ValidatorUtils.validateEntity(inputTaxCheck);

        inputTaxCheckService.updateById(inputTaxCheck);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("data:inputtaxcheck:delete")
    public R delete(@RequestBody Long[] checkIds){
        inputTaxCheckService.removeByIds(Arrays.asList(checkIds));

        return R.ok();
    }

    /**
     * 禁用/启用
     */
    @PostMapping("/disableOrEnable")
    public R disableOrEnable(@RequestBody InputTaxCheckEntity reqVo){
        inputTaxCheckService.disableOrEnable(reqVo);

        return R.ok();
    }

    /**
     * 关键字查询
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        PageUtils page = inputTaxCheckService.search(params);

        return R.ok().put("page", page);
    }

    /**
     * 数据导入
     */
    @PostMapping("/importTaxCheck")
    public R importItem(@RequestParam("file") MultipartFile file){
        Map<String, Object> resMap = inputTaxCheckService.importTaxCheck(file);

        return R.ok().put("res", resMap);
    }


    /**
     * 下载模板
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "TaxCheckInfo" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", InputTaxCheckEntity.class).write(response, fileName).dispose();
        }catch (Exception e){
            log.error("下载进项税率校验Excel模板出错: {}", e);
            throw new RRException("下载进项税率校验Excel模板发生异常");
        }
        return null;
    }

}

package com.pwc.modules.input.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.input.entity.InputCollectFormEntity;
import com.pwc.modules.input.service.InputCollectFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 进项汇总报表
 *
 * @author fanpf
 * @date 2020/9/15
 */
@RestController
@RequestMapping("input/inputCollectForm")
@Slf4j
public class InputCollectFormController {
    @Autowired
    private InputCollectFormService inputCollectFormService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("input:inputcollectform:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inputCollectFormService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{formId}")
//    @RequiresPermissions("input:inputcollectform:info")
    public R info(@PathVariable("formId") Long formId){
        InputCollectFormEntity inputCollectForm = inputCollectFormService.getById(formId);

        return R.ok().put("inputCollectForm", inputCollectForm);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
//    @RequiresPermissions("input:inputcollectform:save")
    public R save(@RequestBody InputCollectFormEntity inputCollectForm){
        inputCollectFormService.save(inputCollectForm);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("input:inputcollectform:update")
    public R update(@RequestBody InputCollectFormEntity inputCollectForm){
        ValidatorUtils.validateEntity(inputCollectForm);
        inputCollectFormService.updateById(inputCollectForm);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("input:inputcollectform:delete")
    public R delete(@RequestBody Long[] formIds){
        inputCollectFormService.removeByIds(Arrays.asList(formIds));

        return R.ok();
    }

    /**
     * 生成报表数据
     */
    @GetMapping("/createData")
    public R createData(@RequestParam Map<String, Object> params){
        List<InputCollectFormEntity> formData = inputCollectFormService.createData(params);

        return R.ok().put("formData", formData);
    }

    /**
     * 报表明细
     */
    @GetMapping("/formDetail")
    public R formDetail(@RequestParam Map<String, Object> params){
        PageUtils page = inputCollectFormService.formDetail(params);

        return R.ok().put("page", page);
    }

    /**
     * 导出报表数据
     */
    @PostMapping("/exportData")
    public R exportData(@RequestBody List<InputCollectFormEntity> formData, HttpServletResponse response){
        // 生成.xlsx文件
        ExcelWriter writer = ExcelUtil.getWriter(true);
        ServletOutputStream out= null;
        try {
            if(CollectionUtil.isEmpty(formData)){
                log.error("导出数据为空");
                throw new RRException("请先生成数据");
            }

            // 区分数据(认证/转出)
            Map<String, List<InputCollectFormEntity>> distinguish = inputCollectFormService.distinguish(formData);
            List<InputCollectFormEntity> authData = distinguish.get("authData");
            List<InputCollectFormEntity> exportData = distinguish.get("exportData");

            // 设置认证标题和表头
            writer.merge(4, "进项认证统计");
            writer.addHeaderAlias("itemType", "认证类型");
            writer.addHeaderAlias("itemCount", "份数");
            writer.addHeaderAlias("taxPrice", "税额");
            writer.addHeaderAlias("totalPrice", "金额");
            writer.addHeaderAlias("createTime", "生成日期");
            // 只写入带别名字段
            writer.setOnlyAlias(true);
            // 一次性写入认证统计数据
            writer.write(authData, true);

            // 设置转出标题和表头
            writer.merge(4, "进项转出统计");
            writer.addHeaderAlias("itemType", "转出类型");
            writer.addHeaderAlias("itemCount", "份数");
            writer.addHeaderAlias("taxPrice", "税额");
            writer.addHeaderAlias("totalPrice", "金额");
            writer.addHeaderAlias("createTime", "生成日期");
            // 只写入带别名字段
            writer.setOnlyAlias(true);
            // 一次性写入转出统计数据
            writer.write(exportData, true);


            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            String fileName = "InputCollectForm" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
            response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");

            out=response.getOutputStream();
            writer.flush(out, true);
        } catch (RRException e) {
            log.error("未生成报表数据或参数有误: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("导出进项统计报表数据出错: {}", e);
            throw new RRException("导出进项统计报表数据发生异常");
        }finally {
            // 关闭writer,释放内存
            writer.close();
            // 关闭输出Servlet流
            if(null != out){
                IoUtil.close(out);
            }
        }

        return null;
    }
}

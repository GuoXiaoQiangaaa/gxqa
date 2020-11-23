package com.pwc.modules.input.controller;

import com.pwc.common.utils.PDFUtil;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputInvoicePoEntity;
import com.pwc.modules.input.service.InputInvoicePoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: Po票据
 * @author: Gxw
 * @create: 2020-09-03 15:07
 **/
@RestController
@RequestMapping("/input/invoicepo")
public class InvoicePoController {

    @Autowired
    private InputInvoicePoService inputInvoicePoService;

    /**
     * po总览
     * @param params
     * @return
     */
    @RequestMapping("/findPoList")
//    @RequiresPermissions("input:invoicepo:findPoList")
    public R findPoList(@RequestParam Map<String, Object> params){

        PageUtils page  =  inputInvoicePoService.getPoEntity(params);
        return R.ok().put("page",page);
    }

    /**
     * 补录
     * @param poEntity
     * @return
     */
    @RequestMapping("/updatePo")
//    @RequiresPermissions("input:invoicepo:updatePo")
    public R updatePo(@RequestBody InputInvoicePoEntity  poEntity){
        inputInvoicePoService.uploadPo(poEntity);
        return R.ok();
    }

    /**
     *  识别失败手动添加
     * @param poEntity
     * @return
     */
    @RequestMapping("/savePo")
//    @RequiresPermissions("input:invoicepo:savePo")
    public R savePo(@RequestBody InputInvoicePoEntity  poEntity){
        inputInvoicePoService.save(poEntity);
        return R.ok();
    }

    /**
     * 查看详情
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
 //    @RequiresPermissions("input:invoicepo:savePo")
    public R findById(@PathVariable("id") Integer id){
        InputInvoicePoEntity  poEntity = inputInvoicePoService.getById(id);
        return R.ok().put("page",poEntity);
    }


    /**
     * 上传PoList
     * @param poEntity
     * @return
     */
    @RequestMapping("/updatePoList")
//    @RequiresPermissions("input:invoicepo:updatePoList")
    public R updatePoList(@RequestParam("file") MultipartFile file){
        List<String> lists = null;
        try {
//            lists = inputInvoicePoService.receiveInvoice(file);
            return R.ok().put("data",lists);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入失败");
        }
    }

    /**
     *  导出信息
     * @param params
     * @return
     */
    @RequestMapping("/exportPoList")
//    @RequiresPermissions("input:invoicepo:exportPoList")
    public R exportPoList(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        List<InputInvoicePoEntity>  poEntitys = new ArrayList<>();
        String[] ids = ((String)params.get("ids")).split(",");
        for (String id:ids){
            InputInvoicePoEntity  poEntity = inputInvoicePoService.getById(id);
            PDFUtil.downFile(response,poEntity.getPoNumber(),poEntity.getInvoiceImage());
        }
        return null;
    }


}

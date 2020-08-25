package com.pwc.modules.input.controller;

import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.common.utils.excel.ImportExcel;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceLabelEntity;
import com.pwc.modules.input.entity.InputInvoiceLabelRelatedEntity;
import com.pwc.modules.input.entity.InputInvoiceResponsibleEntity;
import com.pwc.modules.input.entity.vo.InputInvoiceOutVo;
import com.pwc.modules.input.entity.vo.InputInvoiceTaxationVo;
import com.pwc.modules.input.service.InputInvoiceBatchService;
import com.pwc.modules.input.service.InputInvoiceLabelRelatedService;
import com.pwc.modules.input.service.InputInvoiceLabelService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发票标签管理
 * @author Gxw
 * @date 2020/7/15 15:33
 */
@RestController
@RequestMapping("/input/label")
public class InvoiceLabelController {
    @Autowired
    private InputInvoiceLabelService inputInvoiceLabelService;
    @Autowired
    private InputInvoiceLabelRelatedService inputInvoiceLabelRelatedService;
    @Autowired
    private InputInvoiceBatchService invoiceBatchService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    InputInvoiceService invoiceService;

    /*
    * 新添加标签  / 规则 /科目
    * */
    @RequestMapping("/saveLabel")
    //    @RequiresPermissions("input:label:saveLabel")
    public R  saveLabel(@RequestBody InputInvoiceLabelEntity labelEntity){
        labelEntity.setUpdateTime(new Date());
        inputInvoiceLabelService.save(labelEntity);
        return R.ok();
    }
    /*
     * 查询数据
     * */
    @RequestMapping("/findLabelList")
    //    @RequiresPermissions("input:label:findLabel")
    public R findLabelList(@RequestParam Map<String,Object> params)  {
        List<InputInvoiceLabelEntity> labelEntityList  = inputInvoiceLabelService.findLabelList(params);
        return R.ok().put("data",labelEntityList);
    }

    /*
    * 搜索数据
    * */
    @RequestMapping("/findLabel")
    //    @RequiresPermissions("input:label:findLabel")
    public R findLabel(@RequestParam Map<String,Object> params) throws InvocationTargetException, IllegalAccessException {
        List EntityList = new ArrayList();
        PageUtils page = inputInvoiceLabelService.findLabel(params);
        List<InputInvoiceLabelEntity> labelEntityList = (List<InputInvoiceLabelEntity>) page.getList();
        for (InputInvoiceLabelEntity labelEntity:labelEntityList) {
            if(labelEntity.getLabelAttribution()!=null){
                InputInvoiceLabelEntity label = inputInvoiceLabelService.getById(labelEntity.getLabelAttribution());
                labelEntity.setAttributionName(label!=null?label.getLabelName():"");
            }
            if(labelEntity.getRangeId()!=null){
                InputInvoiceLabelEntity label = inputInvoiceLabelService.getById(labelEntity.getRangeId());
                labelEntity.setLabelRange(label!=null?label.getLabelName():"");
            }
            if(labelEntity.getDeptId()!=null){
                SysDeptEntity sysDeptEntity =sysDeptService.getById(labelEntity.getDeptId());
                labelEntity.setDeptName(sysDeptEntity!=null?sysDeptEntity.getName():"");
            }
            EntityList.add(labelEntity);
        }
        page.setList(EntityList);
        R r = new R();
        r.put("page", page);
        return  r;
    }
    /*
    * 更新标签
    * */
    @RequestMapping("/updateLabel")
    //    @RequiresPermissions("input:label:updateLabel")
    public R  updateLabel(@RequestBody InputInvoiceLabelEntity labelEntity){
        inputInvoiceLabelService.updateById(labelEntity);
        return R.ok();
    }
    /*
    * 删除标签
    * */
    @RequestMapping("/deleteLabel")
    //    @RequiresPermissions("input:label:deleteLabel")
    public R deleteLabel(@RequestParam Map<String,Object> params){
        String[] ids=params.get("ids").toString().split(",");
        for (String id:ids){
            InputInvoiceLabelEntity  labelEntity =inputInvoiceLabelService.getById(id);
            labelEntity.setDeleteFalg(0);//删除
            inputInvoiceLabelService.updateById(labelEntity) ;
        }
        return R.ok();
    }

    /*
    * 启用/暂停
    * */
    @RequestMapping("/updatelabelFalg")
    //    @RequiresPermissions("input:label:updatelabelFalg")
    public R updatelabelFalg(@RequestParam Map<String,Object> params){
        String[] ids=params.get("ids").toString().split(",");
        int labelFalg = Integer.parseInt(params.get("labelFalg").toString());
        for (String id:ids){
            InputInvoiceLabelEntity  labelEntity =inputInvoiceLabelService.getById(id);
            labelEntity.setLabelFalg(labelFalg);//
            inputInvoiceLabelService.updateById(labelEntity) ;
        }
        return R.ok();
    }

    /*
    * 导入
    * */
    @RequestMapping("/UploadExcel")
    //    @RequiresPermissions("input:label:UploadExcel")
    public R UploadExcel(MultipartFile file) {
//        try {
//            ImportExcel ei = new ImportExcel(file, 1, 0);
//            List<InputInvoiceResponsibleEntity> list = ei.getDataList(InputInvoiceResponsibleEntity.class);
//        }catch (Exception e){
//
//        }
        return R.ok();
    }
    /*
    * 导出
    * */
    @RequestMapping("/exportSubject")
    //    @RequiresPermissions("input:label:exportSubject")
    public R exportSubject(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String titele = params.get("title").toString();
        List<InputInvoiceLabelEntity> labelEntityList  = inputInvoiceLabelService.findLabelList(params);
        List EntityList = new ArrayList();
        for (InputInvoiceLabelEntity labelEntity:labelEntityList) {
            if(labelEntity.getLabelAttribution()!=null){
                InputInvoiceLabelEntity label = inputInvoiceLabelService.getById(labelEntity.getLabelAttribution());
                labelEntity.setAttributionName(label!=null?label.getLabelName():"");
            }
            EntityList.add(labelEntity);
        }
        try {
            String fileName = titele + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(titele, InputInvoiceLabelEntity.class).setDataList(EntityList).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }

    }

    /*
    * 手工转出 和转出比更新
    * */
    @RequestMapping("/saveManual")
    //    @RequiresPermissions("input:label:saveManual")
    public R saveManual(@RequestBody InputInvoiceLabelRelatedEntity relatedEntity){
        relatedEntity.setUpdateTime(new Date());
        if(relatedEntity.getId()!=null){
            InputInvoiceLabelRelatedEntity  entity= inputInvoiceLabelRelatedService.findByInvoiceId(relatedEntity.getId());
            entity.setOutRatio(relatedEntity.getOutRatio());
            inputInvoiceLabelRelatedService.updateById(entity);
        }else{

            inputInvoiceLabelRelatedService.save(relatedEntity);
        }
        return R.ok();
    }
    /*
    *
    * 转出信息列表
    * */
    @RequestMapping("/findLabelByInvoice")
    //    @RequiresPermissions("input:label:findLabelByInvoice")
    public R findLabelByInvoice(@RequestParam Map<String, Object> params){
        Integer currPage = 0;
        Integer limit = 0;
        if (params.get("page") != null) {
            currPage = Integer.parseInt((String) params.get("page"));
        }
        if (params.get("limit") != null) {
            limit = Integer.parseInt((String) params.get("limit"));
        }
        params.put("offset", (currPage - 1) * limit);
        params.put("outFlag", "1");
        PageUtils page = invoiceBatchService.findListForThree(params);

        R r = new R();
        r.put("page", page);
        return r;
    }


    /*
    * 进项转出统计
    *
    * */
    @RequestMapping("/findOutRatio")
    //    @RequiresPermissions("input:label:findOutRatio")
    public R findOutRatio(@RequestParam Map<String, Object> params){
        return R.ok().put("data",findOut(params,false));
    }
    /*
    * 添加差异原因
    *
    * */
    @RequestMapping("/saveReason")
    //    @RequiresPermissions("input:label:saveReason")
    public  R saveReason(@RequestParam Map<String, Object> params){
        // id 金额 备注
        InputInvoiceLabelRelatedEntity relatedEntity = new InputInvoiceLabelRelatedEntity();
        relatedEntity.setLevelId(Integer.valueOf(params.get("id").toString()) );
        relatedEntity.setAmount(new BigDecimal(params.get("amount").toString()));
        relatedEntity.setReasonNote(params.get("reasonNote").toString());
        relatedEntity.setCreateTime(DateUtils.stringToDate(params.get("createTime").toString()+"-01","yyyy-MM-dd"));
        inputInvoiceLabelRelatedService.save(relatedEntity);
        return R.ok();
    }
    /*
    * 进项转出核对 Check
    * */
    @RequestMapping("/findOutRatioCheck")
    //    @RequiresPermissions("input:label:findOutRatioCheck")
    public R findOutRatioCheck(@RequestParam Map<String, Object> params){
        return R.ok().put("data",findOut(params,true));
    }
    public List<InputInvoiceOutVo> findOut(Map<String, Object> params ,boolean reasonFalg) {
        String date = params.get("queryDate").toString();
        List<InputInvoiceLabelRelatedEntity> relatedEntityList = inputInvoiceLabelRelatedService.findByDate(date.substring(0, 7));
        List<InputInvoiceOutVo> list = new ArrayList<>();
        BigDecimal outSum = BigDecimal.ZERO;
        BigDecimal amountSum = BigDecimal.ZERO;
        for (InputInvoiceLabelRelatedEntity entity : relatedEntityList) {
            boolean flag = true;
            for (InputInvoiceOutVo OutVo : list) {
                if (OutVo.getId().equals(entity.getLevelId())) {
                    if(entity.getInvoiceId()!=null){
                        InputInvoiceEntity invoice = invoiceService.getById(entity.getInvoiceId());
                        if(reasonFalg){
                            OutVo.setAmountSum(OutVo.getAmountSum().add(invoice.getInvoiceTotalPrice()));
                            amountSum = amountSum.add(invoice.getInvoiceTotalPrice());
                        }else{
                            BigDecimal out = OutVo.getOutAmount().add(invoice.getInvoiceTotalPrice().multiply(entity.getOutRatio().divide(BigDecimal.valueOf(100))));
                            outSum = outSum.add(invoice.getInvoiceTotalPrice().multiply(entity.getOutRatio().divide(BigDecimal.valueOf(100))));
                            OutVo.setOutAmount(out);
                        }
                    }else if(reasonFalg){
                        InputInvoiceOutVo.OutReasonVo reasonVo = new InputInvoiceOutVo.OutReasonVo();
                        reasonVo.setAmount(entity.getAmount());
                        reasonVo.setReasonNote(entity.getReasonNote());
                        OutVo.getResaon().add(reasonVo);
//                        OutVo.setResaon(OutVo.getResaon());
                    }
                    flag = false;
                }
            }
            if (flag) {
                InputInvoiceOutVo vo = new InputInvoiceOutVo();
                List listResason = new ArrayList();
                vo.setMonth(date.substring(5, 7));
                vo.setId(entity.getLevelId());
                InputInvoiceLabelEntity label = inputInvoiceLabelService.getById(entity.getLevelId());
                vo.setObjectName(label.getLabelName());
                if(entity.getInvoiceId()!=null) {
                    InputInvoiceEntity invoice = invoiceService.getById(entity.getInvoiceId());
                    if(reasonFalg){
                        vo.setAmountSum(invoice.getInvoiceTotalPrice());
                        amountSum = amountSum.add(invoice.getInvoiceTotalPrice());
                    }else{
                        vo.setOutAmount(invoice.getInvoiceTotalPrice().multiply(entity.getOutRatio().divide(BigDecimal.valueOf(100))));
                        outSum = outSum.add(invoice.getInvoiceTotalPrice().multiply(entity.getOutRatio().divide(BigDecimal.valueOf(100))));
                    }
                }else if (reasonFalg){
                    InputInvoiceOutVo.OutReasonVo reasonVo = new InputInvoiceOutVo.OutReasonVo();
                    reasonVo.setAmount(entity.getAmount());
                    reasonVo.setReasonNote(entity.getReasonNote());
                    listResason.add(reasonVo);
                }
                vo.setResaon(listResason);
                list.add(vo);
            }
        }
        if (list.size() > 0) {
            InputInvoiceOutVo vo = new InputInvoiceOutVo();
//            vo.setObjectName("进项税额转出额（合计）");
            if(reasonFalg){
                vo.setAmountSum(amountSum);
            }else{
                vo.setOutAmount(outSum);
            }
            list.add(vo);
        }
        return list;
    }

}

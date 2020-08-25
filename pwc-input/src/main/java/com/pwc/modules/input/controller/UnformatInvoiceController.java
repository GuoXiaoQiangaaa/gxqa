package com.pwc.modules.input.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputOaExpenseInvoiceMappingEntity;
import com.pwc.modules.input.entity.InputUnformatInvoiceEntity;
import com.pwc.modules.input.entity.UnformatManualInputVo;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputOaExpenseInvoiceMappingService;
import com.pwc.modules.input.service.InputUnformatInvoiceService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;


/**
 * 
 *
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-29 17:15:57
 */
@RestController
@RequestMapping("input/unformatinvoice")
public class UnformatInvoiceController {
    @Autowired
    private InputUnformatInvoiceService unformatInvoiceService;

    @Autowired
    private InputOaExpenseInvoiceMappingService oaExpenseInvoiceMappingService;

    @Autowired
    private InputInvoiceService invoiceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("input:unformatinvoice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = unformatInvoiceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("input:unformatinvoice:info")
    public R info(@PathVariable("id") Integer id){
		InputUnformatInvoiceEntity unformatInvoice = unformatInvoiceService.getById(id);

        return R.ok().put("unformatInvoice", unformatInvoice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("input:unformatinvoice:save")
    public R save(@RequestBody UnformatManualInputVo unformatInvoice){
        InputUnformatInvoiceEntity unformatInvoiceEntity = new InputUnformatInvoiceEntity();
        unformatInvoiceEntity.setAmount(new BigDecimal(unformatInvoice.getAmount()));
        unformatInvoiceEntity.setIssueDate(unformatInvoice.getIssueDate());
        unformatInvoiceService.save(unformatInvoiceEntity);
        String expenseNo = unformatInvoice.getExpenseNo();
        InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMappingEntity = new InputOaExpenseInvoiceMappingEntity();
        oaExpenseInvoiceMappingEntity.setUnformatNo(unformatInvoiceEntity.getId());
        oaExpenseInvoiceMappingEntity.setMoney(unformatInvoiceEntity.getAmount());
        oaExpenseInvoiceMappingEntity.setExpenseNo(Integer.parseInt(expenseNo));
        oaExpenseInvoiceMappingEntity.setInvoiceType(unformatInvoice.getUnformatType());
        oaExpenseInvoiceMappingEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId());
        oaExpenseInvoiceMappingService.save(oaExpenseInvoiceMappingEntity);

        return R.ok();
    }

    @RequestMapping("/saveManualInvoice")
    @RequiresPermissions("input:unformatinvoice:saveManualInvoice")
    public R saveManualInvoice(@RequestBody UnformatManualInputVo unformatInvoice){
        if (unformatInvoice.getInvoiceType().equals("专用发票")||unformatInvoice.getInvoiceType().equals("普通发票")){
            InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
            invoiceEntity.setInvoiceExpense(Integer.parseInt(unformatInvoice.getExpenseNo()));
//            invoiceEntity.setInvoiceTotalPrice(new BigDecimal(unformatInvoice.getInvoiceTotalPrice()));
            // 不含税金额
            invoiceEntity.setInvoiceFreePrice(new BigDecimal(unformatInvoice.getInvoiceTotalPrice()));
            invoiceEntity.setInvoiceCode(unformatInvoice.getInvoiceCode());
            invoiceEntity.setInvoiceNumber(unformatInvoice.getInvoiceNumber());
            invoiceService.save(invoiceEntity);
            invoiceService.functionCheckTrue(invoiceEntity);
            String expenseNo = unformatInvoice.getExpenseNo();
            InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMappingEntity = new InputOaExpenseInvoiceMappingEntity();
            oaExpenseInvoiceMappingEntity.setInvoiceNo(invoiceEntity.getId());
            oaExpenseInvoiceMappingEntity.setMoney(invoiceEntity.getInvoiceTotalPrice());
            oaExpenseInvoiceMappingEntity.setExpenseNo(Integer.parseInt(expenseNo));
            oaExpenseInvoiceMappingEntity.setInvoiceType(unformatInvoice.getInvoiceType());
            oaExpenseInvoiceMappingEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId());
            oaExpenseInvoiceMappingService.save(oaExpenseInvoiceMappingEntity);
            return R.ok();


        }

        InputUnformatInvoiceEntity unformatInvoiceEntity = new InputUnformatInvoiceEntity();
        unformatInvoiceEntity.setAmount(new BigDecimal(unformatInvoice.getAmount()));
        unformatInvoiceEntity.setIssueDate(unformatInvoice.getIssueDate());
        unformatInvoiceEntity.setCreateTime(unformatInvoice.getIssueDate());
        unformatInvoiceService.save(unformatInvoiceEntity);
        String expenseNo = unformatInvoice.getExpenseNo();
        InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMappingEntity = new InputOaExpenseInvoiceMappingEntity();
        oaExpenseInvoiceMappingEntity.setUnformatNo(unformatInvoiceEntity.getId());
        oaExpenseInvoiceMappingEntity.setMoney(unformatInvoiceEntity.getAmount());
        oaExpenseInvoiceMappingEntity.setExpenseNo(Integer.parseInt(expenseNo));
        oaExpenseInvoiceMappingEntity.setInvoiceType(unformatInvoice.getUnformatType());
        oaExpenseInvoiceMappingEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId());
        oaExpenseInvoiceMappingService.save(oaExpenseInvoiceMappingEntity);

        return R.ok();
    }




    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("input:unformatinvoice:update")
    public R update(@RequestBody InputUnformatInvoiceEntity unformatInvoice){
		unformatInvoiceService.updateById(unformatInvoice);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("input:unformatinvoice:delete")
    public R delete(@RequestBody Integer[] ids){
		unformatInvoiceService.removeByIds(ids);

        return R.ok();
    }



}

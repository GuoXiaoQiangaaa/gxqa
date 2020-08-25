package com.pwc.modules.input.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputExpenseInvoiceMapBean;
import com.pwc.modules.input.entity.InputExpenseInvoiceMappingBean;
import com.pwc.modules.input.entity.InputOaExpenseInfoEntity;
import com.pwc.modules.input.entity.InputOaExpenseInvoiceMappingEntity;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputOaExpenseInfoService;
import com.pwc.modules.input.service.InputOaExpenseInvoiceMappingService;
import com.pwc.modules.input.service.InputUnformatInvoiceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-23 15:15:03
 */
@RestController
@RequestMapping("input/oaexpenseinvoicemapping")
public class OaExpenseInvoiceMappingController {
    @Autowired
    private InputOaExpenseInvoiceMappingService oaExpenseInvoiceMappingService;

    @Autowired
    private InputInvoiceService invoiceService;

    @Autowired
    private InputUnformatInvoiceService unformatInvoiceService;

    @Autowired
    private InputOaExpenseInfoService oaExpenseInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("input:oaexpenseinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = oaExpenseInvoiceMappingService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/expenseNoMappingList")
    @RequiresPermissions("input:oaexpenseinfo:expenseNoMappingList")
    public R expenseNoMappingList(String expenseNo){


        BigDecimal expenseMoney = new BigDecimal(0.0);



        List<InputExpenseInvoiceMappingBean> expenseInvoiceMappingBeanList = oaExpenseInvoiceMappingService.getInvoiceExpenseMappingRelationShip(Integer.parseInt(expenseNo), null);
        for (InputExpenseInvoiceMappingBean expenseInvoiceMappingBean:expenseInvoiceMappingBeanList){
            String totalMoney = null;
            if(expenseInvoiceMappingBean.getInvoiceType() ==null){
                continue;
            }
            if (expenseInvoiceMappingBean.getInvoiceType().equals("专用发票") || expenseInvoiceMappingBean.getInvoiceType().equals("普通发票"))
            {

            List<Integer> ids = oaExpenseInvoiceMappingService.getInvoiceEntitiesByExpenseNoAndType(Integer.parseInt(expenseNo),expenseInvoiceMappingBean.getInvoiceType());
             if (oaExpenseInvoiceMappingService.getInvoicesAmountsAndCountsByIds(ids, "").getTotalMoney() ==null){
                 totalMoney = "0";
             }else{
                 totalMoney =oaExpenseInvoiceMappingService.getInvoicesAmountsAndCountsByIds(ids, "").getTotalMoney().toString();
             }
            if( totalMoney == null){
                totalMoney = "0";
            }
            Integer countNumber = oaExpenseInvoiceMappingService.getInvoicesAmountsAndCountsByIds(ids, "").getCountNumber();
            expenseInvoiceMappingBean.setCountNumber(countNumber);
            expenseInvoiceMappingBean.setTotalMoney(new BigDecimal(totalMoney));
            }
            else{
                List<String> unformatInvoiceIds =  oaExpenseInvoiceMappingService.getInformalInvoiceIdsByExpenseNoAndType(expenseNo,expenseInvoiceMappingBean.getInvoiceType());

                List<Integer> unformatInvoiceIntegerIds = new ArrayList<Integer>();
                for (String unformatInvoiceId:unformatInvoiceIds){
                    if (StringUtils.isNotBlank(unformatInvoiceId)) {
                        unformatInvoiceIntegerIds.add(Integer.parseInt(unformatInvoiceId));
                    }
                }
                BigDecimal money = new BigDecimal("0");
                if (unformatInvoiceIntegerIds.size() > 0) {
                    money = oaExpenseInvoiceMappingService.getUnformatInvoiceIdsAmountsByIds(unformatInvoiceIntegerIds, "").getTotalMoney();
                }
                expenseMoney.add(money);
            }
            if (totalMoney==null){
                totalMoney = "0";
            }
            expenseMoney = expenseMoney.add(new BigDecimal(totalMoney));
            InputOaExpenseInfoEntity oaExpenseInfoEntity = oaExpenseInfoService.getById(Integer.parseInt(expenseNo));
            oaExpenseInfoEntity.setInvoicesAmount(expenseMoney);
            if (expenseMoney.compareTo(oaExpenseInfoEntity.getAmount()) ==1){
                oaExpenseInfoEntity.setExpenseStatus(3); // 异常 金额超出报销金额
            }

            oaExpenseInfoService.update(oaExpenseInfoEntity);

        }
        return R.ok().put("expenseInvoiceMappingBeanList", expenseInvoiceMappingBeanList);

    }

    @RequestMapping("/invoicesList")
    @RequiresPermissions("input:oaexpenseinfo:invoicesList")
    public R invoicesMappingList(@RequestParam Map<String, Object> params, @RequestBody InputExpenseInvoiceMapBean expenseInvoiceMapBean){

        R r  = new R();
        String expenseNo = expenseInvoiceMapBean.getExpenseNo();
        String invoiceType = expenseInvoiceMapBean.getInvoiceType();
        List<Integer> ids = oaExpenseInvoiceMappingService.getInvoiceEntitiesByExpenseNoAndType(Integer.parseInt(expenseNo),invoiceType);

        PageUtils page  = invoiceService.findListByInvoiceIds(params,ids);
        return r.put("page", page);

    }


    @RequestMapping("/informalInvoiceList")
    @RequiresPermissions("input:oaexpenseinfo:informalInvoiceList")
    public R informalInvoiceMappingList(@RequestParam Map<String, Object> params, @RequestBody InputExpenseInvoiceMapBean expenseInvoiceMapBean){
        R r  = new R();
        String expenseNo = expenseInvoiceMapBean.getExpenseNo();
        String invoiceType = expenseInvoiceMapBean.getInvoiceType();
        List<Integer> ids = oaExpenseInvoiceMappingService.getInformalInvoiceEntitiesByExpenseNoAndType(Integer.parseInt(expenseNo),invoiceType);

        PageUtils page  = unformatInvoiceService.findListByIds(params,ids);


        return r.put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("input:oaexpenseinfo:info")
    public R info(@PathVariable("id") Integer id){
		InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMapping = oaExpenseInvoiceMappingService.getById(id);

        return R.ok().put("oaExpenseInvoiceMapping", oaExpenseInvoiceMapping);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("input:oaexpenseinfo:save")
    public R save(@RequestBody InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMapping){
		oaExpenseInvoiceMappingService.save(oaExpenseInvoiceMapping);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("input:oaexpenseinfo:update")
    public R update(@RequestBody InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMapping){
		oaExpenseInvoiceMappingService.updateById(oaExpenseInvoiceMapping);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("input:oaexpenseinfo:delete")
    public R delete(@RequestBody Integer[] ids){

		oaExpenseInvoiceMappingService.removeByIds(ids);

        return R.ok();
    }

}

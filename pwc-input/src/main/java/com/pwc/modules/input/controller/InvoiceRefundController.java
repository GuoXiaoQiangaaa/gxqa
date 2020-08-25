package com.pwc.modules.input.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceRefundEntity;
import com.pwc.modules.input.service.InputInvoiceRefundService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-12-26 16:58:47
 */
@RestController
@RequestMapping("input/invoicerefund")
public class InvoiceRefundController {
    @Autowired
    private InputInvoiceRefundService invoiceRefundService;
    @Autowired
    private InputInvoiceService invoiceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("input:invoicerefund:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = invoiceRefundService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("inpput:invoicerefund:info")
    public R info(@PathVariable("id") Integer id){
        InputInvoiceRefundEntity invoiceRefund = invoiceRefundService.getOne(new QueryWrapper<InputInvoiceRefundEntity>().eq("invoice_id",id));
        return R.ok().put("invoiceRefund", invoiceRefund);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("input:invoicerefund:save")
    public R save(@RequestBody InputInvoiceRefundEntity invoiceRefund){
        String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
        String[] invoiceIds = StrUtil.isNotBlank(invoiceRefund.getInvoiceIds()) ? invoiceRefund.getInvoiceIds().split(",") : null;
        if (null != invoiceIds) {
            Date date =new Date();
            for (String invoiceId : invoiceIds) {
                InputInvoiceRefundEntity invoiceRefundEntity = new InputInvoiceRefundEntity();
                BeanUtil.copyProperties(invoiceRefund, invoiceRefundEntity);
                invoiceRefundEntity.setInvoiceId(Integer.parseInt(invoiceId));
                invoiceRefundEntity.setRefundTime(date);
                invoiceRefundEntity.setRefundUser(username);
                invoiceRefundService.save(invoiceRefundEntity);
                InputInvoiceEntity invoiceEntity=new InputInvoiceEntity();
                String invoiceReturn = invoiceRefundEntity.getRefundStatus();

                // TODO: 现有状态为2，不明不白，因为invoice退款数据库为1，所以判断一下 Author zk
                if ("2".equals(invoiceReturn)) {
                    invoiceReturn = "1";
                }
                invoiceEntity.setInvoiceReturn(invoiceReturn);
                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.REFUND.getValue());

                invoiceEntity.setId(invoiceRefundEntity.getInvoiceId());
                invoiceService.update(invoiceEntity);
            }
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("input:invoicerefund:update")
    public R update(@RequestBody InputInvoiceRefundEntity invoiceRefund){
			invoiceRefundService.updateById(invoiceRefund);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("input:invoicerefund:delete")
    public R delete(@RequestBody Integer[] ids){
			invoiceRefundService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

 /*   *//**
     * 退票
     *//*
    @RequestMapping("/refund")
    public R refund(@RequestBody Integer[] ids){
        String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
        invoiceRefund.setRefundTime(new Date());
        invoiceRefund.setRefundUser(username);
        invoiceRefundService.insert(invoiceRefund);
        InputInvoiceEntity invoiceEntity=new InputInvoiceEntity();
        invoiceEntity.setId(invoiceRefund.getInvoiceId());
        invoiceService.updateByIdRefund(ids);
        return R.ok();
    }*/

}

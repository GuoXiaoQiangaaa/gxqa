package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.input.dao.InputInvoiceCheckActionDao;
import com.pwc.modules.input.entity.InputInvoiceCheckAction;
import com.pwc.modules.input.service.InputInvoiceCheckActionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("InvoiceCheckActionService")
public class InputInvoiceCheckActionServiceImpl extends ServiceImpl<InputInvoiceCheckActionDao, InputInvoiceCheckAction> implements InputInvoiceCheckActionService {

    @Override
    public String checkSubtract(BigDecimal a, BigDecimal b) {
        String c = a.subtract(b)+"";
        String amount = "";
        if(c.equals("0E-8")) {
            amount = "0";
        } else {
            amount = c;
        }
        return amount;
    }

    @Override
    public void updateByAction(InputInvoiceCheckAction invoiceCheckAction) {
        this.baseMapper.updateByAction(invoiceCheckAction);
    }

    @Override
    public List<InputInvoiceCheckAction> findList() {
        return this.baseMapper.findList();
    }

    @Override
    public InputInvoiceCheckAction getByItem(InputInvoiceCheckAction invoiceCheckAction) {
        return this.baseMapper.getByItem(invoiceCheckAction);
    }


    /**
     * 手动匹配强弱校验
     * @param data
     * @return
     */
    @Override
    public String checkAction(String data) {
        InputInvoiceCheckAction invoiceCheckAction = new InputInvoiceCheckAction();
        invoiceCheckAction.setItems(data);
        invoiceCheckAction = this.baseMapper.getByItem(invoiceCheckAction);
        if(invoiceCheckAction.getAction().equals("1")) {
            return "3";
        } else if(invoiceCheckAction.getAction().equals("2")) {
            return "0";
        } else {
            return "0";
        }
    }

//    /**
//     * 自动匹配强弱校验
//     * @param data
//     * @return
//     */
//    public String checkItemAction(String data) {
//        InputInvoiceCheckAction invoiceCheckAction = new InputInvoiceCheckAction();
//        invoiceCheckAction.setItems(data);
//        invoiceCheckAction = this.baseMapper.getByItem(invoiceCheckAction);
//
//    }

}

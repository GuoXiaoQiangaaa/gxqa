/*
package com.pwc.modules.input.service;


import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TreadTasks {
    @Autowired
    private InputInvoiceService inputInvoiceService;

    @Async
    public void startTreadTask(InputInvoiceEntity invoiceEntity, InputInvoiceUploadEntity uploadEntity) {
        System.out.println("异步开启#######################################");
        inputInvoiceService.saveInvoice(invoiceEntity, uploadEntity);
    }
}
*/

package com.pwc.modules.job.task;

import com.pwc.modules.input.service.InputInvoiceSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 勾选结果
 */
@Component("deductResultTask")
public class DeductResultTask implements ITask {

    @Autowired
    private InputInvoiceSyncService invoiceSyncService;
//    @Autowired
//    private InvoiceService invoiceService;

    @Override
    public void run(String params) {
        invoiceSyncService.deductResult();
    }
}

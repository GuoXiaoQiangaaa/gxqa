package com.pwc.modules.job.task;

import com.pwc.modules.input.service.InputInvoiceSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("verifyTask")
public class VerifyTask implements ITask {


    @Autowired
    private InputInvoiceSyncService invoiceSyncService;

    @Override
    public void run(String params) {
        invoiceSyncService.checkInvoice();
    }
}

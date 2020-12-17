package com.pwc.modules.job.task;

import com.pwc.modules.input.service.InputInvoiceCustomsGatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 采集结果
 */
@Component("customsGatherResultTask")
public class CustomsGatherResultTask implements ITask {

    @Autowired
    private InputInvoiceCustomsGatherService inputInvoiceCustomsGatherService;

    @Override
    public void run(String params) {
        inputInvoiceCustomsGatherService.customsGatherResultByTime();
    }
}

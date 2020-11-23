package com.pwc.modules.job.task;

import com.pwc.modules.input.service.InputInvoiceCustomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取海关缴款书勾选/撤销勾选结果
 *
 * @author fanpf
 * @date 2020/9/23
 */
@Component("customsDeductResultTask")
public class CustomsDeductResultTask implements ITask {

    @Autowired
    private InputInvoiceCustomsService customsService;

    @Override
    public void run(String params) {
        customsService.getDeductOrCancelResult();
    }
}

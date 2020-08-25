package com.pwc.modules.job.task;


import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.SyncInvoiceInfo;
import com.fapiao.neon.param.in.SyncInvoiceParamBody;
import com.pwc.common.utils.InputConstant;
import com.pwc.modules.input.service.InputInvoiceSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 底账库更新
 * @author zk
 */
@Component("invoiceSyncTask")
public class InvoiceSyncTask implements ITask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InputInvoiceSyncService invoiceSyncService;


    @Override
    public void run(String params) {
//        int page = 1;
//        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DATE, -1);//-1.昨天时间 0.当前时间 1.明天时间 *以此类推
//        String yesterday = df.format(c.getTime());
//        String nowTime = df.format(date);
//        System.out.println("yesterday:" + yesterday);
//        System.out.println("nowTime:" + nowTime);
//        for ( String taxNo: InputConstant.TAX_CODE) {
//            syncInvoice(page, yesterday, nowTime, taxNo);
//        }
    }


    private void syncInvoice(int page, String startTime, String endTime, String taxNo) {
        SyncInvoiceParamBody syncInvoiceParamBody = new SyncInvoiceParamBody();
        syncInvoiceParamBody.setTaxNo(taxNo);
        syncInvoiceParamBody.setSyncType("1");
        syncInvoiceParamBody.setStartDate(startTime);
        syncInvoiceParamBody.setEndDate(endTime);
        syncInvoiceParamBody.setPage(page);
        syncInvoiceParamBody.setPageSize(200);
        CallResult<SyncInvoiceInfo> results = invoiceSyncService.invoiceSync(syncInvoiceParamBody);
        int currentPage = results.getData().getCurrentPage();
        int totalPage = results.getData().getPages();
        if(results.getData() != null && results.getData().getInvoices() != null) {
            invoiceSyncService.saveInvoice(results.getData().getInvoices());
            logger.info("==save==" + results.getData().getInvoices().toString());
        }
        if(currentPage < totalPage) {
            this.syncInvoice(currentPage + 1, startTime, endTime, taxNo);
            logger.info("==nextPage==" + (currentPage+1));
        }

    }
}

package com.pwc.modules.job.task;


import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.SyncInvoiceInfo;
import com.fapiao.neon.param.in.SyncInvoiceParamBody;
import com.pwc.modules.input.service.InputInvoiceSyncService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 底账库更新
 * @author zk
 */
@Component("invoiceSyncTask")
public class InvoiceSyncTask implements ITask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InputInvoiceSyncService invoiceSyncService;
    @Autowired
    private SysDeptService sysDeptService;


    @Override
    public void run(String params) {
        List<SysDeptEntity>  deptEntitys= sysDeptService.getDeptByStatus();
        for(SysDeptEntity deptEntity:deptEntitys){
            if(deptEntity.getTaxCode()!=null &&!"".equals(deptEntity.getTaxCode())){
                System.out.println(deptEntity.getTaxCode());
                invoiceSyncService.syncInvoice(1, "", "", deptEntity.getTaxCode());
            }
        }
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

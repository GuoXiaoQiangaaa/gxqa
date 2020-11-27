package com.pwc.modules.input.controller;

import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoicePoEntity;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;
import com.pwc.modules.input.service.InputInvoicePoService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputInvoiceUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description: 票据上传
 * @author: Gxw
 * @create: 2020-09-03 16:10
 **/
@RestController
@RequestMapping("/input/invoiceupload")
public class InvoiceUploadController {
    @Autowired
    private  InputInvoiceUploadService  inputInvoiceUploadService;
    @Autowired
    private InputInvoiceService invoiceService;
    @Autowired
    private InputInvoicePoService inputInvoicePoService;
    // 查询
    @RequestMapping("/findUpload")
//    @RequiresPermissions("input:invoiceupload:findUpload")
    public R findUpload(@RequestParam Map<String, Object> params) {
        PageUtils page = inputInvoiceUploadService.findUploadList(params);

        String createUserName = params.get("createUserName").toString();
        List<InputInvoiceUploadEntity>  UploadEntitysList = (List<InputInvoiceUploadEntity>) page.getList();
        if(!UploadEntitysList.isEmpty()){
            List<InputInvoiceUploadEntity>  UploadEntitys= inputInvoiceUploadService.getListAndCreateName(UploadEntitysList,createUserName);
            for(InputInvoiceUploadEntity entity: UploadEntitys){
                if((entity.getUploadType()==InputConstant.InvoiceStyle.PO.getValue())||entity.getUploadType().equals(InputConstant.InvoiceStyle.PO.getValue())){
                     // po 数据
                    entity.setInvoiceEntity(new InputInvoiceEntity());
                    entity.setPoEntity(inputInvoicePoService.findByuploadId(entity.getUploadId().toString()));
                }
                /*else if((entity.getUploadType()==InputConstant.InvoiceStyle.NULL.getValue())||entity.getUploadType().equals(InputConstant.InvoiceStyle.NULL.getValue())){
                    entity.setPoEntity(new InputInvoicePoEntity());
                    entity.setInvoiceEntity(new InputInvoiceEntity());
                }*/
                else {
                    entity.setPoEntity(new InputInvoicePoEntity());
                    entity.setInvoiceEntity(invoiceService.findByuploadId(entity.getUploadId().toString()));
                }
            }
            page.setList(UploadEntitys);
        }
        return R.ok().put("page",page);
    }
    // 补录
    @RequestMapping("/updateUpload")
    //    @RequiresPermissions("input:invoiceupload:updateUpload")
    public R updateUpload(@RequestBody InputInvoiceUploadEntity  uploadEntity) {
        if(uploadEntity.getInvoiceEntity()!=null&&uploadEntity.getInvoiceEntity().getUploadId()!=null){
            uploadEntity.setUploadType(uploadEntity.getUploadSource());
            uploadEntity.getInvoiceEntity().setInvoiceUploadType(uploadEntity.getUploadSource().toString());
            invoiceService.makeUpInvoice(uploadEntity.getInvoiceEntity());
        }else if( uploadEntity.getPoEntity()!=null&&uploadEntity.getPoEntity().getUploadId()!=null){
            uploadEntity.setUploadType(InputConstant.InvoiceStyle.PO.getValue());
            inputInvoicePoService.uploadPo(uploadEntity.getPoEntity());
        }
//        if(uploadEntity.getUploadType().equals(InputConstant.InvoiceStyle.BLUE.getValue())){
//            invoiceService.makeUpInvoice(uploadEntity.getInvoiceEntity());
//        }else if(uploadEntity.getUploadType().equals(InputConstant.InvoiceStyle.PO.getValue())){
//            inputInvoicePoService.uploadPo(uploadEntity.getPoEntity());
//        }
        return R.ok();
    }

    @RequestMapping("/{id}")
    //    @RequiresPermissions("input:invoiceupload:id")
    public R findUploadById(@PathVariable("id") Integer id) {
        InputInvoiceUploadEntity uploadEntity = inputInvoiceUploadService.getById(id);
        if (uploadEntity != null) {
            if ((uploadEntity.getUploadType() == InputConstant.InvoiceStyle.PO.getValue()) || uploadEntity.getUploadType().equals(InputConstant.InvoiceStyle.PO.getValue())) {
                // po 数据
                uploadEntity.setInvoiceEntity(new InputInvoiceEntity());
                uploadEntity.setPoEntity(inputInvoicePoService.findByuploadId(uploadEntity.getUploadId().toString()));
            }/* else if ((uploadEntity.getUploadType() == InputConstant.InvoiceStyle.NULL.getValue()) || uploadEntity.getUploadType().equals(InputConstant.InvoiceStyle.NULL.getValue())) {
                uploadEntity.setPoEntity(new InputInvoicePoEntity());
                uploadEntity.setInvoiceEntity(new InputInvoiceEntity());
            }*/ else {
                uploadEntity.setPoEntity(new InputInvoicePoEntity());
                uploadEntity.setInvoiceEntity(invoiceService.findByuploadId(uploadEntity.getUploadId().toString()));
            }
        }
        return R.ok().put("uploadEntity", uploadEntity);
    }

}

package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.input.entity.InputPoListEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: Gxw
 * @create: 2020-09-11 17:36
 **/
public interface InputPoListService  extends IService<InputPoListEntity> {
    List<String> receiveInvoice(MultipartFile file) throws Exception;
    InputPoListEntity getPoListByPoItem(String poItem);
    InputPoListEntity getPoListByCode(String companyCode);
}

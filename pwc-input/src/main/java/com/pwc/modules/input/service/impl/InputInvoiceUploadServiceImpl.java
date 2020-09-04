package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.SysLog;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceUploadDao;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;
import com.pwc.modules.input.service.InputInvoiceUploadService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description: 上传票据
 * @author: Gxw
 * @create: 2020-09-03 16:17
 **/
@Service("inputInvoiceUploadService")
public class InputInvoiceUploadServiceImpl extends ServiceImpl<InputInvoiceUploadDao, InputInvoiceUploadEntity> implements InputInvoiceUploadService {
    @Override
    public PageUtils findUploadList(Map<String, Object> params){
    IPage<InputInvoiceUploadEntity> page = this.page(
            new Query<InputInvoiceUploadEntity>().getPage(params, null, true),
            new QueryWrapper<InputInvoiceUploadEntity>()
                    .orderByAsc("update_time")
                    .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
    );
    return new PageUtils(page);
}

}

package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.ParamsMap;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceUploadDao;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;
import com.pwc.modules.input.service.InputInvoiceUploadService;
import com.pwc.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: 上传票据
 * @author: Gxw
 * @create: 2020-09-03 16:17
 **/
@Service("inputInvoiceUploadService")
public class InputInvoiceUploadServiceImpl extends ServiceImpl<InputInvoiceUploadDao, InputInvoiceUploadEntity> implements InputInvoiceUploadService {
   @Autowired
   SysUserService sysUserService;

    @Override
    @DataFilter(subDept = true, userId = "create_by")
    public PageUtils findUploadList(Map<String, Object> params){
        String createTime = ParamsMap.findMap(params,"createTime"); // 日期
        String  uploadType = ParamsMap.findMap(params,"uploadType");// 上传类型
        String createUserName = (String) params.get("createUserName");
        Long userId =0L;
        if (StringUtils.isNotBlank(createUserName)) {
            userId = sysUserService.getIdByName(params.get("createUserName").toString());
        } // 上传人
        userId =  null == userId ? 0 : userId;
    IPage<InputInvoiceUploadEntity> page = this.page(
            new Query<InputInvoiceUploadEntity>().getPage(params, null, true),
            new QueryWrapper<InputInvoiceUploadEntity>()
                    .orderByDesc("update_time")
                    .eq(StringUtils.isNotBlank(uploadType),"upload_type",uploadType)
//                    .ge(StringUtils.isNotBlank(createTime),"create_time",!StringUtils.isNotBlank(createTime) ? "":createTime.split(",")[0])
//                    .le(StringUtils.isNotBlank(createTime),"create_time",!StringUtils.isNotBlank(createTime) ? "":createTime.split(",")[1])
                    .apply(StringUtils.isNotBlank(createTime),"date_format(create_time,'%Y-%m-%d')>={0}",!StringUtils.isNotBlank(createTime) ? "":createTime.split(",")[0])
                    .apply(StringUtils.isNotBlank(createTime),"date_format(create_time,'%Y-%m-%d')<={0}",!StringUtils.isNotBlank(createTime) ? "":createTime.split(",")[1])
                    .eq(StringUtils.isNotBlank(createUserName),"create_by",userId)
                    .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
    );
    return new PageUtils(page);

}    @Override
    public List<InputInvoiceUploadEntity> getListAndCreateName(List<InputInvoiceUploadEntity> list, String createUserName) {
        return baseMapper.getListAndCreateName(list, createUserName);
    }

    @Override
    public InputInvoiceUploadEntity getUploadEntity(String id,String type){
        InputInvoiceUploadEntity entity  = this.getOne(
                new QueryWrapper<InputInvoiceUploadEntity>()
                .eq("id",id)
                .eq("upload_type",type)
        );
        return entity;
    }

}

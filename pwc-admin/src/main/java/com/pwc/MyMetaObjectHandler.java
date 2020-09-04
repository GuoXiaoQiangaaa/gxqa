package com.pwc;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * @author zk
 */
public class MyMetaObjectHandler implements MetaObjectHandler {

    protected final static Logger logger = LoggerFactory.getLogger(AdminApplication.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        logger.info("新增的时候干点不可描述的事情");
        String classname = metaObject.getOriginalObject().getClass().getName();

        if (!"com.pwc.modules.input.entity.InputUnformatInvoiceEntity".equals(classname)) {
//            if(metaObject.getValue("createTime")==null){
            setFieldValByName("createTime", new Date(), metaObject);
            setFieldValByName("updateTime", new Date(), metaObject);
//            }
        }

        try {
            if (ShiroUtils.getUserEntity() != null) {
//                if (!classname.contains("input")) {
                    setFieldValByName("createBy", ShiroUtils.getUserEntity().getUserId().intValue(), metaObject);
                    setFieldValByName("updateBy", ShiroUtils.getUserEntity().getUserId().intValue(), metaObject);
                setFieldValByName("deptId", ShiroUtils.getUserEntity().getDeptId().intValue(), metaObject);
//                }
            }
        } catch (UnavailableSecurityManagerException e) {
            System.out.println("定时任务不能由shiro管理，所以执行shiro方法会报错");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        logger.info("更新的时候干点不可描述的事情");
        String classname = metaObject.getOriginalObject().getClass().getName();

        if (!"com.pwc.modules.input.entity.InputUnformatInvoiceEntity".equals(classname)) {
            setFieldValByName("updateTime", new Date(), metaObject);

        }
        try {
            if (ShiroUtils.getUserEntity() != null) {
//                if (!classname.contains("input")) {
                    setFieldValByName("updateBy", ShiroUtils.getUserEntity().getUserId().intValue(), metaObject);
//                }
            }
        } catch (UnavailableSecurityManagerException e) {
            System.out.println("定时任务不能由shiro管理,所以执行shiro方法会报错");
        }
    }

}

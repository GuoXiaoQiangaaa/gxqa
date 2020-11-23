package com.pwc;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;


/**
 * @author zk
 */
public class MyMetaObjectHandler implements MetaObjectHandler {

    protected final static Logger logger = LoggerFactory.getLogger(AdminApplication.class);

    /**
     * 新增自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        String classname = metaObject.getOriginalObject().getClass().getName();

        if (!"com.pwc.modules.input.entity.InputUnformatInvoiceEntity".equals(classname)) {
            exeFill("createTime", new Date(), metaObject);
            exeFill("updateTime", new Date(), metaObject);
        }

        try {
            if (ShiroUtils.getUserEntity() != null) {
                    exeFill("createBy", ShiroUtils.getUserEntity().getUserId().intValue(), metaObject);
                    exeFill("updateBy", ShiroUtils.getUserEntity().getUserId().intValue(), metaObject);
                    exeFill("deptId", ShiroUtils.getUserEntity().getDeptId().intValue(), metaObject);
            }
        } catch (UnavailableSecurityManagerException e) {
            System.out.println("定时任务不能由shiro管理，所以执行shiro方法会报错");
        }
    }

    /**
     * 更新自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        String classname = metaObject.getOriginalObject().getClass().getName();

        if (!"com.pwc.modules.input.entity.InputUnformatInvoiceEntity".equals(classname)) {
            exeFill("updateTime", new Date(), metaObject);

        }
        try {
            if (ShiroUtils.getUserEntity() != null) {
                exeFill("updateBy", ShiroUtils.getUserEntity().getUserId().intValue(), metaObject);
            }
        } catch (UnavailableSecurityManagerException e) {
            System.out.println("定时任务不能由shiro管理,所以执行shiro方法会报错");
        }
    }

    /**
     * 排除@TableField(fill = FieldFill.DEFAULT)字段
     */
    private void exeFill(String fieldName, Object fieldVal, MetaObject metaObject){
        Field[] declaredFields = metaObject.getOriginalObject().getClass().getDeclaredFields();
        if(null != declaredFields && declaredFields.length > 0){
            for (Field field : declaredFields) {
                TableField annotation = field.getAnnotation(TableField.class);
                if(null != annotation && FieldFill.DEFAULT != annotation.fill()){
                    this.setFieldValByName(fieldName, fieldVal, metaObject);
                }
            }
        }
    }

}

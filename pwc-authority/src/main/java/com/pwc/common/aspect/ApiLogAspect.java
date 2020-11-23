
package com.pwc.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.pwc.common.annotation.ApiLogFilter;
import com.pwc.common.utils.DateUtils;
import com.pwc.modules.sys.entity.SysApiLogEntity;
import com.pwc.modules.sys.service.SysApiLogService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 接口数据日志表 sys_api_log，切面处理类
 *
 * @author
 */
@Aspect
@Component
public class ApiLogAspect {
    @Autowired
    private SysApiLogService sysApiLogService;

    @Pointcut("@annotation(com.pwc.common.annotation.ApiLogFilter)")
    public void apiLogPointCut() {

    }

    @Around("apiLogPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String sTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
        //执行方法
        Object result = point.proceed();
        String eTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
        //保存日志
        saveSysLog(point, sTime,eTime,result);
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, String sTime,String eTime, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysApiLogEntity sysLog = new SysApiLogEntity();
        if (ShiroUtils.getUserEntity() != null) {
            sysLog.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        }else {
            sysLog.setCreateBy("TIS");
        }
        sysLog.setRequestTime(sTime);
        ApiLogFilter syslog = method.getAnnotation(ApiLogFilter.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setType(syslog.type());
        }

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = new Gson().toJson(args[0]);
            sysLog.setRequestText(params);
            sysLog.setResponseTime(eTime);
            sysLog.setResponseText(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
        } catch (Exception e) {
        e.printStackTrace();
        }
        //保存接口数据日志表日志
        sysApiLogService.save(sysLog);
    }
}

package com.pwc.common.third;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.pwc.common.third.request.TtkOrgRequest;
import com.pwc.common.third.request.TtkTaxLoginInfoRequest;
import com.pwc.common.third.response.TtkOrgResponse;
import com.pwc.common.third.response.TtkResponse;
import com.ttk.jchl.openapi.sdk.TtkOpenAPI;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author zk
 */
public class TtkOrgUtil {

    private static TtkOpenAPI ttkOpenAPI = TtkOpenApiClientFactory.getTtkOpenAPI();

    /**
     * 创建企业
     * @param request 请求体
     * @return TtkResponse
     */
    public static TtkResponse<TtkOrgResponse> createOrg(TtkOrgRequest request){
        JSONObject res = ttkOpenAPI.createOrg(JSONObject.toJSONString(request));
        Type type= new TypeReference<TtkResponse<TtkOrgResponse>>(){}.getType();
        return res.toJavaObject(type);
    }

    /**
     * 获取企业信息
     * @param orgId 企业id
     */
    public static TtkResponse<TtkOrgResponse> getOrg(Long orgId) {
        String request = "{\"orgId\":"+orgId+"}";
        JSONObject res = ttkOpenAPI.queryOrgDetailInfo(request);
        Type type= new TypeReference<TtkResponse<TtkOrgResponse>>(){}.getType();
        return res.toJavaObject(type);
    }

    /**
     * 上传网报信息
     * @param request 请求体
     * @return TtkResponse
     */
    public static TtkResponse<String> saveTaxLoginInfo(TtkTaxLoginInfoRequest request){
        JSONObject res = ttkOpenAPI.saveTaxLoginInfo(JSONObject.toJSONString(request));
        System.out.println("请求体"+JSONObject.toJSONString(request));
        Type type= new TypeReference<TtkResponse<String>>(){}.getType();
        return res.toJavaObject(type);
    }

    /**
     * 查询企业状态
     * @param orgId 企业id
     */
    public static TtkResponse<String> hasReadSJInfo(Long orgId) {
        String request = "{\"orgId\":"+orgId+"}";
        JSONObject res = ttkOpenAPI.hasReadSJInfo(request);
        Type type= new TypeReference<TtkResponse<String>>(){}.getType();
        return res.toJavaObject(type);
    }

    /**
     * 查询企业状态
     * @param orgId 企业id
     */
    public static String getWebUrlForCompanyInformation(Long orgId) {
        return ttkOpenAPI.getWebUrlForCompanyInformation(orgId.toString());
    }

    /**
     * 更新企业信息
     * @param request
     * @return
     */
    public static TtkResponse<String> updateOrg(TtkOrgRequest request) {
        JSONObject res = ttkOpenAPI.updateOrg(JSONObject.toJSONString(request));
        Type type= new TypeReference<TtkResponse<String>>(){}.getType();
        return res.toJavaObject(type);
    }

    /**
     * 获取企业准则
     * @return
     */
    public static String queryAccountStandardId(Long orgId){
        Integer month = DateUtil.month(DateUtil.date())+1;
        String json = "{\"orgId\":"+orgId+",\"year\":"+DateUtil.year(DateUtil.date())+",\"month\":"+ month +"}";

        JSONObject jsonObject = ttkOpenAPI.queryAccountStandardId(json);

        String errorCode = jsonObject.getJSONObject("head").getString("errorCode");

        if (!"0".equals(errorCode)) {

            System.out.println("错误:"+errorCode+"，"+jsonObject.getJSONObject("head").getString("errorMsg"));

        } else {

            return jsonObject.get("body").toString();

        }
        return null;
    }
}

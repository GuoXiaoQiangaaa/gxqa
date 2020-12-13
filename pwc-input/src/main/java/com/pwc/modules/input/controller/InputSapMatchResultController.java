package com.pwc.modules.input.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.excel.ExportExcel;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.ParamsMap;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import com.pwc.modules.input.entity.InputSapMatchResultEntity;
import com.pwc.modules.input.entity.vo.InvoiceCustomsDifferenceMatch;
import com.pwc.modules.input.entity.vo.InvoiceDifferenceMatch;
import com.pwc.modules.input.entity.vo.RedInvoiceDifferenceMatch;
import com.pwc.modules.input.service.InputSqpMatchResultService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/input/matchResult")
public class InputSapMatchResultController {
    @Autowired
    private InputSqpMatchResultService inputSqpMatchResultService;

    @RequestMapping("/matchResultlist")
    public R list(@RequestBody Map<String, Object> params){
        //参数校验
        String yearAndMonth=ParamsMap.findMap(params, "yearAndMonth");
        String deptId=ParamsMap.findMap(params, "deptId");
        if(yearAndMonth != null  && deptId != null){
            //查询当前月份是否被锁定
            params.put("status","1");
            PageUtils page = inputSqpMatchResultService.queryPage(params);
            if(page.getTotalCount() > 0){
                return R.ok().put("page", page.getList());
            }else{
                //实时查询账票匹配数据
                List<InputSapMatchResultEntity> matchResultList = inputSqpMatchResultService.queryMatchCurTime(params);
                return R.ok().put("page", matchResultList);
            }
        }else{
            return null;
        }
    }

    @RequestMapping("/updateMatchResult")
    public R updateMatchResult(@RequestBody Map<String, Object> params){
        boolean result = inputSqpMatchResultService.updateMatchResult(params);
        return R.ok().put("result",result);
    }

    /**
     * 根据ID查询账票匹配结果
     *
     * @param params
     * @return
     */
    @RequestMapping("/getMatchResultByIds")
    public R getMatchResultByIds(@RequestBody Map<String, Object> params){
        Collection<InputSapMatchResultEntity> matchResultList = inputSqpMatchResultService.getMatchResultByIds(params);
        return R.ok().put("page",matchResultList);
    }


    /**
     * 根据ID查询账票匹配结果(下载)
     *
     * @param params
     * @return
     */
    @RequestMapping("/getMatchResultByIdsAndExcel")
    public R getMatchResultByIdsAndExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        List list = new ArrayList();
        String ids = ParamsMap.findMap(params, "ids");
        if (ids != null) {
            List<String> idList = Arrays.asList(ids.split(","));
            list = inputSqpMatchResultService.list(
                    new QueryWrapper<InputSapMatchResultEntity>()
                            .in("result_id", idList)
                            .orderByDesc("create_time")
            );
        }
        try {
            String fileName = DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", InputSapMatchResultEntity.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 查询发票差异清单
     *
     * @param params
     * @return
     */
    @RequestMapping("/getDifferenceMatchResult")
    public R getDifferenceMatchResult(@RequestBody Map<String, Object> params){
        String yearAndMonth=ParamsMap.findMap(params, "yearAndMonth");
        String deptId=ParamsMap.findMap(params, "deptId");
        Integer currPage = 0;
        Integer limit = 0;
        if (params.get("page") != null) {
            currPage = (Integer) params.get("page");
        }
        if (params.get("limit") != null) {
            limit = (Integer) params.get("limit");
        }
        params.put("offset", (currPage - 1) * limit);
        params.put("outFlag", "1");
        if(yearAndMonth != null  && deptId != null){
            Map<String,Object> matchResultList = inputSqpMatchResultService.getDifferenceMatchResult(params);
            return R.ok().put("page",matchResultList);
        }else{
            return  null;
        }
    }
    /**
     * 查询红字发票差异清单
     *
     * @param params
     * @return
     */
    @RequestMapping("/getRedDifferenceMatchResult")
    public R getRedDifferenceMatchResult(@RequestBody Map<String, Object> params){
        String yearAndMonth=ParamsMap.findMap(params, "yearAndMonth");
        String deptId=ParamsMap.findMap(params, "deptId");
        Integer currPage = 0;
        Integer limit = 0;
        if (params.get("page") != null) {
            currPage = (Integer) params.get("page");
        }
        if (params.get("limit") != null) {
            limit = (Integer) params.get("limit");
        }
        params.put("offset", (currPage - 1) * limit);
        params.put("outFlag", "1");
        if(yearAndMonth != null  && deptId != null){
            Map<String,Object> matchResultList = inputSqpMatchResultService.getRedDifferenceMatchResult(params);
            return R.ok().put("page",matchResultList);
        }else{
            return  null;
        }
    }
    /**
     * 查询海关通知单差异清单
     *
     * @param params
     * @return
     */
    @RequestMapping("/getCustomsDifferenceMatchResult")
    public R getCustomsDifferenceMatchResult(@RequestBody Map<String, Object> params){
        String yearAndMonth=ParamsMap.findMap(params, "yearAndMonth");
        String deptId=ParamsMap.findMap(params, "deptId");
        Integer currPage = 0;
        Integer limit = 0;
        if (params.get("page") != null) {
            currPage = (Integer) params.get("page");
        }
        if (params.get("limit") != null) {
            limit = (Integer) params.get("limit");
        }
        params.put("offset", (currPage - 1) * limit);
        params.put("outFlag", "1");
        if(yearAndMonth != null  && deptId != null){
            Map<String,Object> matchResultList = inputSqpMatchResultService.getCustomsDifferenceMatchResult(params);
            return R.ok().put("page",matchResultList);
        }else{
            return  null;
        }
    }

    /**
     * 查询账票匹配结果(下载)
     *
     * @param params
     * @return
     */
    @RequestMapping("/getDifferenceMatchResultExcel")
    public R getDifferenceMatchResultExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String type = ParamsMap.findMap(params, "type");
        try {
            List list  = inputSqpMatchResultService.getDifferenceMatchResultExcel(params);
            String fileName = DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            if(type.equals("1")){
                new ExportExcel("", InvoiceDifferenceMatch.class).setDataList(list).write(response, fileName).dispose();
            }else if(type.equals("2")){
                new ExportExcel("", InvoiceCustomsDifferenceMatch.class).setDataList(list).write(response, fileName).dispose();
            }else{
                new ExportExcel("", RedInvoiceDifferenceMatch.class).setDataList(list).write(response, fileName).dispose();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }


}

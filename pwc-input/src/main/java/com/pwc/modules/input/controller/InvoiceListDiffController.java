package com.pwc.modules.input.controller;

import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.R;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;
import com.pwc.modules.input.entity.InputInvoiceTaxationEntity;
import com.pwc.modules.input.entity.vo.InputInvoiceTaxationVo;
import com.pwc.modules.input.entity.vo.TaxationDatacheckVo;
import com.pwc.modules.input.entity.vo.TaxationToleranceVo;
import com.pwc.modules.input.service.InputInvoiceListOfDiffService;
import com.pwc.modules.input.service.InputInvoiceMaterialService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputInvoiceTaxationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 发票与入账金额差异清单
 *
 * @author zhgxin
 */
@Slf4j
@RestController
@RequestMapping("/input/listOfDiff")
public class InvoiceListDiffController {

    @Autowired
    private InputInvoiceService invoiceService;
    @Autowired
    private InputInvoiceTaxationService inputInvoiceTaxationService;
    @Autowired
    private InputInvoiceMaterialService invoiceMaterialService;

    @Resource
    private InputInvoiceListOfDiffService inputInvoiceListOfDiffService;

    /*
    * 入账/认证数据查询
    * */
    @RequestMapping("/list")
    //@RequiresPermissions("input:listOfDiff:list")
    public R  list(@RequestParam Map<String, Object> params) {
        String date = params.get("queryDate").toString().substring(0, 7);
        String type = params.get("type").toString();// 1 新增入账，5 新增认证。
        String[] flag = null;
        if (InputConstant.TaxationStats.BENRU_WEIREN.getValue().equals(type)) {
            flag = new String[]{InputConstant.TaxationStats.BENRU_WEIREN.getValue(),
                    InputConstant.TaxationStats.BENRU_BENREN.getValue(),
                    InputConstant.TaxationStats.QIANRU_WEIREN.getValue(),
                    InputConstant.TaxationStats.QIANRU_BENREN.getValue(),
            };
        } else if (InputConstant.TaxationStats.BENREN_WEIRU.getValue().equals(type)) {
            flag = new String[]{InputConstant.TaxationStats.BENREN_WEIRU.getValue(),
                    InputConstant.TaxationStats.BENREN_BENRU.getValue(),
                    InputConstant.TaxationStats.QIANREN_WEIRU.getValue(),
                    InputConstant.TaxationStats.QIANEN_BENRU.getValue(),
            };
        }
        List<InputInvoiceTaxationEntity> taxationEntityList = inputInvoiceTaxationService.findByFlagNoData(flag, date);
        List<InputInvoiceTaxationVo> taxationEntityVoList = processVo(taxationEntityList, type,date);
        return R.ok().put("data", taxationEntityVoList);
    }


    /*
     * 按照月份查询数据
     * */
    @RequestMapping("/findByDate")
    //@RequiresPermissions("input:listOfDiff:findByDate")
    public R findTaxationData(@RequestParam Map<String, Object> params) {
        List<InputInvoiceTaxationVo> list = findTaxationDataVo(params);
        return R.ok().put("data", list);
    }
    /*
    * 添加调整原因
    * */
    @PostMapping("/saveReason")
    //@RequiresPermissions("input:listOfDiff:saveReason")
    public R saveReason(@RequestBody Map<String, Object> params) {
        inputInvoiceTaxationService.saveReason(params);
        return R.ok();
    }
    /*
    * 导出
    * */
    @RequestMapping("/exportRecordBydate")
    //@RequiresPermissions("input:listOfDiff:exportRecordBydate")
    public R exportRecordBydate(@RequestParam Map<String, Object> params, HttpServletResponse response){
        List<InputInvoiceTaxationVo> list = findTaxationDataVo(params);
        try {
            String fileName = "进项列表" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("进项列表", InputInvoiceTaxationVo.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }


    /*
    * 差异清单
    * */
    @RequestMapping("/findDifference")
    //@RequiresPermissions("input:listOfDiff:findDifference")
    public R findDifference(@RequestParam Map<String, Object> params){
        String date = params.get("queryDate").toString();
        String type = "0";
        List<TaxationToleranceVo> taxationVoList =  differenceVo(date,type,"");
        // 查询 erp 的入账税额 查询发票的入账税额  1.2.3.4.6.8
        return R.ok().put("data", taxationVoList);
    }
    /*
    * 设置容差
    * */
    @RequestMapping("/saveTolerance")
    //@RequiresPermissions("input:listOfDiff:saveTolerance")
    public R saveTolerance(@RequestParam Map<String, Object> params){
        String date =params.get("queryDate").toString() ;
        BigDecimal tolerance =new BigDecimal( params.get("tolerance").toString());
        String[] id = {InputConstant.TaxationStats.TOLERANCE.getValue()};
        List<InputInvoiceTaxationEntity> entityList = inputInvoiceTaxationService.findByFlagNoData(id,date.substring(0,7));
        if(entityList.size()>0){
            InputInvoiceTaxationEntity  Entity = entityList.get(0);
            Entity.setTaxationDate(DateUtils.stringToDate(date,"yyyy-MM-dd")); // 设置日期
            Entity.setTolerance(tolerance); // 设置容差额
            inputInvoiceTaxationService.updateById(Entity);
        }else{
            InputInvoiceTaxationEntity  Entity = new InputInvoiceTaxationEntity();
            Entity.setTaxationFlag(InputConstant.TaxationStats.TOLERANCE.getValue());
            Entity.setTaxationDate(DateUtils.stringToDate(date,"yyyy-MM-dd")); // 设置日期
            Entity.setTolerance(tolerance); // 设置容差额
            inputInvoiceTaxationService.save(Entity);
        }
        return R.ok();
    }

    /*
    * 调差导出
    *
    * */
//    @RequestMapping("/exportRecordByTolerance")
    //@RequiresPermissions("input:listOfDiff:exportRecordByTolerance")
    public R exportRecordByTolerance(@RequestParam Map<String, Object> params, HttpServletResponse response){
        String date = params.get("queryDate").toString();
        String type ="1";// params.get("type").toString();
        String ids = params.get("invoiceIds").toString();
        List<TaxationToleranceVo> taxationVoList =  differenceVo(date,type,ids);
        try {
            String fileName = "调差导出" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("调差导出", TaxationToleranceVo.class).setDataList(taxationVoList).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }
    /*
    * 调差确认
    * */
    @RequestMapping("/toleranceConfirm")
    //@RequiresPermissions("input:listOfDiff:toleranceConfirm")
    public R toleranceConfirm(@RequestParam Map<String, Object> params){
        // 是否要判断标志 按照勾选的
        String date = params.get("queryDate").toString();
        String invoiceIds = params.get("invoiceIds").toString();
        List<InputInvoiceTaxationEntity> taxationEntityList = new ArrayList<>();
        if(invoiceIds!=null &&!"".equals(invoiceIds)){
            taxationEntityList= findTaxationById(invoiceIds.split(","));
        }else{
            String[] ids = {InputConstant.TaxationStats.BENRU_WEIREN.getValue(), // 1 本月入账未认证
                    InputConstant.TaxationStats.BENRU_BENREN.getValue(),//2 本月认证未入账
                    InputConstant.TaxationStats.QIANRU_WEIREN.getValue(),//3 前月入账本月认证
                    InputConstant.TaxationStats.QIANRU_BENREN.getValue(),// 4 本月入账认证
                    InputConstant.TaxationStats.QIANEN_BENRU.getValue(),//8 前月认证本月入账
                    InputConstant.TaxationStats.BENREN_BENRU.getValue() //6 本月认证入账
            };
            taxationEntityList = inputInvoiceTaxationService.findByFlagNoData(ids, date.substring(0, 7));
        }
        // 获取容差

        for (InputInvoiceTaxationEntity taxationEntity:taxationEntityList){
            taxationEntity.setToleranceFlag(2); //调差成功
            inputInvoiceTaxationService.updateById(taxationEntity);
        }
        return R.ok();
    }


    /*
     *
     * 进项数据核对
     * */
    @RequestMapping("/findEntryDta")
    //@RequiresPermissions("input:listOfDiff:findEntryDta")
    public R findEntryDta(@RequestParam Map<String, Object> params) {
//        String year = params.get("").toString(); //获取查询的年份
        // 要查询一年 12个月的数据  每个月的数据 要按照是 入账 认证区分，然后计算差值 每一列的总和。
        List<TaxationDatacheckVo>  entryList= findbyMonth(params);
        return R.ok().put("data",entryList);
    }
    /*
    * 进项数据核对导出
    * */
//    @RequestMapping("/exportRecordEntryDta")
    //@RequiresPermissions("input:listOfDiff:exportRecordEntryDta")
    public R exportRecordEntryDta(@RequestParam Map<String, Object> params, HttpServletResponse response){
        String year = params.get("queryDate").toString().substring(0, 4);
        String title =year+"年" ;

        List<TaxationDatacheckVo>  taxationVoList= findbyMonth(params);
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, TaxationDatacheckVo.class).setDataList(taxationVoList).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }



    /*前期处理遗留数据*/
    public void saveOldData(Map<String, Object> params) {
        String Date = DateUtils.format(new Date(), "yyyy-MM-dd");
        inputInvoiceTaxationService.saveOldData(Date);
    }
    //
    public List<InputInvoiceTaxationVo> findTaxationDataVo(@RequestParam Map<String, Object> params) {
        String date = params.get("queryDate").toString().substring(0,7);
        System.out.println("查询日期："+date);
//        String[] flag ={"1","5","4","8","2","6"};
        String[] flag = {InputConstant.TaxationStats.BENRU_WEIREN.getValue(), // 1 本月入账未认证
                InputConstant.TaxationStats.BENREN_WEIRU.getValue(),//5 本月认证未入账
                InputConstant.TaxationStats.QIANRU_BENREN.getValue(),//4 前月入账本月认证
                InputConstant.TaxationStats.QIANEN_BENRU.getValue(),//8 前月认证本月入账
                InputConstant.TaxationStats.BENRU_BENREN.getValue(),// 2 本月入账认证
                InputConstant.TaxationStats.BENREN_BENRU.getValue() //6 本月认证入账
        };
        List<InputInvoiceTaxationEntity> taxationEntityList = inputInvoiceTaxationService.findByFlagNoData(flag, date);
        List list1 = new ArrayList();
        List list4 = new ArrayList();
        List list5 = new ArrayList();
        List list8 = new ArrayList();
        List list2 = new ArrayList();
        List list6 = new ArrayList();
        List<InputInvoiceTaxationVo> list = new ArrayList();
        for (InputInvoiceTaxationEntity taxationEntity : taxationEntityList) {
            if ((InputConstant.TaxationStats.BENRU_WEIREN.getValue()).equals(taxationEntity.getTaxationFlag())) { //
                // 查询根据input_invoice_material
                list1.add(taxationEntity.getInvoiceId());
            } else if ((InputConstant.TaxationStats.QIANRU_BENREN.getValue()).equals(taxationEntity.getTaxationFlag())) {
                list4.add(taxationEntity.getInvoiceId());
            } else if ((InputConstant.TaxationStats.BENREN_WEIRU.getValue()).equals(taxationEntity.getTaxationFlag())) {
                list5.add(taxationEntity.getInvoiceId());
            } else if ((InputConstant.TaxationStats.QIANEN_BENRU.getValue()).equals(taxationEntity.getTaxationFlag())) {
                list8.add(taxationEntity.getInvoiceId());
            } else if ((InputConstant.TaxationStats.BENRU_BENREN.getValue()).equals(taxationEntity.getTaxationFlag())) {
                list2.add(taxationEntity.getInvoiceId());
            } else if ((InputConstant.TaxationStats.BENREN_BENRU.getValue()).equals(taxationEntity.getTaxationFlag())) {
                list6.add(taxationEntity.getInvoiceId());
            }
        }
        list = mapTax(list, list1, InputConstant.TaxationStats.BENRU_WEIREN.getValue(),date);
        list = mapTax(list, list4, InputConstant.TaxationStats.QIANRU_BENREN.getValue(),date);
        list = mapTax(list, list5, InputConstant.TaxationStats.BENREN_WEIRU.getValue(),date);
        list = mapTax(list, list8, InputConstant.TaxationStats.QIANEN_BENRU.getValue(),date);
        list = mapTax(list, list2, InputConstant.TaxationStats.BENRU_BENREN.getValue(),date);
        list = mapTax(list, list6, InputConstant.TaxationStats.BENREN_BENRU.getValue(),date);
        list = findReason(date,list);
        for (InputInvoiceTaxationVo taxationVo : list) { // 入账减认证
            // 差异调整金额
            BigDecimal adjustment = taxationVo.getBenTax().add(taxationVo.getQianTax()).subtract(taxationVo.getBenTaxr().add(taxationVo.getQianTaxr()));
            // 加上调整金额
            List<InputInvoiceTaxationVo.ReasonVo> listReason =  taxationVo.getReasonVo();
            for (InputInvoiceTaxationVo.ReasonVo mapReason:listReason){
                if (null!=mapReason.getAdjustmentTax()&&!"".equals(mapReason.getAdjustmentTax())){
                    BigDecimal tax=new BigDecimal(mapReason.getAdjustmentTax().toString()) ;
                    adjustment = adjustment.add(tax);
                }
            }
            taxationVo.setAdjustment(adjustment);
            //税会差异
            BigDecimal difference = taxationVo.getTax().subtract(taxationVo.getTaxr());
            taxationVo.setDifference(difference);
            //
            BigDecimal CheckTax = difference.subtract(adjustment);
            taxationVo.setCheckTax(CheckTax);

        }
        return  list;
    }
    // 生成页面展示数据
    public List<InputInvoiceTaxationVo> mapTax(List<InputInvoiceTaxationVo> list, List idList, String type,String date) {
        if(idList.size()==0){
            return list;
        }
        List<InputInvoiceMaterialEntity> invoiceMaterialEntityList1 = invoiceMaterialService.getByInvoiceIds(idList);
        for (InputInvoiceMaterialEntity invoiceMaterialEntity : invoiceMaterialEntityList1) {
            // 税额
            String accountNumber = invoiceMaterialEntity.getSphSpmc();//
            String accountDescription = invoiceMaterialEntity.getSphGgxh();//
            // 入账金额
            BigDecimal tax = invoiceMaterialEntity.getSphSe();
            boolean flag = false;
            for (int i = 0; i < list.size(); i++) {
                // 判断原先是否有数据
                if (accountNumber.equals(list.get(i).getAccountNumber())) {
                    if (type.equals(InputConstant.TaxationStats.BENRU_WEIREN.getValue())) { // 本月入账未认证
                        list.get(i).setTax(tax.add(list.get(i).getTax()));
                        list.get(i).setBenTax(tax.add(list.get(i).getBenTax()));
                    } else if (type.equals(InputConstant.TaxationStats.QIANRU_BENREN.getValue())) { // 前月入账本月认证
                        list.get(i).setTaxr(tax.add(list.get(i).getTaxr()));
                        list.get(i).setQianTaxr(tax.add(list.get(i).getQianTaxr()));
                    } else if (type.equals(InputConstant.TaxationStats.BENREN_WEIRU.getValue())) { // 本月认证未入账
                        list.get(i).setTaxr(tax.add(list.get(i).getTaxr()));
                        list.get(i).setBenTaxr(tax.add(list.get(i).getBenTaxr()));
                    } else if (type.equals(InputConstant.TaxationStats.QIANEN_BENRU.getValue())) { // 前月认证本月入账
                        list.get(i).setTax(tax.add(list.get(i).getTax()));
                        list.get(i).setQianTaxr(tax.add(list.get(i).getQianTax()));
                    } else if (type.equals(InputConstant.TaxationStats.BENRU_BENREN.getValue())) {
                        list.get(i).setTax(tax.add(list.get(i).getTax()));
                        list.get(i).setTaxr(tax.add(list.get(i).getTaxr()));
                    } else if (type.equals(InputConstant.TaxationStats.BENREN_BENRU.getValue())) {
                        list.get(i).setTax(tax.add(list.get(i).getTax()));
                        list.get(i).setTaxr(tax.add(list.get(i).getTaxr()));
                    }
                    flag = true;
                }
            }
            // 如果没有就加上一条数据
            if (!flag) {
                InputInvoiceTaxationVo vo = new InputInvoiceTaxationVo();
                vo.setId(invoiceMaterialEntity.getInvoiceId());
                vo.setMonth(date.substring(5,7)+"月");
                vo.setAccountNumber(accountNumber);
                vo.setAccountDescription(accountDescription);
                if (type.equals(InputConstant.TaxationStats.BENRU_WEIREN.getValue())) { //
                    vo.setBenTax(tax);
                    vo.setBenTaxr(BigDecimal.ZERO);
                    vo.setQianTaxr(BigDecimal.ZERO);
                    vo.setQianTax(BigDecimal.ZERO);
                    vo.setTax(tax);
                    vo.setTaxr(BigDecimal.ZERO);
                } else if (type.equals(InputConstant.TaxationStats.QIANRU_BENREN.getValue())) {
                    vo.setBenTax(BigDecimal.ZERO);
                    vo.setBenTaxr(BigDecimal.ZERO);
                    vo.setQianTaxr(tax);
                    vo.setQianTax(BigDecimal.ZERO);
                    vo.setTax(BigDecimal.ZERO);
                    vo.setTaxr(tax);
                } else if (type.equals(InputConstant.TaxationStats.BENREN_WEIRU.getValue())) {
                    vo.setBenTaxr(tax);
                    vo.setBenTax(BigDecimal.ZERO);
                    vo.setQianTaxr(BigDecimal.ZERO);
                    vo.setQianTax(BigDecimal.ZERO);
                    vo.setTax(BigDecimal.ZERO);
                    vo.setTaxr(tax);
                } else if (type.equals(InputConstant.TaxationStats.QIANEN_BENRU.getValue())) {
                    vo.setBenTaxr(BigDecimal.ZERO);
                    vo.setBenTax(BigDecimal.ZERO);
                    vo.setQianTaxr(tax);
                    vo.setQianTax(BigDecimal.ZERO);
                    vo.setTax(tax);
                    vo.setTaxr(BigDecimal.ZERO);
                } else if (type.equals(InputConstant.TaxationStats.BENRU_BENREN.getValue())) {
                    vo.setTax(tax);
                    vo.setTaxr(tax);
                } else if (type.equals(InputConstant.TaxationStats.BENREN_BENRU.getValue())) {
                    vo.setTax(tax);
                    vo.setTaxr(tax);
                }
                list.add(vo);
            }
        }
        return list;
    }

    // 查询 新增原因
    public List<InputInvoiceTaxationVo> findReason(String date,List<InputInvoiceTaxationVo> list) {
        // 根据日期查询
        // 查询出来之后
        String[] flag = {InputConstant.TaxationStats.REASON.getValue()}; // 新增原因
        List<InputInvoiceTaxationEntity> taxationEntityList = inputInvoiceTaxationService.findByFlag(flag, date);
        // 假如一个原因 有五个科目信息
        for (InputInvoiceTaxationEntity taxationEntity : taxationEntityList) {
            for (int i = 0; i < list.size(); i++) {
                boolean falg = true;
                for (InputInvoiceTaxationVo.ReasonVo reasonVo : list.get(i).getReasonVo()) {
                    if((reasonVo.getAdjustmentReason()).equals(taxationEntity.getAdjustmentReason())){
                        falg = false;
                    }
                    if ((taxationEntity.getAccountNumber()).equals(list.get(i).getAccountNumber())) {
                        reasonVo.setAdjustmentTax(taxationEntity.getAdjustmentTax());
                    }
                }
                if (falg) {
                    InputInvoiceTaxationVo.ReasonVo vo = new InputInvoiceTaxationVo.ReasonVo();
                    vo.setAdjustmentReason(taxationEntity.getAdjustmentReason());
                    if ((taxationEntity.getAccountNumber()).equals(list.get(i).getAccountNumber())) { // 科目的判断
                        vo.setAdjustmentTax(taxationEntity.getAdjustmentTax());
                    }
                    list.get(i).getReasonVo().add(vo);
                }

            }
        }
        return list;
    }

    // 入账/认证页面数据处理
    public List<InputInvoiceTaxationVo>  processVo(List<InputInvoiceTaxationEntity> taxationEntityList,String type,String date){
        List<InputInvoiceTaxationVo> taxationEntityVoList =  new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal sumX = BigDecimal.ZERO;
        BigDecimal sumR = BigDecimal.ZERO;
        // 缺少凭证号
        for (InputInvoiceTaxationEntity taxationEntity:taxationEntityList) {
            InputInvoiceTaxationVo taxationVo = new InputInvoiceTaxationVo();
            Date entryDate = new Date();
            if(InputConstant.TaxationStats.BENRU_WEIREN.getValue().equals(type)) { // 入账页面
                // 缺少凭证号
                taxationVo.setVoucherNumber("");
                taxationVo.setEntryDate(taxationEntity.getEntryDate());
                // 账龄
                entryDate = DateUtils.stringToDate(taxationEntity.getEntryDate(), "yyyy-MM-dd");
            }else if(InputConstant.TaxationStats.BENREN_WEIRU.getValue().equals(type)){
                taxationVo.setInvoiceNumber(taxationEntity.getInvoiceNumber());
                taxationVo.setEntryDate(taxationEntity.getInvoiceAuthDate());
                entryDate = DateUtils.stringToDate(taxationEntity.getInvoiceAuthDate(), "yyyy-MM-dd");
            }
            taxationVo.setAging(DateUtils.getMonthSpace(entryDate, new Date()));
            taxationVo.setMonth(date.substring(5,7)+"月");
            taxationVo.setTax(taxationEntity.getInvoiceTotalPrice()); // 入账金额

            if (InputConstant.TaxationStats.BENRU_WEIREN.getValue().equals(taxationEntity.getTaxationFlag())
                    || InputConstant.TaxationStats.BENREN_WEIRU.getValue().equals(taxationEntity.getTaxationFlag())) {  // 1或者5
                taxationVo.setAgingFlag(InputConstant.TaxationStats.FLAG_TAXATION.getValue());
                sumX = sumX.add(taxationVo.getTax());
                sum = sum.add(taxationVo.getTax());
            } else if (InputConstant.TaxationStats.BENRU_BENREN.getValue().equals(taxationEntity.getTaxationFlag())
                    || InputConstant.TaxationStats.BENREN_BENRU.getValue().equals(taxationEntity.getTaxationFlag())) { // 2或者6
                taxationVo.setAgingFlag(InputConstant.TaxationStats.FLAG_TAXATION.getValue());
                taxationVo.setCertificationFlag(InputConstant.TaxationStats.FLAG_TAXATION.getValue());
                sumX = sumX.add(taxationVo.getTax());
                sum = sum.add(taxationVo.getTax());
            } else if (InputConstant.TaxationStats.QIANRU_WEIREN.getValue().equals(taxationEntity.getTaxationFlag())
                    || InputConstant.TaxationStats.QIANREN_WEIRU.getValue().equals(taxationEntity.getTaxationFlag())) { // 3或者7
                taxationVo.setColourFlag(InputConstant.TaxationStats.FLAG_TAXATION.getValue());
                sum = sum.add(taxationVo.getTax());
            } else if (InputConstant.TaxationStats.QIANRU_BENREN.getValue().equals(taxationEntity.getTaxationFlag())
                    || InputConstant.TaxationStats.QIANEN_BENRU.getValue().equals(taxationEntity.getTaxationFlag())) { // 4或者8
                taxationVo.setCertificationFlag(InputConstant.TaxationStats.FLAG_TAXATION.getValue());
                taxationVo.setColourFlag(InputConstant.TaxationStats.FLAG_TAXATION.getValue());
                taxationVo.setTax(BigDecimal.ZERO.subtract(taxationEntity.getInvoiceTotalPrice()));
                sumR = sumR.add(taxationVo.getTax());
                sum = sum.add(taxationVo.getTax());
            }
            if(!InputConstant.TaxationStats.BENRU_BENREN.getValue().equals(taxationEntity.getTaxationFlag())
                    && !InputConstant.TaxationStats.BENREN_BENRU.getValue().equals(taxationEntity.getTaxationFlag())){
                taxationEntityVoList.add(taxationVo);
            }

        }
        if(taxationEntityVoList.size()>0){
            InputInvoiceTaxationVo taxationVo = new InputInvoiceTaxationVo();
            taxationVo.setTax(sum);
            taxationVo.setBenTax(sumX);
            taxationVo.setBenTaxr(sumR);
            taxationEntityVoList.add(taxationVo);
        }
        return  taxationEntityVoList;
    }

    public List<TaxationToleranceVo> differenceVo(String date, String type, String invoiceId) {
        String[] ids = {InputConstant.TaxationStats.BENRU_WEIREN.getValue(), // 1 本月入账未认证
                InputConstant.TaxationStats.BENRU_BENREN.getValue(),//2 本月认证未入账
                InputConstant.TaxationStats.QIANRU_WEIREN.getValue(),//3 前月入账本月认证
                InputConstant.TaxationStats.QIANRU_BENREN.getValue(),// 4 本月入账认证
                InputConstant.TaxationStats.QIANEN_BENRU.getValue(),//8 前月认证本月入账
                InputConstant.TaxationStats.BENREN_BENRU.getValue() //6 本月认证入账
        };
        List<InputInvoiceTaxationEntity> taxationEntityList = new ArrayList<>();
        if (invoiceId != null && !"".equals(invoiceId)) {
            taxationEntityList = findTaxationById(invoiceId.split(","));

        } else {
            taxationEntityList = inputInvoiceTaxationService.findByFlagNoData(ids, date.substring(0, 7));
        }
        if (type.equals("1")) {
            for (InputInvoiceTaxationEntity taxationEntity : taxationEntityList) {
                taxationEntity.setToleranceFlag(1); //调差中
                inputInvoiceTaxationService.updateById(taxationEntity);
            }
        }
        List<TaxationToleranceVo> taxationVoList = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (InputInvoiceTaxationEntity taxationEntity : taxationEntityList) {
            boolean flag = true;
            if (taxationVoList.size() > 0) {
                for (TaxationToleranceVo vo : taxationVoList) {
                    if (vo.getInvoiceNumber().equals(taxationEntity.getInvoiceNumber())
                            && vo.getInvoiceCode().equals(taxationEntity.getInvoiceCode())
                            && vo.getVoucherNumber().equals(taxationEntity.getVoucherNumber())
                    ) {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                String[] id = {InputConstant.TaxationStats.TOLERANCE.getValue()};
                List<InputInvoiceTaxationEntity> entityList = inputInvoiceTaxationService.findByFlagNoData(id, date);
                BigDecimal tolerance = BigDecimal.ZERO;
                if (entityList.size() > 0) {
                    tolerance = entityList.get(0).getTolerance() != null ? entityList.get(0).getTolerance() : BigDecimal.ZERO;
                }
                BigDecimal checkTax = BigDecimal.ZERO;//发票税额 - 入账税额
//                if(tolerance.compareTo(checkTax) ==-1 || (BigDecimal.ZERO.subtract(tolerance)).compareTo(checkTax) ==1){
                TaxationToleranceVo taxationVo = new TaxationToleranceVo();
                taxationVo.setId(taxationEntity.getId());//id
                taxationVo.setMonth(date.substring(5,7)+"月");
                taxationVo.setVoucherNumber(taxationEntity.getVoucherNumber());//凭证号
                taxationVo.setEntryDate(taxationEntity.getEntryDate());//入账日期
//            taxationVo.setTax(); // 入账税额
                taxationVo.setInvoiceNumber(taxationEntity.getInvoiceNumber());//发票号
                taxationVo.setInvoiceCode(taxationEntity.getInvoiceCode());//发票代码
                taxationVo.setTaxr(taxationEntity.getInvoiceTaxPrice()); // 发票税额
                taxationVo.setCheckTax(checkTax);//差异调整
//            sum = sum.add(checkTax);
                taxationVo.setToleranceFlag(taxationEntity.getToleranceFlag());//调差状态
                taxationVoList.add(taxationVo);
            }
//            }

        }
        if(taxationVoList.size()>0){
            TaxationToleranceVo taxationVo = new TaxationToleranceVo();
            taxationVo.setCheckTax(sum);
            taxationVoList.add(taxationVo);
        }
        return taxationVoList;
    }
    // 勾选的id
    public List<InputInvoiceTaxationEntity>  findTaxationById(String[] ids){
        List<InputInvoiceTaxationEntity> entityLists = new ArrayList<>();
        for (String id:ids) {
            InputInvoiceTaxationEntity entity = inputInvoiceTaxationService.getById(id);
            List<InputInvoiceTaxationEntity> entityList =inputInvoiceTaxationService.findByEntity(entity.getInvoiceNumber(),entity.getInvoiceCode(),entity.getVoucherNumber());
            for (InputInvoiceTaxationEntity entitys:entityList){
                entityLists.add(entitys);
            }
        }
        return entityLists;
    }

    // 查询每个月的数据
    public List<TaxationDatacheckVo> findbyMonth(@RequestParam Map<String, Object> params) {
        String queryDate = params.get("queryDate").toString().substring(0, 4);
        Date year = DateUtils.stringToDate(queryDate, "yyyy");
        // 按照月份查询数据
        //  1  2  6 8 本月入账数据  5 6 2 4 本月认账数据
        List<TaxationDatacheckVo> voList = new ArrayList<>();
        int month =0;
        if(DateUtils.getYearSame(year,new Date())){
            month = DateUtils.getMonth(new Date());
        }else if (year.before(new Date())){
            month = 12;
        }
        BigDecimal sumTax = BigDecimal.ZERO; // 入账税额
        BigDecimal sumTaxr = BigDecimal.ZERO; // 认证税额
        BigDecimal sumCheckTax = BigDecimal.ZERO;
        for (int i = 1; i <= month; i++) {
            String date = queryDate+"-"+String.format("%02d",i);
            System.out.println(date);
            TaxationDatacheckVo taxationVo = new TaxationDatacheckVo();
            String[] flag = {InputConstant.TaxationStats.BENRU_WEIREN.getValue(), // 1 本月入账未认证
                    InputConstant.TaxationStats.BENREN_WEIRU.getValue(),//5 本月认证未入账
                    InputConstant.TaxationStats.QIANRU_BENREN.getValue(),//4 前月入账本月认证
                    InputConstant.TaxationStats.QIANEN_BENRU.getValue(),//8 前月认证本月入账
                    InputConstant.TaxationStats.BENRU_BENREN.getValue(),// 2 本月入账认证
                    InputConstant.TaxationStats.BENREN_BENRU.getValue() //6 本月认证入账
            };
            List<InputInvoiceTaxationEntity> taxationEntityList = inputInvoiceTaxationService.findByFlagNoData(flag, date);
            BigDecimal tax = BigDecimal.ZERO; // 入账税额
            BigDecimal taxr = BigDecimal.ZERO; // 认证税额
            for (InputInvoiceTaxationEntity Entity : taxationEntityList) {
                if ((InputConstant.TaxationStats.BENRU_WEIREN.getValue()).equals(Entity.getTaxationFlag())
                        || (InputConstant.TaxationStats.QIANEN_BENRU.getValue()).equals(Entity.getTaxationFlag())
                ) {
                    tax = tax.add(Entity.getInvoiceTotalPrice());
                } else if ((InputConstant.TaxationStats.BENREN_WEIRU.getValue()).equals(Entity.getTaxationFlag()) ||
                        (InputConstant.TaxationStats.QIANRU_BENREN.getValue()).equals(Entity.getTaxationFlag())
                ) {
                    taxr = taxr.add(Entity.getInvoiceTotalPrice());
                } else if ((InputConstant.TaxationStats.BENRU_BENREN.getValue()).equals(Entity.getTaxationFlag()) ||
                        (InputConstant.TaxationStats.BENREN_BENRU.getValue()).equals(Entity.getTaxationFlag())

                ) {
                    tax = tax.add(Entity.getInvoiceTotalPrice());
                    taxr = taxr.add(Entity.getInvoiceTotalPrice());
                }
            }
            BigDecimal checkTax = taxr.subtract(tax);
            taxationVo.setMonth(i+"月");
            taxationVo.setTax(tax);
            taxationVo.setTaxr(taxr);
            taxationVo.setCheckTax(checkTax);
            voList.add(taxationVo);
            sumTax = sumTax.add(tax);
            sumTaxr = sumTaxr.add(taxr);
            sumCheckTax = sumCheckTax.add(checkTax);
        }
        if(voList.size()>0){
            // 本年度合计
            TaxationDatacheckVo taxationVoSum = new TaxationDatacheckVo();
            taxationVoSum.setMonth("本年合计");
            taxationVoSum.setTax(sumTax);
            taxationVoSum.setTaxr(sumTaxr);
            taxationVoSum.setCheckTax(sumCheckTax);
            voList.add(taxationVoSum);
            // 入账合计
            TaxationDatacheckVo taxationVoru = new TaxationDatacheckVo();
            taxationVoru.setMonth("已入账未认证");
            taxationVoru.setTax(sumTax);
            voList.add(taxationVoru);
            // 认证合计
            TaxationDatacheckVo taxationVoren = new TaxationDatacheckVo();
            taxationVoren.setMonth("已认证未入账");
            taxationVoren.setTaxr(sumTaxr);
            voList.add(taxationVoren);
            // 差异
            TaxationDatacheckVo taxationVoCheck = new TaxationDatacheckVo();
            taxationVoCheck.setMonth("差异");
            taxationVoCheck.setCheckTax(sumCheckTax);
            voList.add(taxationVoCheck);
            // 差异调整
            TaxationDatacheckVo taxationVoCheck1 = new TaxationDatacheckVo();
            taxationVoCheck1.setMonth("调整后差异");
            taxationVoCheck1.setCheckTax(sumCheckTax);
            voList.add(taxationVoCheck1);
        }
        return voList;
    }

}

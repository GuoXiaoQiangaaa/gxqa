package com.pwc.modules.input.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.dao.InputInvoiceBatchDao;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("invoiceBatchService")
public class InputInvoiceBatchServiceImpl extends ServiceImpl<InputInvoiceBatchDao, InputInvoiceBatchEntity> implements InputInvoiceBatchService {

    @Autowired
    private InputInvoiceService invoiceService;
    @Autowired
    private InputInvoiceBatchService invoiceBatchService;
    @Autowired
    private InputInvoiceMaterialSapService invoiceMaterialSapService;
    @Autowired
    private InputInvoiceMaterialService invoiceMaterialService;
    @Autowired
    private InputInvoiceMaterialRpaService invoiceMaterialRpaService;
    @Autowired
    private InputInvoiceVoucherService invoiceVoucherService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private InputInvoiceLabelRelatedService inputInvoiceLabelRelatedService;


    @Override
    public List<InputInvoiceBatchEntity> getList() {
        return this.baseMapper.getList();
    }

    @Override
    public InputInvoiceBatchEntity getBatchNumber(InputInvoiceEntity invoiceEntity) {
        return this.baseMapper.getBatchNumber(invoiceEntity);
    }

    @Override
    @DataFilter(deptId = "a.company_id", subDept = true,user = false)
    public PageUtils findListForThree(Map<String, Object> params) {
//        SysUserEntity user = ShiroUtils.getUserEntity();
        String[] companyIds = null;

        // 替换原来的公司查询数据权限
        String sqlFilter = null;
        if (!StrUtil.isBlankIfStr(params.get(Constant.SQL_FILTER))) {
            sqlFilter = (String) params.get(Constant.SQL_FILTER);
        }


        String invoicePurchaserCompany = (String)params.get("invoicePurchaserCompany");
        String invoiceSellCompany = (String)params.get("invoiceSellCompany");
        String invoiceBatchNumber = (String)params.get("invoiceBatchNumber");
        String invoiceBatchStatus = (String)params.get("invoiceBatchStatus");
        String updateBy = (String)params.get("updateBy");
        String invoiceCode = (String)params.get("invoiceCode");
        String invoiceNumber = (String)params.get("invoiceNumber");
        String iEntity = (String)params.get("invoiceEntity");
        String invoiceType = (String)params.get("invoiceType");
        String invoiceCreateDate = (String)params.get("invoiceCreateDate");
        String enDate = (String)params.get("entryDate");
        String minAmount = (String)params.get("minAmount");
        String maxAmount = (String)params.get("maxAmount");

        String menuType = (String) params.get("menuType");
        String statusSql = null;
        List<String> statusList = null;
        if (StrUtil.isNotBlank(menuType)) {
            // 1.管理 2.验真 3.三单匹配 4.入账 5.异常
            if (InputConstant.MenuType.MANAGE.getValue().equals(menuType)) {
                if (StrUtil.isNotBlank(invoiceBatchStatus)) {
                    statusSql = "i.invoice_status in ("+invoiceBatchStatus+")";
                    statusList = new ArrayList<String>(){{
                        add(invoiceBatchStatus);
                    }};
                }
            } else if (InputConstant.MenuType.CHECK_TRUE.getValue().equals(menuType)) {
                statusSql = "i.invoice_status in (3,4)";
                statusList = new ArrayList<String>(){{
                    add("3");
                    add("4");
                }};
            } else if (InputConstant.MenuType.TRIPLE_MATCH.getValue().equals(menuType)) {
                statusSql = "i.invoice_status in (5,6)";
                statusList = new ArrayList<String>(){{
                    add("5");
                    add("6");
                }};
            } else if (InputConstant.MenuType.ENTER.getValue().equals(menuType)) {
                statusSql = "i.invoice_status in (7,8)";
                statusList = new ArrayList<String>(){{
                    add("7");
                    add("8");
                }};
            } else if (InputConstant.MenuType.ABNORMAL.getValue().equals(menuType)) {
                statusSql = "i.invoice_status in (-1, -2, -3, -4, -5)";
                statusList = new ArrayList<String>(){{
                    add("-1");
                    add("-2");
                    add("-3");
                    add("-4");
                    add("-5");
                }};
            }else if (InputConstant.MenuType.VERIFY.getValue().equals(menuType)){
                statusSql = "i.invoice_status in (9, 10, 12, 13)";
                statusList = new ArrayList<String>(){{
                    add("9");
                    add("10");
                    add("12");
                    add("13");
                }};
            }
        }

        String[] createDate= null;
        String createTime="";
        String createTime2="";
        if (StringUtils.isNotBlank((String)params.get("createDate"))){
            createDate= params.get("createDate").toString().split(",");
            createTime = createDate[0]+" 00:00";
            createTime2 =  createDate[1]+" 23:59";
        }
        String invoiceCreateDateBegin="", invoiceCreateDateEnd="";
        // 发票时间
        if (StringUtils.isNotBlank(invoiceCreateDate)) {
            String[] invoiceDate = invoiceCreateDate.split(",");
            invoiceCreateDateBegin = invoiceDate[0]+" 00:00";
            invoiceCreateDateEnd = invoiceDate[1]+" 23:59";
        }
        String entryDateBegin="", entryDateEnd="";
        // 入账时间
        if (StringUtils.isNotBlank(enDate)) {
            String[] invoiceDate = enDate.split(",");
            entryDateBegin = invoiceDate[0]+" 00:00";
            entryDateEnd = invoiceDate[1]+" 23:59";
        }
        String createBy = "";
        if (StringUtils.isNotBlank((String)params.get("createBy"))){
            createBy = (String)params.get("createBy");
        }
        System.out.println("invoiceBatchNumber===="+invoiceBatchNumber);
        //DOTO  查询
        String[] array = null;
        if(!StrUtil.isBlankOrUndefined(invoiceBatchNumber)) {
            array = invoiceBatchNumber.split(",");
        }
        List<InputInvoiceBatchEntity> invoiceBatchEntityList=new ArrayList<>();
        int offset=Integer.parseInt(params.get("offset").toString());
        int limit=Integer.parseInt(params.get("limit").toString());


        invoiceBatchEntityList=this.baseMapper.findListForThree(offset,limit,invoicePurchaserCompany,invoiceSellCompany,array,statusSql,createTime,createTime2,createBy,updateBy,
                invoiceCode,invoiceNumber,iEntity,invoiceType,invoiceCreateDateBegin,invoiceCreateDateEnd,entryDateBegin,entryDateEnd,minAmount,maxAmount, sqlFilter);
        //总记录数
        //int totalCount = this.baseMapper.selectCount(invoicePurchaserCompany,invoiceSellCompany);
        int totalCount = this.baseMapper.findListForThree2(offset,limit,invoicePurchaserCompany,invoiceSellCompany,array,companyIds,statusSql,createTime,createTime2,createBy,updateBy,
                invoiceCode,invoiceNumber,iEntity,invoiceType,invoiceCreateDateBegin,invoiceCreateDateEnd,entryDateBegin,entryDateEnd,minAmount,maxAmount, sqlFilter);
        //每页记录数
        int pageSize=Integer.parseInt((String)params.get("limit"));
        //当前页数
        int currPage = Integer.parseInt((String)params.get("page"));
        if (invoiceBatchEntityList.size()<=0){
            return new PageUtils(invoiceBatchEntityList,totalCount,pageSize,currPage);
        }
        List<String> batchIdList=new ArrayList<>();
        for(int i=0;i<invoiceBatchEntityList.size();i++){
            batchIdList.add(String.valueOf(invoiceBatchEntityList.get(i).getId()));
        }
        InputInvoiceEntity invoiceEntity=new InputInvoiceEntity();
        invoiceEntity.setInvoices(batchIdList);
        List<InputInvoiceEntity> invoiceEntityList=invoiceService.getListByBatchIds(invoiceEntity);
        for(int i=0;i<invoiceBatchEntityList.size();i++){
            for(int j=0;j<invoiceEntityList.size();j++){
                if(invoiceEntityList.get(j).getInvoicePurchaserCompany()!=null&&!"".equals(invoiceEntityList.get(j).getInvoicePurchaserCompany())){
                    if(invoiceBatchEntityList.get(i).getId()==Integer.parseInt(invoiceEntityList.get(j).getInvoiceBatchNumber())){
                        if (StringUtils.isNotBlank(invoiceEntityList.get(j).getInvoicePurchaserCompany())) {
                            invoiceBatchEntityList.get(i).setInvoicePurchaserCompany(invoiceEntityList.get(j).getInvoicePurchaserCompany());
                        }
                        if (StringUtils.isNotBlank(invoiceEntityList.get(j).getInvoicePurchaserParagraph())) {
                            invoiceBatchEntityList.get(i).setInvoicePurchaserParagraph(invoiceEntityList.get(j).getInvoicePurchaserParagraph());
                        }
                        if (StringUtils.isNotBlank(invoiceEntityList.get(j).getInvoiceSellCompany()) ) {
                            invoiceBatchEntityList.get(i).setInvoiceSellCompany(invoiceEntityList.get(j).getInvoiceSellCompany());
                        }
                        if (StringUtils.isBlank(invoiceEntityList.get(j).getInvoiceSellParagraph())) {
                            invoiceBatchEntityList.get(i).setInvoiceSellParagraph(invoiceEntityList.get(j).getInvoiceSellParagraph());
                        }
                    }
                }
            }
        }
        for(int i=0;i<invoiceBatchEntityList.size();i++){

            List<InputInvoiceEntity> invoiceEntities = invoiceService.getListByBatchIdAndStatus(invoiceBatchEntityList.get(i).getId()+"", statusList);
            if(params.get("outFlag")!=null&& params.get("outFlag").equals("1")){
                invoiceBatchEntityList.get(i).setInvoiceList(inputInvoiceLabelRelatedService.findLabelByInvoiceId(invoiceEntities));
            }else{
                invoiceBatchEntityList.get(i).setInvoiceList(invoiceEntities);
            }
            if(invoiceBatchEntityList.get(i).getInvoiceBatchStatus().equals("6")) {
                invoiceBatchEntityList.get(i).setThreeErrorDescription("请编辑批次修改物料清单");
            } else if(invoiceBatchEntityList.get(i).getInvoiceBatchStatus().equals("8")) {
                invoiceBatchEntityList.get(i).setThreeErrorDescription(invoiceEntities.get(0).getInvoiceErrorDescription());
                invoiceBatchEntityList.get(i).setEntryDate("");
                invoiceBatchEntityList.get(i).setEntrySuccessCode("");
                invoiceBatchEntityList.get(i).setBelnr("");
            } else if(invoiceBatchEntityList.get(i).getInvoiceBatchStatus().equals("7")) {
                // 如果批次已入账
//                List<InputInvoiceEntity> invoiceEntities = invoiceService.getListByGroup(invoiceBatchEntityList.get(i));
                // 会计凭证编号
                String belnr = "";
                // 入账日期
                String entryDate = "";
                // 发票凭证编号
                String entrySuccessCode = "";
                for(int j = 0; j < invoiceEntities.size(); j++) {
                    if(j == invoiceEntities.size() - 1) {
                        belnr += invoiceEntities.get(j).getBelnr();
                        entryDate += invoiceEntities.get(j).getEntryDate();
                        entrySuccessCode += invoiceEntities.get(j).getEntrySuccessCode();
                    } else {
                        belnr += invoiceEntities.get(j).getBelnr() + "\n";
                        entryDate += invoiceEntities.get(j).getEntryDate() + "\n";
                        entrySuccessCode += invoiceEntities.get(j).getEntrySuccessCode() + "\n";
                    }
                }
                if(StringUtils.isNotBlank(entryDate) && !entryDate.equals("null")) {
                    invoiceBatchEntityList.get(i).setEntryDate(entryDate);
                }
                if(StringUtils.isNotBlank(entrySuccessCode) && !entrySuccessCode.equals("null")) {
                    invoiceBatchEntityList.get(i).setEntrySuccessCode(entrySuccessCode);
                }
                if(StringUtils.isNotBlank(belnr) && !belnr.equals("null")) {
                    invoiceBatchEntityList.get(i).setBelnr(belnr);
                }
            } else if(invoiceBatchEntityList.get(i).getInvoiceBatchStatus().equals("4") && StringUtils.isBlank(invoiceBatchEntityList.get(i).getCheckWhiteBlackStatus())) {
                invoiceBatchEntityList.get(i).setThreeErrorDescription("请完成发票验真");
            } else if((StringUtils.isNotBlank(invoiceBatchEntityList.get(i).getCheckWhiteBlackStatus()))) {
                if((invoiceBatchEntityList.get(i).getCheckWhiteBlackStatus().equals("1") || invoiceBatchEntityList.get(i).getCheckWhiteBlackStatus().equals("2") || invoiceBatchEntityList.get(i).getCheckWhiteBlackStatus().equals("3"))) {
                    invoiceBatchEntityList.get(i).setThreeErrorDescription(invoiceBatchEntityList.get(i).getErrorInfo());
                }
            } else {
                if(!"1".equals(invoiceBatchEntityList.get(i).getInvoiceBatchStatus())){
                    List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList=new ArrayList<>();
                    InputInvoiceMaterialSapEntity invoiceMaterialSapEntity=new InputInvoiceMaterialSapEntity();
                    invoiceMaterialSapEntity.setBatchId(String.valueOf(invoiceBatchEntityList.get(i).getId()));
                    invoiceMaterialSapEntityList=invoiceMaterialSapService.getListByBatchId(invoiceMaterialSapEntity);
                    String sapStr = "1";
                    int sapIndex = -1;
                    for(int j=0;j<invoiceMaterialSapEntityList.size();j++){
                        if("0".equals(invoiceMaterialSapEntityList.get(j).getMate()) && (StringUtils.isBlank(invoiceMaterialSapEntityList.get(j).getMatchStatus()) || !invoiceMaterialSapEntityList.get(j).getMatchStatus().equals("1111111111")) && StringUtils.isNotBlank(invoiceMaterialSapEntityList.get(j).getDescription())){
                            sapStr="0";
                            sapIndex = j;
                            break;
                        }
                    }
                    List<Integer> integerList = new ArrayList<>();
                    InputInvoiceEntity invoiceEntity1 = new InputInvoiceEntity();
                    invoiceEntity1.setInvoiceBatchNumber(String.valueOf(invoiceBatchEntityList.get(i).getId()));
//                    List<InputInvoiceEntity> invoiceEntities = invoiceService.getListByBatchId(invoiceEntity1);
                    for(int j = 0; j < invoiceEntities.size(); j++) {
                        integerList.add(invoiceEntities.get(j).getId());
                    }
                    List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = new ArrayList<InputInvoiceMaterialEntity>();
                    if (integerList.size()>0){
                        invoiceMaterialEntityList = invoiceMaterialService.getByInvoiceIds(integerList);

                    }
                    String materialStr = "1";
                    int materialIndex = -1;
                    for(int j = 0; j < invoiceMaterialEntityList.size(); j++) {
                        if("0".equals(invoiceMaterialEntityList.get(j).getStatus()) && (StringUtils.isBlank(invoiceMaterialEntityList.get(j).getMatchStatus()) || !invoiceMaterialEntityList.get(j).getMatchStatus().equals("1111111111"))) {
                            materialStr = "0";
                            materialIndex = j;
                            break;
                        }
                    }
                    if(sapStr.equals("0") && materialStr.equals("0")) {
                        for(int j=0;j<invoiceMaterialSapEntityList.size();j++){
                            if("0".equals(invoiceMaterialSapEntityList.get(j).getMate()) && (StringUtils.isBlank(invoiceMaterialSapEntityList.get(j).getMatchStatus()) || !invoiceMaterialSapEntityList.get(j).getMatchStatus().equals("1111111111")) && StringUtils.isNotBlank(invoiceMaterialSapEntityList.get(j).getDescription())){
                                String a = invoiceMaterialSapEntityList.get(j).getMblnr();
                                String b = invoiceMaterialSapEntityList.get(j).getDescription();
                                String threeErrorDescription = a+b;
                                System.out.println(threeErrorDescription);
                                invoiceBatchEntityList.get(i).setThreeErrorDescription(threeErrorDescription);
                                break;
                            }
                        }
                    } else if(sapStr.equals("0") && materialStr.equals("1")) {
                        invoiceBatchEntityList.get(i).setThreeErrorDescription("物料凭证" + invoiceMaterialSapEntityList.get(sapIndex).getMblnr() + "无法匹配");
                    } else if(sapStr.equals("1") && materialStr.equals("0")) {
                        invoiceBatchEntityList.get(i).setThreeErrorDescription("发票" + invoiceMaterialEntityList.get(materialIndex).getInvoiceNumber() +"无法匹配");
                    }
                }
            }
        }
        return new PageUtils(invoiceBatchEntityList,totalCount,pageSize,currPage);
    }

    @Override
    public InputInvoiceBatchEntity getLastOne() {
        return this.baseMapper.getLastOne();
    }


    /**
     * 计算批次号
     *
     * @return 批次号
     * @throws Exception
     */
    @Override
    public String calculateBatchNumber() {
        String number = "";
        try {
            InputInvoiceBatchEntity invoiceBatchEntity = invoiceBatchService.getLastOne();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String nowDate = sdf.format(new Date());
            if (invoiceBatchEntity != null) {
                String batchNumber = invoiceBatchEntity.getInvoiceBatchNumber().substring(0, 8);
                if (batchNumber.equals(nowDate)) {
                    String count = "1" + invoiceBatchEntity.getInvoiceBatchNumber().substring(8, 11);
                    Integer val = Integer.parseInt(count) + 1;
                    String sum = String.valueOf(val);
                    String before = invoiceBatchEntity.getInvoiceBatchNumber().substring(0, 8);
                    number = before + sum.substring(1, 4);
                } else {
                    number = nowDate + "001";
                }
            } else {
                number = nowDate + "001";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }

    /**
     * 删除批次，若图片下载失败，则删除批次
     * @param id
     */
    @Override
    public void delBatch(Integer id){
        InputInvoiceBatchEntity invoiceBatchEntity=new InputInvoiceBatchEntity();
        invoiceBatchEntity.setId(id);
        try {
            this.baseMapper.deleteBatch(invoiceBatchEntity);
            InputInvoiceEntity invoiceEntity=new InputInvoiceEntity();
            invoiceEntity.setInvoiceBatchNumber(String.valueOf(invoiceBatchEntity.getId()));
            List<InputInvoiceEntity> invoiceEntityList=invoiceService.getListByBatchId(invoiceEntity);
            if (invoiceEntityList.size() == 0){

                return ;

            }


            invoiceService.deleteInvoiceByInvoiceBatchNumber(invoiceEntity);
            /*更新数据库中的数据,将不重复的发票更新状态*/
            for (int i=0;i<invoiceEntityList.size();i++){
                String code = invoiceEntityList.get(i).getInvoiceCode();
                String number = invoiceEntityList.get(i).getInvoiceNumber();
                Integer num = invoiceService.getOAInvoiceList(code,number);
                if (num>=2){
                    //根据发票号码和发票代码更新发票重复状态
                    invoiceService.updateRepeat(invoiceEntity.getInvoiceCode(),invoiceEntity.getInvoiceNumber(),"1");
                }else {
                    //根据发票号码和发票代码更新发票重复状态
                    invoiceService.updateRepeat(invoiceEntity.getInvoiceCode(),invoiceEntity.getInvoiceNumber(),"0");
                }
            }
            List<String> invoiceIds=new ArrayList<>();
            InputInvoiceMaterialEntity invoiceMaterialEntity=new InputInvoiceMaterialEntity();
            for(int i=0;i<invoiceEntityList.size();i++){
                invoiceIds.add(String.valueOf(invoiceEntityList.get(i).getId()));
            }
            invoiceMaterialEntity.setInvoiceIds(invoiceIds);
            invoiceMaterialService.deleteInvoiceMaterialByInvoiceId(invoiceMaterialEntity);
            InputInvoiceMaterialSapEntity invoiceMaterialSapEntity=new InputInvoiceMaterialSapEntity();
            invoiceMaterialSapEntity.setBatchId(String.valueOf(invoiceBatchEntity.getId()));
            invoiceMaterialSapService.deleteSapByInvoiceBatchId(invoiceMaterialSapEntity);
            InputInvoiceMaterial invoiceMaterial=new InputInvoiceMaterial();
            invoiceMaterial.setStatus("3");
            invoiceMaterial.setInvoiceBatchId(invoiceBatchEntity.getId());
            invoiceMaterialRpaService.deleteByBatchId(invoiceMaterial);
            InputInvoiceVoucherEntity invoiceVoucherEntity=new InputInvoiceVoucherEntity();
            invoiceVoucherEntity.setInvoiceBatchNumber(invoiceBatchEntity.getId());
            invoiceVoucherService.deleteByBatchId(invoiceVoucherEntity);
        }catch(Exception e){
//            e.printStackTrace();
            R.error();
        }
    }


    @Override
    public void update(InputInvoiceBatchEntity invoiceBatchEntity) {
        this.baseMapper.update(invoiceBatchEntity);
    }

    @Override
    public InputInvoiceBatchEntity get(InputInvoiceBatchEntity invoiceBatchEntity) {
        return this.baseMapper.get(invoiceBatchEntity);
    }


    @Override
    public void updateInvocieStatus(List<InputInvoiceMaterialEntity> invoiceMaterialList) {
        List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < invoiceMaterialList.size(); i++) {
            ids.add(invoiceMaterialList.get(i).getInvoiceId());
        }
        // 获取失效的发票明细的所属发票
        List<InputInvoiceEntity> invoiceEntityList = invoiceService.getListByIds(ids);
        for(int i = 0; i < invoiceEntityList.size(); i++) {
            InputInvoiceMaterialEntity materialEntity = new InputInvoiceMaterialEntity();
            materialEntity.setInvoiceId(invoiceEntityList.get(i).getId());
//            materialEntity.setStatus("1");
            List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = invoiceMaterialService.getByInvoiceId(materialEntity);
            int count = 0;
            for(int j = 0; j < invoiceMaterialEntityList.size(); j++) {
                if(invoiceMaterialEntityList.get(j).getStatus().equals("1")) {
                    count ++;
                }
            }
//            // 获取批次下的所有物料明细
//            InvoiceMaterialSapEntity materialSapEntity = new InvoiceMaterialSapEntity();
//            materialSapEntity.setBatchId(invoiceEntityList.get(0).getInvoiceBatchNumber());
//            List<InvoiceMaterialSapEntity> invoiceMaterialSapEntityList = invoiceMaterialSapDao.getListByBatchId(materialSapEntity);
            if(count == invoiceMaterialEntityList.size()) {
                invoiceEntityList.get(i).setInvoiceStatus("8");
                invoiceEntityList.get(i).setInvoiceErrorDescription("");
            } else if(count < invoiceMaterialEntityList.size() && count != 0) {
                invoiceEntityList.get(i).setInvoiceStatus("14");
            } else {
                invoiceEntityList.get(i).setInvoiceStatus("9");
            }
        }
        invoiceService.updateList(invoiceEntityList);
    }
    
    
    /**
     * 每次操作完成后对批次状态进行更新
     * @param invoiceEntityList
     */
    @Override
    public void setStatus(List<InputInvoiceEntity> invoiceEntityList) {
        List<Integer> integerList = new ArrayList<>();
        for(int i = 0; i < invoiceEntityList.size(); i++) {
            integerList.add(invoiceEntityList.get(i).getId());
        }
        // 获取批次下所有发票物料
        List<InputInvoiceMaterialEntity> invoiceMaterialEntities = invoiceMaterialService.getByInvoiceIds(integerList);
        int matchCount = 0;
        for(int i = 0; i < invoiceMaterialEntities.size(); i++) {
            if(StringUtils.isNotBlank(invoiceMaterialEntities.get(i).getStatus())) {
                if(invoiceMaterialEntities.get(i).getStatus().equals("1")) {
                    matchCount += 1;
                }
            }
        }
        InputInvoiceMaterialSapEntity materialSapEntity = new InputInvoiceMaterialSapEntity();
        materialSapEntity.setBatchId(invoiceEntityList.get(0).getInvoiceBatchNumber());
        List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList = invoiceMaterialSapService.getListByBatchId(materialSapEntity);
        int count = 0;
        for(int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            if(StringUtils.isNotBlank(invoiceMaterialSapEntityList.get(i).getMate())) {
                if(invoiceMaterialSapEntityList.get(i).getMate().equals("1")) {
                    count += 1;
                }
            }
        }
        InputInvoiceBatchEntity invoiceBatchEntity = new InputInvoiceBatchEntity();
        invoiceBatchEntity.setId(Integer.valueOf(invoiceEntityList.get(0).getInvoiceBatchNumber()));
        if(count == invoiceMaterialSapEntityList.size() && matchCount == invoiceMaterialEntities.size()) {
            invoiceBatchEntity.setInvoiceBatchStatus("1");
        } else if(count == 0 && matchCount == 0) {
            invoiceBatchEntity.setInvoiceBatchStatus("3");
        } else {
            invoiceBatchEntity.setInvoiceBatchStatus("2");
        }
        invoiceBatchService.update(invoiceBatchEntity);
    }

    @Override
    public boolean updateStatus(String batchId) {
        if (StrUtil.isNotBlank(batchId)) {
            InputInvoiceEntity inputInvoiceEntity = new InputInvoiceEntity();
            inputInvoiceEntity.setInvoiceBatchNumber(batchId);
            List<InputInvoiceEntity> invoiceEntityList = invoiceService.getListByBatchId(inputInvoiceEntity);
            Integer invoiceStatus = 100;
            for (InputInvoiceEntity invoice : invoiceEntityList) {
                Integer status = Integer.parseInt(invoice.getInvoiceStatus());
                if (status < invoiceStatus) {
                    invoiceStatus = status;
                }
            }
            //      后修改采购单状态。
//            InputInvoiceBatchEntity invoiceBatchEntity = invoiceBatchService.getById(batchId);
//            invoiceBatchEntity.setInvoiceBatchStatus(invoiceStatus+"");
//            return invoiceBatchService.updateById(invoiceBatchEntity);
        }
        return false;
    }

}

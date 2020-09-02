package com.pwc.modules.input.service.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.fapiao.neon.client.in.CheckInvoiceClient;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.inspect.*;
import com.fapiao.neon.param.in.InvoiceInspectionParamBody;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.*;
import com.pwc.common.utils.excel.ImportExcel;
import com.pwc.modules.input.entity.InputSapConfEntity;
import com.pwc.modules.input.service.InputSapConfEntityService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pwc.modules.input.dao.InputRedInvoiceDao;
import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import com.pwc.modules.input.service.InputRedInvoiceService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 红字发票服务实现
 *
 * @author fanpf
 * @date 2020/8/25
 */
@Service("inputRedInvoiceService")
@Slf4j
public class InputRedInvoiceServiceImpl extends ServiceImpl<InputRedInvoiceDao, InputRedInvoiceEntity> implements InputRedInvoiceService {

    @Autowired
    private InputSapConfEntityService inputSapConfEntityService;
    @Autowired
    private HttpUploadFile httpUploadFile;
    @Resource
    private CheckInvoiceClient checkInvoiceClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params),
                new QueryWrapper<InputRedInvoiceEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 红字通知单条件查询
     *
     * @param params 分页参数
     * @param redInvoiceEntity 条件参数
     * @return 查询结果
     */
    @Override
    public PageUtils conditionList(Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity) {
        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputRedInvoiceEntity>()
                        .orderByDesc("red_id")
                        .like(StringUtils.isNotBlank(redInvoiceEntity.getPurchaserCompany()), "purchaser_company", redInvoiceEntity.getPurchaserCompany())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedNoticeNumber()), "red_notice_number", redInvoiceEntity.getRedNoticeNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getBlueInvoiceNumber()), "blue_invoice_number", redInvoiceEntity.getBlueInvoiceNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()), "red_status", !StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()) ? "0" : redInvoiceEntity.getRedStatus())
        );
        return new PageUtils(page);
    }

    /**
     * 导入红字通知单
     *
     * @param file 待导入文件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importRedNotice(MultipartFile file) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<InputRedInvoiceEntity> entityList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        try {
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            String[] excelHead = {"红字通知单编号", "填写日期", "购方名称", "购方纳税人识别号", "销方名称",
                    "销方纳税人识别号", "发票总额", "不含税金额", "税额", "税率", "蓝字发票号码", "蓝字发票代码"};
            String [] excelHeadAlias = {"redNoticeNumber", "writeDate", "purchaserCompany", "purchaserTaxCode",
                    "sellCompany", "sellTaxCode", "totalPrice", "freePrice", "taxPrice", "taxRate", "blueInvoiceNumber", "blueInvoiceCode"};
            for (int i = 0; i < excelHead.length; i++) {
                reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
            }
            List<InputRedInvoiceEntity> dataList = reader.read(0, 1, InputRedInvoiceEntity.class);

            if(CollectionUtils.isEmpty(dataList)){
                log.error("上传的Excel为空,请重新上传");
                throw new RRException("上传的Excel为空,请重新上传");
            }
            total = dataList.size();
            for (InputRedInvoiceEntity redInvoiceEntity : dataList) {
                // 参数校验
                if(1 == checkExcel(redInvoiceEntity)){
                    // 参数有误
                    fail += 1;
                }else {
                    // 添加转义后的实体
                    redInvoiceEntity.setRedStatus("0");
                    redInvoiceEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                    redInvoiceEntity.setCreateTime(new Date());
                    entityList.add(redInvoiceEntity);
                }
            }
            resMap.put("total", total);
            resMap.put("success", entityList.size());
            resMap.put("fail", fail);
            super.saveBatch(entityList);

            return resMap;
        } catch (RRException e){
            throw e;
        } catch (Exception e) {
            log.error("导入红字通知单出错: {}", e);
            throw new RRException("导入红字通知单出现异常");
        }
    }


    /**
     * 接收红字发票并更新红字通知单状态
     *
     * @param file 待导入红字发票
     */
    @Override
    public void receiveRedInvoice(MultipartFile file) {
        // 获取文件类型
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        // 文件后缀校验
        if(!("jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix))){
            throw new RRException("上传文件只支持jpg,jpeg,png类型");
        }

        try {
            // 将文件保存在项目根路径
            String filePath = "statics/red/pic";
            UploadFileEntity fileEntity = UploadKitUtil.uploadFile(file, filePath, true, false);
            String fileName = fileEntity.getServerPath();
            // 调用OCR接口
            InputSapConfEntity sapConfEntity = inputSapConfEntityService.getOneById(7);
            String url = sapConfEntity.getPostUrl();
            String result = httpUploadFile.connectionOCR(url, fileName);
            // 接收到OCR返回后删除图片
            File file1 = new File(ClassUtil.getClassPath() + fileName);
            file1.delete();
            // 解析OCR返回内容
            JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
            if(StringUtils.isNotBlank(result)){
                if(result.contains("scan_result")){
                    JsonElement jsonElement = jsonObject.get("scan_result");
                    JsonObject content = jsonElement.getAsJsonObject();
                    String code = content.get("code").getAsString();
                    String number = content.get("number").getAsString();
                    String date = content.get("date").getAsString();
                    String checkCode = content.get("check_code").getAsString();
                    if(StringUtils.isNotBlank(checkCode) && checkCode.length() > 6){
                        checkCode = checkCode.substring(checkCode.length() - 6);
                    }
                    String totalAmount = content.get("total_amount_without_tax").getAsString();
                    // 发票验真获取备注的红字通知单编号
                    String redNoticeNumber = this.checkInvoice(code, number, date, totalAmount, checkCode);
                    // 根据红字通知单编号关联红票
                    InputRedInvoiceEntity redInvoiceEntity = super.getOne(
                            new QueryWrapper<InputRedInvoiceEntity>()
                                    .likeRight("red_notice_number", redNoticeNumber)
                    );
                    // 更改红字通知单的状态为: 红票已开
                    redInvoiceEntity.setRedStatus("1");
                    redInvoiceEntity.setRedInvoiceCode(code);
                    redInvoiceEntity.setRedInvoiceNumber(number);
                    redInvoiceEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                    redInvoiceEntity.setUpdateTime(new Date());
                    super.updateById(redInvoiceEntity);
                }else {
                    String errorMsg = jsonObject.get("errorMsg").getAsString();
                    String errorCode = jsonObject.get("errorCode").getAsString();
                    throw new RRException(errorMsg, Integer.valueOf(errorCode));
                }
            }else {
                log.error("OCR接口异常");
                throw new RRException("OCR接口异常");
            }
        } catch (Exception e) {
            log.error("导入红字发票出错: {}", e);
            throw new RRException("导入红字发票发生异常");
        }
    }

    /**
     * 红字发票监控条件查询
     *
     * @param params 分页参数
     * @param redInvoiceEntity 条件参数
     * @return 查询结果
     */
    @Override
    public PageUtils redList(Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity) {
        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputRedInvoiceEntity>()
                        .orderByDesc("red_id")
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedNoticeNumber()), "red_notice_number", redInvoiceEntity.getRedNoticeNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedInvoiceNumber()), "red_invoice_number", redInvoiceEntity.getRedInvoiceNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()), "red_status", !StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()) ? "0" : redInvoiceEntity.getRedStatus())
        );
        return new PageUtils(page);
    }

    /**
     * 发票验真获取发票备注信息
     *
     * @param invoiceCode 发票代码
     * @param invoiceNumber 发票号码
     * @param billingDate 开票日期
     * @param totalAmount 发票金额
     * @param checkCode 校验码
     * @return 发票备注信息
     */
    private String checkInvoice(String invoiceCode, String invoiceNumber, String billingDate, String totalAmount, String checkCode){
        InvoiceInspectionParamBody paramBody = new InvoiceInspectionParamBody();
        paramBody.setInvoiceCode(invoiceCode);
        paramBody.setInvoiceNumber(invoiceNumber);
        paramBody.setBillingDate(billingDate);
        paramBody.setTotalAmount(totalAmount);
        paramBody.setCheckCode(checkCode);

        // 初始化备注信息
        String remarks = "";
        try {
            CallResult<BaseInvoice> result = checkInvoiceClient.check(paramBody);
            BaseInvoice data = result.getData();
            // 根据发票类型,基类强转获取子类
            String invoiceType = data.getInvoiceType();
            if("01".equals(invoiceCode)){
                SpecialInvoiceInfo specialInvoiceInfo = (SpecialInvoiceInfo)data;
                remarks = specialInvoiceInfo.getRemarks();
            }else if("02".equals(invoiceCode)){
                TransportInvoiceInfo transportInvoiceInfo = (TransportInvoiceInfo)data;
                remarks = transportInvoiceInfo.getRemarks();
            }else if("03".equals(invoiceCode)){
                VehicleInvoiceInfo vehicleInvoiceInfo = (VehicleInvoiceInfo)data;
                log.info("此类发票没有备注字段,需进行手动关联");
            }else if("04".equals(invoiceCode)){
                NormalInvoiceInfo normalInvoiceInfo = (NormalInvoiceInfo)data;
                remarks = normalInvoiceInfo.getRemarks();
            }else if("10".equals(invoiceCode)){
                ENormalInvoiceInfo eNormalInvoiceInfo = (ENormalInvoiceInfo)data;
                remarks = eNormalInvoiceInfo.getRemarks();
            }else if("11".equals(invoiceCode)){
                RollInvoiceInfo rollInvoiceInfo = (RollInvoiceInfo)data;
                remarks = rollInvoiceInfo.getRemarks();
            }else if("14".equals(invoiceCode)){
                TollInvoiceInfo tollInvoiceInfo = (TollInvoiceInfo)data;
                remarks = tollInvoiceInfo.getRemarks();
            }else if("15".equals(invoiceCode)){
                UsedCarInvoiceInfo usedCarInvoiceInfo = (UsedCarInvoiceInfo)data;
                remarks = usedCarInvoiceInfo.getRemarks();
            }else if("20".equals(invoiceCode)){
                ESpecialInvoiceInfo eSpecialInvoiceInfo = (ESpecialInvoiceInfo)data;
                remarks = eSpecialInvoiceInfo.getRemarks();
            }
            // TODO 截取前16位为红字通知单编号
        } catch (Exception e) {
            log.error("发票验真出错: {}", e);
            throw new RRException("发票验真发生异常");
        }
        return remarks;
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(InputRedInvoiceEntity entity){
        if(StringUtils.isBlank(entity.getRedNoticeNumber()) || StringUtils.isBlank(entity.getPurchaserCompany()) ||
            StringUtils.isBlank(entity.getPurchaserTaxCode()) || StringUtils.isBlank(entity.getSellCompany()) ||
            StringUtils.isBlank(entity.getSellTaxCode()) || null == entity.getTotalPrice() ||
            null == entity.getFreePrice() || null == entity.getTaxPrice() || StringUtils.isBlank(entity.getTaxRate()) ||
            StringUtils.isBlank(entity.getBlueInvoiceNumber()) || StringUtils.isBlank(entity.getBlueInvoiceCode())){
            return 1;
        }
        return 0;
    }
}

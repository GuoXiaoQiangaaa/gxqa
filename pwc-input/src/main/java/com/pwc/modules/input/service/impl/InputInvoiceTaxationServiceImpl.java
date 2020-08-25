package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.InputConstant;
import com.pwc.modules.input.dao.InputInvoiceTaxationDao;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceSyncEntity;
import com.pwc.modules.input.entity.InputInvoiceTaxationEntity;
import com.pwc.modules.input.service.InputInvoiceSyncService;
import com.pwc.modules.input.service.InputInvoiceTaxationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author gxw
 * @date 2020/6/23 19:07
 */
@Service("")
public class InputInvoiceTaxationServiceImpl extends ServiceImpl<InputInvoiceTaxationDao, InputInvoiceTaxationEntity> implements InputInvoiceTaxationService {

    @Autowired
    private InputInvoiceSyncService invoiceSyncService;
    @Override
    public InputInvoiceTaxationEntity findDataByCreateDate(Integer invoiceId, String[] flag,String date) {
        QueryWrapper<InputInvoiceTaxationEntity> wrapper = new QueryWrapper<>();
        wrapper.in("taxation_flag", flag)
                .eq("invoice_id", invoiceId)
                .eq("taxation_data_flag","0")
                .likeRight("taxation_date", date);


        return baseMapper.selectOne(wrapper);

    }

    @Override
    public List<InputInvoiceTaxationEntity> findByFlag(String[] ids, String date) {
        QueryWrapper<InputInvoiceTaxationEntity> wrapper = new QueryWrapper<>();
        wrapper.in("taxation_flag", ids)
                .eq("taxation_data_flag","0")
                .likeRight("taxation_date", date);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void saveTaxation(InputInvoiceEntity invoice,String flag) {

        String date = DateUtils.format(new Date(), "yyyy-MM-dd").substring(0,7);
        //查询 1 5 数据是否存在
        String[] ids = {InputConstant.TaxationStats.BENRU_WEIREN.getValue(),
                InputConstant.TaxationStats.BENREN_WEIRU.getValue()};
        InputInvoiceTaxationEntity taxationEntity = this.findDataByCreateDate(invoice.getId(), ids,date);
        if (null!=taxationEntity&&InputConstant.TaxationStats.BENREN_WEIRU.getValue().equals(taxationEntity.getTaxationFlag()) && flag.equals(InputConstant.TaxationStats.BENRU_WEIREN.getValue())) { // 不存在 判断抵账库是否数据

            updateTaxation(invoice);
        }else if(null==taxationEntity) {
            taxationEntity = new InputInvoiceTaxationEntity();
            taxationEntity.setTaxationDataFlag(InputConstant.TaxationStats.NO.getValue());
            taxationEntity.setTaxationDate(new Date());
            taxationEntity.setInvoiceId(invoice.getId());
            taxationEntity.setInvoiceCode(invoice.getInvoiceCode()); // 发票代码
            taxationEntity.setInvoiceNumber(invoice.getInvoiceNumber()); // 发票号
            taxationEntity.setInvoiceTaxPrice(invoice.getInvoiceTaxPrice());//税额
            if(flag.equals(InputConstant.TaxationStats.BENRU_WEIREN.getValue())){ //入账
                taxationEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue());// 发票状态
                taxationEntity.setTaxationFlag(InputConstant.TaxationStats.BENRU_WEIREN.getValue());// 本月入账未认证
                taxationEntity.setEntryDate(invoice.getEntryDate());// 入账日期
                this.save(taxationEntity);
            }else if(flag.equals(InputConstant.TaxationStats.BENREN_WEIRU.getValue())){ // 认证
                // 查询抵账库
                InputInvoiceSyncEntity invoiceSyncEntity = invoiceSyncService.findInvoiceSync(invoice);
                if(null!=invoiceSyncEntity){
                    taxationEntity.setTaxationDate(DateUtils.stringToDate(invoiceSyncEntity.getDeductibleDate(), "yyyy-MM-dd"));
                    taxationEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue());// 发票状态
                    taxationEntity.setTaxationFlag(InputConstant.TaxationStats.BENREN_WEIRU.getValue());// 本月认证未入账
                    taxationEntity.setInvoiceAuthDate(invoice.getInvoiceAuthDate()); // 认证日期
                    this.save(taxationEntity);
                }
            }

        }
    }

    @Override
    public void updateTaxation(InputInvoiceEntity invoice) {
        // 如果根据主发票id查询。标志位为3或者7的数据。生成一条标志为4或者8的数据
        String[] ids = {InputConstant.TaxationStats.QIANRU_WEIREN.getValue(),
                InputConstant.TaxationStats.QIANREN_WEIRU.getValue()};
        String Date = DateUtils.format(new Date(), "yyyy-MM-dd");
        String date = Date.substring(0,7);
        InputInvoiceTaxationEntity taxationEntity = this.findDataByCreateDate(invoice.getId(), ids,date);
        if (taxationEntity != null) {
            String[] id =null;
            taxationEntity.setTaxationDate(new Date());
            taxationEntity.setInvoiceStatus(InputConstant.InvoiceStatus.FINISHED.getValue());// 发票状态
            taxationEntity.setTaxationDataFlag(InputConstant.TaxationStats.YES.getValue());
            if(taxationEntity.getTaxationFlag().equals(InputConstant.TaxationStats.QIANRU_WEIREN.getValue())){
                taxationEntity.setTaxationFlag(InputConstant.TaxationStats.QIANRU_BENREN.getValue());// 4
                id = new String[] {
                    InputConstant.TaxationStats.BENRU_WEIREN.getValue(),//1
                    InputConstant.TaxationStats.QIANRU_WEIREN.getValue(),//3
                };
            }else if(taxationEntity.getTaxationFlag().equals(InputConstant.TaxationStats.QIANREN_WEIRU.getValue())){
                taxationEntity.setTaxationFlag(InputConstant.TaxationStats.QIANEN_BENRU.getValue());// 8
                id = new String[] {
                        InputConstant.TaxationStats.BENREN_WEIRU.getValue(),//5
                        InputConstant.TaxationStats.QIANREN_WEIRU.getValue() //7
                };
            }
            this.save(taxationEntity);
            if(null!=id){
                baseMapper.updateByFlag(taxationEntity.getInvoiceId(),id);
            }
        } else {
            // 没有就查询标志位为1或者5的数据。有标志更新为2或者6
            ids = new String[] {InputConstant.TaxationStats.BENRU_WEIREN.getValue(),
                    InputConstant.TaxationStats.BENREN_WEIRU.getValue()};
            taxationEntity = this.findDataByCreateDate(invoice.getId(), ids,date);
            if (taxationEntity != null) {
                taxationEntity.setTaxationDate(new Date());
                if (taxationEntity.getTaxationFlag().equals(InputConstant.TaxationStats.BENRU_WEIREN.getValue())) {
                    taxationEntity.setTaxationFlag(InputConstant.TaxationStats.BENRU_BENREN.getValue());// 2
                } else if (taxationEntity.getTaxationFlag().equals(InputConstant.TaxationStats.BENREN_WEIRU.getValue())) {
                    taxationEntity.setTaxationFlag(InputConstant.TaxationStats.BENREN_BENRU.getValue());// 6
                }
                taxationEntity.setInvoiceStatus(InputConstant.InvoiceStatus.FINISHED.getValue());// 发票状态
                taxationEntity.setTaxationDataFlag(InputConstant.TaxationStats.YES.getValue());
                this.updateById(taxationEntity);
            }else {
                saveOldData(Date);
                updateTaxation(invoice);
            }
        }
    }

    @Override
    public void saveReason(Map<String, Object> params) {
        String date = params.get("queryDate").toString(); // 时间
        String reason = params.get("reason").toString(); // 原因
        String accountNumber = params.get("accountNumber").toString();// 科目编号
        String accountDescription = params.get("accountDescription").toString();      // 科目描述
        String tax = params.get("tax").toString();
        if (null != reason&&null!=accountNumber) {
            // 根据时间查询调整原因查询
            System.out.println("时间查询:" + date.substring(0, 7));
            InputInvoiceTaxationEntity taxationEntity = this.getOne(
                    new QueryWrapper<InputInvoiceTaxationEntity>().eq("taxation_flag", InputConstant.TaxationStats.REASON.getValue())
                            .eq("account_number", accountNumber)
                            .eq("adjustment_reason", reason)
                            .eq("adjustment_tax", "")
                            .likeRight("taxation_date", date.substring(0, 7))
            );
            if (taxationEntity != null) {
                if (null != taxationEntity.getAccountNumber() && null != taxationEntity.getAccountDescription()
                        && (null == taxationEntity.getAdjustmentTax() || "".equals(taxationEntity.getAdjustmentTax()))) {
                    taxationEntity.setAccountNumber(accountNumber);
                    taxationEntity.setAccountDescription(accountDescription);
                    taxationEntity.setAdjustmentTax(tax);
                    this.updateById(taxationEntity);
                } else {
                    taxationEntity = new InputInvoiceTaxationEntity();
                    taxationEntity.setTaxationDate(taxationEntity.getTaxationDate());
                    taxationEntity.setTaxationFlag(InputConstant.TaxationStats.REASON.getValue());
                    taxationEntity.setTaxationDataFlag(InputConstant.TaxationStats.NO.getValue());
                    taxationEntity.setAccountNumber(accountNumber);
                    taxationEntity.setAccountDescription(accountDescription);
                    taxationEntity.setAdjustmentReason(reason);
                    this.save(taxationEntity);
                }
            } else {
                taxationEntity = new InputInvoiceTaxationEntity();
                taxationEntity.setTaxationDate(DateUtils.stringToDate(date, "yyyy-MM"));
                taxationEntity.setTaxationFlag(InputConstant.TaxationStats.REASON.getValue());
                taxationEntity.setTaxationDataFlag(InputConstant.TaxationStats.NO.getValue());
                taxationEntity.setAccountNumber(accountNumber);
                taxationEntity.setAccountDescription(accountDescription);
                taxationEntity.setAdjustmentReason(reason);
                taxationEntity.setAdjustmentTax(tax);
                this.save(taxationEntity);
                System.out.println(taxationEntity.getId());
            }
        }
    }



    /*
    *查询当前日期之前的 1和5的数据
     */
    public InputInvoiceTaxationEntity findDataByFalg(Integer invoiceId, String[] flag){
        QueryWrapper<InputInvoiceTaxationEntity> wrapper = new QueryWrapper<>();
        wrapper.in("taxation_flag", flag)
                .eq("invoice_id", invoiceId)
                .eq("taxation_data_flag","0");
        return baseMapper.selectOne(wrapper);

    }

    @Override
    public List<InputInvoiceTaxationEntity> findByFlagNoData(String[] ids, String date) {
        QueryWrapper<InputInvoiceTaxationEntity> wrapper = new QueryWrapper<>();
        wrapper.in("taxation_flag", ids)
                .likeRight("taxation_date", date);
        return baseMapper.selectList(wrapper);
    }
    /*
    * 前期处理遗留数据
    * */
    @Override
    public void saveOldData(String Date) {
        // 处理前月数据的
 //       String Date = DateUtils.format(new Date(), "yyyy-MM-dd").substring(0,7);
        String DateBefore =  DateUtils.format(DateUtils.addDateMonths(DateUtils.stringToDate(Date,"yyyy-MM-dd"),-1),"yyyy-MM-dd");
        // 原来就有的数据
        String[] ids = {InputConstant.TaxationStats.BENRU_WEIREN.getValue(),InputConstant.TaxationStats.BENREN_WEIRU.getValue(),
                InputConstant.TaxationStats.BENRU_WEIREN.getValue(),InputConstant.TaxationStats.QIANREN_WEIRU.getValue()};// 1,5,3,7
        String[] id = {InputConstant.TaxationStats.BENRU_WEIREN.getValue(),InputConstant.TaxationStats.QIANREN_WEIRU.getValue()};// 3或者7
        List<InputInvoiceTaxationEntity> taxationEntityLista = this.findByFlag(id, Date.substring(0,7));
        if("0".equals(taxationEntityLista.size())){ // 查询本月有没有标志未3和7的数据如果有说明已经处理过了上个月的数据了
            // 本月没有3 和7 的数据，就开始处理 首先查出来上月的 3 7 1 5
            List<InputInvoiceTaxationEntity> taxationEntityList = this.findByFlag(ids, DateBefore.substring(0,7));
            for (InputInvoiceTaxationEntity invoiceTaxationEntity : taxationEntityList) {
                InputInvoiceTaxationEntity taxationEntitya = this.findDataByCreateDate(
                        invoiceTaxationEntity.getInvoiceId(),id,DateBefore.substring(0,7));
                if (null != taxationEntitya) {
                    taxationEntitya.setTaxationDate(DateUtils.stringToDate(Date, "yyyy-MM-dd"));
                    this.saveOrUpdate(taxationEntitya);
                } else {
                    InputInvoiceTaxationEntity taxationEntitye = new InputInvoiceTaxationEntity();
                    BeanUtils.copyProperties(invoiceTaxationEntity, taxationEntitye);
                    taxationEntitye.setTaxationDate(DateUtils.stringToDate(Date, "yyyy-MM-dd"));
                    if(invoiceTaxationEntity.getTaxationFlag().equals(InputConstant.TaxationStats.BENRU_WEIREN.getValue())){
                        taxationEntitye.setTaxationFlag(InputConstant.TaxationStats.QIANRU_WEIREN.getValue());//3
                    }else if(invoiceTaxationEntity.getTaxationFlag().equals(InputConstant.TaxationStats.BENREN_WEIRU.getValue())){
                        taxationEntitye.setTaxationFlag(InputConstant.TaxationStats.QIANREN_WEIRU.getValue());//7
                    }
                    taxationEntitye.setTaxationDataFlag(InputConstant.TaxationStats.NO.getValue());
                    this.save(taxationEntitye);
                }
            }
        }
    }

    @Override
    public List<InputInvoiceTaxationEntity> findByEntity(String invoiceNumber,String invoiceCode,String voucherNumber) {
        QueryWrapper<InputInvoiceTaxationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(invoiceNumber!= null && !"".equals(invoiceNumber), "invoice_number", invoiceNumber)
                .eq(invoiceCode!= null && !"".equals(invoiceCode), "invoice_code", invoiceNumber)
                .eq(voucherNumber!= null && !"".equals(voucherNumber), "voucher_number", invoiceNumber);
        return baseMapper.selectList(wrapper);
    }


}

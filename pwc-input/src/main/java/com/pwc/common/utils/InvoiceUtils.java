package com.pwc.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputUnformatInvoiceEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class InvoiceUtils {


    public InputInvoiceEntity saveHEHEVATInvoice(InputInvoiceEntity invoiceEntity, JsonArray itemList, String VATType, String invoiceFromTo) {

        List<Integer> invoiceNoList = new ArrayList<>();

        for (JsonElement jsonElement : itemList) {
            JsonObject obj = jsonElement.getAsJsonObject();
            String key = obj.get("key").getAsString();
            String value = obj.get("value").getAsString();
            if (key.equals("vat_invoice_type")) {
                if (value.equals("电子发票")) {
                    invoiceEntity.setInvoiceType("电子");
                } else {
                    invoiceEntity.setInvoiceType("纸质");
                }
            }
            if (VATType.equals("vat_special_invoice")) {
                invoiceEntity.setInvoiceEntity("专用发票");

            }
            if (VATType.equals("vat_electronic_invoice") || VATType.equals("vat_common_invoice")) {
                invoiceEntity.setInvoiceEntity("普通发票");

            }
            invoiceEntity.setInvoiceUploadType("1");
            if (key.equals("vat_invoice_daima")) {
                invoiceEntity.setInvoiceCode(value);
            }
            if (key.equals("vat_invoice_haoma")) {
                invoiceEntity.setInvoiceNumber(value);
            }

            if (key.equals("vat_invoice_total_cover_tax_digits")) {
                if (!StrUtil.isNullOrUndefined(value) && StrUtil.isNotBlank(value)) {
                    invoiceEntity.setInvoiceTotalPrice(new BigDecimal(value));
                }

            }
            if (key.equals("vat_invoice_issue_date")) {
                if (!StrUtil.isNullOrUndefined(value) && StrUtil.isNotBlank(value)) {
                    String date = DateUtil.format(DateUtil.parseDate(value), "yyyy-MM-dd");
                    invoiceEntity.setInvoiceCreateDate(date);
                }
            }
            if (key.equals("vat_invoice_seller_id")) {
                invoiceEntity.setInvoiceSellParagraph(value);
            }
            if (key.equals("vat_invoice_seller_addr_tell")) {
                invoiceEntity.setInvoiceSellAddress(value);
            }
            if (key.equals("vat_invoice_seller_bank_account")) {
                invoiceEntity.setInvoiceSellBankAccount(value);
            }
            if (key.equals("vat_invoice_seller_name")) {
                invoiceEntity.setInvoiceSellCompany(value);
            }
            if (key.equals("vat_invoice_rate_payer_id")) {
                invoiceEntity.setInvoicePurchaserParagraph(value);
            }
            if (key.equals("vat_invoice_payer_addr_tell")) {
                invoiceEntity.setInvoicePurchaserAddress(value);
            }
            if (key.equals("vat_invoice_payer_bank_account")) {
                invoiceEntity.setInvoicePurchaserBankAccount(value);
            }
            if (key.equals("vat_invoice_payer_name")) {
                invoiceEntity.setInvoicePurchaserCompany(value);
            }
            if (key.equals("vat_invoice_correct_code")) {
                invoiceEntity.setInvoiceCheckCode(value);
            }
            if (key.equals("vat_invoice_total")) {
                if (!StrUtil.isNullOrUndefined(value) && StrUtil.isNotBlank(value)) {
                    invoiceEntity.setInvoiceFreePrice(new BigDecimal(value));
                }
            }
            if (key.equals("vat_invoice_tax_total")) {
                if (!StrUtil.isNullOrUndefined(value) && StrUtil.isNotBlank(value)) {
                    invoiceEntity.setInvoiceTaxPrice(new BigDecimal(value));
                }
            }

        }
        invoiceEntity.setInvoiceFromto(invoiceFromTo);

        invoiceEntity.setInvoiceStatus("2");
        return invoiceEntity;
    }


    public InputUnformatInvoiceEntity saveHEHEUnformatInvoide(InputUnformatInvoiceEntity unformatInvoiceEntity,
                                                              JsonArray itemList) {
        unformatInvoiceEntity.setContent(itemList.toString());

        for (JsonElement jsonElement : itemList) {
            JsonObject obj = jsonElement.getAsJsonObject();
            String key = obj.get("key").getAsString();
            String value = obj.get("value").getAsString();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            unformatInvoiceEntity.setCreateTime(sdf2.format(new Date()));

            if (key.equals("money_small")) {
                value = value.trim().equals("") ? "0" : value;
                unformatInvoiceEntity.setAmount(new BigDecimal(value));
            }
            if (key.equals("sum")) {
                value = value.trim().equals("") ? "0" : value;

                unformatInvoiceEntity.setAmount(new BigDecimal(value));
            }
            if (key.equals("price")) {
                value = value.trim().equals("") ? "0" : value;

                unformatInvoiceEntity.setAmount(new BigDecimal(value));
            }
            if (key.equals("fare")) {
                value = value.trim().equals("") ? "0" : value;

                unformatInvoiceEntity.setAmount(new BigDecimal(value));
            }
            if (key.equals("money")) {
                value = value.trim().equals("") ? "0" : value;

                unformatInvoiceEntity.setAmount(new BigDecimal(value));
            }
            if (key.equals("amount_small")) {
                value = value.trim().equals("") ? "0" : value;

                unformatInvoiceEntity.setAmount(new BigDecimal(value));
            }
            if (key.equals("tax_total")) {
                value = value.trim().equals("") ? "0" : value;

                unformatInvoiceEntity.setAmount(new BigDecimal(value));
            }


        }
        return unformatInvoiceEntity;

    }

    /**
     * list
     * @param menuType
     * @param invoiceStatus
     * @return
     */
    public static List<String> invoiceStatusList(String menuType, String invoiceStatus){
        List<String> statusList = new ArrayList<>();
        if (StrUtil.isNotBlank(menuType)) {
            // 1.管理 2.验真 3.三单匹配 4.入账 5.异常 6.认证
            if (InputConstant.MenuType.MANAGE.getValue().equals(menuType)) {
                if (StrUtil.isNotBlank(invoiceStatus)) {
                    statusList = new ArrayList<String>(){{
                        add(invoiceStatus);
                    }};
                }
            } else if (InputConstant.MenuType.CHECK_TRUE.getValue().equals(menuType)) {
                statusList = new ArrayList<String>(){{
                    add("3");
                    add("4");
                }};
            } else if (InputConstant.MenuType.TRIPLE_MATCH.getValue().equals(menuType)) {
                statusList = new ArrayList<String>(){{
                    add("5");
                    add("6");
                }};
            } else if (InputConstant.MenuType.ENTER.getValue().equals(menuType)) {
                statusList = new ArrayList<String>(){{
                    add("7");
                    add("8");
                }};
            } else if (InputConstant.MenuType.ABNORMAL.getValue().equals(menuType)) {
                statusList = new ArrayList<String>(){{
                    add("-1");
                    add("-2");
                    add("-3");
                    add("-4");
                    add("-5");
                }};
            } else if (InputConstant.MenuType.VERIFY.getValue().equals(menuType)){
                statusList = new ArrayList<String>(){{
                    add("9");
                    add("10");
                    add("12");
                    add("13");
                }};
            }
        }
        return statusList;
    }

    /**
     * sql
     * @param menuType
     * @param invoiceStatus
     * @return
     */
    public static String invoiceStatusSql(String menuType, String invoiceStatus, String tableAlias){
        String alias = "";
        if (StrUtil.isNotBlank(tableAlias)) {
            alias = tableAlias+".";
        }
        String statusSql = null;
        if (StrUtil.isNotBlank(menuType)) {
            // 1.管理 2.验真 3.三单匹配 4.入账 5.异常
            if (InputConstant.MenuType.MANAGE.getValue().equals(menuType)) {
                if (StrUtil.isNotBlank(invoiceStatus)) {
                    statusSql = alias + "invoice_status in ("+invoiceStatus+")";
                }
            } else if (InputConstant.MenuType.CHECK_TRUE.getValue().equals(menuType)) {
                statusSql = alias + "invoice_status in (3,4)";
            } else if (InputConstant.MenuType.TRIPLE_MATCH.getValue().equals(menuType)) {
                statusSql = alias + "invoice_status in (5,6)";
            } else if (InputConstant.MenuType.ENTER.getValue().equals(menuType)) {
                statusSql = alias + "invoice_status in (7,8)";
            } else if (InputConstant.MenuType.ABNORMAL.getValue().equals(menuType)) {
                statusSql = alias + "invoice_status in (-1, -2, -3, -4, -5)";
            }
        }
        return statusSql;
    }
}

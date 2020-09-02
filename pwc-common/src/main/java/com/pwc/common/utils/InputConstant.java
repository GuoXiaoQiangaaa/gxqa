package com.pwc.common.utils;

import com.google.common.collect.Maps;

import java.util.Map;


public class InputConstant {

    /**
     * 字典
     */
    private static Map<String, Map<String, String>> ALL = Maps.newHashMap();

    private static Map<String, String> STATUS_MAP = Maps.newHashMap();

    private static Map<String, String> BOOLEAN_MAP = Maps.newHashMap();

    private static Map<String, String> LOOKUP_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> LOOKUP_STATUS_MAP = Maps.newHashMap();

    private static Map<String, String> CARRY_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> DANGER_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> JOB_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> VIOLATION_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> REPAIR_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> MAINTAIN_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> MAINTAIN_STATUS_MAP = Maps.newHashMap();

    private static Map<String, String> PROCEDURE_STATUS_MAP = Maps.newHashMap();

    private static Map<String, String> PROCEDURE_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> INVOICE_TYPE_MAP = Maps.newHashMap();

    private static Map<String, String> INVOICE_STATUS_MAP = Maps.newHashMap();

    private static Map<String, String> INVOICE_ENTITY_MAP = Maps.newHashMap();

    private static Map<String, String> INVOICE_FROMTO_MAP = Maps.newHashMap();

    private static Map<String, String> INVOICE_UPLOADTYPE_MAP = Maps.newHashMap();

    private static Map<String, String> INVOICE_VERIFYTRYTH_MAP = Maps.newHashMap();

    private static Map<String, String> TOLERANCE_FLAG_MAP = Maps.newHashMap();



    static {
        STATUS_MAP.put("1", "正常");
        STATUS_MAP.put("2", "异常");
        ALL.put("status", STATUS_MAP);

        LOOKUP_TYPE_MAP.put("1", "巡查");
        LOOKUP_TYPE_MAP.put("2", "暗查");
        ALL.put("lookupType", LOOKUP_TYPE_MAP);

        BOOLEAN_MAP.put("1", "是");
        BOOLEAN_MAP.put("2", "否");
        ALL.put("boolean", BOOLEAN_MAP);

        LOOKUP_STATUS_MAP.put("1", "良好");
        LOOKUP_STATUS_MAP.put("2", "正常");
        LOOKUP_STATUS_MAP.put("3", "整改");
        LOOKUP_STATUS_MAP.put("4", "违规");
        ALL.put("lookupStatus", LOOKUP_STATUS_MAP);

        CARRY_TYPE_MAP.put("1", "双肩包");
        CARRY_TYPE_MAP.put("2", "开口带");
        ALL.put("carryType", CARRY_TYPE_MAP);

        JOB_TYPE_MAP.put("1", "引导岗");
        JOB_TYPE_MAP.put("2", "检测岗");
        JOB_TYPE_MAP.put("3", "开包岗");
        ALL.put("jobType", JOB_TYPE_MAP);

        VIOLATION_TYPE_MAP.put("1", "未拦截");
        VIOLATION_TYPE_MAP.put("2", "未发现危险品");
        VIOLATION_TYPE_MAP.put("3", "未按规定人工检测");
        ALL.put("violationType", VIOLATION_TYPE_MAP);

        DANGER_TYPE_MAP.put("1", "1.25升大容量液体两瓶");
        DANGER_TYPE_MAP.put("2", "高升1组");
        DANGER_TYPE_MAP.put("3", "1000响鞭炮");
        DANGER_TYPE_MAP.put("4", "管制刀具");
        DANGER_TYPE_MAP.put("5", "仿真枪");
        ALL.put("dangerType", DANGER_TYPE_MAP);

        REPAIR_TYPE_MAP.put("0", "已报修");
        REPAIR_TYPE_MAP.put("1", "报修通过");
        REPAIR_TYPE_MAP.put("2", "报修驳回");
        REPAIR_TYPE_MAP.put("3", "已维修");
        REPAIR_TYPE_MAP.put("4", "维修驳回");
        REPAIR_TYPE_MAP.put("5", "维修完毕");
        REPAIR_TYPE_MAP.put("6", "保存");
        ALL.put("repairType", REPAIR_TYPE_MAP);

        MAINTAIN_TYPE_MAP.put("0", "季保");
        MAINTAIN_TYPE_MAP.put("1", "年保");
        ALL.put("maintainType", MAINTAIN_TYPE_MAP);

        MAINTAIN_STATUS_MAP.put("0", "未审核");
        MAINTAIN_STATUS_MAP.put("1", "已审核");
        ALL.put("maintainStatus", MAINTAIN_STATUS_MAP);

        PROCEDURE_TYPE_MAP.put("1", "病假");
        PROCEDURE_TYPE_MAP.put("2", "事假");
        PROCEDURE_TYPE_MAP.put("3", "公休");
        ALL.put("procedureType", PROCEDURE_TYPE_MAP);

        PROCEDURE_STATUS_MAP.put("1", "待审核");
        PROCEDURE_STATUS_MAP.put("2", "审核中");
        PROCEDURE_STATUS_MAP.put("3", "审核通过");
        PROCEDURE_STATUS_MAP.put("4", "审核驳回");
        ALL.put("procedureStatus", PROCEDURE_STATUS_MAP);

        INVOICE_TYPE_MAP.put("1", "专票");
        INVOICE_TYPE_MAP.put("2", "普票");
        ALL.put("invoiceType", INVOICE_TYPE_MAP);

        INVOICE_STATUS_MAP.put("1", "未识别");
        INVOICE_STATUS_MAP.put("2", "已识别");
        INVOICE_STATUS_MAP.put("3", "部分缺失");
        INVOICE_STATUS_MAP.put("4", "识别失败");
        INVOICE_STATUS_MAP.put("5", "已退票");
        ALL.put("invoiceStatus", INVOICE_STATUS_MAP);

        INVOICE_ENTITY_MAP.put("1", "纸票");
        INVOICE_ENTITY_MAP.put("2", "电子发票");
        ALL.put("invoiceEntity", INVOICE_ENTITY_MAP);

        INVOICE_FROMTO_MAP.put("1", "原料采集");
        INVOICE_FROMTO_MAP.put("2", "非集中采集");
        INVOICE_FROMTO_MAP.put("3", "报销费用");
        ALL.put("invoiceFromto", INVOICE_FROMTO_MAP);

        INVOICE_UPLOADTYPE_MAP.put("1", "手动上传pdf");
        INVOICE_UPLOADTYPE_MAP.put("2", "扫描上传");
        ALL.put("invoiceUploadType", INVOICE_UPLOADTYPE_MAP);

        INVOICE_VERIFYTRYTH_MAP.put("1", "自动验真");
        INVOICE_VERIFYTRYTH_MAP.put("2", "手动验真");
        ALL.put("invoiceVerifyTruth", INVOICE_VERIFYTRYTH_MAP);

        TOLERANCE_FLAG_MAP.put("0","待调差");
        TOLERANCE_FLAG_MAP.put("1","调差中");
        TOLERANCE_FLAG_MAP.put("2","调差完成");
        ALL.put("toleranceFlag", TOLERANCE_FLAG_MAP);



    }

    public static String getValue(String key, String type) {
        return ALL.get(type).get(key);
    }


    public enum MenuType {

        /**
         * 管理
         */
        MANAGE("1"),
        /**
         * 验真
         */
        CHECK_TRUE("2"),
        /**
         * 三单匹配
         */
        TRIPLE_MATCH("3"),
        /**
         * 入账
         */
        ENTER("4"),
        /**
         * 异常
         */
        ABNORMAL("5"),
        /**
         * 认证
         */
        VERIFY("6");

        private String value;

        MenuType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 发票状态
     */
    public enum InvoiceStatus {

        /**
         * drtu
         */
        RECOGNITION_FAILED("2"),
        /**
         * 待验真（识别成功）
         */
        PENDING_VERIFICATION("3"),
        /**
         * 验真失败
         */
        VERIFICATION_FAILED("4"),
        /**
         * 待匹配（验真成功）
         */
        PENDING_MATCHED("5"),
        /**
         * 匹配失败
         */
        MATCHING_FAILED("6"),
        /**
         * 待入账（匹配成功）
         */
        PENDING_ENTRY("7"),
        /**
         * 入账失败
         */
        ENTRY_FAILED("8"),
        /**
         * 待认证（入账成功）
         */
        PENDING_CERTIFIED("9"),
        /**
         * 认证中
         */
        CERTIFICATION("10"),
        /**
         * 撤销认证
         */
        UNDO_CERTIFICATION("11"),
        /**
         * 认证成功
         */
        SUCCESSFUL_AUTHENTICATION("12"),
        /**
         * 认证失败
         */
        AUTHENTICATION_FAILED("13"),
        /**
         * 完成
         */
        FINISHED("18"),
        /**
         * 异常（重复（OCR识别）
         */
        REPEAT("-1"),
        /**
         * 退票（入账前）
         */
        REFUND("-2"),
        /**
         * 删除（入账前）
         */
        DELETE("-3"),
        /**
         * 冲销（入账前）
         */
        CHARGE_AGAINST("-4"),
        /**ii
         * 串号（OCR识别/发票号码和发票代码不一致）
         */
        DIFFERENCE("-5"),
        /**
         * 红冲
         */
        REVERSE("-6"),
        /**
         * 作废
         */
        INVALID("-7")

        ;


        private String value;

        InvoiceStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /*
    *
    * */

    public enum TaxationStats {
        BENRU_WEIREN("1"),// 1 本月入账未认证

        BENRU_BENREN("2"),// 2 本月入账认证

        QIANRU_WEIREN("3"),//3 前月入账未认证

        QIANRU_BENREN("4"),//4 前月入账本月认证

        BENREN_WEIRU("5"),//5 本月认证未入账

        BENREN_BENRU("6"),//6 本月认证入账

        QIANREN_WEIRU("7"),//7 前月认证未入账
        
        QIANEN_BENRU("8"),//8 前月认证本月入账

        REASON("9"), //9 调整原因

        TOLERANCE("10"),//设置容差数据

        FLAG_TAXATION("1"),// 1 页面展示打√

        YES("1"), // 数据处理标志
         NO("0")
        ;
        private  String value;

        TaxationStats(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}

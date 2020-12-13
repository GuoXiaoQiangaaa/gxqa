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
    private static Map<String, String> UPDOLD_STATE = Maps.newHashMap();
    private static Map<String, String> INVOICE_ENTITY = Maps.newHashMap();
    private static Map<String, String> GOLDENTAX_STATUS = Maps.newHashMap();
    private static Map<String, String> INVOICE_CLASS = Maps.newHashMap();

    private static Map<String, String> INVOICE_STYLE = Maps.newHashMap();
    private static Map<String, String> SAP_SORT = Maps.newHashMap();


    static {
        STATUS_MAP.put("1", "正常");
        STATUS_MAP.put("2", "异常");
        ALL.put("status", STATUS_MAP);

        SAP_SORT.put("0", "发票");
        SAP_SORT.put("1", "海关通知单");
        SAP_SORT.put("2", "红字通知单");
        ALL.put("sapSort", SAP_SORT);

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

        INVOICE_UPLOADTYPE_MAP.put("0", "抵账库同步");
        INVOICE_UPLOADTYPE_MAP.put("1", "扫描仪上传");
        INVOICE_UPLOADTYPE_MAP.put("2", "手工上传");
        ALL.put("invoiceUploadType", INVOICE_UPLOADTYPE_MAP);

        INVOICE_VERIFYTRYTH_MAP.put("1", "自动验真");
        INVOICE_VERIFYTRYTH_MAP.put("2", "手动验真");
        ALL.put("invoiceVerifyTruth", INVOICE_VERIFYTRYTH_MAP);

        TOLERANCE_FLAG_MAP.put("0","待调差");
        TOLERANCE_FLAG_MAP.put("1","调差中");
        TOLERANCE_FLAG_MAP.put("2","调差完成");
        ALL.put("toleranceFlag", TOLERANCE_FLAG_MAP);


        UPDOLD_STATE.put("0","未识别");
        UPDOLD_STATE.put("1","识别失败");
        UPDOLD_STATE.put("2","识别成功");
        UPDOLD_STATE.put("3","重复识别");
        ALL.put("UpdoldState", UPDOLD_STATE);

        INVOICE_ENTITY.put("1","增值税专用发票");
        INVOICE_ENTITY.put("4","增值税普通发票");
        INVOICE_ENTITY.put("10","增值税电子普通发票");
        ALL.put("invoiceEntity",INVOICE_ENTITY);

        GOLDENTAX_STATUS.put("0","正常");
        GOLDENTAX_STATUS.put("1","失控");
        GOLDENTAX_STATUS.put("2","作废");
        GOLDENTAX_STATUS.put("3","红冲");
        ALL.put("goldenTaxStatus",GOLDENTAX_STATUS);

        INVOICE_CLASS.put("0","NonPo Related");
        INVOICE_CLASS.put("1","MRKO");
        INVOICE_CLASS.put("2","DFU");
        INVOICE_CLASS.put("3","EDI");
        INVOICE_CLASS.put("4","R&D_外部");
        INVOICE_CLASS.put("5","IC_R&D");
        INVOICE_CLASS.put("6","IC_RRB");
        INVOICE_CLASS.put("7","IC_非R&D");
        INVOICE_CLASS.put("8","Red-letter VAT");
        INVOICE_CLASS.put("9","General");
        ALL.put("invoiceClass",INVOICE_CLASS);

        INVOICE_STYLE.put("0","其他");
        INVOICE_STYLE.put("1","蓝字发票");
        INVOICE_STYLE.put("2","红字发票");
        INVOICE_STYLE.put("3","PO发票");
        INVOICE_STYLE.put("4","TE发票");
        INVOICE_STYLE.put("5","AP发票");
        ALL.put("invoiceStyle", INVOICE_STYLE);

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
         * 识别失败
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
         * （验真成功）
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
         * 撤销认证中
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
         * 撤销认证失败
         */
        REVOKE_CERTIFICATION("14"),
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
         * 购方信息不一致
         */
        DIFFERENT_MESSAGE("-3"),
        /**
         *   购方信息错误
         */
        CHARGE_AGAINST("-4"),
        /**
         * PO信息缺失
         */
        DIFFERENCE("-5"),
        /**
         * 税率异常
         */
        REVERSE("-6"),
        /**
         * 作废
         */
        INVALID("-7"),
        /**
         * 初次验真失败
         */
        FIRST_VERIFICATION_FAILED("-8")
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
        // 1 本月入账未认证
        BENRU_WEIREN("1"),
        // 2 本月入账认证
        BENRU_BENREN("2"),
        //3 前月入账未认证
        QIANRU_WEIREN("3"),
        //4 前月入账本月认证
        QIANRU_BENREN("4"),
        //5 本月认证未入账
        BENREN_WEIRU("5"),
        //6 本月认证入账
        BENREN_BENRU("6"),
        //7 前月认证未入账
        QIANREN_WEIRU("7"),
        //8 前月认证本月入账
        QIANEN_BENRU("8"),
        //9 调整原因
        REASON("9"),
        //设置容差数据
        TOLERANCE("10"),
        // 1 页面展示打√
        FLAG_TAXATION("1"),
        // 数据处理标志
        YES("1"),
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
    public enum InvoiceStyle {
        // 其他
        NULL(0),
        // ap蓝
        BLUE(1),
        // ap 红
        RED(2),
        // po
        PO(3),
        // te
        TE(4),
        // AP
        AP(5)
        ;
        ;
        private  int value;

        InvoiceStyle(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    public enum UpdoldState {
        // 未识别
        NULL("0"),
        // 识别失败
        RECOGNITION_FAILED("1"),
        // 识别成功
        PENDING_VERIFICATION("2"),
        // 重复识别
        REPEAT("3"),
        ;
        private  String value;

        UpdoldState(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    // TODO 发票分类

    /**
     * 发票分类(0:NonPo Related; 1:MRKO; 2:DFU; 3:EDI; 4:R&D_外部; 5:IC_R&D; 6:IC_RRB; 7:IC_非R&D; 8:Red-letter VAT; 9:General)
     * @param Related
     */
    public enum InvoiceClass {
        /**  NonPo Related */
        NONPO_RELATED("0"),
        /** MRKO */
        MRKO("1"),
        /** DFU */
        DFU("2"),
        //  EDI
        EDI("3"),
        // R&D_外部
        RD_OUT("4"),
        // IC_R&D
        IC_RD("5"),
        // IC_RRB
        IC_RRB("6"),
        // IC_非R&D
        IC_NOTRD("7"),
        // Red-letter VAT
        RED_LETTER("8"),
        // General
        GENERAL("9"),
        ;
        private  String value;

        InvoiceClass(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
    /**
     *    1 增值税专用发票  4 增值税普通发票  10 增值税电子普通发票
     */
    public enum InvoiceEntity{
        SPECIAL("1"),
        AVERAGE("4"),
        ELECTRON_AVERAGE("10"),
        ;

        private  String value;

        InvoiceEntity(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
    /**
     *    -1识别失败 0识别重复  1识别成功 2匹配成功
     */
    public enum InvoicePo{
        FAIL("-1"),
        REPEAT("0"),
        SUCCESS("1"),
        MATCH("2")
        ;
        private  String value;
        InvoicePo(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
    /**
     * 金税发票状态0 正常，1 失控，2 作废，3 红冲
     */
    public enum GoldenTaxStatus{
        NORMAL("0"),
        OUT_OF_CONTROL("1"),
        INVALID("2"),
        RED_PUNCH("3"),
        ;
        private  String value;
        GoldenTaxStatus(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    /**
     * 通用状态0 否 1 是
     */
    public enum YesAndNo{
        NO("0"),
        YES("1"),
        ;
        private  String value;
        YesAndNo(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    /**
     * 匹配状态0 否 1 是 2 失败
     */
    public enum InvoiceMatch{
        MATCH_NO("0"),
        MATCH_YES("1"),
        MATCH_ERROR("2"),
        ;
        private  String value;
        InvoiceMatch(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

}

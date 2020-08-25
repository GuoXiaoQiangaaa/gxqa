package com.pwc.common.utils;

/**
 *
 * @author zk
 */
public class FilingConstants {
    /**
     * 设置节点状态
     */
    public enum NodeStatus {
        /**
         * 已修改
         */
        MODIFIED(1),
        /**
         * 未修改
         */
        UNMODIFIED(0);

        private int value;

        NodeStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }


    /**
     * 流程执行节点名称
     */
    public enum ProcessNodeName {
        /**
         * 上传
         */
        UPLOAD,
        /**
         * 报告确认
         */
        CONFIRM,
        /**
         * 申报
         */
        DECLARE,
        /**
         * 扣款
         */
        DEDUCTION
    }

    /**
     * 流程执行节点状态
     */
    public enum ProcessNodeStatus {
        /**
         * 已关闭
         */
        DISABLE(0),
        /**
         * 未开始
         */
        NOT_STARTED(1),
        /**
         * 未上传（待上传）
         */
        PENDING_UPLOAD(2),
        /**
         * 待确认
         */
        PENDING_CONFIRMED(3),
        /**
         * 待审核
         */
        PENDING_AUDIT(4),
        /**
         * 审核驳回
         */
        REJECTED(5),
        /**
         * 审核通过
         */
        AUDITED(7),
        /**
         * 已完成
         */
        FINISHED(6),
        ;

        private int value;

        ProcessNodeStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }



    /**
     * 通用否开启状态 1开启 0关闭
     */
    public enum FilingRecordStatus {
        /**
         * 已销毁
         */
        DESTROYED(-1),
        /**
         * 未开启
         */
        NOT_STARTED(0),
        /**
         * 处理中
         */
        PROCESSING(1),
        /**
         * 已完成
         */
        FINISHED(2)
        ;

        private int value;

        FilingRecordStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }


    /**
     * 通用否开启状态 1开启 0关闭
     */
    public enum CommonStatus {
        /**
         * 开启
         */
        ENABLED(1),
        /**
         * 关闭
         */
        DISABLED(0);

        private int value;

        CommonStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 税种
     */
    public enum Tax {

        /**
         * 个人所得税
         */
        VAT(1),
        /**
         * 印花税
         */
        SD(2),
        /**
         * 企业所得税
         */
        CIT(3),
        /**
         * 财报
         */
        FS(4),

        /**
         * 附加税
         */
        SUPERTAX(5)
        ;

        private int value;

        Tax(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 税种
     */
    public enum OperateLogType {

        /**
         * 文件上传
         */
        UPLOAD(1),
        /**
         * 申报提交
         */
        FILING(2),
        /**
         * 报告确认
         */
        COMFIRM(3),
        /**
         * 报告审核
         */
        AUDIT(4),
        /**
         * 申报
         */
        DECLARE(5),
        /**
         * 扣款
         */
        DEDUCTION(6),
        /**
         * 完成申报
         */
        FINISHED(7),
        /**
         * 销毁文件
         */
        DESTROYED(8),
        /**
         * 新建流程
         */
        CREATE(9),
        /**
         * 上传审核福建
         */
        UPLOAD_AUDIT(10),
        ;

        private int value;

        OperateLogType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 申报记录上传文件类型
     */
    public enum FilingFileType {

        /**
         * 申报上传
         */
        UPLOAD(1),
        /**
         * 报告确认
         */
        COMFIRM(2),
        /**
         * 申报
         */
        DECLARE(3),
        /**
         * 扣款
         */
        DEDUCTION(4),
        ;

        private int value;

        FilingFileType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 纳税人类型
     */
    public enum TaxPayerType {
        /**
         * 一般纳税人
         */
        GENERAL("2000010001"),
        /**
         * 小规模纳税人
         */
        SMALL("2000010002");

        private String value;
        TaxPayerType(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    /**
     * 纳税期限代码
     */
    public enum TaxPeriodCode {
        /**
         * 按期
         */
        SCHEDULE("06"),
        /**
         * 按次
         */
        TIME("11");

        private String value;
        TaxPeriodCode(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    /**
     * 会计准则
     */
    public enum AccountingStandards {
        /**
         * 企业会计准则
         */
        BUSINESS("2000020001"),
        /**
         * 小企业
         */
        SMALL("2000020002"),
        /**
         * 商业银行
         */
        BANK("2000020003"),
        /**
         * 保险公司
         */
        INSURANCE("2000020004"),
        /**
         * 证券公司
         */
        securities("2000020005"),
        /**
         * 担保企业会计核算办法
         */
        GUARANTEE("2000020006"),
        /**
         * 事业单位会计制度
         */
        INSTITUTIONAL("2000020007"),

        ;

        private String value;
        AccountingStandards(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}

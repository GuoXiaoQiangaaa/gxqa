package com.pwc.common.utils;

/**
 * 状态定义
 * @author zk
 */
public class StatusDefine {

    /**
     * 部门状态
     */
    public enum DeptStatus {
        /**
         * 禁用
         */
        DISABLE(0),
        /**
         * 启用
         */
        ENABLE(1);

        private int value;

        DeptStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    /**
     * 企业类型
     */
    public enum DeptType {
        /**
         * 总公司
         */
        HEAD_OFFICE(1),
        /**
         * 分公司
         */
        BRANCH_OFFICE(2);

        private int value;

        DeptType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 企业类型
     */
    public enum RoleIds {
        /**
         * super admin
         */
        ADMIN(1),
        /**
         * 集团管理员
         */
        GROUP_MANAGER(2),
        /**
         * 集团申报用户
         */
        GROUP_FILING_USER(3),
        /**
         * 集团审核用户
         */
        GROUP_AUDIT_USER(4),
        /**
         * 分公司管理员
         */
        BRANCH_MANAGER(5),

        /**
         * 分公司申报用户
         */
        BRANCH_FILING_USER(6),
        ;

        private long value;

        RoleIds(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }

    /**
     * 用户状态
     */
    public enum UserStatus {
        /**
         * 禁用
         */
        DISABLE(0),
        /**
         * 启用
         */
        ENABLE(1);

        private int value;

        UserStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

package com.pwc.common.third.request;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author zk
 * @email
 * @date 2019-11-18 18:34:05
 */
@Data
public class FilingVat implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long vatId;
    /**
     * 门店税号
     */
    private String storesEin;
    /**
     * 门店号
     */
    private String storesNo;
    /**
     * 所在省份
     */
    private String province;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 公司名称（简称）
     */
    private String companyName;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 进项明细-进项专用发票份数
     */
    private String jxmxJxzyfpfs;
    /**
     * 进项明细-进项销售额
     */
    private String jxmxJxxse;
    /**
     * 进项明细-进项税额
     */
    private String jxmxJxse;
    /**
     * 进项明细-进项转出税额（红字专用发票）
     */
    private String jxmxJxzcseHzzyfp;
    /**
     * 进项明细-进项转出税额（非正常损失）
     */
    private String jxmxJxzcseFzcss;
    /**
     * 进项明细-进项转出税额（集体福利个人消费）
     */
    private String jxmxJxzcseJtflgrxf;
    /**
     * 进项明细-进项转出税额（其他）
     */
    private String jxmxJxzcseQt;
    /**
     * 进项明细-增值税税控系统专用设备费及技术维护费
     */
    private String jxmxZzsskxtzysbfjjswhf;
    /**
     * 进项明细-非湖北地区小规模减征
     */
    private String jxmxFhbdqxgmjz;
    /**
     * 进项明细-旅客运输数量
     */
    private String jxmxLkyssl;
    /**
     * 进项明细-旅客运输金额
     */
    private String jxmxLkysje;
    /**
     * 进项明细-旅客运输税额
     */
    private String jxmxLkysse;
    /**
     * 进项合计-进项销售额
     */
    private String jxhjJxxse;
    /**
     * 进项合计-进项税额
     */
    private String jxhjJxse;
    /**
     * 销项明细-专用发票分数
     */
    private String xxmxZyfpfs;
    /**
     * 销项明细-普通发票分数
     */
    private String xxmxPtfpfs;
    /**
     * 销项明细-其他发票分数
     */
    private String xxmxQtfpfs;
    /**
     * 销项明细-销售金额（专票）
     */
    private String xxmxXsjeZp;
    /**
     * 销项明细-增值税税额（专票）
     */
    private String xxmxZzsseZp;
    /**
     * 销项明细-销售金额（普票）
     */
    private String xxmxXsjePp;
    /**
     * 销项明细-增值税税额（普票）
     */
    private String xxmxZzssePp;
    /**
     * 销项明细-16%销售金额（其他发票）
     */
    private String xxmxXsje16Qtfp;
    /**
     * 销项明细-16%增值税税额（其他发票）
     */
    private String xxmxZzsse16Qtfp;
    /**
     * 销项明细-17%销售金额（专票）
     */
    private String xxmxXsje17Zp;
    /**
     * 销项明细-17%增值税税额（专票）
     */
    private String xxmxZzsse17Zp;
    /**
     * 销项明细-17%销售金额（普票）
     */
    private String xxmxXsje17Pp;
    /**
     * 销项明细-17%增值税税额（普票）
     */
    private String xxmxZzsse17Pp;
    /**
     * 销项明细-17%销售金额（其他发票）
     */
    private String xxmxXsje17Qtfp;
    /**
     * 销项明细-17%增值税税额（其他发票）
     */
    private String xxmxZzsse17Qtfp;
    /**
     * 销项明细-0%增值税普通发票金额（出口）
     */
    private String xxmxZzsptfpje0Ck;
    /**
     * 销项明细-0%增值税普通发票税额
     */
    private String xxmxZzsptfpse0;
    /**
     * 销项明细-未开票金额3%
     */
    private String xxmxWkpje3;
    /**
     * 销项明细-未开票税额3%
     */
    private String xxmxWkpse3;
    /**
     * 销项明细-未开票金额
     */
    private String xxmxWkpje;
    /**
     * 销项明细-未开票税额
     */
    private String xxmxWkpse;
    /**
     * 销项合计-销项销售额
     */
    private String xxhjXxxse;
    /**
     * 销项合计-销项税额
     */
    private String xxhjXxse;
    /**
     * 本月应交增值税-本月应交增值税
     */
    private String byyjzzsByyjzzs;
    /**
     * 本月应交增值税-本月账面应交增值税
     */
    private String byyjzzsByzmyjzzs;
    /**
     * 本月应交增值税-上期应纳税额（留底为负数）
     */
    private String byyjzzsSqynseLdwfs;
    /**
     * 城市编码判断是否是北京
     */
    private String cityCode;
    /**
     * 地税计税依据-印花税计税依据（购销合同）金额"
     */
    private String dsjsyjYhsjsyjgxhtje;

    /**
     * 地税计税依据-印花税计税依据（财产租赁合同）金额"
     */
    private String dsjsyjYhsjsyjcczlhtje;

    /**
     * 地税计税依据-工人经费人数
     */
    private String dsjsyjGrjfrs;
    /**
     * 地税计税依据-工会经费（工资）
     */
    private String dsjsyjGhjfgz;
    /**
     * 地税计税依据-"残保金人数"
     */
    private String dsjsyjCbjrs;
    /**
     * 地税计税依据-"残保金计税依据（工资等）"
     */
    private String dsjsyjCbjjsyjgzd;
    /**
     * 企业所得税-由于六费二税减征造成的账面与申报数据的差异
     */
    private String qysdsYylfesjzzcdzmysbsjdcy;
    /**
     * 企业所得税-上年度职工薪酬
     */
    private String qysdsSndzgxc;
    /**
     * 企业所得税-上年度资产总额
      */
    private String qysdsSndzcze;
    /**
     * 企业所得税-从业人数
     */
    private String qysdsCyrs;
    /**
     * 企业所得税-分摊比例
     */
    private String qysdsFtbl;
    /**
     * 企业所得税-本季度分摊税额
     */
    private String qysdsBjdftse;
}

package com.pwc.common.third.request;

import lombok.Data;

/**
* 税报取数方式信息设置
 * @author zk
 */
@Data
public class TaxReportAccessType {

   /**
    * 税种代码
    *
    *  所得税预缴A类	    BDA0611033
    *  附加税	        BDA0610678
    *  文化事业建设费	    BDA0610334
    *  小规模增值税	    BDA0610611
    *  一般纳税人增值税	BDA0610606
    *  印花税	        BDA0610794
    */
   private String taxCode;

   /**
    * 取数方式
    *  1：推送模式，通过接口将报表推送到金财管家平台
    *  2：拉取模式，提供URL让金财管家从第三方系统拉取报表(由于调试复杂工作量大处于淘汰的边缘)
    *  3：云上票据模式，使用金财管家平台的汇总票据功能生成报表数据
    */
   private Integer accessType;
}
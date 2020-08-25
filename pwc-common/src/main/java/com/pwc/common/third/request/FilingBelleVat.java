package com.pwc.common.third.request;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author louxin
 * @email 
 * @date 2020-01-13 18:32:25
 */
@Data
public class FilingBelleVat implements Serializable {
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
	 * 应征增值税不含税销售额（3%征收率）
	 */
	private String yzzzsbhsxse;
	/**
	 * 税务机关代开的增值税专用发票不含税销售额（3%征收率）
	 */
	private String swjgdkdzzszyfpbhsxse;
	/**
	 * 税控器具开具的普通发票不含税销售额
	 */
	private String skqjkjdptfpbhsxse;
	/**
	 * 应税增值税不含税销售额（5%征收率)
	 */
	private String xsczbdcbhsxse;
	/**
	 * 税务机关代开的增值税专用发票不含税销售额（5%征收率）
	 */
	private String swjgdkdzzszyfpbhsxse1;
	/**
	 * 税控器具开具的普通发票不含税销售额
	 */
	private String skqjkjdptfpbhsxse2;
	/**
	 * 税销售使用过的固定资产不含税销售额
	 */
	private String xssygdysgdzcbhsxse;
	/**
	 * 其中：税控器具开具的普通发票不含税销售额
	 */
	private String skqjkjdptfpbhsxse1;
	/**
	 * 免税销售额
	 */
	private String msxse;
	/**
	 * 其中：小微企业免税销售额
	 */
	private String xwqymsxse;
	/**
	 * 未达起征点销售额
	 */
	private String wdqzdxse;
	/**
	 * 其他免税销售额
	 */
	private String qtmsxse;
	/**
	 * 出口免税销售额
	 */
	private String ckmsxse;
	/**
	 * 其中：税控器具开具的普通发票销售额
	 */
	private String skqjkjdptfpxse1;
	/**
	 * 核定销售额
	 */
	private String hdxse;
	/**
	 * 本期应纳税额
	 */
	private String bqynse;
	/**
	 * 核定应纳税额
	 */
	private String hdynse;
	/**
	 * 本期应纳税额减征额
	 */
	private String bqynsejze;
	/**
	 * 本期免税额
	 */
	private String bqmse;
	/**
	 * 其中：小微企业免税额
	 */
	private String xwqymse;
	/**
	 * 未达起征点免税额
	 */
	private String wdqzdmse;
	/**
	 * 应纳税额合计
	 */
	private String ynsehj;
	/**
	 * 本期预缴税额
	 */
	private String bqyjse1;
	/**
	 * 本期应补（退）税额
	 */
	private String bqybtse;
	/**
	 * 本期销售不动产的销售额
	 */
	private String bdcxse;
	/**
	 * 应征增值税不含税销售额（3%征收率）
	 */
	private String yzzzsbhsxse_1;
	/**
	 * 税务机关代开的增值税专用发票不含税销售额（3%征收率）
	 */
	private String swjgdkdzzszyfpbhsxse_1;
	/**
	 * 税控器具开具的普通发票不含税销售额
	 */
	private String skqjkjdptfpbhsxse_1;
	/**
	 * 应税增值税不含税销售额（5%征收率)
	 */
	private String xsczbdcbhsxse_1;
	/**
	 * 税务机关代开的增值税专用发票不含税销售额（5%征收率）
	 */
	private String swjgdkdzzszyfpbhsxse1_1;
	/**
	 * 税控器具开具的普通发票不含税销售额
	 */
	private String skqjkjdptfpbhsxse2_1;
	/**
	 * 税销售使用过的固定资产不含税销售额
	 */
	private String xssygdysgdzcbhsxse_1;
	/**
	 * 其中：税控器具开具的普通发票不含税销售额
	 */
	private String skqjkjdptfpbhsxse1_1;
	/**
	 * 免税销售额
	 */
	private String msxse_1;
	/**
	 * 其中：小微企业免税销售额
	 */
	private String xwqymsxse_1;
	/**
	 * 未达起征点销售额
	 */
	private String wdqzdxse_1;
	/**
	 * 其他免税销售额
	 */
	private String qtmsxse_1;
	/**
	 * 出口免税销售额
	 */
	private String ckmsxse_1;
	/**
	 * 其中：税控器具开具的普通发票销售额
	 */
	private String skqjkjdptfpxse1_1;
	/**
	 * 核定销售额
	 */
	private String hdxse_1;
	/**
	 * 本期应纳税额
	 */
	private String bqynse_1;
	/**
	 * 核定应纳税额
	 */
	private String hdynse_1;
	/**
	 * 本期应纳税额减征额
	 */
	private String bqynsejze_1;
	/**
	 * 本期免税额
	 */
	private String bqmse_1;
	/**
	 * 其中：小微企业免税额
	 */
	private String xwqymse_1;
	/**
	 * 未达起征点免税额
	 */
	private String wdqzdmse_1;
	/**
	 * 应纳税额合计
	 */
	private String ynsehj_1;
	/**
	 * 本期预缴税额
	 */
	private String bqyjse1_1;
	/**
	 * 本期应补（退）税额
	 */
	private String bqybtse_1;
	/**
	 * 本期销售不动产的销售额
	 */
	private String bdcxse_1;

}

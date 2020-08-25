package com.pwc.common.utils.apidemo.apidemo.invoiceauth;

import com.alibaba.fastjson.JSONObject;
import com.pwc.common.utils.apidemo.utils.Base64Util;
import com.pwc.common.utils.apidemo.utils.HttpUtil;
import com.pwc.common.utils.apidemo.utils.SignUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 发票查验接口测试类
 * @copyright www.holytax.com
 * @version 1.0
 * @description
 */
public class TestInvoiceCheck {

	private static final String jtnsrsbh = "91350000611528672E";//集团 、 合作伙伴 税号
	private static final String appid = "250419f41703";// appid
	private static final String appsecret = "3c419162c2b6069849bb";//appsecret
	private static final String url = "https://pubapi.holytax.com/pre/api";//接口地址
	private static final String nsrsbh = "";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//S000401();

	}
	/**
	 * 发票查验
	 */
	private static void S000401(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);// appid
		querys.put("serviceid", "S000401");
		querys.put("jtnsrsbh", jtnsrsbh);// jtnsrsbh
		querys.put("nsrsbh", "");//值可以为空

		JSONObject contentMap = new JSONObject();
		contentMap.put("fplx", "01");//发票类型
		contentMap.put("fpdm", "3200174130");//发票代码
		contentMap.put("fphm", "18504968");//发票号码
		contentMap.put("kprq", "20180407");//开票日期
		contentMap.put("fpje", "9900.47");//发票金额

		System.out.println("contentMap===="+contentMap);
		String content = Base64Util.encode(contentMap.toString());
		querys.put("content", content);

		String  signR = SignUtil.sign(appsecret, querys);
		querys.put("signature", signR);
		System.out.println("querys===="+querys);

		Map<String, String> result = HttpUtil.post(url, querys);
		System.out.println("result==="+result);
	}

}

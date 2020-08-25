package com.pwc.common.utils.apidemo.apidemo.invoiceauth;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pwc.common.utils.apidemo.utils.Base64Util;
import com.pwc.common.utils.apidemo.utils.HttpUtil;
import com.pwc.common.utils.apidemo.utils.SignUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 发票异步批量查验接口测试类
 * @copyright www.holytax.com
 * @version 1.0
 * @description
 */
public class TestAsyncInvoiceCheck {

	private static final String jtnsrsbh = "91310000607378229C";//集团 、 合作伙伴 税号
	private static final String appid = "5d206b9d5b12";// appid
	private static final String appsecret = "3638a4cda4468ad7fab1";//appsecret
	private static final String url = "https://pubapi.holytax.com/pre/api";//接口地址
	private static final String nsrsbh = "";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//S000403();
		S000404();
	}
	/**
	 * 异步发票批量查验--发送查验请求
	 */
	private static void S000403(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);// appid
		querys.put("serviceid", "S000403");
		querys.put("jtnsrsbh", jtnsrsbh);// jtnsrsbh
		querys.put("nsrsbh", "");//值可以为空

		List<Map<String,String>> invList = new ArrayList<>();
		Map<String,String> fpxx1 = new HashMap<String,String>();
		fpxx1.put("fplx", "01");//发票类型
		fpxx1.put("fpdm", "1100181131");//发票代码
		fpxx1.put("fphm", "11728478");//发票号码
		fpxx1.put("kprq", "20180416");//开票日期
		fpxx1.put("fpje", "4529.06");//发票金额
		invList.add(fpxx1);
		Map<String,String> fpxx2 = new HashMap<String,String>();
		fpxx2.put("fplx", "01");//发票类型
		fpxx2.put("fpdm", "2100181131");//发票代码
		fpxx2.put("fphm", "21728478");//发票号码
		fpxx2.put("kprq", "20170416");//开票日期
		fpxx2.put("fpje", "4529.06");//发票金额
		invList.add(fpxx2);
		Map<String,String> fpxx3 = new HashMap<String,String>();
		fpxx3.put("fplx", "10");//发票类型
		fpxx3.put("fpdm", "032001600811");//发票代码
		fpxx3.put("fphm", "27695774");//发票号码
		fpxx3.put("kprq", "20180626");//开票日期
		fpxx3.put("jym", "656833");//校验码后六位
		invList.add(fpxx3);

		JSONObject contentMap = new JSONObject();
		contentMap.put("invList", invList);//需要查验的发票数据

		System.out.println("contentMap===="+contentMap);
		String content = Base64Util.encode(contentMap.toString());
		querys.put("content", content);

		String  signR = SignUtil.sign(appsecret, querys);
		querys.put("signature", signR);
		System.out.println("querys===="+querys);

		Map<String, String> result = HttpUtil.post(url, querys);
		System.out.println("result==="+result);
	}

	/**
	 * 异步发票批量查验--获取查验结果
	 */
	private static void S000404(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);// appid
		querys.put("serviceid", "S000404");
		querys.put("jtnsrsbh", jtnsrsbh);// jtnsrsbh
		querys.put("nsrsbh", "");//值可以为空

		JSONObject contentMap = new JSONObject();
		contentMap.put("pch", "21ce7a26cb933d8ea843cbe1be43d7ce");//批次号
//		contentMap.put("rwh", "6afc65ef1ae33ad2940251a01a0e22bd");//任务号

		System.out.println("contentMap===="+contentMap);
		String content = Base64Util.encode(contentMap.toString());
		querys.put("content", content);

		String  signR = SignUtil.sign(appsecret, querys);
		querys.put("signature", signR);
		System.out.println("querys===="+querys);

		Map<String, String> result = HttpUtil.post(url, querys);
		System.out.println("result==="+result);
		String dataValue = result.get("response");
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(dataValue);
		System.out.println(jsonObject);
		String responseValue = (String) jsonObject.get("content");

		Gson gson = new Gson();
		String val2 = Base64Util.decode(responseValue);
		Map<String, Object> mapValue2 = new HashMap<String, Object>();
		mapValue2 = gson.fromJson(val2, new TypeToken<Map<String, Object>>() {

		}.getType());
		System.out.println(mapValue2);
	}

}

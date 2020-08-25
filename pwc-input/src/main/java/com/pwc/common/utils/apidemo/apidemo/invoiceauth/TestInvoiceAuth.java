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
 * 认证相关接口测试类
 * @copyright www.holytax.com
 * @author wn
 * @date 2018年9月11日 下午4:45:34
 * @version 1.0
 * @description
 */
public class TestInvoiceAuth {

	private static final String jtnsrsbh = "91310000607378229C";// 集团税号
	private static final String appid = "5d206b9d5b12";// appid
	private static final String appsecret = "3638a4cda4468ad7fab1";// appsecret
	private static final String url = "https://pubapi.holytax.com/pre/api";//接口地址
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//数据同步01
	  //S000501_01_02_03_04();
		//S000501_05_06();
		//S000501_07();
		//发票认证
		S000502();
		//S000504();
		//发票状态查询
		//S000505();
	}

	/**
	 * 数据同步01
	 */
	private static void S000501_01_02_03_04(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);
		querys.put("serviceid", "S000501");
		querys.put("jtnsrsbh", jtnsrsbh);// 集团税号
		querys.put("nsrsbh", jtnsrsbh);// 集团下企业税号

		JSONObject contentMap = new JSONObject();
		contentMap.put("synctype", "01");//01--按开票日期同步（YYYY-MM-DD）
		contentMap.put("synccondition", "2018-03-01");
//		contentMap.put("synctype", "02");//02--按开票日期所属月份同步（YYYY-MM）
//		contentMap.put("synccondition", "2018-03");
//		contentMap.put("synctype", "03");//03--按税款所属期同步（YYYY-MM）
//		contentMap.put("synccondition", "2018-03");
//		contentMap.put("synctype", "04");//04--按开票日期范围同步（YYYY-MM-DD,YYYY-MM-DD）
//		contentMap.put("synccondition", "2018-03-01,2018-03-31");
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
	 * 数据同步,发票数据初始化
	 */
	private static void S000501_05_06(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);
		querys.put("serviceid", "S000501");
		querys.put("jtnsrsbh", jtnsrsbh);//集团税号
		querys.put("nsrsbh", jtnsrsbh);//集团下企业税号

		JSONObject contentMap = new JSONObject();
		contentMap.put("synctype", "05");//05--数据初始化
//		contentMap.put("synctype", "06");//06--按批次同步
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
	 * 数据同步07-结果回调
	 */
	private static void S000501_07(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);
		querys.put("serviceid", "S000501");
		querys.put("jtnsrsbh", jtnsrsbh);//集团税号
		querys.put("nsrsbh", jtnsrsbh);//集团下企业税号

		JSONObject contentMap = new JSONObject();
		contentMap.put("synctype", "07");//07--按批次同步，结果回调 （taskno:进项发票数据同步  任务编号）
		contentMap.put("synccondition", "0be51f9148b741111a2b13ef5a1eb3d");
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
	 * 获取企业当前税款所属期
	 */
	private static void S000502(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);// appid
		querys.put("serviceid", "S000502");
		querys.put("jtnsrsbh", jtnsrsbh);// 集团税号
		querys.put("nsrsbh", "913100007178541939");// 集团下企业税号
		querys.put("content", "");//值可以为空串，参数名称需要有

		String  signR = SignUtil.sign(appsecret, querys);
		querys.put("signature", signR);
		System.out.println("querys===="+querys);

		//解密
		Map<String, String> result = HttpUtil.post(url, querys);
		System.out.println("result==="+result);

		String value = result.get("response");
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(value);
		System.out.println(json);
		String uuid = (String) json.get("uuid");
		String code = (String) json.get("code");
		String msg = (String) json.get("msg");
		String contentValue = (String) json.get("content");

		String val = Base64Util.decode(contentValue);
		Gson gson = new Gson();
		Map<String, Object> mapValue = new HashMap<String, Object>();
		mapValue = gson.fromJson(val, new TypeToken<Map<String, Object>>() {

		}.getType());
		Map<String, Object> fpxx = (Map<String, Object>) mapValue;
		// 企业当前税款所属期
		String skssq = (String) fpxx.get("skssq");
		System.out.println(skssq);
	}



	/**
	 * 发票认证（测试时，一定不要用“未认证的 正常状态的发票” 来测试）
	 */
	private static void S000504(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);// appid
		querys.put("serviceid", "S000504");
		querys.put("jtnsrsbh", jtnsrsbh);//集团税号
		querys.put("nsrsbh", jtnsrsbh);//集团下企业税号

		String period = "201809";
		Map<String, String> invMap1 = new HashMap<String, String>();
		invMap1.put("InvoiceCode", "4400181130");//发票代码
		invMap1.put("InvoiceNumber", "07712406");//发票号码
		Map<String, String> invMap2 = new HashMap<String, String>();
		invMap2.put("InvoiceCode", "3200182130");//发票代码
		invMap2.put("InvoiceNumber", "27491505");//发票号码
		List<Map<String, String>> invList = new ArrayList<Map<String, String>>();
		invList.add(invMap1);
		invList.add(invMap2);

		Map<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("Period", period);
		contentMap.put("InvoiceList", invList);
		System.out.println("contentMap===="+contentMap);
		String content = Base64Util.encode(JSONObject.toJSONString(contentMap));
		querys.put("content", content);

		String  signR = SignUtil.sign(appsecret, querys);
		querys.put("signature", signR);
		System.out.println("querys===="+querys);

		Map<String, String> result = HttpUtil.post(url, querys);
		System.out.println("result==="+result);
	}
	/**
	 * 发票状态查询
	 */
	private static void S000505(){
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("appid", appid);// appid
		querys.put("serviceid", "S000505");
		querys.put("jtnsrsbh", jtnsrsbh);//集团税号
		querys.put("nsrsbh", jtnsrsbh);//集团下企业税号

		JSONObject contentMap = new JSONObject();
		contentMap.put("InvoiceCode", "3400182130");//发票代码
		contentMap.put("InvoiceNumber", "08459347");//发票号码
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

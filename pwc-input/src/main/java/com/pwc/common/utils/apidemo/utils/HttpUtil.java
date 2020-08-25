package com.pwc.common.utils.apidemo.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/*
 * http工具类
 */
public class HttpUtil {

	/**
	 * pose方式请求
	 * @param url
	 * @return {statusCode : "请求结果状态代码", responseString : "请求结果响应字符串"}
	 */
	public static Map<String,String> post(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost post = postForm(url, params);
//		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
//		post.addHeader("Content-Type","text/html;charset=UTF-8");
//		post.setHeader("Content-Type","text/html;charset=UTF-8");
		Map reponseMap = invoke(httpclient, post);
		httpclient.getConnectionManager().shutdown();
		return reponseMap;
	}

	private static HttpPost postForm(String url, Map<String, String> params){
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		Set<String> keySet = params.keySet();
		for(String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return httpost;
	}


    private static Map invoke(DefaultHttpClient httpclient,
                              HttpUriRequest httpost) {
		Map returnMap = new HashMap();
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		returnMap.put("statusCode", response.getStatusLine().getStatusCode());	// 请求返回结果状态
		returnMap.put("response", body);
		return returnMap;
	}

    private static HttpResponse sendRequest(DefaultHttpClient httpclient,
                                            HttpUriRequest httpost) {
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

    private static String paseResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		String charset = EntityUtils.getContentCharSet(entity);
		String body = null;
		try {
			body = EntityUtils.toString(entity,"UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
}

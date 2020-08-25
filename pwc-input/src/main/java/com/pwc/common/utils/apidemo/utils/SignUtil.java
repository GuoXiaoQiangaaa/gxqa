
package com.pwc.common.utils.apidemo.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名工具
 */
public class SignUtil {

	public static final String HMAC_SHA256 = "HmacSHA256";
	public static final String ENCODING = "UTF-8";
	public static final String LF = "\n";
	public static final String SPE1 = ",";
	public static final String SPE2 = ":";
	public static final String SPE3 = "&";
	public static final String SPE4 = "=";
	public static final String SPE5 = "?";

	public static String sign(String secret, Map<String, String> querys) {
		try {
			Mac hmacSha256 = Mac.getInstance(HMAC_SHA256);
			byte[] keyBytes = secret.getBytes(ENCODING);
			hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, HMAC_SHA256));
			return new String(Base64.encodeBase64(hmacSha256.doFinal(buildResource(querys).getBytes(ENCODING))),
					ENCODING);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String buildResource(Map<String, String> querys) {
		StringBuilder sb = new StringBuilder();
		Map<String, String> sortMap = new TreeMap<String, String>();
		if (null != querys) {
			for (Map.Entry<String, String> query : querys.entrySet()) {
				if (!StringUtils.isBlank(query.getKey())) {
					sortMap.put(query.getKey(), query.getValue());
				}
			}
		}

		StringBuilder sbParam = new StringBuilder();
		for (Map.Entry<String, String> item : sortMap.entrySet()) {
			if (!StringUtils.isBlank(item.getKey())) {
				if (0 < sbParam.length()) {
					sbParam.append(SPE3);
				}
				sbParam.append(item.getKey());
				if (!StringUtils.isBlank(item.getValue())) {
					sbParam.append(SPE4).append(item.getValue());
				}
			}
		}
		if (0 < sbParam.length()) {
			sb.append(SPE5);
			sb.append(sbParam);
		}

		return sb.toString();
	}
}

package com.sjn.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

public class StringUtils {

	public static final String encoding = "UTF-8";

	/**
	 * 获取uuid
	 * 
	 * @param is32bit
	 * @return
	 */
	public static String getUuidByJdk(boolean is32bit) {
		String uuid = UUID.randomUUID().toString();
		if (is32bit) {
			return uuid.toString().replace("-", "");
		}
		return uuid;
	}

	public static boolean isEmpty(String str) {
		return (null == str || str.trim().length() == 0);
	}

	/**
	 * Url Base64编码
	 * 
	 * @param data
	 *            待编码数据
	 * @return String 编码数据
	 * @throws Exception
	 */
	public static String encode(String data) throws Exception {
		// 执行编码
		byte[] b = Base64.encodeBase64URLSafe(data.getBytes(encoding));
		return new String(b, encoding);
	}

	/**
	 * Url Base64解码
	 * 
	 * @param data
	 *            待解码数据
	 * @return String 解码数据
	 * @throws Exception
	 */
	public static String decode(String data) throws Exception {
		// 执行解码
		byte[] b = Base64.decodeBase64(data.getBytes(encoding));
		return new String(b, encoding);
	}

	/**
	 * URL编码（utf-8）
	 * 
	 * @param source
	 * @return
	 */
	public static String urlEncode(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 是否是手机号码
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		//TODO 还没写手机号判断呢
		Pattern pattern = Pattern.compile("^1[3-8]{9}$");
        Matcher matcher = pattern.matcher(mobile);
        
        if (matcher.matches()) {
            return true;
        }
        return false;
	}
}

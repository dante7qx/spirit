package com.ymrs.spirit.ffx.util;

/**
 * 字符串工具类
 * 
 * @author dante
 *
 */
public abstract class StringUtils extends org.springframework.util.StringUtils {
	
	/**
	 * str 为 null 或 "" 时返回false，否则返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 *   str == null     返回  ""
	 *   str == ""       返回  ""
	 *   str == " abc "  返回 "abc"
	 *   str == "a bc"   放回 "a bc"
	 */
	public static String trimToEmpty(String str)
	{
		if (isEmpty(str)){
			return "";
		}
		return str.trim();
	}
	
	public static String deleteStr(String str) {
		if(str.startsWith("`")){
			str = str.substring(1, str.length());
		}
		return str;
	}
	

	public static String formatStr(String str) {
		if(str.startsWith("-")){
			str = str.substring(1, str.length());
		}
		return str;
	}
	
}

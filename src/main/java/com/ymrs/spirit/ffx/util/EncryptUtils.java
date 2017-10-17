package com.ymrs.spirit.ffx.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密工具类
 * 
 * @author dante
 *
 */
public final class EncryptUtils {
	private static final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	private EncryptUtils() {
		throw new IllegalAccessError("EncryptUtils 工具类，不能实例化！");
	}
	
	/**
	 * 加密方法
	 * 
	 * @param rawPassword
	 * @return
	 */
	public static String encrypt(String rawPassword) {
		return bcryptEncoder.encode(rawPassword);
	}
	
	/**
	 * 密文比对
	 * 
	 * @param rawPassword
	 * @param password
	 * @return
	 */
	public static boolean match(String rawPassword, String password) {
		return bcryptEncoder.matches(rawPassword, password);
	}
	
}

package com.ymrs.spirit.ffx.constant;

/**
 * Spring Security 工具类
 * 
 * @author dante
 *
 */
public class SecurityConsts {
	
	private SecurityConsts() {
		throw new IllegalAccessError("SecurityConsts 常量类，不能实例化！");
	}
	
	/**
	 * 超级管理员
	 */
	public static final String SUPER_ADMIN = "superadmin";
	
	/**
	 * 用户状态
	 */
	public static final String STATUS_NORMAL = "NORMAL";
	public static final String STATUS_LOCK = "LOCK";
	public static final String STATUS_DEL = "DEL";
	
}

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
	
	public static final String ROLE_PREFIX = "AUTH_";
	public static final String LOGIN_PAGE = "/loginpage";
	public static final String INDEX_PAGE = "/";
	public static final String SESSION_TIMEOUT = "/session-timeout";
	
	/**
	 * 用户状态
	 */
	public static final String STATUS_NORMAL = "NORMAL";
	public static final String STATUS_LOCK = "LOCK";
	public static final String STATUS_DEL = "DEL";
	
}

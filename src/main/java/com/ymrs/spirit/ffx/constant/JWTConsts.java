package com.ymrs.spirit.ffx.constant;

/**
 * JWT 相关常量类
 * 
 * @author dante
 *
 */
public final class JWTConsts {

	private JWTConsts() {
		throw new IllegalAccessError("JWTConsts 工具类，不能实例化！");
	}
	
	/**
	 * JWT Sub 分隔符
	 */
	public static final String TOKEN_SPLIT = ",";
}

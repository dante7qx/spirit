package com.ymrs.spirit.ffx.constant.sysmgr;

/**
 * 系统管理 - 用户常量
 * 
 * @author dante
 *
 */
public final class UserConsts {
	
	private UserConsts() {
		throw new IllegalAccessError("UserConsts 常量类，不能实例化！");
	}
	
	/**
	 * 用户状态
	 */
	public static final String STATUS_NORMAL = "NORMAL";
	public static final String STATUS_LOCK = "LOCK";
	public static final String STATUS_DEL = "DEL";
}

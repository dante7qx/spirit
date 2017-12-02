package com.ymrs.spirit.ffx.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.ymrs.spirit.ffx.security.SpiritLoginUser;
import com.ymrs.spirit.ffx.security.SpiritPrincipal;

/**
 * 登录用户工具类
 * 
 * @author dante
 */
public class LoginUserUtils {
	
	private LoginUserUtils() {
		throw new IllegalAccessError("LoginUserUtils 工具类，不能实例化！");
	}
	
	/**
	 * 当前登录用户
	 * 
	 * @return
	 */
	public static SpiritLoginUser loginUser() {
		SpiritLoginUser loginUser = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal != null && principal instanceof SpiritPrincipal) {
			SpiritPrincipal spiritPrincipal = (SpiritPrincipal) principal;
			loginUser = spiritPrincipal.getSpiritLoginUser();
		}
		return loginUser;
	}
	
	/**
	 * 当前登录用户Id
	 * 
	 * @return
	 */
	public static Long loginUserId() {
		Long loginUserId = null;
		SpiritLoginUser loginUser = loginUser();
		if(loginUser != null) {
			loginUserId = loginUser.getId();
		}
 		return loginUserId;
	}
}

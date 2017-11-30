package com.ymrs.spirit.ffx.service.sysmgr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 在线用户统计 Service
 * 
 * @author dante
 *
 */
public interface OnlineUserService {
	
	public void addOnlineUser(String account, HttpServletRequest request);
	
	public void removeOnlineUser(HttpSession session);
}

package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ymrs.spirit.ffx.vo.sysmgr.OnlineUserVO;

/**
 * 在线用户统计 Service
 * 
 * @author dante
 *
 */
public interface OnlineUserService {
	
	public void addOnlineUser(String account, HttpServletRequest request);
	
	public void removeOnlineUser(HttpSession session);
	
	public List<OnlineUserVO> onlineUser();
	
}

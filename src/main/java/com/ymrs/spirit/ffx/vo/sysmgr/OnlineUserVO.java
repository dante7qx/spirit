package com.ymrs.spirit.ffx.vo.sysmgr;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.ymrs.spirit.ffx.util.DateUtils;

import lombok.Data;

/**
 * 在线用户VO
 * 
 * @author dante
 *
 */
@Data
public class OnlineUserVO {
	
	public OnlineUserVO() {
	}
	
	public OnlineUserVO(HttpSession session, String account, String ip) {
		this.sessionId = session.getId();
		this.loginUserId = account;
		this.ip = ip;
		this.createDate = DateUtils.formatDateTime(new Date(session.getCreationTime()));
//		this.lastAccessedTime = DateUtils.formatDateTime(new Date(session.getLastAccessedTime()));
	}
 
	/**
	 * SessionId
	 */
	private String sessionId;
	
	/**
	 * 当前登录帐号
	 */
	private String loginUserId;
	
	/**
	 * 当前用户IP地址
	 */
	private String ip;
	
	/**
	 * 登录时间
	 */
	private String createDate;
	
	/**
	 * 最后访问时间
	 */
	private String lastAccessedTime;
	
	
	
}

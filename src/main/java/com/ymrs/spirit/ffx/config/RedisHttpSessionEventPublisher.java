package com.ymrs.spirit.ffx.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.ymrs.spirit.ffx.service.sysmgr.OnlineUserService;

/**
 * Session监听类
 * 
 * @author dante
 *
 */
public class RedisHttpSessionEventPublisher extends HttpSessionEventPublisher {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisHttpSessionEventPublisher.class);
	
	@Autowired
	private OnlineUserService onlineUserService;
	
	public void sessionCreated(HttpSessionEvent event) {
		super.sessionCreated(event);
		HttpSession session = event.getSession();
		LOGGER.info("session ({}) create", session.getId());
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		onlineUserService.removeOnlineUser(session);
		Object obj = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if(obj == null) {
			return;
		}
		SecurityContext sc = (SecurityContext) obj;
		if(sc != null) {
			String principal = sc.getAuthentication().getPrincipal().toString();
			LOGGER.info("session ({})->{} destroy.", session.getId(), principal);
		}
		super.sessionDestroyed(event);
	}
}

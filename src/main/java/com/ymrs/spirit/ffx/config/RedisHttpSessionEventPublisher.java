package com.ymrs.spirit.ffx.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.util.StringUtils;

/**
 * Session监听类
 * 
 * @author dante
 *
 */
public class RedisHttpSessionEventPublisher extends HttpSessionEventPublisher {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisHttpSessionEventPublisher.class);
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	public void sessionCreated(HttpSessionEvent event) {
		super.sessionCreated(event);
		HttpSession session = event.getSession();
		SessionInformation sessionInfo = sessionRegistry.getSessionInformation(session.getId());
		if(sessionInfo != null && !StringUtils.isEmpty(sessionInfo.getPrincipal())) {
			LOGGER.info("session {}->{} create.", session.getId(), sessionInfo.getPrincipal());
		}
		LOGGER.info("session ({}) create", session.getId());
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
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

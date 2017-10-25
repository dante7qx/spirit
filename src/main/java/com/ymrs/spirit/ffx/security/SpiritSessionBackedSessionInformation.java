package com.ymrs.spirit.ffx.security;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

public class SpiritSessionBackedSessionInformation extends SessionInformation {

	private static final long serialVersionUID = -8673113358907629667L;

	private static final Log logger = LogFactory.getLog(SpiritSessionBackedSessionInformation.class);

	private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

	/**
	 * Tries to determine the principal's name from the given Session.
	 *
	 * @param session Spring Session session
	 * @return the principal's name, or empty String if it couldn't be determined
	 */
	private static String resolvePrincipal(Session session) {
		String principalName = session.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
		if (principalName != null) {
			return principalName;
		}
		SecurityContext securityContext = session.getAttribute(SPRING_SECURITY_CONTEXT);
		if (securityContext != null && securityContext.getAuthentication() != null) {
			return securityContext.getAuthentication().getName();
		}
		return "";
	}

	private final SessionRepository<? extends ExpiringSession> sessionRepository;

	SpiritSessionBackedSessionInformation(ExpiringSession session, SessionRepository<? extends ExpiringSession> sessionRepository) {
		super(resolvePrincipal(session), session.getId(), new Date(session.getLastAccessedTime()));
		this.sessionRepository = sessionRepository;
		if (session.isExpired()) {
			super.expireNow();
		}
	}

	@Override
	public void expireNow() {
		if (logger.isDebugEnabled()) {
			logger.debug("Expiring session " + getSessionId() + " for user '" + getPrincipal() +
					"', presumably because maximum allowed concurrent sessions was exceeded");
		}
		super.expireNow();
		sessionRepository.delete(getSessionId());
	}
	
}

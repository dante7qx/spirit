package com.ymrs.spirit.ffx.security;

import static java.util.stream.Collectors.toList;
import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpiritSessionBackedSessionRegistry implements SessionRegistry {

	private FindByIndexNameSessionRepository<? extends ExpiringSession> sessionRepository;
	
	public SpiritSessionBackedSessionRegistry(FindByIndexNameSessionRepository<? extends ExpiringSession> sessionRepository) {
		Assert.notNull(sessionRepository, "sessionRepository cannot be null");
		this.sessionRepository = sessionRepository;
	}

	public List<Object> getAllPrincipals() {
		throw new UnsupportedOperationException("SpringSessionBackedSessionRegistry does not support retrieving all principals, " +
				"since Spring Session provides no way to obtain that information");
	}

	public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
		return sessionRepository
                .findByIndexNameAndIndexValue(PRINCIPAL_NAME_INDEX_NAME, name(principal))
                .values()
                .stream()
                .filter(session -> includeExpiredSessions || !session.isExpired())
                .map(session -> new SpiritSessionBackedSessionInformation(session, sessionRepository))
                .collect(toList());
	}

	public SessionInformation getSessionInformation(String sessionId) {
		ExpiringSession session = this.sessionRepository.getSession(sessionId);
		if (session != null) {
			return new SpiritSessionBackedSessionInformation(session, this.sessionRepository);
		}
		return null;
	}

	/*
	 * This is a no-op, as we don't administer sessions ourselves.
	 */
	@Override
	public void refreshLastRequest(String sessionId) {
	}

	/*
	 * This is a no-op, as we don't administer sessions ourselves.
	 */
	@Override
	public void registerNewSession(String sessionId, Object principal) {
	}

	/*
	 * This is a no-op, as we don't administer sessions ourselves.
	 */
	@Override
	public void removeSessionInformation(String sessionId) {
	}

	/**
	 * Derives a String name for the given principal.
	 *
	 * @param principal as provided by Spring Security
	 * @return name of the principal, or its {@code toString()} representation if no name could be derived
	 */
	protected String name(Object principal) {
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		if (principal instanceof Principal) {
			return ((Principal) principal).getName();
		}
		return principal.toString();
	}
	
}

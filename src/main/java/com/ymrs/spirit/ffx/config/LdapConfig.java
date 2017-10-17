package com.ymrs.spirit.ffx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import com.ymrs.spirit.ffx.prop.SpiritProperties;

@Configuration
public class LdapConfig {
	
	@Autowired
	private SpiritProperties spiritProperties;
	
	@Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSourceTarget());
	}
	
	@Bean
	public LdapContextSource contextSourceTarget() {
		LdapContextSource ldapContextSource = new LdapContextSource();
		ldapContextSource.setUrl(spiritProperties.getLdapurl());
		return ldapContextSource;
	}
}

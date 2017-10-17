package com.ymrs.spirit.ffx.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.constant.SecurityConsts;
import com.ymrs.spirit.ffx.security.SpiritLoginFilter;
import com.ymrs.spirit.ffx.security.SpiritPasswordEncoder;
import com.ymrs.spirit.ffx.security.SpiritUserDetailsService;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers()
				.frameOptions()
				.sameOrigin()
			.and()
			.authorizeRequests()
				.antMatchers("/images/**", "/ux/**", "/webjars/**", "/druid/**", SecurityConsts.LOGIN_PAGE+"?", SecurityConsts.SESSION_TIMEOUT).permitAll()
				.anyRequest().authenticated()
			.and()
				.formLogin()
					.loginPage(SecurityConsts.LOGIN_PAGE)
					.loginProcessingUrl("/login")
					.defaultSuccessUrl("/", true)
				.permitAll()
//			.and()
//				.logout()
//					.logoutUrl("/sys_logout")
//					.deleteCookies("JSESSIONID")
//				.permitAll()
			.and()
				.addFilterBefore(spiritLoginFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterAt(concurrencyFilter(), ConcurrentSessionFilter.class)
				.sessionManagement()
				.sessionAuthenticationStrategy(compositeSessionAuthenticationStrategy())
				.invalidSessionUrl(SecurityConsts.SESSION_TIMEOUT);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}
	
	@Bean
	public SpiritUserDetailsService userDetailsService() {
		return new SpiritUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new SpiritPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setHideUserNotFoundExceptions(false);
		return authenticationProvider;
	}
	
	@Bean
	public SpiritLoginFilter spiritLoginFilter() {
		SpiritLoginFilter spiritLoginFilter = new SpiritLoginFilter();
		spiritLoginFilter.setAuthenticationManager(authenticationManager);
		spiritLoginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
		spiritLoginFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
		return spiritLoginFilter;
	}
	
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler() {
		SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		authenticationSuccessHandler.setDefaultTargetUrl("/");
		authenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
		return authenticationSuccessHandler;
	}
	
	@Bean
	public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
		SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
		authenticationFailureHandler.setDefaultFailureUrl(SecurityConsts.LOGIN_PAGE);
		return authenticationFailureHandler;
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
		SessionRegistry sessionRegistry = new SessionRegistryImpl();
		return sessionRegistry;
	}

	@Bean
	public ConcurrentSessionFilter concurrencyFilter() {
		ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry());
		return concurrentSessionFilter;
	}

	@Bean
	public CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy() {
		List<SessionAuthenticationStrategy> delegateStrategies = Lists.newLinkedList();
		delegateStrategies.add(concurrentSessionControlAuthenticationStrategy());
		delegateStrategies.add(sessionFixationProtectionStrategy());
		delegateStrategies.add(registerSessionAuthenticationStrategy());
		CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy = new CompositeSessionAuthenticationStrategy(
				delegateStrategies);
		return compositeSessionAuthenticationStrategy;
	}

	@Bean
	public ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy() {
		ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(
				sessionRegistry());
		concurrentSessionControlAuthenticationStrategy.setMaximumSessions(20); // 单个用户最大并行会话数
		concurrentSessionControlAuthenticationStrategy.setExceptionIfMaximumExceeded(false); // 设置为true时会报错且后登录的会话不能登录，默认为false不报错且将前一会话置为失效
		return concurrentSessionControlAuthenticationStrategy;
	}

	@Bean
	public SessionFixationProtectionStrategy sessionFixationProtectionStrategy() {
		SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();
		sessionFixationProtectionStrategy.setMigrateSessionAttributes(true);
		return sessionFixationProtectionStrategy;
	}

	@Bean
	public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}
}

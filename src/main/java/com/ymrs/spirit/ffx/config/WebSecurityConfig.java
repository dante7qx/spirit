package com.ymrs.spirit.ffx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;

import com.ymrs.spirit.ffx.constant.SecurityConsts;
import com.ymrs.spirit.ffx.prop.SpiritProperties;
import com.ymrs.spirit.ffx.security.SpiritLoginFilter;
import com.ymrs.spirit.ffx.security.SpiritPasswordEncoder;
import com.ymrs.spirit.ffx.security.SpiritSessionBackedSessionRegistry;
import com.ymrs.spirit.ffx.security.SpiritUserDetailsService;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SpiritProperties spiritProperties;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private SpiritSessionBackedSessionRegistry sessionRegistry;
	
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
				.addFilterAt(spiritLoginFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterAt(concurrencyFilter(), ConcurrentSessionFilter.class)
				;
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
		spiritLoginFilter.setSessionAuthenticationStrategy(concurrentSessionControlAuthenticationStrategy());
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
	public ConcurrentSessionFilter concurrencyFilter() {
		ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(this.sessionRegistry, sessionInformationExpiredStrategy());
		return concurrentSessionFilter;
	}
	
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new SimpleRedirectSessionInformationExpiredStrategy(SecurityConsts.SESSION_TIMEOUT);
    }
	
	private ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy() {
		ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(
				this.sessionRegistry);
		if(spiritProperties.getSingleUser() != null && spiritProperties.getSingleUser().booleanValue()) {
			concurrentSessionControlAuthenticationStrategy.setMaximumSessions(1);
		} else {
			concurrentSessionControlAuthenticationStrategy.setMaximumSessions(-1);
		}
		concurrentSessionControlAuthenticationStrategy.setExceptionIfMaximumExceeded(false); // 设置为true时会报错且后登录的会话不能登录，默认为false不报错且将前一会话置为失效
		return concurrentSessionControlAuthenticationStrategy;
	}

}

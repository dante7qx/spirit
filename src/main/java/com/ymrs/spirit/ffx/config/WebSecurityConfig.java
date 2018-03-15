package com.ymrs.spirit.ffx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ymrs.spirit.ffx.security.SpiritJwtAuthenticationTokenFilter;
import com.ymrs.spirit.ffx.security.SpiritJwtEntryPoint;
import com.ymrs.spirit.ffx.security.SpiritPasswordEncoder;
import com.ymrs.spirit.ffx.security.SpiritUserDetailsService;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
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
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(jwtEntryPoint())
			.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.antMatchers("/druid/**").permitAll()
				.antMatchers("/auth/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/druid/**");
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
	public SpiritJwtEntryPoint jwtEntryPoint() {
		return new SpiritJwtEntryPoint();
	}
	
	@Bean
    public SpiritJwtAuthenticationTokenFilter authenticationTokenFilter() throws Exception {
        return new SpiritJwtAuthenticationTokenFilter();
    }
	
	

}

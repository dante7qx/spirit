package com.ymrs.spirit.ffx.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring MVC 页面配置
 * 
 * @author dante
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static final String ERROR_404 = "/error/404";
	private static final String ERROR_500 = "/error/50";

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController(ERROR_404).setViewName("common/error/404");
		registry.addViewController(ERROR_500).setViewName("common/error/500");
		
		/**
		 * 系统管理页面配置
		 */
		registry.addViewController("/sysmgr/user").setViewName("sysmgr/user/userlist");
		registry.addViewController("/sysmgr/edituser").setViewName("sysmgr/user/userdetail");
		registry.addViewController("/sysmgr/authority").setViewName("sysmgr/authority/authoritymanager");
		registry.addViewController("/sysmgr/role").setViewName("sysmgr/role/rolelist");
		registry.addViewController("/sysmgr/editrole").setViewName("sysmgr/role/roledetail");
		registry.addViewController("/sysmgr/menu").setViewName("sysmgr/resource/resourcemanager");
		registry.addViewController("/sysmgr/scheduler").setViewName("sysmgr/scheduler/schedulerlist");
		registry.addViewController("/sysmgr/editscheduler").setViewName("sysmgr/scheduler/schedulerdetail");
		registry.addViewController("/sysmgr/online").setViewName("sysmgr/online/onlinelist");
		registry.addViewController("/sysmgr/syslog").setViewName("sysmgr/syslog/sysloglist");
	}

	/**
	 * 自定义系统错误页
	 * 
	 * @return
	 */
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, ERROR_404));
				container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_500));
				container.addErrorPages(new ErrorPage(java.lang.Throwable.class, ERROR_500));
			}
		};
	}
}

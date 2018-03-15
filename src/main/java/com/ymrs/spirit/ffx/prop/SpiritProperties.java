package com.ymrs.spirit.ffx.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "spirit")
@Data
public class SpiritProperties {
	
	/**
	 * 域服务器URL
	 */
	private String ldapurl;

	/**
	 * 安全检查
	 */
	private Boolean safeCheck;

	/**
	 * 单用户登录
	 */
	private Boolean singleUser;

	/**
	 * JWT 相关配置
	 */
	private Jwt jwt = new Jwt();

	public static class Jwt {
		private String header = "Authorization";

		private String secret;

		private Long expiration = 3600L;

		private String tokenHead = "Bearer ";

		public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public Long getExpiration() {
			return expiration;
		}

		public void setExpiration(Long expiration) {
			this.expiration = expiration;
		}

		public String getTokenHead() {
			return tokenHead;
		}

		public void setTokenHead(String tokenHead) {
			this.tokenHead = tokenHead;
		}

	}
}

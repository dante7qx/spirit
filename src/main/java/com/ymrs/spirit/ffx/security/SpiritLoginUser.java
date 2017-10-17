package com.ymrs.spirit.ffx.security;

import java.io.Serializable;
import java.util.Set;

/**
 * 当前登录用户（登录后存入redis session）
 * 
 * @author dante
 *
 */
public class SpiritLoginUser implements Serializable {

	private static final long serialVersionUID = -5339236104490631398L;

	private Long id;
	private String account;
	private String name;
	private String email;
	private String status;
	private Set<String> authoritys;
	
	public SpiritLoginUser() {
		// 默认构造函数
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Set<String> getAuthoritys() {
		return authoritys;
	}
	public void setAuthoritys(Set<String> authoritys) {
		this.authoritys = authoritys;
	}

	@Override
	public String toString() {
		return "SpiritLoginUser [id=" + id + ", account=" + account + ", name=" + name + ", email=" + email
				+ ", status=" + status + "]";
	}
	
}

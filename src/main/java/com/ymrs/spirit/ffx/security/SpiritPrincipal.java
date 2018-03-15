package com.ymrs.spirit.ffx.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

/**
 * 认证实体类
 * 
 * @author dante
 *
 */
public class SpiritPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;

	private SpiritLoginUser spiritLoginUser;
	private String password;

	public SpiritPrincipal() {
	}

	public SpiritPrincipal(SpiritLoginUser spiritLoginUser, String password) {
		this.spiritLoginUser = spiritLoginUser;
		this.password = password;
	}

	/**
	 * 当前认证实体的所有权限
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthoritys = Lists.newLinkedList();
		Set<String> authoritys = this.getSpiritLoginUser().getAuthoritys();
		if (!CollectionUtils.isEmpty(authoritys)) {
			grantedAuthoritys = AuthorityUtils.createAuthorityList(authoritys.toArray(new String[authoritys.size()]));
		}
		return grantedAuthoritys;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return spiritLoginUser.getAccount();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public SpiritLoginUser getSpiritLoginUser() {
		return spiritLoginUser;
	}

	/**
	 * 重写equals()方法
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() == obj.getClass()) {
			return getUsername().equals(((SpiritPrincipal) obj).getUsername());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getUsername().hashCode();
	}

	@Override
	public String toString() {
		return "SpiritPrincipal [spiritLoginUser=" + spiritLoginUser + "]";
	}

}

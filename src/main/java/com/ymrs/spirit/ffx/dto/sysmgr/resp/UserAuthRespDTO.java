package com.ymrs.spirit.ffx.dto.sysmgr.resp;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * 当前用户权限返回参数
 * 
 * @author dante
 *
 */
@Data
public class UserAuthRespDTO {
	private Long id;
	private String account;
	private String password;
	private String name;
	private String email;
	private Boolean ldapUser;
	private Set<String> authoritys;
	private String status;

	public Set<String> getAuthoritys() {
		if (this.authoritys == null) {
			this.authoritys = new HashSet<>();
		}
		return authoritys;
	}

}

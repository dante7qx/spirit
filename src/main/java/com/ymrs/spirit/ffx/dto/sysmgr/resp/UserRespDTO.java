package com.ymrs.spirit.ffx.dto.sysmgr.resp;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * 用户请求返回参数
 * 
 * @author dante
 *
 */
@Data
public class UserRespDTO {
	private Long id;
	private String account;
	private String name;
	private String email;
	private Boolean ldapUser;
	private Set<Long> roleIds;
	private String status;
	private String updateUserName;
	private String updateDate;
	
	public Set<Long> getRoleIds() {
		if(this.roleIds == null) {
			this.roleIds = new HashSet<>();
		}
		return this.roleIds;
	}

}

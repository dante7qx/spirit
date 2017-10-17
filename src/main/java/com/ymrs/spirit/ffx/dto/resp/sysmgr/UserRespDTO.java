package com.ymrs.spirit.ffx.dto.resp.sysmgr;

import java.util.Set;

import com.google.common.collect.Sets;

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
	private String updateUserName;
	private String updateDate;
	private Set<Long> roleIds;
	private String status;
	
	public Set<Long> getRoleIds() {
		if(this.roleIds == null) {
			this.roleIds = Sets.newHashSet();
		}
		return this.roleIds;
	}

}

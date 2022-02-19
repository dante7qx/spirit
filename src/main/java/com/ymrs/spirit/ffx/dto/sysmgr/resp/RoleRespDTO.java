package com.ymrs.spirit.ffx.dto.sysmgr.resp;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

/**
 * 角色返回参数
 * 
 * @author dante
 *
 */
@Data
public class RoleRespDTO {
	private Long id;
	private String name;
	private String roleDesc;
	private Set<Long> authorityIds;
	private String updateUserName;
	private String updateDate;

	public Set<Long> getAuthorityIds() {
		if (authorityIds == null) {
			authorityIds = new HashSet<>();
		}
		return authorityIds;
	}

}

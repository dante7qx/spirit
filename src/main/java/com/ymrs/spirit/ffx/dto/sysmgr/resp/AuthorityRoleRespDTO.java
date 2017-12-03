package com.ymrs.spirit.ffx.dto.sysmgr.resp;

import lombok.Data;

/**
 * 权限角色返回参数
 * 
 * @author dante
 *
 */
@Data
public class AuthorityRoleRespDTO {
	private Long id;
	private Long pid;
	private String name;
	private String code;
	private String authorityDesc;
	private Integer showOrder;
	private Long roleId;
	private Boolean hasRelRole = false;

	public Boolean getHasRelRole() {
		if (roleId != null) {
			hasRelRole = true;
		}
		return hasRelRole;
	}
}

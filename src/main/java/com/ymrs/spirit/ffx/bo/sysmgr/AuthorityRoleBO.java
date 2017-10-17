package com.ymrs.spirit.ffx.bo.sysmgr;

import lombok.Data;

/**
 * 角色、权限业务辅助BO
 * 
 * @author dante
 *
 */
@Data
public class AuthorityRoleBO {
	private Long id;
	private Long pid;
	private String name;
	private String code;
	private String authorityDesc;
	private Integer showOrder;
	private Long roleId;
	private Boolean hasRelRole = false;
}

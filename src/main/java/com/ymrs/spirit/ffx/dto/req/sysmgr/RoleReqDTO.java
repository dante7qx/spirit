package com.ymrs.spirit.ffx.dto.req.sysmgr;

import java.util.Set;

import lombok.Data;

/**
 * 角色请求参数
 * 
 * @author dante
 *
 */
@Data
public class RoleReqDTO {

	private Long id;
	private String name;
	private String roleDesc;
	private Long updateUser;
	private Set<Long> authorityIds;

}

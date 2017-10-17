package com.ymrs.spirit.ffx.dto.req.sysmgr;

import lombok.Data;

/**
 * 权限请求参数
 * 
 * @author dante
 *
 */
@Data
public class AuthorityReqDTO {
	private Long id;
	private String code;
	private String name;
	private String authorityDesc;
	private Integer showOrder;
	private Long pid;
	private Long updateUser;
	private String updateDate;
}

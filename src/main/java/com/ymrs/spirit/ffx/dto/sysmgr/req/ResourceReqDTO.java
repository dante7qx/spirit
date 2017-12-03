package com.ymrs.spirit.ffx.dto.sysmgr.req;

import lombok.Data;

/**
 * 资源请求参数
 * 
 * @author dante
 *
 */
@Data
public class ResourceReqDTO {
	private Long id;
	private String name;
	private String url;
	private Long authorityId;
	private String iconClass;
	private String fullId;
	private Integer showOrder;
	private Long pid;
	private Long updateUser;

}
package com.ymrs.spirit.ffx.dto.sysmgr.resp;

import lombok.Data;

/**
 * 资源返回参数
 * 
 * @author dante
 *
 */
@Data
public class ResourceRespDTO {
	private Long id;
	private String name;
	private String url;
	private Long authorityId;
	private String fullId;
	private Integer showOrder;
	private Long pid;
	private String iconClass;

}

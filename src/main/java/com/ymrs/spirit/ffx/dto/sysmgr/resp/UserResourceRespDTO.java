package com.ymrs.spirit.ffx.dto.sysmgr.resp;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

/**
 * 当前用户所有资源返回参数
 * 
 * @author dante
 *
 */
@Data
public class UserResourceRespDTO {
	private Long id;
	private String name;
	private String url;
	private Long pid;
	private String iconClass;
	private List<UserResourceRespDTO> children;

	public List<UserResourceRespDTO> getChildren() {
		if (this.children == null) {
			this.children = new LinkedList<>();
		}
		return children;
	}


}

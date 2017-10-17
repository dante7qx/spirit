package com.ymrs.spirit.ffx.dto.resp.sysmgr;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 当前用户所有资源返回参数
 * 
 * @author dante
 *
 */
public class UserResourceRespDTO {
	private Long id;
	private String name;
	private String url;
	private Long pid;
	private List<UserResourceRespDTO> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public List<UserResourceRespDTO> getChildren() {
		if (this.children == null) {
			this.children = Lists.newLinkedList();
		}
		return children;
	}

	public void setChildren(List<UserResourceRespDTO> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "UserResourceRespDTO [id=" + id + ", name=" + name + ", url=" + url + ", pid=" + pid + ", children="
				+ children + "]";
	}

}

package com.ymrs.spirit.ffx.dto.resp.sysmgr;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * 角色返回参数
 * 
 * @author dante
 *
 */
public class RoleRespDTO {
	private Long id;
	private String name;
	private String roleDesc;
	private Set<Long> authorityIds;
	private String updateUserName;
	private String updateDate;

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

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Set<Long> getAuthorityIds() {
		if (authorityIds == null) {
			authorityIds = Sets.newHashSet();
		}
		return authorityIds;
	}

	public void setAuthorityIds(Set<Long> authorityIds) {
		this.authorityIds = authorityIds;
	}

	@Override
	public String toString() {
		return "RoleRespDTO [id=" + id + ", name=" + name + ", roleDesc=" + roleDesc + ", authorityIds=" + authorityIds
				+ ", updateUserName=" + updateUserName + ", updateDate=" + updateDate + "]";
	}

}

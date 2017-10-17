package com.ymrs.spirit.ffx.dto.resp.sysmgr;

/**
 * 权限角色返回参数
 * 
 * @author dante
 *
 */
public class AuthorityRoleRespDTO {
	private Long id;
	private Long pid;
	private String name;
	private String code;
	private String authorityDesc;
	private Integer showOrder;
	private Long roleId;
	private Boolean hasRelRole = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAuthorityDesc() {
		return authorityDesc;
	}

	public void setAuthorityDesc(String authorityDesc) {
		this.authorityDesc = authorityDesc;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Boolean getHasRelRole() {
		if (roleId != null) {
			hasRelRole = true;
		}
		return hasRelRole;
	}

	public void setHasRelRole(Boolean hasRelRole) {
		this.hasRelRole = hasRelRole;
	}

	@Override
	public String toString() {
		return "AuthorityRoleRespDTO [id=" + id + ", pid=" + pid + ", name=" + name + ", code=" + code
				+ ", authorityDesc=" + authorityDesc + ", showOrder=" + showOrder + ", roleId=" + roleId
				+ ", hasRelRole=" + hasRelRole + "]";
	}

}

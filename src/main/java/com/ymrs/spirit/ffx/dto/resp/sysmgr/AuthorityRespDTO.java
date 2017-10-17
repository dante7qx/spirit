package com.ymrs.spirit.ffx.dto.resp.sysmgr;

/**
 * 权限返回参数
 * 
 * @author dante
 *
 */
public class AuthorityRespDTO {
	private Long id;
	private String code;
	private String name;
	private String authorityDesc;
	private Long pid;
	private Integer showOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthorityDesc() {
		return authorityDesc;
	}

	public void setAuthorityDesc(String authorityDesc) {
		this.authorityDesc = authorityDesc;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	@Override
	public String toString() {
		return "AuthorityRespDTO [id=" + id + ", code=" + code + ", name=" + name + ", authorityDesc=" + authorityDesc
				+ ", pid=" + pid + ", showOrder=" + showOrder + "]";
	}

}

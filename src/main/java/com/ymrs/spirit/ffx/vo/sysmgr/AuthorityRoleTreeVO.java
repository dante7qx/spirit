package com.ymrs.spirit.ffx.vo.sysmgr;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.ymrs.spirit.ffx.bo.sysmgr.AuthorityRoleBO;
import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;

import lombok.Data;

@Data
public class AuthorityRoleTreeVO {

	private Long id;
	private String text;
	private String state = EasyUITreeConsts.STATE_CLOSED;
	private Long pid;
	private Boolean checked;
	private List<AuthorityRoleTreeVO> children;

	public AuthorityRoleTreeVO(AuthorityRoleBO authorityRole) {
		this.id = authorityRole.getId();
		this.text = authorityRole.getName();
		this.state = authorityRole.getPid() == null ? EasyUITreeConsts.STATE_OPEN : EasyUITreeConsts.STATE_CLOSED;
		this.pid = authorityRole.getPid();
		this.checked = authorityRole.getHasRelRole();
	}

	public String getState() {
		if(CollectionUtils.isEmpty(children)) {
			state = EasyUITreeConsts.STATE_OPEN;
		}
		return state;
	}

	public void setChildren(List<AuthorityRoleTreeVO> children) {
		this.children = children;
	}

}

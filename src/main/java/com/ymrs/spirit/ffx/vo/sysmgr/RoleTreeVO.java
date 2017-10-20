package com.ymrs.spirit.ffx.vo.sysmgr;

import java.util.List;
import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;

public class RoleTreeVO {
	private Long id;
	private String text;
	private String state = EasyUITreeConsts.STATE_OPEN;
	private List<RoleTreeVO> children;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<RoleTreeVO> getChildren() {
		return children;
	}
	public void setChildren(List<RoleTreeVO> children) {
		this.children = children;
	}
	
}

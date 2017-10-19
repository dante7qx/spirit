package com.ymrs.spirit.ffx.vo.login;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Data;

/**
 * 当前登录用户资源 VO
 * 
 * @author dante
 *
 */
@Data
public class LoginUserMenuVO {
	private Long id;
	private String name;
	private String url;
	private String iconClass;
	private List<LoginUserMenuVO> children;

	public List<LoginUserMenuVO> getChildren() {
		if (this.children == null) {
			this.children = Lists.newLinkedList();
		}
		return children;
	}

}

package com.ymrs.spirit.ffx.vo.login;

import java.util.LinkedList;
import java.util.List;

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
			this.children = new LinkedList<>();
		}
		return children;
	}

}

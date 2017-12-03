package com.ymrs.spirit.ffx.vo.sysmgr;

import java.util.List;

import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.AuthorityRespDTO;

import lombok.Data;

@Data
public class AuthorityTreeVO {
	private Long id;
	private String text;
	private String iconCls;
	private String state = EasyUITreeConsts.STATE_CLOSED;
	private List<AuthorityTreeVO> children;
	private AuthorityRespDTO attributes;

	public List<AuthorityTreeVO> getChildren() {
		if (this.children == null) {
			this.children = Lists.newArrayList();
		}
		return children;
	}

}

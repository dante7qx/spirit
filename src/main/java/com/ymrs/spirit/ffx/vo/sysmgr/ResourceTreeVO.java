package com.ymrs.spirit.ffx.vo.sysmgr;

import java.util.List;

import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.ResourceRespDTO;

import lombok.Data;

@Data
public class ResourceTreeVO {
	private Long id;
	private String text;
	private String iconCls;
	private String state = EasyUITreeConsts.STATE_OPEN;
	private List<ResourceTreeVO> children;
	private ResourceRespDTO attributes;

	public List<ResourceTreeVO> getChildren() {
		if(children == null) {
			children = Lists.newArrayList();
		}
		return children;
	}

}

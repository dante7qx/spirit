package com.ymrs.spirit.ffx.vo.sysmgr;

import java.util.ArrayList;
import java.util.List;

import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.ResourceRespDTO;

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
			children = new ArrayList<>();
		}
		return children;
	}

}

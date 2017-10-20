package com.ymrs.spirit.ffx.vo.sysmgr;

import java.util.List;

import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;

import lombok.Data;

@Data
public class RoleTreeVO {
	private Long id;
	private String text;
	private String state = EasyUITreeConsts.STATE_OPEN;
	private List<RoleTreeVO> children;
}

package com.ymrs.spirit.ffx.dto.sysmgr.req;

import lombok.Data;

/**
 * 用户密码修改请求参数
 * 
 * @author dante
 *
 */
@Data
public class UserModifyPasswordReqDTO {
	
	private Long id;
	private String oldPassword;
	private String newPassword;
	private Long updateUser;
	
}

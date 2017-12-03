package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.List;

import com.ymrs.spirit.ffx.dto.sysmgr.req.UserModifyPasswordReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.req.UserReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserAuthRespDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.template.SpiritAbstractService;

/**
 * 用户 Service
 * 
 * @author dante
 *
 */
public interface UserService extends SpiritAbstractService<UserReqDTO, UserRespDTO> {
	
	/**
	 * 根据account登录
	 * 
	 * @param account
	 * @return
	 * @throws SpiritServiceException
	 */
	public UserAuthRespDTO loginAccount(String account) throws SpiritServiceException;

	/**
	 * 根据account获取用户
	 * 
	 * @param account
	 * @return
	 * @throws SpiritServiceException
	 */
	public UserRespDTO findByAccount(String account) throws SpiritServiceException;

	/**
	 * 根据roleId获取用户
	 * 
	 * @param roleId
	 * @return
	 * @throws SpiritServiceException
	 */
	public List<UserRespDTO> findByRoleId(Long roleId) throws SpiritServiceException;
	
	/**
	 * 锁定用户
	 * 
	 * @param userReqDTO
	 * @throws SpiritServiceException
	 */
	public void lockUser(UserReqDTO userReqDTO) throws SpiritServiceException;
	
	/**
	 * 解锁用户
	 * 
	 * @param userReqDTO
	 * @throws SpiritServiceException
	 */
	public void releaseLockUser(UserReqDTO userReqDTO) throws SpiritServiceException;
	
	/**
	 * 检测用户原始密码是否正确
	 * 
	 * @param userModifyPasswordReqDTO
	 * @return
	 * @throws SpiritServiceException
	 */
	public Boolean checkPassword(UserModifyPasswordReqDTO userModifyPasswordReqDTO) throws SpiritServiceException;
	
	/**
	 * 修改用户密码
	 * 
	 * @param userModifyPasswordReqDTO
	 * @throws SpiritServiceException
	 */
	public void modifyPassword(UserModifyPasswordReqDTO userModifyPasswordReqDTO) throws SpiritServiceException;
}

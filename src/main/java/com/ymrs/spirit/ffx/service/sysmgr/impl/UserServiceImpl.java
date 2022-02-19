package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ymrs.spirit.ffx.constant.sysmgr.UserConsts;
import com.ymrs.spirit.ffx.dao.sysmgr.AuthorityDAO;
import com.ymrs.spirit.ffx.dao.sysmgr.UserDAO;
import com.ymrs.spirit.ffx.dto.sysmgr.req.UserModifyPasswordReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.req.UserReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserAuthRespDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.AuthorityPO;
import com.ymrs.spirit.ffx.po.sysmgr.RolePO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.service.sysmgr.UserService;
import com.ymrs.spirit.ffx.specification.sysmgr.UserSpecification;
import com.ymrs.spirit.ffx.template.SpiritServiceTemplate;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.util.EncryptUtils;

/**
 * 用户服务实现类
 * 
 * @author dante
 *
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends SpiritServiceTemplate<UserReqDTO, UserRespDTO, UserPO> implements UserService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AuthorityDAO authorityDAO;

	/**
	 * 分页查询用户
	 */
	@Override
	public PageResult<UserRespDTO> findPage(PageReq pageReq) throws SpiritServiceException {
		return super.findEasyUIPage(pageReq);
	}

	/**
	 * 根据account获取用户
	 */
	@Override
	public UserRespDTO findByAccount(String account) throws SpiritServiceException {
		try {
			UserPO userPO = userDAO.findByAccount(account);
			return convertPoToRespDto(userPO);
		} catch (SpiritDaoException e) {
			throw new SpiritServiceException(e);
		}
	}

	/**
	 * 用户登录 
	 */
	@Override
	public UserAuthRespDTO loginAccount(String account) throws SpiritServiceException {
		UserAuthRespDTO userAuthRespDTO = null;
		try {
			UserPO userPO = userDAO.findByAccount(account);
			if(userPO == null) {
				return null;
			}
			userAuthRespDTO = new UserAuthRespDTO();
			BeanUtils.copyProperties(userPO, userAuthRespDTO);
			if("superadmin".equalsIgnoreCase(account)) {
				List<AuthorityPO> authoritys = authorityDAO.findAll();
				for (AuthorityPO authorityPO : authoritys) {
					userAuthRespDTO.getAuthoritys().add(authorityPO.getCode());
				}
			} else {
				Set<RolePO> roles = userPO.getRoles();
				if (CollectionUtils.isEmpty(roles)) {
					return userAuthRespDTO;
				}
				for (RolePO rolePo : roles) {
					Set<AuthorityPO> authoritys = rolePo.getAuthoritys();
					if (!CollectionUtils.isEmpty(authoritys)) {
						for (AuthorityPO authorityPO : authoritys) {
							userAuthRespDTO.getAuthoritys().add(authorityPO.getCode());
						}
					}
				}
			}
		} catch (SpiritDaoException e) {
			throw new SpiritServiceException(e);
		}
		return userAuthRespDTO;
	}

	/**
	 * 根据roleId获取用户
	 */
	@Override
	public List<UserRespDTO> findByRoleId(Long roleId) throws SpiritServiceException {
		List<UserRespDTO> userResps = new ArrayList<>();
		List<UserPO> users = userDAO.findAll(UserSpecification.queryUserByRoleId(roleId));
		if (!CollectionUtils.isEmpty(users)) {
			for (UserPO userPO : users) {
				UserRespDTO userResp = new UserRespDTO();
				BeanUtils.copyProperties(userPO, userResp);
				userResps.add(userResp);
			}
		}
		return userResps;
	}

	/**
	 * 持久化用户
	 */
	@Override
	@Transactional
	public UserRespDTO persist(UserReqDTO userReqDTO) throws SpiritServiceException {
		UserPO delUserPO = null;
		try {
			delUserPO = userDAO.findByAccount(userReqDTO.getAccount());
		} catch (SpiritDaoException e) {
			throw new SpiritServiceException(e);
		}
		if (delUserPO != null && UserConsts.STATUS_DEL.equals(delUserPO.getStatus())) {
			return recoverDelUser(userReqDTO, delUserPO);
		}
		UserPO userPO = convertReqDtoToPo(userReqDTO);
		Long id = userPO.getId();
		if (id == null) {
			userPO.setPassword(EncryptUtils.encrypt(userReqDTO.getPassword()));
			userPO.setLastPwdUpdateDate(DateUtils.currentDate());
			userPO.setStatus(UserConsts.STATUS_NORMAL);
		} else {
			UserPO oldUserPo = userDAO.getById(id);
			userPO.setPassword(oldUserPo.getPassword());
			userPO.setLastPwdUpdateDate(oldUserPo.getLastPwdUpdateDate());
			userPO.setStatus(oldUserPo.getStatus());
		}
		userDAO.save(userPO);
		return convertPoToRespDto(userPO);
	}

	/**
	 * 恢复已删除的用户
	 * 
	 * @param userReqDTO
	 * @param delUserPO
	 * @return
	 */
	private UserRespDTO recoverDelUser(UserReqDTO userReqDTO, UserPO delUserPO) {
		Long id = delUserPO.getId();
		UserPO delUser = convertReqDtoToPo(userReqDTO);
		delUser.setId(id);
		delUser.setPassword(EncryptUtils.encrypt(userReqDTO.getPassword()));
		delUser.setStatus(UserConsts.STATUS_NORMAL);
		userDAO.save(delUser);
		return convertPoToRespDto(delUser);
	}

	/**
	 * 逻辑删除用户
	 */
	@Override
	@Transactional
	public void delete(UserReqDTO userReqDTO) throws SpiritServiceException {
		UserPO userPO = userDAO.getById(userReqDTO.getId());
		userPO.setStatus(UserConsts.STATUS_DEL);
		userPO.setUpdateDate(DateUtils.currentDate());
		userPO.setUpdateUser(new UserPO(userReqDTO.getUpdateUser()));
		userDAO.save(userPO);
		userDAO.deleteUserRoleByUserId(userReqDTO.getId());
	}

	/**
	 * 锁定用户
	 */
	@Override
	@Transactional
	public void lockUser(UserReqDTO userReqDTO) throws SpiritServiceException {
		UserPO userPO = userDAO.getById(userReqDTO.getId());
		userPO.setStatus(UserConsts.STATUS_LOCK);
		userPO.setUpdateDate(DateUtils.currentDate());
		userPO.setUpdateUser(new UserPO(userReqDTO.getUpdateUser()));
		userDAO.save(userPO);
	}

	/**
	 * 解锁用户
	 */
	@Override
	@Transactional
	public void releaseLockUser(UserReqDTO userReqDTO) throws SpiritServiceException {
		UserPO userPO = userDAO.getById(userReqDTO.getId());
		userPO.setStatus(UserConsts.STATUS_NORMAL);
		userPO.setUpdateDate(DateUtils.currentDate());
		userPO.setUpdateUser(new UserPO(userReqDTO.getUpdateUser()));
		userDAO.save(userPO);
	}

	/**
	 * 根据id获取用户
	 */
	@Override
	public UserRespDTO findById(Long id) throws SpiritServiceException {
		UserPO userPO = userDAO.getById(id);
		return convertPoToRespDto(userPO);
	}

	/**
	 * 检查用户原始密码是否正确
	 */
	@Override
	public Boolean checkPassword(UserModifyPasswordReqDTO userModifyPasswordReqDTO) throws SpiritServiceException {
		UserPO userPO = userDAO.getById(userModifyPasswordReqDTO.getId());
		String oldPassword = userModifyPasswordReqDTO.getOldPassword();
		String dbUserPassword = userPO.getPassword();
		return EncryptUtils.match(oldPassword, dbUserPassword);
	}

	/**
	 * 修改用户密码
	 */
	@Override
	@Transactional
	public void modifyPassword(UserModifyPasswordReqDTO userModifyPasswordReqDTO) throws SpiritServiceException {
		UserPO userPO = userDAO.getById(userModifyPasswordReqDTO.getId());
		String oldPassword = userModifyPasswordReqDTO.getOldPassword();
		String dbUserPassword = userPO.getPassword();
		if (!EncryptUtils.match(oldPassword, dbUserPassword)) {
			throw new SpiritServiceException("原始密码错误");
		}
		String newPassword = userModifyPasswordReqDTO.getNewPassword();
		userPO.setPassword(EncryptUtils.encrypt(newPassword));
		if (userModifyPasswordReqDTO.getUpdateUser() != null) {
			userPO.setUpdateUser(new UserPO(userModifyPasswordReqDTO.getUpdateUser()));
		}
		userPO.setLastPwdUpdateDate(DateUtils.currentDate());
		userPO.setUpdateDate(DateUtils.currentDate());
		userDAO.save(userPO);
	}

	/**
	 * 将 UserReqDTO 转化为 UserPO
	 */
	@Override
	protected UserPO convertReqDtoToPo(UserReqDTO userReqDto) {
		UserPO userPO = new UserPO();
		BeanUtils.copyProperties(userReqDto, userPO, "updateUser");
		userPO.setUpdateDate(DateUtils.currentDate());
		if(userReqDto.getLdapUser() == null) {
			userReqDto.setLdapUser(false);
		}
		if (userReqDto.getUpdateUser() != null) {
			userPO.setUpdateUser(new UserPO(userReqDto.getUpdateUser()));
		}
		Set<Long> roleIds = userReqDto.getRoleIds();
		if (!CollectionUtils.isEmpty(roleIds)) {
			Set<RolePO> roles = new HashSet<>();
			for (Long roleId : roleIds) {
				if (roleId < 0) {
					continue;
				}
				roles.add(new RolePO(roleId));
			}
			userPO.setRoles(roles);
		}
		return userPO;
	}

	/**
	 * 将 UserPO 转化为 UserRespDTO
	 */
	@Override
	protected UserRespDTO convertPoToRespDto(UserPO userPO) {
		UserRespDTO userRespDTO = new UserRespDTO();
		if (userPO != null) {
			BeanUtils.copyProperties(userPO, userRespDTO, "updateUser", "updateDate");
			if (userPO.getUpdateUser() != null) {
				userRespDTO.setUpdateUserName(userPO.getUpdateUser().getName());
			}
			if (userPO.getUpdateDate() != null) {
				userRespDTO.setUpdateDate(DateUtils.formatDateTime(userPO.getUpdateDate()));
			}
			Set<RolePO> roles = userPO.getRoles();
			if (!CollectionUtils.isEmpty(roles)) {
				for (RolePO role : roles) {
					userRespDTO.getRoleIds().add(role.getId());
				}
			}
		}
		return userRespDTO;
	}

	/**
	 * 构造用户查询条件
	 */
	@Override
	protected Specification<UserPO> buildSpecification(Map<String, Object> filter) {
		return UserSpecification.querySpecification(filter);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws SpiritServiceException {
		// 物理删除用户，用户采用逻辑删除，本方法不做实现
	}

}

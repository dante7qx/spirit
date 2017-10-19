package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ymrs.spirit.ffx.bo.sysmgr.AuthorityRoleBO;
import com.ymrs.spirit.ffx.dao.sysmgr.RoleDAO;
import com.ymrs.spirit.ffx.dto.req.sysmgr.RoleReqDTO;
import com.ymrs.spirit.ffx.dto.req.sysmgr.specification.RoleSpecification;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.AuthorityRoleRespDTO;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.RoleRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.mapper.sysmgr.AuthorityMapper;
import com.ymrs.spirit.ffx.po.sysmgr.AuthorityPO;
import com.ymrs.spirit.ffx.po.sysmgr.RolePO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.service.sysmgr.RoleService;
import com.ymrs.spirit.ffx.template.SpiritServiceTemplate;
import com.ymrs.spirit.ffx.util.DateUtils;

/**
 * 角色服务实现类
 * 
 * @author dante
 *
 */
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl extends SpiritServiceTemplate<RoleReqDTO, RoleRespDTO, RolePO> implements RoleService {

	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private AuthorityMapper authorityMapper;

	@Override
	public PageResult<RoleRespDTO> findPage(PageReq pageReq) throws SpiritServiceException {
		return super.findEasyUIPage(pageReq);
	}

	@Override
	@Transactional
	public RoleRespDTO persist(RoleReqDTO roleReqDTO) throws SpiritServiceException {
		RolePO rolePO = roleDAO.save(convertReqDtoToPo(roleReqDTO));
		return convertPoToRespDto(rolePO);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws SpiritServiceException {
		roleDAO.delete(id);
	}

	@Override
	public RoleRespDTO findById(Long id) throws SpiritServiceException {
		return convertPoToRespDto(roleDAO.findOne(id));
	}

	@Override
	public List<AuthorityRoleRespDTO> findAuthorityRoleByRoleId(Long roleId) throws SpiritServiceException {
		List<AuthorityRoleRespDTO> authorityRoleRespDtos = Lists.newArrayList();
		try {
//			List<AuthorityRoleBO> authorityRoleBOs = JpaEntityConvertUtils.castEntity(authorityDAO.findAuthorityRoleByRoleId(roleId), AuthorityRoleBO.class);
			List<AuthorityRoleBO> authorityRoleBOs = authorityMapper.findAuthorityRoleByRoleId(roleId);
			if (!CollectionUtils.isEmpty(authorityRoleBOs)) {
				for (AuthorityRoleBO authorityRoleBO : authorityRoleBOs) {
					AuthorityRoleRespDTO authorityRoleRespDTO = new AuthorityRoleRespDTO();
					BeanUtils.copyProperties(authorityRoleBO, authorityRoleRespDTO);
					authorityRoleRespDtos.add(authorityRoleRespDTO);
				}
			}
		} catch (SpiritDaoException e) {
			throw new SpiritServiceException(e);
		}
		return authorityRoleRespDtos;
	}

	@Override
	public List<RoleRespDTO> findAllRoles() throws SpiritServiceException {
		List<RoleRespDTO> roleRespDTOs = Lists.newLinkedList();
		List<RolePO> rolePOs = roleDAO.findAll(new Sort(Direction.ASC, "name"));
		if (!CollectionUtils.isEmpty(rolePOs)) {
			for (RolePO rolePO : rolePOs) {
				roleRespDTOs.add(convertPoToRespDto(rolePO));
			}
		}
		return roleRespDTOs;
	}

	@Override
	protected RolePO convertReqDtoToPo(RoleReqDTO roleReqDTO) {
		RolePO rolePO = new RolePO();
		if (roleReqDTO != null) {
			BeanUtils.copyProperties(roleReqDTO, rolePO, "updateUser");
			rolePO.setUpdateDate(DateUtils.currentDate());
			if (roleReqDTO.getUpdateUser() != null) {
				rolePO.setUpdateUser(new UserPO(roleReqDTO.getUpdateUser()));
			}
			Set<Long> authorityIds = roleReqDTO.getAuthorityIds();
			if (!CollectionUtils.isEmpty(authorityIds)) {
				Set<AuthorityPO> authorityPOs = Sets.newHashSet();
				for (Long authorityId : authorityIds) {
					authorityPOs.add(new AuthorityPO(authorityId));
				}
				rolePO.setAuthoritys(authorityPOs);
			}
		}
		return rolePO;
	}

	@Override
	protected RoleRespDTO convertPoToRespDto(RolePO rolePO) {
		RoleRespDTO roleRespDTO = new RoleRespDTO();
		if (rolePO != null) {
			BeanUtils.copyProperties(rolePO, roleRespDTO, "updateUser", "updateDate");
			if (rolePO.getUpdateUser() != null) {
				roleRespDTO.setUpdateUserName(rolePO.getUpdateUser().getName());
			}
			if (rolePO.getUpdateDate() != null) {
				roleRespDTO.setUpdateDate(DateUtils.formatDateTime(rolePO.getUpdateDate()));
			}
			Set<AuthorityPO> authoritys = rolePO.getAuthoritys();
			if (!CollectionUtils.isEmpty(authoritys)) {
				for (AuthorityPO authority : authoritys) {
					roleRespDTO.getAuthorityIds().add(authority.getId());
				}
			}
		}
		return roleRespDTO;
	}

	@Override
	protected Specification<RolePO> buildSpecification(Map<String, Object> filter) {
		return RoleSpecification.querySpecification(filter);
	}

	@Override
	public void delete(RoleReqDTO reqDTO) throws SpiritServiceException {
		// 逻辑删除，此功能使用物理删除，故本方法不做实现
	}
}

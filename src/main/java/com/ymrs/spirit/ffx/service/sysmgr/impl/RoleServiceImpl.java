package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

import com.ymrs.spirit.ffx.bo.sysmgr.AuthorityRoleBO;
import com.ymrs.spirit.ffx.dao.sysmgr.RoleDAO;
import com.ymrs.spirit.ffx.dto.sysmgr.req.RoleReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.RoleRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.mapper.sysmgr.AuthorityMapper;
import com.ymrs.spirit.ffx.po.sysmgr.AuthorityPO;
import com.ymrs.spirit.ffx.po.sysmgr.RolePO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.service.sysmgr.RoleService;
import com.ymrs.spirit.ffx.specification.sysmgr.RoleSpecification;
import com.ymrs.spirit.ffx.template.SpiritServiceTemplate;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.AuthorityRoleTreeVO;
import com.ymrs.spirit.ffx.vo.sysmgr.RoleTreeVO;

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
		roleDAO.deleteById(id);
	}

	@Override
	public RoleRespDTO findById(Long id) throws SpiritServiceException {
		return convertPoToRespDto(roleDAO.getById(id));
	}
	
	@Override
	public List<AuthorityRoleTreeVO> findAuthoritysByRoleId(Long roleId) throws SpiritServiceException {
		List<AuthorityRoleTreeVO> authRoleTrees = new ArrayList<>();
		try {
			List<AuthorityRoleBO> authorityRoleBOs = authorityMapper.findAuthorityRoleByRoleId(roleId);
			Map<String, AuthorityRoleTreeVO> treeMap = new LinkedHashMap<>();
			for (AuthorityRoleBO authorityRole : authorityRoleBOs) {
				AuthorityRoleTreeVO roleAuthTree = new AuthorityRoleTreeVO(authorityRole);
				treeMap.put("_"+authorityRole.getId(), roleAuthTree);
			}
			Set<String> keySet = treeMap.keySet();
			Iterator<String> iterNode = keySet.iterator();
			while(iterNode.hasNext()) {
				String key = (String) iterNode.next();
				AuthorityRoleTreeVO tempTree = treeMap.get(key);
				Long pid = tempTree.getPid();
				if(pid == null) {
					authRoleTrees.add(tempTree);
				} else {
					Set<String> childKeySet = treeMap.keySet();
					Iterator<String> iterChild = childKeySet.iterator();
					while(iterChild.hasNext()) {
						String childKey = (String) iterChild.next();
						AuthorityRoleTreeVO sameTree = treeMap.get(childKey);
						if(childKey.equals("_"+pid)) {
							if(CollectionUtils.isEmpty(sameTree.getChildren())) {
								List<AuthorityRoleTreeVO> subTrees = new ArrayList<>();
								subTrees.add(tempTree);
								sameTree.setChildren(subTrees);
							} else {
								sameTree.getChildren().add(tempTree);
							}
						}
					}
				}
			}
		} catch (SpiritDaoException e) {
			throw new SpiritServiceException(e);
		}
		return authRoleTrees;
	}

	@Override
	public List<RoleRespDTO> findAllRoles() throws SpiritServiceException {
		List<RoleRespDTO> roleRespDTOs = new LinkedList<>();
		List<RolePO> rolePOs = roleDAO.findAll(Sort.by(Direction.ASC, "name"));
		if (!CollectionUtils.isEmpty(rolePOs)) {
			for (RolePO rolePO : rolePOs) {
				roleRespDTOs.add(convertPoToRespDto(rolePO));
			}
		}
		return roleRespDTOs;
	}
	
	@Override
	public List<RoleTreeVO> findRoleTree() throws SpiritServiceException {
		List<RolePO> rolePOs = roleDAO.findAll(Sort.by(Direction.ASC, "name"));
		List<RoleTreeVO> roleTreeVOs = new LinkedList<>();
		if(!CollectionUtils.isEmpty(rolePOs)) {
			for (RolePO rolePO : rolePOs) {
				RoleTreeVO roleTreeVO = new RoleTreeVO();
				roleTreeVO.setId(rolePO.getId());
				roleTreeVO.setText(rolePO.getName());
				roleTreeVOs.add(roleTreeVO);
			}
		}
		return roleTreeVOs;
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
				Set<AuthorityPO> authorityPOs = new HashSet<>();
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

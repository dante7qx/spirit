package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;
import com.ymrs.spirit.ffx.dao.sysmgr.ResourceDAO;
import com.ymrs.spirit.ffx.dto.sysmgr.req.ResourceReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.ResourceRespDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserResourceRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.AuthorityPO;
import com.ymrs.spirit.ffx.po.sysmgr.ResourcePO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;
import com.ymrs.spirit.ffx.pub.EasyUIDragTreeReq;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.service.sysmgr.ResourceService;
import com.ymrs.spirit.ffx.specification.sysmgr.ResourceSpecification;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.ResourceTreeVO;

/**
 * 资源服务实现类
 * 
 * @author dante
 *
 */
@Service
@Transactional(readOnly = true)
public class ResourceServiceImpl implements ResourceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceImpl.class);

	@Autowired
	private ResourceDAO resourceDAO;

	@Override
	public PageResult<ResourceRespDTO> findPage(PageReq pageReq) throws SpiritServiceException {
		return null;
	}

	@Override
	public List<ResourceRespDTO> findRootResource() throws SpiritServiceException {
		List<ResourceRespDTO> resourceRespDTOs = Lists.newArrayList();
		try {
			List<ResourcePO> pos = resourceDAO.findRootResource();
			for (ResourcePO po : pos) {
				resourceRespDTOs.add(convertPoToRespDto(po));
			}
		} catch (SpiritDaoException e) {
			LOGGER.error("ResourceDAO findRootResource error.", e);
			throw new SpiritServiceException("ResourceDAO findRootResource error.", e);
		}
		return resourceRespDTOs;
	}

	@Override
	public List<ResourceRespDTO> findByPid(Long pid) throws SpiritServiceException {
		List<ResourceRespDTO> resourceRespDTOs = Lists.newArrayList();
		try {
			List<ResourcePO> pos = resourceDAO.findByPid(pid);
			for (ResourcePO po : pos) {
				resourceRespDTOs.add(convertPoToRespDto(po));
			}
		} catch (SpiritDaoException e) {
			LOGGER.error("ResourceDAO findByPid {} error.", pid, e);
			throw new SpiritServiceException("ResourceDAO findByPid error.", e);
		}
		return resourceRespDTOs;
	}

	@Override
	@Transactional
	public ResourceRespDTO persist(ResourceReqDTO resourceReqDTO) throws SpiritServiceException {
		ResourcePO resourcePO = convertReqDtoToPo(resourceReqDTO);
		resourceDAO.save(resourcePO);
		StringBuilder fullIdBuilder = new StringBuilder(resourcePO.getId() + "");
		this.buildFullId(resourceReqDTO.getPid(), fullIdBuilder);
		resourcePO.setFullId(fullIdBuilder.toString());
		resourceDAO.save(resourcePO);
		return convertPoToRespDto(resourcePO);
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws SpiritServiceException {
		List<ResourcePO> resources = null;
		try {
			resources = resourceDAO.findByPid(id);
		} catch (SpiritDaoException e) {
			LOGGER.error("ResourceDAO findByParentId {} error.", id, e);
			throw new SpiritServiceException("ResourceDAO findByParentId error.", e);
		}
		if (!CollectionUtils.isEmpty(resources)) {
			resourceDAO.deleteInBatch(resources);
		}
		resourceDAO.delete(id);
	}

	private void buildFullId(Long pid, StringBuilder fullIdBuilder) {
		if (pid == null) {
			return;
		}
		if(pid != null && pid < 0) {
			return;
		}
		fullIdBuilder.append("-").append(pid);
		ResourcePO parentResource = resourceDAO.findOne(pid);
		if (parentResource.getParentResource() != null) {
			buildFullId(parentResource.getParentResource().getId(), fullIdBuilder);
		}
	}

	@Override
	public ResourceRespDTO findById(Long id) throws SpiritServiceException {
		return convertPoToRespDto(resourceDAO.findOne(id));
	}

	@Override
	public List<UserResourceRespDTO> findUserResourceByUserId(Long userId) throws SpiritServiceException {
		List<Long> pids = Lists.newArrayList();
		try {
			pids = resourceDAO.findAllParentId();
		} catch (SpiritDaoException e) {
			LOGGER.error("ResourceDAO findAllParentId {} error.", e);
			throw new SpiritServiceException("ResourceDAO findAllParentId error.", e);
		}
		List<ResourcePO> resourcePOs = resourceDAO.findAll(ResourceSpecification.findResourceTreeByUserId(userId));

		Map<Long, UserResourceRespDTO> menuTreeMap = Maps.newLinkedHashMap();
		for (ResourcePO resource : resourcePOs) {
			menuTreeMap.put(resource.getId(), convertPoToUserResourceRespDTO(resource));
		}
		List<UserResourceRespDTO> userResources = Lists.newLinkedList();
		List<UserResourceRespDTO> menus = buildUserResourceTree(pids, menuTreeMap);
		for (UserResourceRespDTO menu : menus) {
			if (!menu.getChildren().isEmpty() && !pids.contains(menu.getPid())) {
				userResources.add(menu);
			}
		}
		return userResources;
	}
	
	@Override
	public List<ResourceTreeVO> findResourceTrees() throws SpiritServiceException {
		List<ResourceTreeVO> roots = Lists.newArrayList();
		try {
			List<ResourcePO> resourcePOs = resourceDAO.findRootResource();
			if (CollectionUtils.isEmpty(resourcePOs)) {
				return roots;
			}
			for (ResourcePO resourcePO : resourcePOs) {
				ResourceTreeVO treeVO = convertToResourceTreeVO(resourcePO);
				roots.add(treeVO);
				buildResourceTree(treeVO);
			}
		} catch (SpiritDaoException e) {
			LOGGER.error("ResourceDAO findRootResource error.", e);
			throw new SpiritServiceException("ResourceDAO findRootResource error.", e);
		}
		return roots;
	}

	@Override
	public void updateResourceWhenDrag(EasyUIDragTreeReq dragTreeReq) throws SpiritServiceException {
		String point = dragTreeReq.getPoint();
		if (EasyUITreeConsts.POINT_APPEND.equalsIgnoreCase(point)) {
			handleDragAppend(dragTreeReq.getTargetId(), dragTreeReq.getSourceId(), dragTreeReq.getUpdateUser());
		} else if (EasyUITreeConsts.POINT_TOP.equalsIgnoreCase(point)) {
			handleDragTop(dragTreeReq.getTargetPid(), dragTreeReq.getTargetShowOrder(), dragTreeReq.getSourceId(),
					dragTreeReq.getUpdateUser());
		} else if (EasyUITreeConsts.POINT_BOTTOM.equalsIgnoreCase(point)) {
			handleDragBottom(dragTreeReq.getTargetPid(), dragTreeReq.getTargetShowOrder(), dragTreeReq.getSourceId(),
					dragTreeReq.getUpdateUser());
		} else {
			throw new SpiritServiceException("Drag point mush match 'append' 'top' 'bottom'");
		}
	}
	
	private void buildResourceTree(ResourceTreeVO tree) throws SpiritServiceException {
		Long id = tree.getId();
		List<ResourcePO> childResources;
		try {
			childResources = resourceDAO.findByPid(id);
			if (!CollectionUtils.isEmpty(childResources)) {
				for (ResourcePO childResource : childResources) {
					ResourceTreeVO childTree = convertToResourceTreeVO(childResource);
					tree.getChildren().add(childTree);
					buildResourceTree(childTree);
				}
			}
		} catch (SpiritDaoException e) {
			LOGGER.error("ResourceDAO findByPid {} error.", id, e);
			throw new SpiritServiceException("ResourceDAO findByPid error.", e);
		}
		
	}
	
	private ResourceTreeVO convertToResourceTreeVO(ResourcePO resourcePO) {
		ResourceTreeVO tree = new ResourceTreeVO();
		tree.setId(resourcePO.getId());
		tree.setText(resourcePO.getName());
		tree.setIconCls(resourcePO.getIconClass());
		tree.setAttributes(convertPoToRespDto(resourcePO));
		return tree;
	}

	protected ResourcePO convertReqDtoToPo(ResourceReqDTO resourceReqDTO) {
		ResourcePO resourcePO = new ResourcePO();
		if (resourceReqDTO != null) {
			Long pid = resourceReqDTO.getPid();
			BeanUtils.copyProperties(resourceReqDTO, resourcePO, "updateUser");
			resourcePO.setUpdateDate(DateUtils.currentDate());
			if (pid != null && pid > 0) {
				resourcePO.setParentResource(new ResourcePO(pid));
			}
			if (resourceReqDTO.getAuthorityId() != null) {
				resourcePO.setAuthority(new AuthorityPO(resourceReqDTO.getAuthorityId()));
			}
			if (resourceReqDTO.getUpdateUser() != null) {
				resourcePO.setUpdateUser(new UserPO(resourceReqDTO.getUpdateUser()));
			}
		}
		return resourcePO;
	}

	protected ResourceRespDTO convertPoToRespDto(ResourcePO resourcePO) {
		ResourceRespDTO resourceRespDTO = new ResourceRespDTO();
		BeanUtils.copyProperties(resourcePO, resourceRespDTO);
		resourceRespDTO.setAuthorityId(resourcePO.getAuthority().getId());
		ResourcePO parentResourcePO = resourcePO.getParentResource();
		if (parentResourcePO != null) {
			resourceRespDTO.setPid(parentResourcePO.getId());
		}
		return resourceRespDTO;
	}

	protected UserResourceRespDTO convertPoToUserResourceRespDTO(ResourcePO resourcePO) {
		UserResourceRespDTO userResourceRespDTO = new UserResourceRespDTO();
		BeanUtils.copyProperties(resourcePO, userResourceRespDTO);
		ResourcePO parentResourcePO = resourcePO.getParentResource();
		if (parentResourcePO != null) {
			userResourceRespDTO.setPid(parentResourcePO.getId());
		}
		return userResourceRespDTO;
	}

	private List<UserResourceRespDTO> buildUserResourceTree(List<Long> pids,
			Map<Long, UserResourceRespDTO> menuTreeMap) {
		List<UserResourceRespDTO> menus = Lists.newLinkedList();
		Set<Long> keySet = menuTreeMap.keySet();
		for (Long key : keySet) {
			UserResourceRespDTO menu = menuTreeMap.get(key);
			Long parentId = menu.getPid();
			if (parentId == null) {
				menus.add(menu);
			} else {
				UserResourceRespDTO m = menuTreeMap.get(parentId);
				if (m != null) {
					m.getChildren().add(menu);
				}
			}
		}

		if (!CollectionUtils.isEmpty(pids)) {
			for (Long pid : pids) {
				removeEmptyMenu(pid, menuTreeMap);
			}
		}
		return menus;
	}

	private void removeEmptyMenu(Long pid, Map<Long, UserResourceRespDTO> menuTreeMap) {
		UserResourceRespDTO menu = menuTreeMap.get(pid);
		if (menu == null || !CollectionUtils.isEmpty(menu.getChildren())) {
			return;
		}
		Long parentId = menu.getPid();
		UserResourceRespDTO subMenu = menuTreeMap.get(parentId);
		if (subMenu != null) {
			subMenu.getChildren().remove(menu);
			removeEmptyMenu(subMenu.getId(), menuTreeMap);
		}
	}

	@Override
	public void delete(ResourceReqDTO reqDTO) throws SpiritServiceException {
		// 逻辑删除，业务为物理删除，本方法不做实现
	}
	
	/**
	 * 将源节点移动到目标节点的上方 showOrder(source) = showOrder(target) - 1, pid(source) =
	 * pid(target)
	 * 
	 * @param targetPid
	 * @param targetShowOrder
	 * @param sourceId
	 * @throws SpiritServiceException
	 */
	private void handleDragTop(Long targetPid, int targetShowOrder, Long sourceId, Long updateUser)
			throws SpiritServiceException {
		ResourcePO sourceResource = resourceDAO.findOne(sourceId);
		sourceResource.setParentResource(new ResourcePO(targetPid));
		sourceResource.setShowOrder(targetShowOrder > 1 ? targetShowOrder - 1 : 1);
		sourceResource.setUpdateUser(new UserPO(updateUser));
		resourceDAO.save(sourceResource);
	}

	/**
	 * 将源节点移动到目标节点的下方 showOrder(source) = showOrder(target) - 1, pid(source) =
	 * pid(target)
	 * 
	 * @param targetPid
	 * @param targetShowOrder
	 * @param sourceId
	 * @throws SpiritServiceException
	 */
	private void handleDragBottom(Long targetPid, int targetShowOrder, Long sourceId, Long updateUser)
			throws SpiritServiceException {
		ResourcePO sourceResource = resourceDAO.findOne(sourceId);
		sourceResource.setParentResource(new ResourcePO(targetPid));
		sourceResource.setShowOrder(targetShowOrder + 1);
		sourceResource.setUpdateUser(new UserPO(updateUser));
		resourceDAO.save(sourceResource);
	}

	/**
	 * 将源节点移动到目标节点内 pid(source) = id(target)
	 * 
	 * @param targetId
	 * @param sourceId
	 * @throws SpiritServiceException
	 */
	private void handleDragAppend(Long targetId, Long sourceId, Long updateUser) throws SpiritServiceException {
		ResourcePO sourceResource = resourceDAO.findOne(sourceId);
		if (targetId > 0) {
			sourceResource.setParentResource(new ResourcePO(targetId));
		} else {
			sourceResource.setParentResource(null);
		}
		sourceResource.setUpdateUser(new UserPO(updateUser));
		resourceDAO.save(sourceResource);
	}

}

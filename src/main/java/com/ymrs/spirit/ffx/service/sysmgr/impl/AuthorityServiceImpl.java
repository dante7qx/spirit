package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;
import com.ymrs.spirit.ffx.dao.sysmgr.AuthorityDAO;
import com.ymrs.spirit.ffx.dto.sysmgr.req.AuthorityReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.AuthorityRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.AuthorityPO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;
import com.ymrs.spirit.ffx.pub.EasyUIDragTreeReq;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.service.sysmgr.AuthorityService;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.AuthorityTreeVO;

/**
 * 权限服务实现类
 * 
 * @author dante
 *
 */
@Service
@Transactional(readOnly = true)
public class AuthorityServiceImpl implements AuthorityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityServiceImpl.class);

	@Autowired
	private AuthorityDAO authorityDAO;

	@Override
	public PageResult<AuthorityRespDTO> findPage(PageReq pageReq) throws SpiritServiceException {
		return null;
	}

	@Override
	public List<AuthorityRespDTO> findRootAuthority() throws SpiritServiceException {
		List<AuthorityRespDTO> authorityRespDTOs = new ArrayList<>();
		try {
			List<AuthorityPO> pos = authorityDAO.findRootAuthority();
			for (AuthorityPO po : pos) {
				authorityRespDTOs.add(convertPoToRespDto(po));
			}
		} catch (SpiritDaoException e) {
			LOGGER.error("AuthorityDAO findRootAuthority error.", e);
			throw new SpiritServiceException("AuthorityDAO findRootAuthority error.", e);
		}
		return authorityRespDTOs;
	}

	@Override
	public List<AuthorityRespDTO> findByPid(Long pid) throws SpiritServiceException {
		List<AuthorityRespDTO> authorityRespDTOs = new ArrayList<>();
		try {
			List<AuthorityPO> pos = authorityDAO.findByParentId(pid);
			for (AuthorityPO po : pos) {
				authorityRespDTOs.add(convertPoToRespDto(po));
			}
		} catch (SpiritDaoException e) {
			LOGGER.error("AuthorityDAO findByPid {} error.", pid, e);
			throw new SpiritServiceException("AuthorityDAO findByPid error.", e);
		}
		return authorityRespDTOs;
	}

	@Override
	@Transactional
	public AuthorityRespDTO persist(AuthorityReqDTO authorityReqDTO) throws SpiritServiceException {
		return convertPoToRespDto(authorityDAO.save(convertReqDtoToPo(authorityReqDTO)));
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws SpiritServiceException {
		List<AuthorityPO> authoritys = null;
		try {
			authoritys = authorityDAO.findByParentId(id);
		} catch (SpiritDaoException e) {
			LOGGER.error("AuthorityDAO findByParentId {} error.", id, e);
			throw new SpiritServiceException("AuthorityDAO findByParentId error.", e);
		}

		if (!CollectionUtils.isEmpty(authoritys)) {
			authorityDAO.deleteInBatch(authoritys);
		}
		authorityDAO.deleteById(id);
	}

	@Override
	public AuthorityRespDTO findById(Long id) throws SpiritServiceException {
		AuthorityPO authorityPO = authorityDAO.findById(id).orElse(null);
		return convertPoToRespDto(authorityPO);
	}
	
	@Override
	public List<AuthorityTreeVO> findAuthorityTrees() throws SpiritServiceException {
		List<AuthorityTreeVO> trees = new ArrayList<>();
		List<AuthorityRespDTO> authoritys = findRootAuthority();
		if(CollectionUtils.isEmpty(authoritys)) {
			return trees;
		}
		for (AuthorityRespDTO authorityVO : authoritys) {
			AuthorityTreeVO treeVO = convertToTree(authorityVO);
			trees.add(treeVO);
			buildAuthorityTree(treeVO);
		}
		return trees;
	}
	
	private void buildAuthorityTree(AuthorityTreeVO treeVO) throws SpiritServiceException {
		Long id = treeVO.getId();
		List<AuthorityRespDTO> childVos = findByPid(id);
		if(!CollectionUtils.isEmpty(childVos)) {
			for (AuthorityRespDTO authorityVO : childVos) {
				AuthorityTreeVO childTreeVO = convertToTree(authorityVO);
				treeVO.getChildren().add(childTreeVO);
				buildAuthorityTree(childTreeVO);
			}
		} else {
			treeVO.setState(EasyUITreeConsts.STATE_OPEN);
		}
	}

	@Override
	@Transactional
	public void updateAuthorityWhenDrag(EasyUIDragTreeReq dragTreeReq) throws SpiritServiceException {
		String point = dragTreeReq.getPoint();
		if(EasyUITreeConsts.POINT_APPEND.equalsIgnoreCase(point)) {
			handleDragAppend(dragTreeReq.getTargetId(), dragTreeReq.getSourceId(), dragTreeReq.getUpdateUser());
		} else if(EasyUITreeConsts.POINT_TOP.equalsIgnoreCase(point)) {
			handleDragTop(dragTreeReq.getTargetPid(), dragTreeReq.getTargetShowOrder(), dragTreeReq.getSourceId(), dragTreeReq.getUpdateUser());
		} else if(EasyUITreeConsts.POINT_BOTTOM.equalsIgnoreCase(point)) {
			handleDragBottom(dragTreeReq.getTargetPid(), dragTreeReq.getTargetShowOrder(), dragTreeReq.getSourceId(), dragTreeReq.getUpdateUser());
		} else {
			throw new SpiritServiceException("Drag point mush match 'append' 'top' 'bottom'");
		}
	}
	
	/**
	 * 将源节点移动到目标节点的上方
	 * showOrder(source) = showOrder(target) - 1, pid(source) = pid(target)
	 * 
	 * @param targetPid
	 * @param targetShowOrder
	 * @param sourceId
	 * @throws SpiritServiceException 
	 */
	private void handleDragTop(Long targetPid, int targetShowOrder, Long sourceId, Long updateUser) throws SpiritServiceException {
		AuthorityPO authorityPO = authorityDAO.findById(sourceId).orElse(null);
		authorityPO.setParentAuthority(new AuthorityPO(targetPid));
		authorityPO.setShowOrder(targetShowOrder > 1 ?  targetShowOrder - 1 : 1);
		authorityPO.setUpdateUser(new UserPO(updateUser));
		authorityDAO.save(authorityPO);
	}
	
	/**
	 * 将源节点移动到目标节点的下方
	 * showOrder(source) = showOrder(target) - 1, pid(source) = pid(target)
	 * 
	 * @param targetPid
	 * @param targetShowOrder
	 * @param sourceId
	 * @throws SpiritServiceException 
	 */
	private void handleDragBottom(Long targetPid, int targetShowOrder, Long sourceId, Long updateUser) throws SpiritServiceException {
		AuthorityPO authorityPO = authorityDAO.findById(sourceId).orElse(null);
		authorityPO.setParentAuthority(new AuthorityPO(targetPid));
		authorityPO.setShowOrder(targetShowOrder + 1);
		authorityPO.setUpdateUser(new UserPO(updateUser));
		authorityDAO.save(authorityPO);
	}
	
	/**
	 * 将源节点移动到目标节点内
	 * pid(source) = id(target)
	 * 
	 * @param targetId
	 * @param sourceId
	 * @throws SpiritServiceException 
	 */
	private void handleDragAppend(Long targetId, Long sourceId, Long updateUser) throws SpiritServiceException {
		AuthorityPO authorityPO = authorityDAO.findById(sourceId).orElse(null);
		if(targetId > 0) {
			authorityPO.setParentAuthority(new AuthorityPO(targetId));
		} else {
			authorityPO.setParentAuthority(null);
		}
		authorityPO.setUpdateUser(new UserPO(updateUser));
		authorityDAO.save(authorityPO);
	}
	
	protected AuthorityPO convertReqDtoToPo(AuthorityReqDTO authorityReqDTO) {
		AuthorityPO authorityPO = new AuthorityPO();
		if (authorityReqDTO != null) {
			BeanUtils.copyProperties(authorityReqDTO, authorityPO, "updateUser");
			authorityPO.setUpdateDate(DateUtils.currentDate());
			if (authorityReqDTO.getUpdateUser() != null) {
				authorityPO.setUpdateUser(new UserPO(authorityReqDTO.getUpdateUser()));
			}
			if(authorityReqDTO.getPid() != null) {
				authorityPO.setParentAuthority(new AuthorityPO(authorityReqDTO.getPid()));
			}
		}
		return authorityPO;
	}

	protected AuthorityRespDTO convertPoToRespDto(AuthorityPO authorityPO) {
		AuthorityRespDTO authorityRespDTO = new AuthorityRespDTO();
		BeanUtils.copyProperties(authorityPO, authorityRespDTO);
		AuthorityPO parentAuthorityPO = authorityPO.getParentAuthority();
		if(parentAuthorityPO != null) {
			authorityRespDTO.setPid(parentAuthorityPO.getId());
		}
		return authorityRespDTO;
	}
	
	private AuthorityTreeVO convertToTree(AuthorityRespDTO authorityVO) {
		AuthorityTreeVO treeVO = new AuthorityTreeVO();
		treeVO.setId(authorityVO.getId());
		treeVO.setText(authorityVO.getName());
		treeVO.setAttributes(authorityVO);
		return treeVO;
	}

	@Override
	public void delete(AuthorityReqDTO reqDTO) throws SpiritServiceException {
		// 逻辑删除，此功能使用物理删除，故本方法不做实现
	}

}

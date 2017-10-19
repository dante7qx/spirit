package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.dao.sysmgr.AuthorityDAO;
import com.ymrs.spirit.ffx.dto.req.sysmgr.AuthorityReqDTO;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.AuthorityRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.AuthorityPO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.service.sysmgr.AuthorityService;
import com.ymrs.spirit.ffx.util.DateUtils;

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
		List<AuthorityRespDTO> authorityRespDTOs = Lists.newArrayList();
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
		List<AuthorityRespDTO> authorityRespDTOs = Lists.newArrayList();
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
		authorityDAO.delete(id);

	}

	@Override
	public AuthorityRespDTO findById(Long id) throws SpiritServiceException {
		AuthorityPO authorityPO = authorityDAO.findOne(id);
		return convertPoToRespDto(authorityPO);
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

	@Override
	public void delete(AuthorityReqDTO reqDTO) throws SpiritServiceException {
		// 逻辑删除，此功能使用物理删除，故本方法不做实现
	}

}

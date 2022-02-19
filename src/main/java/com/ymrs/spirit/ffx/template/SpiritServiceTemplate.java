package com.ymrs.spirit.ffx.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.CollectionUtils;

import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResp;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.util.SpiritDaoUtils;

/**
 * RESTful API 服务层公共模板方法
 * 
 * @author dante
 *
 * @param <REQ> ReqDTO
 * @param <RESP> RespDTO
 * @param <P> 数据库PO
 */
public abstract class SpiritServiceTemplate<REQ, RESP, P> {
	
	@Autowired
	private JpaRepository<P, Long> jpaRepository;
	@Autowired
	private JpaSpecificationExecutor<P> specificationExecutor;
	
	/**
	 * 公共分页模板方法
	 * 
	 * @param pageReq
	 * @return
	 */
	protected PageResp<RESP> findCommonPage(PageReq pageReq) throws SpiritServiceException {
		int pageNo = pageReq.getPageNo();
		int pageSize = pageReq.getPageSize();
		String sortCol = pageReq.getSort();
		String sortDir = pageReq.getOrder();
		Map<String, Object> filter = pageReq.getQ();
		Page<P> page;
		Pageable pageRequest = buildPageRequest(pageNo, pageSize, sortCol, sortDir);
		if(!filter.isEmpty()) {
			Specification<P> spec = buildSpecification(filter);
			page = specificationExecutor.findAll(spec, pageRequest); 
		} else {
			page = jpaRepository.findAll(pageRequest);
		}
		PageResp<RESP> pageResp = new PageResp<>();
		List<P> dbList = page.getContent();
		if(!CollectionUtils.isEmpty(dbList)) {
			List<RESP> list = new ArrayList<>();
			for (P po : dbList) {
				RESP t = convertPoToRespDto(po);
				list.add(t);
			}
			pageResp.setResult(list);
		}
		pageResp.setPageNo(page.getNumber() + 1);
		pageResp.setPageSize(page.getNumberOfElements());
		pageResp.setTotalPage(page.getTotalPages());
		pageResp.setTotalCount(Integer.valueOf(page.getTotalElements() + ""));
		return pageResp;
	}
	
	/**
	 * Easyui分页公共模板
	 * 
	 * @param pageReq
	 * @return
	 * @throws SpiritServiceException
	 */
	protected PageResult<RESP> findEasyUIPage(PageReq pageReq) throws SpiritServiceException {
		PageResp<RESP> pageResp = findCommonPage(pageReq);
		PageResult<RESP> pageResult = new PageResult<>();
		pageResult.setRows(pageResp.getResult());
		pageResult.setTotal(pageResp.getTotalCount());
		return pageResult;
	}
	
	/**
	 * 构造分页参数
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sortCol
	 * @param sortDir
	 * @return
	 */
	private Pageable buildPageRequest(int pageNo, int pageSize, String sortCol, String sortDir) {
		return PageRequest.of(pageNo - 1, pageSize, SpiritDaoUtils.buildJPABasicOrder(sortCol, sortDir));
	}
	
	/**
	 * 将ReqDTO对象转化为数据库的实体类
	 * 
	 * @param reqDTO
	 * @param po
	 * @return
	 */
	protected abstract P convertReqDtoToPo(REQ reqDTO);
	
	/**
	 * 将数据库的实体类转化为RespDTO对象
	 * 
	 * @param domain
	 * @return
	 */
	protected abstract RESP convertPoToRespDto(P po);
	
	/**
	 * 构造查询条件
	 * 
	 * @param filter
	 * @return
	 */
	protected abstract Specification<P> buildSpecification(Map<String, Object> filter);
	
}

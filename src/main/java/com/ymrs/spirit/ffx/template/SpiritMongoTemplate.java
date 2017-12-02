package com.ymrs.spirit.ffx.template;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResp;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.pub.SpiritMongoPageable;
import com.ymrs.spirit.ffx.util.SpiritDaoUtils;

/**
 * MongoDB 分页查询模板
 * 
 * @author dante
 *
 * @param <T>
 */
public abstract class SpiritMongoTemplate<T> {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public PageResp<T> findPage(PageReq pageReq, Class<T> clazz) {
		int pageNo = pageReq.getPageNo();
		int pageSize = pageReq.getPageSize();
		String sortCol = pageReq.getSort();
		String sortDir = pageReq.getOrder();
		Map<String, Object> q = pageReq.getQ();
		
		Query query = new Query();
		if(q != null && !q.isEmpty()) {
			Criteria criteria = buildCriteria(q);
			query.addCriteria(criteria);
		}
		Sort sort = SpiritDaoUtils.buildJPABasicOrder(sortCol, sortDir);
		if (sort != null) {
			query.with(sort);
		}
		SpiritMongoPageable pageable = new SpiritMongoPageable();
		pageable.setPagenumber(pageNo);
		pageable.setPagesize(pageSize);
		if (sort != null) {
			pageable.setSort(sort);
		}
		Long count = mongoTemplate.count(query, clazz);
		List<T> list = mongoTemplate.find(query.with(pageable), clazz);
		Page<T> page = new PageImpl<T>(list, pageable, count);
		PageResp<T> pageResp = new PageResp<>();
		pageResp.setResult(page.getContent());
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
	protected PageResult<T> findEasyUIPage(PageReq pageReq, Class<T> clazz) throws SpiritServiceException {
		PageResp<T> pageResp = findPage(pageReq, clazz);
		PageResult<T> pageResult = new PageResult<>();
		pageResult.setRows(pageResp.getResult());
		pageResult.setTotal(pageResp.getTotalCount());
		return pageResult;
	}
	
	/**
	 * 构造查询条件
	 * 
	 * @param filter
	 * @return
	 */
	protected abstract Criteria buildCriteria(Map<String, Object> filter);
	
}

package com.ymrs.spirit.ffx.specification.sysmgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.ymrs.spirit.ffx.po.sysmgr.ScheduleJobPO;

/**
 * 定时任务查询规约
 * 
 * @author dante
 *
 */
public class SchedulerJobSpecification {
	
	private SchedulerJobSpecification() {
		throw new IllegalAccessError("SchedulerJobSpecification 不可实例化！");
	}
	
	public static Specification<ScheduleJobPO> querySpecification(Map<String, Object> filter) {
		return new Specification<ScheduleJobPO>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ScheduleJobPO> root, CriteriaQuery<? extends Object> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				String jobName = (String) filter.get("jobName");

				if (StringUtils.hasText(jobName)) {
					Predicate jobNameLike = cb.like(root.get("jobName").as(String.class), "%" + jobName.trim() + "%");
					predicates.add(jobNameLike);
				}
				return predicates.isEmpty() ? cb.conjunction()
						: cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}

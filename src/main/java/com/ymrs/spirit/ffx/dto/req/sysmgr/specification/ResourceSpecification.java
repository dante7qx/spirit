package com.ymrs.spirit.ffx.dto.req.sysmgr.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;

import com.ymrs.spirit.ffx.po.sysmgr.ResourcePO;
import com.ymrs.spirit.ffx.po.sysmgr.RolePO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;

/**
 * 资源查询规约
 * 
 * @author dante
 *
 */
public class ResourceSpecification {
	
	private ResourceSpecification() {
		throw new IllegalAccessError("ResourceSpecification 不可实例化！");
	}

	/**
	 * 获取当前登录用户的可见菜单
	 * 
	 * @param userId
	 * @return
	 */
	public static Specification<ResourcePO> findResourceTreeByUserId(Long userId) {
		return new Specification<ResourcePO>() {
			@Override
			public Predicate toPredicate(Root<ResourcePO> root, CriteriaQuery<? extends Object> query, CriteriaBuilder cb) {
				Join<ResourcePO, ResourcePO> parentResourceJoin = root
						.join(root.getModel().getSingularAttribute("parentResource", ResourcePO.class), JoinType.LEFT);
				Root<UserPO> userRoot = query.from(UserPO.class);
				SetJoin<UserPO, RolePO> roleJoin = userRoot.join(userRoot.getModel().getSet("roles", RolePO.class),
						JoinType.LEFT);
				Predicate authorityIn = root.get("authority").get("id").as(Long.class)
						.in(roleJoin.join("authoritys").get("id").as(Long.class));
				Predicate userIdEq = cb.equal(userRoot.get("id").as(Long.class), userId);
//				query.distinct(true);
				query.where(cb.and(authorityIn, userIdEq));
				query.orderBy(cb.asc(parentResourceJoin.get("showOrder")), cb.asc(root.get("showOrder")));
				return query.getRestriction();
			}
		};
	}
}

package com.ymrs.spirit.ffx.specification.sysmgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.ymrs.spirit.ffx.po.sysmgr.RolePO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;

/**
 * 用户查询规约
 * 
 * @author dante
 *
 */
public class UserSpecification {
	
	private UserSpecification() {
		throw new IllegalAccessError("UserSpecification 不可实例化！");
	}
	
	/**
	 * 构造多参数查询规范
	 * 
	 * @param filter
	 * @param sortCol
	 * @param sortDir
	 * @return
	 */
	public static Specification<UserPO> querySpecification(Map<String, Object> filter) {
		return new Specification<UserPO>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<UserPO> root, CriteriaQuery<? extends Object> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				String account = (String) filter.get("account");
				String name = (String) filter.get("name");
				String email = (String) filter.get("email");
				String status = (String) filter.get("status");
				
				if(StringUtils.hasLength(account)) {
					Predicate accountLike = cb.like(root.get("account").as(String.class), "%"+account.trim()+"%");
					predicates.add(accountLike);
				}
				if(StringUtils.hasLength(name)) {
					Predicate nameLike = cb.like(root.get("name").as(String.class), "%"+name.trim()+"%");
					predicates.add(nameLike);
				}
				if(StringUtils.hasLength(email)) {
					Predicate emailLike = cb.like(root.get("email").as(String.class), "%"+email.trim()+"%");
					predicates.add(emailLike);
				}
				if(StringUtils.hasLength(status)) {
					Predicate statusEq = cb.equal(root.get("status").as(String.class), status.trim());
					predicates.add(statusEq);
				}
				return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}};
	}
	
	/**
	 * 根据指定roleId下的用户
	 * 
	 * @param roleId
	 * @return
	 */
	public static Specification<UserPO> queryUserByRoleId(Long roleId) {
		return new Specification<UserPO>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<UserPO> root, CriteriaQuery<? extends Object> query, CriteriaBuilder cb) {
				SetJoin<UserPO, RolePO> roleJoin = root.join(root.getModel().getSet("roles", RolePO.class),
						JoinType.LEFT);
				Predicate eqRoleId = cb.equal(roleJoin.get("id").as(Long.class), roleId);
				query.where(eqRoleId);
				query.orderBy(cb.desc(root.get("account").as(String.class)));
				return query.getRestriction();
			}
		};
	}
}
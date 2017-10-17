package com.ymrs.spirit.ffx.dao.sysmgr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;



/**
 * 用户 DAO 
 * 
 * @author dante
 *
 */
public interface UserDAO extends JpaRepository<UserPO, Long>, JpaSpecificationExecutor<UserPO>{
	
	/**
	 * 根据account获取用户
	 * 
	 * @param account
	 * @return
	 * @throws SpiritDaoException
	 */
	public UserPO findByAccount(String account) throws SpiritDaoException;
	
	/**
	 * 根据userId删除用户，及其对应的角色
	 * 
	 * @param userId
	 */
	@Modifying
	@Query(value = "delete from t_user_role where user_id = ?1", nativeQuery = true)
	public void deleteUserRoleByUserId(Long userId);
	
}

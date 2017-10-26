package com.ymrs.spirit.ffx.dao.sysmgr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ymrs.spirit.ffx.po.sysmgr.RolePO;

/**
 * 角色管理 DAO
 * 
 * @author dante
 *
 */
public interface RoleDAO extends JpaRepository<RolePO, Long>, JpaSpecificationExecutor<RolePO>{

}

package com.ymrs.spirit.ffx.dao.sysmgr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.po.sysmgr.ResourcePO;


public interface ResourceDAO extends JpaRepository<ResourcePO, Long>, JpaSpecificationExecutor<ResourcePO>{
	
	@Query("select r from ResourcePO r where r.parentResource.id is null order by r.showOrder asc")
	public List<ResourcePO> findRootResource() throws SpiritDaoException;
	
	@Query("select r from ResourcePO r where r.parentResource.id =:pid order by r.showOrder asc")
	public List<ResourcePO> findByPid(@Param("pid") Long pid) throws SpiritDaoException;
	
	@Query("select distinct r.parentResource.id from ResourcePO r where r.parentResource.id is not null")
	public List<Long> findAllParentId() throws SpiritDaoException;
}

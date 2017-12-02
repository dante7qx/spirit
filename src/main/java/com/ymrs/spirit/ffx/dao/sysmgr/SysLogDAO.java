package com.ymrs.spirit.ffx.dao.sysmgr;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ymrs.spirit.ffx.po.sysmgr.SysLogPO;

public interface SysLogDAO extends MongoRepository<SysLogPO, String> {

}

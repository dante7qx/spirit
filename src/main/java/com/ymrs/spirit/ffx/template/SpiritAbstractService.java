package com.ymrs.spirit.ffx.template;

import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResp;

/**
 * 抽象API业务接口，所有业务Service接口需要继承此接口
 * 
 * @author dante
 *
 * @param <ReqDTO>
 * @param <RespDTO>
 */
public abstract interface SpiritAbstractService<ReqDTO, RespDTO> {
	
	/**
	 * 分页查询
	 * 
	 * @param pageReq
	 * @return
	 * @throws SpiritAPIServiceException
	 */
	public PageResp<RespDTO> findPage(PageReq pageReq) throws SpiritServiceException;
	
	/**
	 * 持久化（新增、更新）
	 * 
	 * @param reqDTO
	 * @return
	 * @throws SpiritAPIServiceException
	 */
	public RespDTO persist(ReqDTO reqDTO) throws SpiritServiceException;
	
	/**
	 * 物理删除
	 * 
	 * @param id
	 * @throws SpiritAPIServiceException
	 */
	public void deleteById(Long id) throws SpiritServiceException;
	
	/**
	 * 逻辑删除
	 * 
	 * reqDTO：id、updateUser必须传递，不能为空
	 * 
	 * @param reqDTO
	 * @throws SpiritAPIServiceException
	 */
	public void delete(ReqDTO reqDTO) throws SpiritServiceException;
	
	/**
	 * 根据id（主键）查询
	 * 
	 * @param id
	 * @return
	 * @throws SpiritAPIServiceException
	 */
	public RespDTO findById(Long id) throws SpiritServiceException;
	
}

package com.ymrs.spirit.ffx.pub;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 公共分页返回基类
 * 
 * @author dante
 *
 * @param <T>
 */
public class PageResp<T> {

	// 结果
	private List<T> result;

	// 当前页数
	private int pageNo;

	// 每页显示记录数
	private int pageSize;

	// 总页数
	private int totalPage;

	// 总记录数
	private long totalCount;
	
	public PageResp() {
	}

	public List<T> getResult() {
		if(result == null) {
			this.result = Lists.newArrayList();
		}
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

}

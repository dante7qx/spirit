package com.ymrs.spirit.ffx.pub;

import java.util.List;

import com.google.common.collect.Lists;

public class PageResult<T> {

	private List<T> rows;
	private long total;
	
	public PageResult() {
	}
	
	public PageResult(List<T> rows, long total) {
		super();
		this.rows = rows;
		this.total = total;
	}

	public List<T> getRows() {
		if(rows == null) {
			rows = Lists.newArrayList();
		}
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}

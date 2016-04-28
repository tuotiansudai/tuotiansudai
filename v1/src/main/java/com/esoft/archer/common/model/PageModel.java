package com.esoft.archer.common.model;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PageModel<E> {
	
	private int count ;
	
	private int pageSize ;
	
	private int page ;
	
	private List<E> data ;
	
	private int totalPage;

	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setData(List<E> data) {
		this.data = data;
	}

	public List<E> getData() {
		return data;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalPage() {
		if(pageSize > 0 ){
			if(count % pageSize > 0 ){
				totalPage = (count / pageSize) + 1;
			}else{
				totalPage = count / pageSize;
			}
		}
		return totalPage ;
	}

	
}

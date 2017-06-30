package com.framework.utils.pager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Pager {
	/**
	 * 每页显示条数
	 */
	public int pageSize = 10;

	/**
	 * 当前页
	 */
	public int currPage = 1;

	/**
	 * 排序列名
	 */
	public String orderField;

	/**
	 * 排序类型：asc or desc
	 */
	public String orderDirection;

	/**
	 * 总页数
	 */
	public int totalPage = 1;

	/**
	 * 总记录数
	 */
	public long totalCount = 0;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		totalPage = (int) (totalCount - 1) / this.pageSize + 1;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public Pageable parsePageable() {
		if (StringUtils.isNotBlank(getOrderField())) {
			return new PageRequest(
					getCurrPage() - 1,
					getPageSize(),
					getOrderDirection().equalsIgnoreCase("asc") ? Sort.Direction.ASC
							: Sort.Direction.DESC, getOrderField());
		}
		return new PageRequest(getCurrPage() - 1, getPageSize());
	}

	public String toJson() {
		return "{\"totalPage\":" + getTotalPage() + ",\"currPage\":"
				+ getCurrPage() + ",\"totalCount\": " + getTotalCount()
				+ ",\"rows\":%s}";
	}

}

package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.DataControl;
import com.framework.entity.Permission;
import com.framework.utils.dwz.Page;

public interface DataControlService {
	DataControl get(Long id);

	void saveOrUpdate(DataControl dataControl);

	void delete(Long id);

	List<DataControl> findAll(Page page);

	List<DataControl> findByPageable(Specification<DataControl> specification,
			Page page);

	/**
	 * 检查数据权限是否有声明的列，即该操作权限是否可再细分数据权限
	 * 
	 * @param permission
	 * @param dataControl
	 * @param checkDepend
	 *            true:检查entity间接关联，false:检查entity直接关联
	 * @return
	 */
	boolean isDeclaredField(Permission permission, DataControl dataControl,
			boolean checkDepend);
}

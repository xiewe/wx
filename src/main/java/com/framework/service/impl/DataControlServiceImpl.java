package com.framework.service.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.AppConstants;
import com.framework.dao.DataControlDAO;
import com.framework.entity.DataControl;
import com.framework.entity.Permission;
import com.framework.service.DataControlService;
import com.framework.utils.dwz.Page;
import com.framework.utils.dwz.PageUtils;

@Service
@Transactional
public class DataControlServiceImpl implements DataControlService {

	@Autowired
	private DataControlDAO dataControlDAO;

	@Override
	public DataControl get(Long id) {
		return dataControlDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(DataControl dataControl) {
		dataControlDAO.save(dataControl);
	}

	@Override
	public void delete(Long id) {
		dataControlDAO.delete(id);
	}

	@Override
	public List<DataControl> findAll(Page page) {
		org.springframework.data.domain.Page<DataControl> springDataPage = dataControlDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<DataControl> findByPageable(
			Specification<DataControl> specification, Page page) {
		org.springframework.data.domain.Page<DataControl> springDataPage = dataControlDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public boolean isDeclaredField(Permission permission,
			DataControl dataControl, boolean checkDepend) {
		// 是否有定义列名
		boolean isDeclaredField = false;
		try {
			Class clazz = Class.forName(permission.getClassName());
			Field[] fields = clazz.getDeclaredFields();// 根据Class对象获得属性,私有的也可以获得
			for (Field field : fields) {
				String fieldName = "";
				// 类型为用户关联的数据
				if (dataControl.getCategory().equals(
						AppConstants.DATACONTROL_CATEGORY_OWNS)) {
					fieldName = "user";
				}
				// 类型为组织关联的数据
				else if (dataControl.getCategory().equals(
						AppConstants.DATACONTROL_CATEGORY_ORGANIZATION)) {
					fieldName = "organization";
					// 若要检查依赖，需再判断一下如果是组织关联的数据：如果entity末与组织表直接关联，也可根据间接条件过滤,目前已知与组织关联的entity只有用户表
					if (checkDepend && !field.getName().equals(fieldName)) {
						fieldName = "user";
					}
				}
				// 自定义
				else {
					fieldName = dataControl.getFieldName();
				}

				// 判断是否有关联，即指定过滤列是否包含在该操作对应的entity的列中。
				if (field.getName().equals(fieldName)) {
					isDeclaredField = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDeclaredField;
	}
}

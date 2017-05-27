package com.framework.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysLogDAO;
import com.framework.entity.SysLog;
import com.framework.service.SysLogService;
import com.framework.utils.page.Page;
import com.framework.utils.page.PageUtils;

@Service
@Transactional
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private SysLogDAO logInfoDAO;

	@Override
	public SysLog get(Long id) {
		return logInfoDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(SysLog logInfo) {
		logInfoDAO.save(logInfo);
	}

	@Override
	public void delete(Long id) {
		logInfoDAO.delete(id);
	}

	@Override
	public List<SysLog> findAll(Page page) {
		org.springframework.data.domain.Page<SysLog> springDataPage = logInfoDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<SysLog> findByPageable(Specification<SysLog> specification,
			Page page) {
		if (null != page && StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderDirection("desc");
			page.setOrderField("createTime");
		}
		org.springframework.data.domain.Page<SysLog> springDataPage = logInfoDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}
}

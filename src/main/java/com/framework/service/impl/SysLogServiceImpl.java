package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysLogDAO;
import com.framework.entity.SysLog;
import com.framework.service.SysLogService;
import com.framework.utils.pager.Pager;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDAO logInfoDAO;

    @Override
    public SysLog get(Long id) {
        return logInfoDAO.findOne(id);
    }

    @Transactional
    @Override
    public void saveOrUpdate(SysLog logInfo) {
        logInfoDAO.save(logInfo);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        logInfoDAO.delete(id);
    }

    @Override
    public List<SysLog> findAll(Pager pager) {
        org.springframework.data.domain.Page<SysLog> springDataPage = logInfoDAO.findAll(pager.parsePageable());
        pager.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public List<SysLog> findByPageable(Specification<SysLog> specification, Pager pager) {
        org.springframework.data.domain.Page<SysLog> springDataPage = logInfoDAO.findAll(specification,
                pager.parsePageable());
        pager.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public List<SysLog> findAll() {
        return logInfoDAO.findAll();
    }

    @Override
    public List<SysLog> findAll(Specification<SysLog> specification) {
        return logInfoDAO.findAll(specification);
    }
}

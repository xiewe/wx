package com.framework.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysMenuDAO;
import com.framework.entity.SysMenu;
import com.framework.service.SysMenuService;
import com.framework.utils.pager.Pager;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuDAO oDao;

    @Override
    public SysMenu get(Integer id) {
        return oDao.findOne(id);
    }

    @Transactional
    @Override
    public SysMenu saveOrUpdate(SysMenu o) {
        return oDao.save(o);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        oDao.delete(id);
    }

    @Override
    public List<SysMenu> findAll(Pager pager) {
        org.springframework.data.domain.Page<SysMenu> springDataPage = oDao.findAll(pager.parsePageable());
        pager.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public List<SysMenu> findByPageable(Specification<SysMenu> specification, Pager pager) {
        org.springframework.data.domain.Page<SysMenu> springDataPage = oDao.findAll(specification,
                pager.parsePageable());
        pager.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public List<SysMenu> findAll() {
        return oDao.findAll();
    }

    @Override
    public List<SysMenu> findAll(Specification<SysMenu> specification) {
        return oDao.findAll(specification);
    }

    @Override
    public List<SysMenu> findByRoleId(Integer rid) {
        return oDao.findByRoleId(rid);
    }
}

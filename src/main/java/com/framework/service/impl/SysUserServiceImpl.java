package com.framework.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.SysUserDAO;
import com.framework.entity.SysUser;
import com.framework.exception.ExistedException;
import com.framework.exception.IncorrectPasswordException;
import com.framework.exception.ServiceException;
import com.framework.service.SysUserService;
import com.framework.shiro.ShiroRealm;
import com.framework.shiro.ShiroRealm.HashPassword;
import com.framework.utils.pager.Pager;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDAO oDao;

    @Autowired
    private ShiroRealm shiroRealm;

    @Override
    public SysUser get(Long id) {
        return oDao.findOne(id);
    }

    @Transactional
    @Override
    public SysUser saveOrUpdate(SysUser o) {
        if (o.getId() == null) {
            if (oDao.findByU(o.getUsername()) != null) {
                throw new ExistedException("用户名：" + o.getUsername() + "已存在。");
            }

            // 设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
            if (StringUtils.isNotBlank(o.getPlainPassword()) && shiroRealm != null) {
                HashPassword hashPassword = ShiroRealm.encryptPassword(o.getPlainPassword());
                o.setSalt(hashPassword.salt);
                o.setPassword(hashPassword.password);
            }
        }

        shiroRealm.clearCachedAuthorizationInfo(o);

        return oDao.save(o);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        SysUser user = oDao.findOne(id);
        oDao.delete(id);
        // 从shiro中注销
        shiroRealm.clearCachedAuthorizationInfo(user);
    }

    @Override
    public List<SysUser> findAll(Pager pager) {
        org.springframework.data.domain.Page<SysUser> springDataPage = oDao.findAll(pager.parsePageable());
        pager.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public List<SysUser> findByPageable(Specification<SysUser> specification, Pager pager) {
        org.springframework.data.domain.Page<SysUser> springDataPage = oDao.findAll(specification,
                pager.parsePageable());
        pager.setTotalCount(springDataPage.getTotalElements());
        return springDataPage.getContent();
    }

    @Override
    public SysUser findByUsername(String username) {
        return oDao.findByUsername(username);
    }

    @Override
    public SysUser findByEmail(String email) {
        return oDao.findByEmail(email);
    }

    @Override
    public SysUser findByUid(String uid) {
        return oDao.findByUid(uid);
    }

    @Override
    public SysUser findByPhone(String phone) {
        return oDao.findByPhone(phone);
    }

    @Override
    public SysUser findByU(String s) {
        return oDao.findByU(s);
    }

    @Transactional
    @Override
    public void updatePwd(SysUser user, String newPwd) throws ServiceException {
        // 设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
        boolean isMatch = ShiroRealm.validatePassword(user.getPlainPassword(), user.getPassword(), user.getSalt());
        if (isMatch) {
            HashPassword hashPassword = ShiroRealm.encryptPassword(newPwd);
            user.setSalt(hashPassword.salt);
            user.setPassword(hashPassword.password);

            oDao.save(user);
            shiroRealm.clearCachedAuthorizationInfo(user);

            return;
        }

        throw new IncorrectPasswordException("用户密码错误！");
    }

    @Transactional
    @Override
    public void resetPwd(SysUser user, String newPwd) {
        if (newPwd == null) {
            newPwd = "123456";
        }

        HashPassword hashPassword = ShiroRealm.encryptPassword(newPwd);
        user.setSalt(hashPassword.salt);
        user.setPassword(hashPassword.password);

        oDao.save(user);
    }

    @Override
    public List<SysUser> findAll() {
        return oDao.findAll();
    }

    @Override
    public List<SysUser> findAll(Specification<SysUser> specification) {
        return oDao.findAll(specification);
    }
}

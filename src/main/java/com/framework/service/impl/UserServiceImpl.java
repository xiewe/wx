package com.framework.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.UserDAO;
import com.framework.entity.User;
import com.framework.exception.ExistedException;
import com.framework.exception.IncorrectPasswordException;
import com.framework.exception.ServiceException;
import com.framework.service.UserService;
import com.framework.shiro.ShiroRealm;
import com.framework.shiro.ShiroRealm.HashPassword;
import com.framework.utils.page.Page;
import com.framework.utils.page.PageUtils;


@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ShiroRealm shiroRealm;

	@Override
	public User get(Long id) {
		return userDAO.findOne(id);
	}

	@Override
	public void saveOrUpdate(User user) {
		if (user.getId() == null) {
			if (userDAO.getByUsername(user.getUsername()) != null) {
				throw new ExistedException("登录名：" + user.getUsername() + "已存在。");
			}

			// 设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
			if (StringUtils.isNotBlank(user.getPlainPassword())
					&& shiroRealm != null) {
				HashPassword hashPassword = ShiroRealm.encryptPassword(user
						.getPlainPassword());
				user.setSalt(hashPassword.salt);
				user.setPassword(hashPassword.password);
			}
		}

		userDAO.save(user);
		shiroRealm.clearCachedAuthorizationInfo(user);
	}

	@Override
	public void delete(Long id) {

		User user = userDAO.findOne(id);
		userDAO.delete(user.getId());

		// 从shiro中注销
		shiroRealm.clearCachedAuthorizationInfo(user);
	}

	@Override
	public List<User> findAll(Page page) {
		org.springframework.data.domain.Page<User> springDataPage = userDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<User> findByPageable(Specification<User> specification,
			Page page) {
		if (null != page && StringUtils.isEmpty(page.getOrderField())) {
			page.setOrderDirection("desc");
			page.setOrderField("createTime");
		}
		org.springframework.data.domain.Page<User> springDataPage = userDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public void updatePwd(User user, String newPwd) throws ServiceException {
		// 设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
		boolean isMatch = ShiroRealm.validatePassword(user.getPlainPassword(),
				user.getPassword(), user.getSalt());
		if (isMatch) {
			HashPassword hashPassword = ShiroRealm.encryptPassword(newPwd);
			user.setSalt(hashPassword.salt);
			user.setPassword(hashPassword.password);

			userDAO.save(user);
			shiroRealm.clearCachedAuthorizationInfo(user);

			return;
		}

		throw new IncorrectPasswordException("用户密码错误！");
	}

	@Override
	public void resetPwd(User user, String newPwd) {
		if (newPwd == null) {
			newPwd = "123456";
		}

		HashPassword hashPassword = ShiroRealm.encryptPassword(newPwd);
		user.setSalt(hashPassword.salt);
		user.setPassword(hashPassword.password);

		userDAO.save(user);
	}

	@Override
	public User getByUsername(String username) {
		return userDAO.getByUsername(username);
	}

	@Override
	public List<User> findAllUsersByOrganization(Long[] organizationIds) {
		if (organizationIds == null)
			return null;

		return userDAO.findByOrganizationIdIn(organizationIds);
	}

	@Override
	public Long[] findAllUserIdsByOrganization(Long[] organizationIds) {
		if (organizationIds == null)
			return null;
		List<User> lstUsers = findAllUsersByOrganization(organizationIds);
		Long[] ids = new Long[lstUsers.size()];
		for (int i = 0; i < lstUsers.size(); i++) {
			ids[i] = lstUsers.get(i).getId();
		}
		return ids;
	}


}

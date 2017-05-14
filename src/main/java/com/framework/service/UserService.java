package com.framework.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.framework.entity.User;
import com.framework.utils.dwz.Page;

public interface UserService {
	User get(Long id);

	void saveOrUpdate(User user);

	void delete(Long id);

	List<User> findAll(Page page);

	List<User> findByPageable(Specification<User> specification, Page page);

	void updatePwd(User user, String newPwd);

	void resetPwd(User user, String newPwd);

	User getByUsername(String username);

	/**
	 * 查找指定用户隶属组织下所有用户
	 * 
	 * @param organizationIds
	 * @return
	 */
	List<User> findAllUsersByOrganization(Long[] organizationIds);

	Long[] findAllUserIdsByOrganization(Long[] organizationIds);
}

package com.framework.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framework.dao.OrganizationDAO;
import com.framework.dao.UserDAO;
import com.framework.entity.Organization;
import com.framework.entity.User;
import com.framework.exception.NotDeletedException;
import com.framework.exception.NotExistedException;
import com.framework.service.OrganizationService;
import com.framework.utils.dwz.Page;
import com.framework.utils.dwz.PageUtils;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationDAO organizationDAO;

	@Autowired
	private UserDAO userDAO;

	@Override
	public Organization get(Long id) {
		return organizationDAO.findOne(id);
	}

	@Override
	public Organization getByName(String name) {
		return organizationDAO.getByName(name);
	}

	@Override
	public void saveOrUpdate(Organization organization) {
		if (organization.getId() == null) {
			Organization parentOrganization = organizationDAO
					.findOne(organization.getParent().getId());
			if (parentOrganization == null) {
				throw new NotExistedException("id="
						+ organization.getParent().getId() + "父组织不存在！");
			}

			if (organizationDAO.getByName(organization.getName()) != null) {
				throw new NotExistedException(organization.getName() + "已存在！");
			}
		}

		organizationDAO.save(organization);
	}

	/**
	 * 判断是否是根组织.
	 */
	private boolean isRoot(Long id) {
		return id == 1;
	}

	@Override
	public void delete(Long id) {
		if (isRoot(id)) {
			throw new NotDeletedException("不允许删除根组织。");
		}

		Organization organization = this.get(id);

		// 先判断是否存在子模块，如果存在子模块，则不允许删除
		if (organization.getChildren().size() > 0) {
			throw new NotDeletedException(organization.getName()
					+ "组织下存在子组织，不允许删除。");
		}

		if (userDAO.findByOrganizationId(id).size() > 0) {
			throw new NotDeletedException(organization.getName()
					+ "组织下存在用户，不允许删除。");
		}

		organizationDAO.delete(organization);
	}

	@Override
	public List<Organization> findAll(Page page) {
		org.springframework.data.domain.Page<Organization> springDataPage = organizationDAO
				.findAll(PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public List<Organization> findByPageable(
			Specification<Organization> specification, Page page) {
		org.springframework.data.domain.Page<Organization> springDataPage = organizationDAO
				.findAll(specification, PageUtils.createPageable(page));
		page.setTotalCount(springDataPage.getTotalElements());
		return springDataPage.getContent();
	}

	@Override
	public Organization getTree() {
		Organization root = organizationDAO.findOne(1L);
		makeChildre(root);
		return root;
	}

	private void makeChildre(Organization parent) {
		List<Organization> children = organizationDAO.findByParentId(parent
				.getId());
		if (null != children && children.size() > 0) {
			for (Organization o : children) {
				makeChildre(o);
			}
		}
		parent.setChildren(children);
	}

	@Override
	public List<Organization> findAllOrganizationsByUser(User user) {
		if (user.getOrganization() == null
				|| user.getOrganization().getId() == null)
			return null;
		List<Organization> lstOrganizations = new ArrayList();
		Organization root = organizationDAO.findOne(user.getOrganization()
				.getId());
		lstOrganizations.add(root);
		makeChildre(lstOrganizations, root);
		return lstOrganizations;
	}

	private void makeChildre(List<Organization> lstOrganizations,
			Organization parent) {
		List<Organization> children = organizationDAO.findByParentId(parent
				.getId());
		if (null != children && children.size() > 0) {
			for (Organization o : children) {
				makeChildre(lstOrganizations, o);
				lstOrganizations.add(o);
			}
		}
	}

	@Override
	public Long[] findAllOrganizationIdsByUser(User user) {
		List<Organization> lstOrganizations = findAllOrganizationsByUser(user);
		if (null == lstOrganizations || lstOrganizations.size() <= 0) {
			return null;
		}
		Long[] ids = new Long[lstOrganizations.size()];
		for (int i = 0; i < lstOrganizations.size(); i++) {
			ids[i] = lstOrganizations.get(i).getId();
		}
		return ids;
	}
}

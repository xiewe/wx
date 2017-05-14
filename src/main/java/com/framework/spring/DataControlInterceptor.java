package com.framework.spring;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.framework.AppConstants;
import com.framework.entity.DataControl;
import com.framework.entity.Permission;
import com.framework.entity.User;
import com.framework.service.DataControlService;
import com.framework.service.OrganizationService;
import com.framework.service.PermissionService;
import com.framework.service.RolePermissionDataControlService;
import com.framework.service.RolePermissionService;
import com.framework.service.RoleService;
import com.framework.service.UserService;
import com.framework.utils.Exceptions;
import com.framework.utils.persistence.DynamicSpecifications;
import com.framework.utils.persistence.SearchFilter;
import com.framework.utils.persistence.SearchFilter.Operator;

public class DataControlInterceptor extends HandlerInterceptorAdapter {
	private final static Logger logger = LoggerFactory
			.getLogger(DataControlInterceptor.class);

	@Autowired
	protected RolePermissionService rolePermissionService;

	@Autowired
	protected RolePermissionDataControlService rolePermissionDataControlService;

	@Autowired
	protected PermissionService permissionService;

	@Autowired
	protected RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private DataControlService dataControlService;

	@Autowired
	private OrganizationService organizationService;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		final HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();

		final RequiresPermissions requiresPermissions = method
				.getAnnotation(RequiresPermissions.class);
		if (requiresPermissions == null) {
			return true;
		}
		Logical logical = requiresPermissions.logical();
		String[] arrPermissions = requiresPermissions.value();

		for (String permission : arrPermissions) {
			try {
				boolean checkResult = checkDataControlPermission(request,
						response, permission, method);
				if (!checkResult) {
					throw new UnauthorizedException("数据权限验证失败！");
				}
				if (checkResult == true && logical.equals(Logical.OR)) {
					return true;
				}
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(e));
				throw new UnauthorizedException("数据权限验证失败！");
			}
		}

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	public boolean checkDataControlPermission(HttpServletRequest request,
			HttpServletResponse response, String permissionString, Method method) {

		if (!SecurityUtils.getSubject().isPermitted(permissionString)) {
			return false;
		}

		User loginUser = (User) SecurityUtils.getSubject().getPrincipal();

		// 从缓存中找出登录用户对应此操作的据级权限
		List<DataControl> lstPermissionDataControls = DataControlCache.get(
				loginUser.getUsername()).get(permissionString);

		if (null != lstPermissionDataControls
				&& lstPermissionDataControls.size() > 0) {

			// 得到过滤条件
			Set<SearchFilter> searchFilterSet = new HashSet<SearchFilter>();
			for (DataControl o : lstPermissionDataControls) {
				// 通用类型：用户关联的数据
				if (o.getCategory().equalsIgnoreCase(
						AppConstants.DATACONTROL_CATEGORY_OWNS)) {
					searchFilterSet.add(new SearchFilter("user.id",
							Operator.EQ, loginUser.getId()));
				}
				// 通用类型：组织关联的数据
				else if (o.getCategory().equalsIgnoreCase(
						AppConstants.DATACONTROL_CATEGORY_ORGANIZATION)) {
					// 因当前loginUser是shiro缓存的，并末缓存关联对象，当访问外键时需要重新查询user，否则抛org.hibernate.LazyInitializationException
					loginUser = userService.get(loginUser.getId());
					Permission permission = permissionService
							.getBySn(permissionString);
					// 查找出登录用户所属组织及下属组织
					Long[] organizationIds = organizationService
							.findAllOrganizationIdsByUser(loginUser);
					// 此处有大概有二种情况：
					// 一是直接关联，业务表直接与组织关联，查找组织及下属组织的相关记录；
					if (dataControlService
							.isDeclaredField(permission, o, false)) {
						searchFilterSet.add(new SearchFilter("organization.id",
								Operator.IN, organizationIds));
					}
					// 二是间接关联，框架已知与组织关联的表现只有用户表，通过组织查找所有用户，再根据用户关联去过滤；
					else {
						// 查找出登录用户所属组织及下属组织下的所有用户
						Long[] userIds = userService
								.findAllUserIdsByOrganization(organizationIds);
						searchFilterSet.add(new SearchFilter("user.id",
								Operator.IN, userIds));
					}
				}
				// 自定义
				else {
					searchFilterSet.add(new SearchFilter(o.getFieldName(),
							Operator.valueOf(o.getOperator()), o
									.getFieldValue()));
				}
			}

			try {
				boolean handleResult = true;
				// 根据方法命名规则，以[list]开头的方法为分页查询，只需要注入数据过滤条件，无需验证结果
				if (method.getName().startsWith("list")) {
					handleResult = handleSearchFilter(request, searchFilterSet);
				}
				// 新增操作需要单独处理
				else if (method.getName().startsWith("create")) {
					// 暂末实现，看实际场景。如新增设了数据权限控制，一般情况下是新增记录外键关联数据的一些过滤，后续业务按需要扩展。
				}
				// 其它为单记录操作：修改、删除、查看需要验证结果
				else {
					Permission permission = permissionService
							.getBySn(permissionString);
					handleResult = handleOther(request, searchFilterSet,
							permission);
				}
				return handleResult;
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(e));
				throw new UnauthorizedException("数据权限验证失败！");
			}
		}

		return true;
	}

	/**
	 * 处理分页显示的方法，增加数据级权限过滤条件
	 */
	@SuppressWarnings("unchecked")
	protected boolean handleSearchFilter(HttpServletRequest request,
			Set<SearchFilter> searchFilterSet) {
		Set<SearchFilter> searchFilter = (Set<SearchFilter>) request
				.getAttribute(AppConstants.NEST_DYNAMIC_SEARCH);
		if (searchFilter == null) {
			searchFilter = new HashSet<SearchFilter>();
		}
		searchFilter.addAll(searchFilterSet);
		request.setAttribute(AppConstants.NEST_DYNAMIC_SEARCH, searchFilter);
		return true;
	}

	/**
	 * 处理其他单个记录的方法，验证结果
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean handleOther(HttpServletRequest request,
			Set<SearchFilter> filterSet, Permission permission)
			throws Exception {

		String[] ids = request.getParameterValues("ids");

		if (ids != null) {
			filterSet.add(new SearchFilter("id", Operator.IN, ids));
		} else {
			String id = request.getParameter("id");
			if (id != null) {
				filterSet.add(new SearchFilter("id", Operator.EQ, id));
			} else {
				// 截取类似/update/{id} | /delete/{id} | /view/{id} 的id
				String uri = request.getRequestURI();
				String tmp = StringUtils.substringAfterLast(uri, "/");
				Long longId = NumberUtils.toLong(tmp);
				if (longId != 0L) {
					filterSet.add(new SearchFilter("id", Operator.EQ, longId));
				}
			}
		}

		Object clazz = Class.forName(permission.getClassName()).newInstance();

		Specification specification = DynamicSpecifications.buildSpecification(
				request, clazz.getClass(), filterSet);

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = builder.createQuery(clazz.getClass());
		Root root = criteriaQuery.from(clazz.getClass());

		Predicate predicate = specification.toPredicate(root, criteriaQuery,
				builder);
		criteriaQuery.where(predicate);

		List<Object> objects = entityManager.createQuery(criteriaQuery)
				.getResultList();
		if (ids != null) {
			if (objects.size() == ids.length) {
				return true;
			}
		} else {
			if (objects.size() > 0) {
				return true;
			}
		}

		return false;
	}

}

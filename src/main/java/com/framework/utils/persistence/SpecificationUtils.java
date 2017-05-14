package com.framework.utils.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.framework.AppConstants;
import com.framework.utils.Exceptions;
import com.framework.utils.ServletUtils;

public class SpecificationUtils {
	private static final Logger logger = LoggerFactory.getLogger(SpecificationUtils.class);

	/**
	 * 操作类型
	 * 
	 * @author dengyong
	 *
	 */
	public enum Operate {
		AND, OR
	}

	private static final String SHORT_DATE = "yyyy-MM-dd";
	private static final String LONG_DATE = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME = "HH:mm:ss";

	public static Collection<SearchFilter> genSearchFilter(ServletRequest request) {
		Map<String, Object> searchParams = ServletUtils.getParametersStartingWith(request, AppConstants.SEARCH_PREFIX);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		return filters.values();
	}

	public static <T> Specification<T> buildSpecification(ServletRequest request, final Class<T> entityClazz,
			final Operate op, final Collection<SearchFilter> searchFilters) {
		return buildSpecification(request, entityClazz, op, searchFilters.toArray(new SearchFilter[] {}));
	}

	public static <T> Specification<T> buildSpecification(ServletRequest request, final Class<T> entityClazz,
			final Operate op, final SearchFilter... searchFilters) {
		Collection<SearchFilter> filters = genSearchFilter(request);
		Set<SearchFilter> set = new HashSet<SearchFilter>(filters);
		for (SearchFilter searchFilter : searchFilters) {
			set.add(searchFilter);
		}
		return buildSpecification(entityClazz, null, op, set, request);
	}

	/***
	 * 增加分组功能
	 * 
	 * @param request
	 * @param entityClazz
	 * @param groupName
	 *            分组字段
	 * @param searchFilters
	 * @return
	 */
	public static <T> Specification<T> buildSpecification(ServletRequest request, final Class<T> entityClazz,
			final String groupName, final Operate op, final SearchFilter... searchFilters) {
		Collection<SearchFilter> filters = genSearchFilter(request);
		Set<SearchFilter> set = new HashSet<SearchFilter>(filters);
		for (SearchFilter searchFilter : searchFilters) {
			set.add(searchFilter);
		}
		return buildSpecification(entityClazz, groupName, op, set, request);
	}

	@SuppressWarnings("unchecked")
	public static <T> Specification<T> buildSpecification(final Class<T> entityClazz, final String groupName,
			final Operate op, final Collection<SearchFilter> searchFilters, ServletRequest request) {
		final Set<SearchFilter> filterSet = new HashSet<SearchFilter>();
		if (request != null) {
			// 数据权限中的filter
			Collection<SearchFilter> nestFilters = (Collection<SearchFilter>) request
					.getAttribute(AppConstants.NEST_DYNAMIC_SEARCH);
			if (nestFilters != null && !nestFilters.isEmpty()) {
				for (SearchFilter searchFilter : nestFilters) {
					filterSet.add(searchFilter);
				}
			}
		}

		// 自定义
		for (SearchFilter searchFilter : searchFilters) {
			filterSet.add(searchFilter);
		}

		return new Specification<T>() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (filterSet != null && !filterSet.isEmpty()) {
					List<Predicate> predicates = new ArrayList<Predicate>();
					for (SearchFilter filter : filterSet) {
						// nested path translate, 如Task的名为"user.name"的filedName,
						// 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.getFieldName(), ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						// 自动进行enum和date的转换。
						Class clazz = expression.getJavaType();
						if (Date.class.isAssignableFrom(clazz) && !filter.getValue().getClass().equals(clazz)) {
							filter.setValue(convert2Date((String) filter.getValue()));
						} else if (Enum.class.isAssignableFrom(clazz) && !filter.getValue().getClass().equals(clazz)) {
							filter.setValue(convert2Enum(clazz, (String) filter.getValue()));
						}

						// logic operator
						switch (filter.getOperator()) {
						case EQ:
							predicates.add(builder.equal(expression, filter.getValue()));
							break;
						case NOTEQ:
							predicates.add(builder.notEqual(expression, (Comparable) filter.getValue()));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.getValue() + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.getValue()));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.getValue()));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.getValue()));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.getValue()));
							break;
						case IN:
							predicates.add(builder.and(expression.in((Object[]) filter.getValue())));
							break;
						}
					}

					// 按groupName分组
					if (groupName != null) {
						Path groupPression = root.get(groupName);
						query.groupBy(groupPression);
					}

					// 将所有条件用 and 联合起来
					if (predicates.size() > 0) {
						switch (op) {
						case AND:
							return builder.and(predicates.toArray(new Predicate[predicates.size()]));
						case OR:
							return builder.or(predicates.toArray(new Predicate[predicates.size()]));
						default:
							return builder.and(predicates.toArray(new Predicate[predicates.size()]));
						}
					}
				}
				return builder.conjunction();
			}
		};
	}

	private static Date convert2Date(String dateString) {
		SimpleDateFormat sFormat = new SimpleDateFormat(LONG_DATE);
		try {
			return sFormat.parse(dateString);
		} catch (ParseException e) {
			try {
				sFormat = new SimpleDateFormat(SHORT_DATE);
				return sFormat.parse(dateString);
			} catch (ParseException e1) {
				try {
					sFormat = new SimpleDateFormat(TIME);
					return sFormat.parse(dateString);
				} catch (ParseException e2) {
					logger.error("Convert time is error! The dateString is " + dateString + "."
							+ Exceptions.getStackTraceAsString(e2));
				}
			}
		}

		return null;
	}

	private static <E extends Enum<E>> E convert2Enum(Class<E> enumClass, String enumString) {
		return EnumUtils.getEnum(enumClass, enumString);
	}

}

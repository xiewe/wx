package com.framework.utils.persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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

public class DynamicSpecifications {
	private static final Logger logger = LoggerFactory.getLogger(DynamicSpecifications.class);

	private static final String SHORT_DATE = "yyyy-MM-dd";
	private static final String LONG_DATE = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME = "HH:mm:ss";

	public static Collection<SearchFilter> genSearchFilter(ServletRequest request) {
		Map<String, Object> searchParams = ServletUtils.getParametersStartingWith(request, AppConstants.SEARCH_PREFIX);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		return filters.values();
	}

	public static <T> Specification<T> buildSpecification(ServletRequest request, final Class<T> entityClazz,
			final Collection<SearchFilter> searchFilters) {
		return buildSpecification(request, entityClazz, searchFilters.toArray(new SearchFilter[] {}));
	}

	public static <T> Specification<T> buildSpecification(ServletRequest request, final Class<T> entityClazz,
			final SearchFilter... searchFilters) {
		Collection<SearchFilter> filters = genSearchFilter(request);
		Set<SearchFilter> set = new HashSet<SearchFilter>(filters);
		for (SearchFilter searchFilter : searchFilters) {
			set.add(searchFilter);
		}
		return buildSpecification(entityClazz, null, null, null, null, set, request);
	}

	/***
	 * 增加分组功能
	 * 
	 * @param entityClazz
	 * @param groupName
	 *            分组字段
	 * @param searchFilters
	 * @return
	 */
	public static <T> Specification<T> buildSpecification(final Class<T> entityClazz, final String groupName,
			final SearchFilter... searchFilters) {
		Set<SearchFilter> set = new HashSet<SearchFilter>();
		if (null != searchFilters) {
			for (SearchFilter searchFilter : searchFilters) {
				if (null != searchFilter)
					set.add(searchFilter);
			}
		}
		return buildSpecification(entityClazz, groupName, null, null, null, set, null);
	}

	/***
	 * 增加分组功能 增加多字段关键字模糊查询
	 * 
	 * @param entityClazz
	 * @param keywordNameList
	 *            需要查询的字段list
	 * @param keywordNameList
	 *            关键字值
	 * @param searchFilters
	 * @return
	 */
	public static <T> Specification<T> buildSpecification(final Class<T> entityClazz, final String groupName,
			final List<KeywordFilter> keywordNameList, final String keywordValue,
			final List<SearchFilter> searchFilters) {

		return buildSpecification(entityClazz, groupName, keywordNameList, keywordValue, null, searchFilters, null);
	}

	/***
	 * 增加分组功能 增加多字段关键字模糊查询 增加关联查询
	 * 
	 * @param entityClazz
	 * @param keywordNameList
	 *            需要查询的字段list
	 * @param keywordNameList
	 *            关键字值
	 * @param joinSearchMap
	 *            关联查询map
	 * @param searchFilters
	 * @return
	 */
	public static <T> Specification<T> buildSpecification(final Class<T> entityClazz, final String groupName,
			final List<KeywordFilter> keywordNameList, final String keywordValue,
			final Map<String, List<SearchFilter>> joinSearchMap, final List<SearchFilter> searchFilters) {

		return buildSpecification(entityClazz, groupName, keywordNameList, keywordValue, joinSearchMap, searchFilters,
				null);
	}

	@SuppressWarnings("unchecked")
	public static <T> Specification<T> buildSpecification(final Class<T> entityClazz, final String groupName,
			final List<KeywordFilter> keywordNameList, final String keywordValue,
			final Map<String, List<SearchFilter>> joinSearchMap, final Collection<SearchFilter> searchFilters,
			ServletRequest request) {
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

				List<Predicate> predicates = new ArrayList<Predicate>();
				// 关键字模糊查询（用or连接）
				List<Predicate> predicateKeywords = new ArrayList<Predicate>();

				// 多表关联查询
				if (joinSearchMap != null && joinSearchMap.size() > 0) {
					for (Iterator queryIterator = joinSearchMap.keySet().iterator(); queryIterator.hasNext();) {
						String key = (String) queryIterator.next();
						List<SearchFilter> sfr = joinSearchMap.get(key);

						if (key != null && !"".equals(key.trim()) && sfr != null && sfr.size() > 0) {

							String joinKey = key.trim();
							Join join = null;
							if (joinKey.contains(".")) { // 左关联(支持两层,为了支持外贸信息可以根据用户过滤)
								join = root.join(joinKey.substring(0, joinKey.indexOf(".")), JoinType.INNER);
								join = join.join(joinKey.substring(joinKey.indexOf(".") + 1), JoinType.LEFT);
							} else {
								join = root.join(joinKey, JoinType.LEFT);
							}

							for (SearchFilter filter : sfr) {
								String[] names = StringUtils.split(filter.getFieldName(), ".");
								Path expression = join.get(names[0]);
								for (int i = 1; i < names.length; i++) {
									expression = expression.get(names[i]);
								}

								// 自动进行enum和date的转换。
								Class clazz = expression.getJavaType();
								if (Date.class.isAssignableFrom(clazz) && !filter.getValue().getClass().equals(clazz)) {
									filter.setValue(convert2Date((String) filter.getValue()));
								} else if (Enum.class.isAssignableFrom(clazz)
										&& !filter.getValue().getClass().equals(clazz)) {
									filter.setValue(convert2Enum(clazz, (String) filter.getValue()));
								}

								// 关联字段查询
								switch (filter.getOperator()) {
								case EQ:
									predicates.add(builder.equal(expression, filter.getValue()));
									break;
								case LIKE:
									// 关键字关联表模糊查询（包括行业，身份，语言）
									predicateKeywords.add(builder.like(expression, "%" + filter.getValue() + "%"));
									break;
								case GT:
									predicates.add(builder.greaterThan(expression, (Comparable) filter.getValue()));
									break;
								case LT:
									predicates.add(builder.lessThan(expression, (Comparable) filter.getValue()));
									break;
								case GTE:
									predicates.add(
											builder.greaterThanOrEqualTo(expression, (Comparable) filter.getValue()));
									break;
								case LTE:
									predicates
											.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.getValue()));
									break;
								case IN:
									predicates.add(builder.and(expression.in((Object[]) filter.getValue())));
									break;
								}
							}
						}
					}
				}

				// 多字段关键字模糊查询
				if (keywordNameList != null && keywordNameList.size() > 0 && keywordValue != null
						&& !"".equals(keywordValue.trim())) {
					for (KeywordFilter key : keywordNameList) {
						String[] names = StringUtils.split(key.getFieldName(), ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}
						if (key.getDataType() == KeywordFilter.DataType.INT) {
							if (StringUtils.isNumeric(keywordValue.trim())) {
								predicateKeywords.add(builder.equal(expression, keywordValue.trim()));
							}
						} else if (key.getDataType() == KeywordFilter.DataType.VARCHAR) {
							predicateKeywords.add(builder.like(expression, "%" + keywordValue.trim() + "%"));
						}
					}
				}
				if (predicateKeywords.size() > 0) {
					predicates.add(builder.or(predicateKeywords.toArray(new Predicate[predicateKeywords.size()])));
				}

				// 其他字段查询
				if (filterSet != null && !filterSet.isEmpty()) {
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
						case NOTIN:
							Object[] obj = null;
							try {
								obj = (Object[]) filter.getValue();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (null != obj)
								predicates.add(builder.not(expression.in(obj)));
							break;
						}
					}
				}

				// 按groupName分组
				if (!StringUtils.isEmpty(groupName)) {
					Path groupPression = root.get(groupName);
					query.groupBy(groupPression);
				}

				// 将所有条件用 and 联合起来
				if (predicates.size() > 0) {
					return builder.and(predicates.toArray(new Predicate[predicates.size()]));
				}

				return builder.conjunction();
			}
		};
	}

	/**
	 * 推荐好友
	 * 
	 * @param entityClazz
	 * @param joinSearchMap
	 *            关联表查询
	 * @param idList
	 *            好友和关注的id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Specification<T> buildSpecifRecommend(final Class<T> entityClazz,
			final Map<String, List<SearchFilter>> joinSearchMap, final List<Long> idList, final Long addrId) {

		return new Specification<T>() {

			@SuppressWarnings({ "rawtypes" })
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				List<Predicate> predicateOrs = new ArrayList<Predicate>();
				List<Predicate> predicates = new ArrayList<Predicate>();

				// 多表关联查询
				if (joinSearchMap != null && joinSearchMap.size() > 0) {
					for (Iterator queryIterator = joinSearchMap.keySet().iterator(); queryIterator.hasNext();) {
						String key = (String) queryIterator.next();
						List<SearchFilter> sfr = joinSearchMap.get(key);

						if (key != null && !"".equals(key.trim()) && sfr != null && sfr.size() > 0) {
							Join join = root.join(key.trim(), JoinType.LEFT); // 左关联
							for (SearchFilter filter : sfr) {
								String[] names = StringUtils.split(filter.getFieldName(), ".");
								Path expression = join.get(names[0]);
								for (int i = 1; i < names.length; i++) {
									expression = expression.get(names[i]);
								}

								// 自动进行enum和date的转换。
								Class clazz = expression.getJavaType();
								if (Date.class.isAssignableFrom(clazz) && !filter.getValue().getClass().equals(clazz)) {
									filter.setValue(convert2Date((String) filter.getValue()));
								} else if (Enum.class.isAssignableFrom(clazz)
										&& !filter.getValue().getClass().equals(clazz)) {
									filter.setValue(convert2Enum(clazz, (String) filter.getValue()));
								}
								predicateOrs.add(builder.and(expression.in((Object[]) filter.getValue())));
							}
						}
					}
				}

				// 状态 ，类型过滤
				Path expresstatus = root.get("status");
				predicates.add(builder.equal(expresstatus, 0));
				Path exprestype = root.get("category");
				predicates.add(builder.equal(exprestype, 2));
				Path expresComplete = root.get("isComplete");
				predicates.add(builder.equal(expresComplete, 1));

				// 所在地
				if (addrId != null) {
					Path expresaddr = root.get("address");
					predicateOrs.add(builder.equal(expresaddr, addrId));
				}

				// 将predicateOrs条件用 or 联合起来
				if (predicateOrs.size() > 0) {
					predicates.add(builder.or(predicateOrs.toArray(new Predicate[predicateOrs.size()])));
				}

				// 过滤已是好友，关注记录
				if (idList != null && idList.size() > 0) {
					Path expresId = root.get("id");
					predicates.add(builder.not(expresId.in(idList.toArray())));
				}

				// 按用户分组去重
				Path groupPression = root.get("id");
				query.groupBy(groupPression);

				// 将所有条件用 and 联合起来
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
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

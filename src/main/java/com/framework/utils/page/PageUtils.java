package com.framework.utils.page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 解决dwz page 的遗留问题，使程序更易移植和替换
 * 
 */

public class PageUtils {

	/**
	 * 生成spring data JPA 对象 描述
	 * 
	 * @param page
	 * @return
	 */
	public static Pageable createPageable(Page page) {
		if (StringUtils.isNotBlank(page.getOrderField())) {
			// 忽略大小写
			// OrderField：如果是多个字段排序，则用逗号隔开，在这里作适配
			String[] orderFields = page.getOrderField().split(",");
			if (page.getOrderDirection().equalsIgnoreCase(
					Page.ORDER_DIRECTION_ASC)) {
				return new PageRequest(page.getPlainPageNum() - 1,
						page.getNumPerPage(), Sort.Direction.ASC, orderFields);
			} else {
				return new PageRequest(page.getPlainPageNum() - 1,
						page.getNumPerPage(), Sort.Direction.DESC, orderFields);
			}
		}

		return new PageRequest(page.getPlainPageNum() - 1, page.getNumPerPage());
	}

	/**
	 * 将springDataPage的属性注入page描述
	 * 
	 * @param page
	 * @param springDataPage
	 */
	@Deprecated
	public static void injectPageProperties(Page page,
			org.springframework.data.domain.Page<?> springDataPage) {
		// 暂时只注入总记录数量
		page.setTotalCount(springDataPage.getTotalElements());
	}
}

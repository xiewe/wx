package com.framework.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.entity.I18N;

public class I18NAttributeUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(I18NAttributeUtil.class);
	static ObjectMapper mapper = new ObjectMapper();
	/**
	 * 默认语言
	 */
	static String DEFAULT_LANGUAGE = "zh";

	/**
	 * 返回指定参数的国际化内容
	 * 
	 * @param content为JSON串
	 *            ,示例:[{"k":"en_US","v":"Hello"},{"k":"zh_CN","v":"你好"} ]
	 * @param lang要返回的语言
	 *            ，例：zh 或 zh_CN
	 * @return
	 */
	public static String i18n(String content, String lang) {
		// logger.error(String.format("----> content:%s , lang:%s", content,
		// lang));
		String i18n = "";
		String defaultI18N = "";
		try {
			if (StringUtils.isEmpty(content)) {
				return i18n;
			}
			if (StringUtils.isEmpty(lang)) {
				lang = DEFAULT_LANGUAGE;
			}
			if (lang.lastIndexOf("_") > 0) {
				lang = lang.split("_")[0];
			}
			Set<I18N> set = mapper.readValue(content,
					new TypeReference<Set<I18N>>() {
					});
			Iterator<I18N> iterator = set.iterator();
			while (iterator.hasNext()) {
				I18N o = iterator.next();
				if (o.getK().equals(lang)) {
					i18n = o.getV();
					return i18n;
				} else if (o.getK().equals(DEFAULT_LANGUAGE)) {
					defaultI18N = o.getV();
				} else {
					i18n = o.getV();
				}
			}
		} catch (Exception e) {
			i18n = "";
			logger.error(String.format(
					"获取属性内容异常, content:%s , lang:%s, cause:%s", content, lang,
					e.getMessage()));
		}
		return StringUtils.isEmpty(defaultI18N) ? i18n : defaultI18N;
	}

	/**
	 * 更新语言内容集合，存在更新，反之新增
	 * 
	 * @param content
	 * @return
	 */
	public static Set<I18N> updateI18NSet(Set<I18N> set, String content,
			String lang) {
		try {
			if (lang.lastIndexOf("_") > 0) {
				lang = lang.split("_")[0];
			}
			Iterator<I18N> iterator = set.iterator();
			while (iterator.hasNext()) {
				I18N o = iterator.next();
				if (o.getK().equals(lang)) {
					o.setV(content);
					return set;
				}
			}
			// 若不存在
			I18N o = new I18N(lang, content);
			set.add(o);
		} catch (Exception e) {
			logger.error(String.format(
					"更新属性内容异常, content:%s , lang:%s, cause:%s", content, lang,
					e.getMessage()));
		}
		return set;
	}

	/**
	 * 根据国际化内容返回语言内容集合
	 * 
	 * @param content
	 * @return
	 */
	public static Set<I18N> i18nSet(String content) {
		Set<I18N> set = new HashSet<I18N>();
		try {
			set = mapper.readValue(content, new TypeReference<Set<I18N>>() {
			});
		} catch (Exception e) {
			logger.error(String.format("根据国际化内容返回语言内容集合异常, cause:%s",
					e.getMessage()));
		}
		return set;
	}

	/**
	 * 将集合转换成json串
	 * 
	 * @param set
	 * @return
	 */
	public static String writeValueAsString(Set<I18N> set) {
		mapper.setSerializationInclusion(Include.NON_NULL);
		try {
			return mapper.writeValueAsString(set);
		} catch (JsonProcessingException e) {
			logger.error(String.format("将集合转换成json串异常, cause:%s",
					e.getMessage()));
			return "";
		}
	}

	public static void main(String[] args) {
		String content = "[{\"k\":\"zh\",\"v\":\"放寒假\"},{\"k\":\"en\",\"v\":\"franco\"}]";
		System.out.println(I18NAttributeUtil.i18n(content, "zh"));
	}
}

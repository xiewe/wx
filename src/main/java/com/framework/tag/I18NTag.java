package com.framework.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.utils.I18NAttributeUtil;

public class I18NTag extends SimpleTagSupport {

	private String content;
	private String lang;
	ObjectMapper mapper = new ObjectMapper();

	@Override
	public void doTag() throws JspException, IOException {
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(lang)) {
			getJspContext().getOut().write("");
			return;
		}
		// System.out.println(content + lang);
		getJspContext().getOut().write(I18NAttributeUtil.i18n(content, lang));
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}

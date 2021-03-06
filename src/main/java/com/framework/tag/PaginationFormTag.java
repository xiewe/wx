package com.framework.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.framework.utils.pager.Pager;

public class PaginationFormTag extends SimpleTagSupport {

	private Pager pager;
	private String action;
	private String onsubmit;

	@Override
	public void doTag() throws JspException, IOException {
		StringBuilder builder = new StringBuilder();
		if (onsubmit != null) {
			builder.append("<form id=\"pagerForm\" method=\"post\" action=\"" + action + "\" onsubmit=\"" + onsubmit
			        + "\">\n");
		} else {
			builder.append("<form id=\"pagerrForm\" method=\"post\" action=\"" + action + "\">\n");
		}

		builder.append("<input type=\"hidden\" name=\"currPage\" value=\"" + pager.getCurrPage() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"pageSize\" value=\"" + pager.getPageSize() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"totalPage\" value=\"" + pager.getTotalPage() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"orderField\" value=\"" + pager.getOrderField() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"orderDirection\" value=\"" + pager.getOrderDirection() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"totalCount\" value=\"" + pager.getTotalCount() + "\"/>\n");

		getJspContext().getOut().write(builder.toString());

		getJspBody().invoke(null);

		getJspContext().getOut().write("</form>\n");
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOnsubmit() {
		return onsubmit;
	}

	public void setOnsubmit(String onsubmit) {
		this.onsubmit = onsubmit;
	}

}

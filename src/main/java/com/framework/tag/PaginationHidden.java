package com.framework.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.framework.utils.pager.Pager;

public class PaginationHidden extends SimpleTagSupport {

	private Pager pager;

	@Override
	public void doTag() throws JspException, IOException {
		StringBuilder builder = new StringBuilder();

		builder.append("<input type=\"hidden\" name=\"currPage\" value=\"" + pager.getCurrPage() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"pageSize\" value=\"" + pager.getPageSize() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"totalPage\" value=\"" + pager.getTotalPage() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"orderField\" value=\"" + pager.getOrderField() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"orderDirection\" value=\"" + pager.getOrderDirection() + "\"/>\n");
		builder.append("<input type=\"hidden\" name=\"totalCount\" value=\"" + pager.getTotalCount() + "\"/>\n");

		getJspContext().getOut().write(builder.toString());
	}

	/**
	 * @param pager
	 *            the pager to set
	 */
	public void setPage(Pager pager) {
		this.pager = pager;
	}
}

package com.framework.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.framework.utils.pager.Pager;

public class PaginationTag extends SimpleTagSupport {

	private Pager pager;

	// 必须为偶数
	private int pagerNumShown = 6;
	private int begin = 10;
	private int end = 50;
	private int step = 5;

	@Override
	public void doTag() throws JspException, IOException {
		StringBuilder builder = new StringBuilder();

		builder.append("<div class=\"row\">\n");
		builder.append("    <div class=\"col-xs-12 col-sm-3\">\n");
		builder.append("        <p>每页显示\n");
		builder.append("        <select class=\"pager-select\" style=\"width: 50px; border: 1px solid #ccc; border-radius: 5px;\" onchange=\"pagerChange({pageSize:this.value})\">\n");

		for (int i = begin; i <= end; i += step) {
			if (i == pager.getPageSize()) {
				builder.append("            <option selected>").append(i).append("</option>\n");
			} else {
				builder.append("            <option>").append(i).append("</option>\n");
			}
		}

		builder.append("        </select>，共<span id=\"totalCount\">").append(pager.getTotalCount())
		        .append("</span>条数据</p>\n");
		builder.append("    </div>\n");
		builder.append("    <div class=\"col-xs-12 col-sm-8 text-right\" style=\"padding-right: 0px;\">\n");
		builder.append("      <ul class=\"pagination pagination-sm pager-list\" style=\"margin:0px;\">\n");

		if (pager.getTotalPage() <= 1) {
			builder.append("        <li class=\"active\"><a href=\"#\" onclick=\"goToN(event,1)\">1</a></li>\n");
		} else {
			builder.append("        <li><a href=\"#\" onclick=\"goToN(event,1)\">首页</a></li>\n");
			builder.append("        <li><a href=\"#\" onclick=\"goToPre(event)\">上一页</a></li>\n");

			int mid = pagerNumShown / 2;
			int diff = pagerNumShown - 1;
			int startNum = 1;
			int endNum = 1;

			if (pager.getCurrPage() >= mid) {
				if (pager.getCurrPage() + mid > pager.getTotalPage()) {
					startNum = pager.getTotalPage() - diff;
					endNum = pager.getTotalPage();
				} else {
					startNum = pager.getCurrPage() + mid - diff;
					endNum = pager.getCurrPage() + mid;
				}
			} else {
				if (pager.getTotalPage() < pagerNumShown) {
					startNum = 1;
					endNum = pager.getTotalPage();
				} else {
					startNum = 1;
					endNum = pagerNumShown;
				}
			}

			for (int i = startNum; i <= endNum; i++) {
				if (i == pager.getCurrPage()) {
					builder.append("        <li class=\"active\"><a href=\"#\" onclick=\"goToN(event,").append(i).append(")\">").append(i).append("</a></li>\n");
				} else {
					builder.append("        <li><a href=\"#\" onclick=\"goToN(event,").append(i).append(")\">").append(i).append("</a></li>\n");
				}
			}

			builder.append("        <li><a href=\"#\" onclick=\"goToNext(event)\">下一页</a></li>\n");
			builder.append("        <li><a href=\"#\" onclick=\"goToLast(event)\">尾页</a></li>\n");
		}

		builder.append("      </ul>\n");
		builder.append("    </div>\n");
		builder.append("    <div class=\"input-group input-group-sm col-sm-1 text-left\">\n");
		builder.append("        <input type=\"number\" class=\"form-control pager-input\">\n");
		builder.append("        <span class=\"input-group-btn\">\n");
		builder.append("            <button class=\"btn btn-default pager-go\" type=\"button\" onclick=\"goToD(event)\">Go</button>\n");
		builder.append("        </span>\n");
		builder.append("    </div>\n");
		builder.append("</div>\n");

		getJspContext().getOut().write(builder.toString());
	}

	/**
	 * @param pager
	 *            the pager to set
	 */
	public void setPage(Pager pager) {
		this.pager = pager;
	}

	/**
	 * @param pagerNumShown
	 *            the pagerNumShown to set
	 */
	public void setPageNumShown(int pagerNumShown) {
		this.pagerNumShown = pagerNumShown;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(int begin) {
		this.begin = begin;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @param step
	 *            the step to set
	 */
	public void setStep(int step) {
		this.step = step;
	}

}

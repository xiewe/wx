package com.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidationUtils {

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return true：是数字 false:不是数字
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 验证输入的邮箱格式是否符合
	 * 
	 * @param email
	 * @return 是否合法
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
			Matcher m = p.matcher(email);
			flag = m.find();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

}
